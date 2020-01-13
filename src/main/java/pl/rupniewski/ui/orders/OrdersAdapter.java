package pl.rupniewski.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import pl.rupniewski.R;
import pl.rupniewski.models.Order;

public class OrdersAdapter extends ArrayAdapter<Order> {

    public OrdersAdapter(Context context, List<Order> serviceList) {
        super(context, 0, serviceList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Order order = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.orders_list, parent, false);
        }

        TextView time = convertView.findViewById(R.id.tv_date_order_orders);
        TextView shop_name = convertView.findViewById(R.id.tv_name_shop_orders);
        TextView service_name = convertView.findViewById(R.id.tv_name_order_orders);
        TextView rating = convertView.findViewById(R.id.tv_rate_order_orders);
        String pattern = "yyyy-MM-dd, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("pl","PL"));
        time.setText(simpleDateFormat.format(order.getDate()));

        if (order.getComment() == null && order.getRating() == null) {
            rating.setTextColor(convertView.getResources().getColor(R.color.red));
            rating.setText("You did not place any feedback about this order");
        }
        else {
            rating.setTextColor(convertView.getResources().getColor(R.color.green));
            rating.setText(order.getRating() + ", " + order.getComment());
        }

        shop_name.setText(order.getShop().getName());
        service_name.setText(order.getService().getName());

        return convertView;
    }
}
