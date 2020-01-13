package pl.rupniewski.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import pl.rupniewski.R;
import pl.rupniewski.models.Service;
import pl.rupniewski.models.Shop;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.DatabaseStatics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ListView serviceList;
    private List<Shop> shops;
    private List<Shop> filteredShops = new ArrayList<>();
    private Context context;
    private boolean mLocationPermissionGranted = false;
    private LatLng currentLocation = new LatLng(0,0);
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = root.getContext();
        initialize(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();

        if(bundle != null){
            String category = bundle.getString("category");
            long maxDistance = (long) bundle.getInt("distance");
            String search = bundle.getString("search");
            Log.e("INFO FROM filter", category + ", " + maxDistance + ", " + search);

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<List<Shop>> call = apiInterface.getAllShops();
            call.enqueue(new Callback<List<Shop>>() {
                @Override
                public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                    shops = response.body();
                    for (Shop s : shops) {
                        float distance = DatabaseStatics.distFrom(
                                s.getGeoLocation().getLatitude(),
                                s.getGeoLocation().getLongitude(),
                                (float) currentLocation.latitude,
                                (float) currentLocation.longitude);
                        if (distance != 0) {
                            if (distance < maxDistance) {
                                filteredShops.add(s);
                            }
                        }
                        if (!Strings.isEmptyOrWhitespace(category)) {
                            if (category.equals(s.getCategory().getName())) {
                                if (!filteredShops.contains(s)) {
                                    filteredShops.add(s);
                                }
                            }
                        }
                        if (!Strings.isEmptyOrWhitespace(search)) {
                            if (s.getCategory().getName().contains(search.toLowerCase()) || s.getName().contains(search.toLowerCase())) {
                                if (!filteredShops.contains(s)) {
                                    filteredShops.add(s);
                                }
                            }
                            for (Service service : s.getServices()) {
                                if (service.getName().contains(search.toLowerCase())) {
                                    if (!filteredShops.contains(s)) {
                                        filteredShops.add(s);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    serviceList.setAdapter(new ServiceListAdapter(context, filteredShops, currentLocation));
                    Log.e("Shops size: ", String.valueOf(shops.size()));
                }
                @Override
                public void onFailure(Call<List<Shop>> call, Throwable t) {
                    Log.e("Fail: ", t.getMessage());

                }
            });

        }


    }

    private void initialize(View root) {
        serviceList = root.findViewById(R.id.listView1);
        search = root.findViewById(R.id.btn_filter_home);
        search.setOnClickListener(handleFilter());
        getLocationPremissions();
        checkGoogleServices();
        getDeviceLocation();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Shop>> call = apiInterface.getAllShops();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                shops = response.body();
                Log.e("Shops size: ", String.valueOf(shops.size()));
                serviceList.setAdapter(new ServiceListAdapter(context, shops, currentLocation));
            }
            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.e("Fail: ", t.getMessage());

            }
        });
        serviceList.setOnItemClickListener((parent, view, position, id) -> {
            Shop s = shops.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("shop", s);
            ShopFragment shopFragment = new ShopFragment();
            shopFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), shopFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .commit();
        });
    }

    private View.OnClickListener handleFilter() {
        return v -> {
            FilterFragment filterFragment = new FilterFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(((ViewGroup)getView().getParent()).getId(), filterFragment, "Cokolwiek")
                    .addToBackStack(null)
                    .commit();
        };
    }

    private void getLocationPremissions(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }else {
                ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            }
        }else {
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1:{
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
    private void checkGoogleServices() {
        int maps_available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (maps_available == ConnectionResult.SUCCESS)
        {
            Log.d("Maps","its fine");
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(maps_available))
        {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), maps_available, 9001);
            dialog.show();
        }
        else {
            Toast.makeText(getContext(), "Maps not available", Toast.LENGTH_LONG).show();
        }
    }
    private void getDeviceLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        currentLocation = new LatLng(0,0);
        try {
            if (mLocationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Maps", "Found location");
                        Location currentLocation1 = (Location) task.getResult();
                        currentLocation = new LatLng(currentLocation1.getLatitude(), currentLocation1.getLongitude());
                        Log.e("Git", currentLocation1.toString());

                    }else {
                        Log.d( "Maps", "Current location is null");
                    }
                });
            }
        }catch (SecurityException e) {
            Log.e("Maps", e.getMessage());
        }
    }
}