package pl.rupniewski.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.rupniewski.MainActivity;
import pl.rupniewski.R;
import pl.rupniewski.models.Service;
import pl.rupniewski.models.Shop;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ShopFragment extends Fragment implements OnMapReadyCallback {
    private Context context;
    Shop shop;
    private MapView mapView;
    private GoogleMap googleMap;
    private ListView serviceList;
    private Button btnOpions;
    private TextView shopLocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        context = root.getContext();
        initialize(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void initialize(View root) {
        Bundle bundle = getArguments();
        shop = (Shop) bundle.getSerializable("shop");
        Log.e("SHOP: ", shop.toString());
        ((MainActivity) getActivity()).setActionBarTitle(shop.getName());
        serviceList = root.findViewById(R.id.service_list);
        shopLocation = root.findViewById(R.id.shop_location);
        btnOpions = root.findViewById(R.id.btn_feedback_shop);
        btnOpions.setOnClickListener(handleOptions());
        shopLocation.setText("");
        serviceList.setAdapter(new OneServiceListAdapter(context, shop.getServices() ));
        getAddress();
        serviceList.setOnItemClickListener((parent, view, position, id) -> {
            Service service = shop.getServices().get(position);
            bundle.putSerializable("service", service);
            OrderServiceFragment orderServiceFragment = new OrderServiceFragment();
            orderServiceFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), orderServiceFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .commit();
        });
    }

    private View.OnClickListener handleOptions() {
        return v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("shop", shop);
            OpinionsFragment opinionsFragment = new OpinionsFragment();
            opinionsFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), opinionsFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .commit();
        };
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng shopPosition = new LatLng(shop.getGeoLocation().getLatitude(), shop.getGeoLocation().getLongitude());

        googleMap.addMarker(new MarkerOptions().position(shopPosition).title(shop.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopPosition, 12f));
    }

    private void getAddress() {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String errorMessage = "";

        try {
            addresses = geocoder.getFromLocation(
                    shop.getGeoLocation().getLatitude(),
                    shop.getGeoLocation().getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Service not available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid lat long used";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + shop.getGeoLocation().getLatitude() +
                    ", Longitude = " +
                    shop.getGeoLocation().getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Address not found";
                Log.e(TAG, errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.e("GIt", "zwyciestwo!");
            for (String s : addressFragments) {
                shopLocation.setText(s);
            }
        }

    }
}
