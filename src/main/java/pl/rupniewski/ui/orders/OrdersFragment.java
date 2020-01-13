package pl.rupniewski.ui.orders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.rupniewski.R;
import pl.rupniewski.models.Order;
import pl.rupniewski.models.Shop;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.GlobalSharedPreferences;
import pl.rupniewski.ui.home.OneServiceListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment {

    ListView ordersList;
    private GlobalSharedPreferences globalSharedPreferences;
    private Users users;
    private Context context;
    private String comment;
    private Double rating;
    private List<Order> orders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_orders, container, false);
        context = root.getContext();
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        ordersList = root.findViewById(R.id.orders_list);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        String username = globalSharedPreferences.readPreference(getString(R.string.user_username));
        Call<Users> call = apiInterface.findUserByUsername(username);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.e("Ok: ", response.message());
                users = response.body();
                Log.e("UserId: ", String.valueOf(users.getId()));
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e("Fail: ", t.getMessage());
            }
        });
//        try {
//            Response<Users> usersResponse = call.execute();
//            users = usersResponse.body();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Call<List<Shop>> call1 = apiInterface.getAllShops();
        orders = new ArrayList<>();
        call1.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                for (Shop shop : response.body()) {
                    for (Order order: shop.getOrders()) {
                        if (order.getUsers().getId().equals(users.getId())) {
                            order.setShop(shop);
                            orders.add(order);
                        }
                    }
                }
                ordersList.setAdapter(new OrdersAdapter(root.getContext(), orders ));
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.e("FAIL WITH OREDERS", t.getMessage());
            }
        });
        ordersList.setOnItemClickListener((parent, view, position, id) -> handleOnClick(position));
    }

    private void handleOnClick(int position) {
        AlertDialog.Builder dialogBuilder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(dialogView);
// Set up the input

        EditText ratingText = dialogView.findViewById(R.id.comment_order);
        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar_order);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text


// Set up the buttons
        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            rating = (double) ratingBar.getRating();
            comment = ratingText.getText().toString();
            handleMessage(position);
        });
        dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void handleMessage(int position) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Order order = orders.get(position);
        order.setRating(rating);
        order.setComment(comment);
        Long id = order.getShop().getId();
        Shop shop = order.getShop();
        order.setShop(null);
        Call<Shop> call = apiInterface.updateShop(id, order);
        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                Toast.makeText(context, "Your opinion has been saved." + rating.toString() + ", " + comment, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {
                Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            }
        });
        orders.get(position).setComment(comment);
        orders.get(position).setRating(rating);
        orders.get(position).setShop(shop);

        ordersList.setAdapter(new OrdersAdapter(context, orders ));
    }
}