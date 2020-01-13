package pl.rupniewski.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import pl.rupniewski.R;
import pl.rupniewski.models.Order;
import pl.rupniewski.models.Service;

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(Context context, List<Order> serviceList) {
        super(context, 0, serviceList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Order order = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_list, parent, false);
        }

        TextView time = convertView.findViewById(R.id.order_date);
        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("pl","PL"));
        time.setText(simpleDateFormat.format(order.getDate()));

        return convertView;
    }
    /*
            Order movie = orderList.get(position);

     */
}
