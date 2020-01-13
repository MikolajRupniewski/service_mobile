package pl.rupniewski.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.List;

import pl.rupniewski.R;
import pl.rupniewski.models.Service;
import pl.rupniewski.models.Shop;

public class OneServiceListAdapter extends ArrayAdapter<Service> {
    public OneServiceListAdapter(Context context, List<Service> serviceList) {
        super(context, 0, serviceList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_serive_list, parent, false);
        }

        TextView textServiceName = convertView.findViewById(R.id.tv_name_one_service);
        TextView textServiceDuration = convertView.findViewById(R.id.tv_duration_one_service);
        TextView textServicePrice = convertView.findViewById(R.id.tv_price_one_service);

        textServiceName.setText(service.getName());
        Duration duration = Duration.parse(service.getDuration());
        textServiceDuration.setText(duration.getSeconds()/60 + " minutes");
        textServicePrice.setText(service.getPrice() + "PLN");
        return convertView;
    }
}
