package pl.rupniewski.ui.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.util.Strings;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import pl.rupniewski.MainActivity;
import pl.rupniewski.R;
import pl.rupniewski.models.Order;
import pl.rupniewski.models.Shop;


public class OpinionsFragment extends Fragment {

    private Context context;
    private Shop shop;
    ListView opinions;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_opinions, container, false);

        context = root.getContext();
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        Bundle bundle = getArguments();
        shop = (Shop) bundle.getSerializable("shop");
        Log.e("SHOP: ", shop.toString());
        ((MainActivity) getActivity()).setActionBarTitle(shop.getName() + " - opinions");
        opinions = root.findViewById(R.id.opinions_list);
        List<Order> orders = shop.getOrders().stream()
                .filter(order -> !Strings.isEmptyOrWhitespace(order.getComment()))
                .filter(order -> order.getRating()!= null)
                .collect(Collectors.toList());
        opinions.setAdapter(new OpinionAdapter(context, orders));
        opinions.setFocusable(false);
    }
}
