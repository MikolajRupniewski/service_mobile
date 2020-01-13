package pl.rupniewski.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import pl.rupniewski.R;
import pl.rupniewski.models.Order;

public class OpinionAdapter extends ArrayAdapter<Order> {
    public OpinionAdapter(Context context, List<Order> serviceList) {
        super(context, 0, serviceList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Order order = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.opinions_list, parent, false);
        }

        TextView comment = convertView.findViewById(R.id.comment_opinions);
        RatingBar ratingBar = convertView.findViewById(R.id.rating_bar_opinions);
        comment.setText(order.getComment());
        ratingBar.setRating( order.getRating().floatValue());
        ratingBar.setEnabled(false);

        return convertView;
    }
}
