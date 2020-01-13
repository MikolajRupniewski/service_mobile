package pl.rupniewski.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.rupniewski.MainActivity;
import pl.rupniewski.R;
import pl.rupniewski.models.Service;
import pl.rupniewski.models.Shop;
import pl.rupniewski.staticcontent.DatabaseStatics;

public class ServiceListAdapter extends ArrayAdapter<Shop> {
    LatLng currentLocation;
    public ServiceListAdapter(Context context, List<Shop> serviceList, LatLng currentLocation) {
        super(context, 0, serviceList);
        this.currentLocation = currentLocation;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Shop shop = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_list, parent, false);
        }
        ImageView imgShop = convertView.findViewById(R.id.img_shop);
        TextView textShopName = convertView.findViewById(R.id.tv_name_shop);
        textShopName.setTypeface(null, Typeface.BOLD);
        TextView textShopDistance = convertView.findViewById(R.id.tv_distance_shop);
        textShopDistance.setTypeface(null, Typeface.BOLD);
        TextView textShopCategory = convertView.findViewById(R.id.tv_category_shop);
        textShopCategory.setTypeface(null, Typeface.BOLD);
        Picasso.get().load(shop.getPictures().get(0)).into(imgShop);
        textShopName.setText(shop.getName());
        float distance = DatabaseStatics.distFrom(
                shop.getGeoLocation().getLatitude(),
                shop.getGeoLocation().getLongitude(),
                (float) currentLocation.latitude,
                (float) currentLocation.longitude
        );
        distance = Math.round(distance * 10) / 10.0f;
        if(distance < 5.0f)
            textShopDistance.setTextColor(getContext().getResources().getColor(R.color.green));
        else if(distance >=5.0f && distance < 15.0f)
            textShopDistance.setTextColor(getContext().getResources().getColor(R.color.yellow));
        else
            textShopDistance.setTextColor(getContext().getResources().getColor(R.color.red));
        String content = distance + " " + getContext().getString(R.string.distance);
        textShopDistance.setText(content);
        textShopCategory.setText(shop.getCategory().getName());
        return convertView;
    }
}
