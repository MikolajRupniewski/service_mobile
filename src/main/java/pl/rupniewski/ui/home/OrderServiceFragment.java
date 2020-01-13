package pl.rupniewski.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import org.apache.commons.lang3.time.DateUtils;
import org.lucasr.twowayview.TwoWayView;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import pl.rupniewski.R;
import pl.rupniewski.models.Employee;
import pl.rupniewski.models.Order;
import pl.rupniewski.models.Service;
import pl.rupniewski.models.Shop;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.AlertDialogs;
import pl.rupniewski.staticcontent.GlobalSharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderServiceFragment extends Fragment {
    private Context context;
    private CalendarPickerView calendar_view;
    private Date picedDate;
    private Shop shop;
    private Service service;
    private Users users;
    private GlobalSharedPreferences globalSharedPreferences;
    private Order order;
    private List<Order> availableOrders = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        context = root.getContext();
        initialize(root);
        return root;
    }

    private void initialize(View root) {
       calendar_view = root.findViewById(R.id.calendar_view);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        Bundle bundle = getArguments();
        shop = (Shop) bundle.getSerializable("shop");
        service = (Service) bundle.getSerializable("service");

        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.SINGLE);
        Button btn_show_dates = root.findViewById(R.id.btn_show_dates);
        order = new Order();
        order.setShop(shop);
        order.setService(service);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        String username = globalSharedPreferences.readPreference(getString(R.string.user_username));
        Call<Users> call = apiInterface.findUserByUsername(username);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.e("Ok: ", response.message());
                users = response.body();
                order.setUsers(users);
                Log.e("UserId: ", String.valueOf(users.getId()));
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e("Fail: ", t.getMessage());
            }
        });


        btn_show_dates.setOnClickListener(v -> {
            availableOrders = new ArrayList<>();
                picedDate = calendar_view.getSelectedDate();
                if(picedDate == null) return;
                List<Employee> employees = shop.getEmployees();
                for (Employee e : employees) {
                    String start = "";
                    String end = "";
                    if(picedDate.getDay() == 0) {
                        start = e.getSunday().getOpen();
                        end = e.getSunday().getClose();
                    }
                    else if (picedDate.getDay() == 1) {
                        start = e.getMonday().getOpen();
                        end = e.getMonday().getClose();
                    }
                    else if (picedDate.getDay() == 2) {
                        start = e.getTuesday().getOpen();
                        end = e.getTuesday().getClose();
                    }
                    else if (picedDate.getDay() == 3) {
                        start = e.getWednesday().getOpen();
                        end = e.getWednesday().getClose();
                    }
                    else if (picedDate.getDay() == 4) {
                        start = e.getThursday().getOpen();
                        end = e.getThursday().getClose();
                    }
                    else if (picedDate.getDay() == 5) {
                        start = e.getFriday().getOpen();
                        end = e.getFriday().getClose();
                    }
                    else if (picedDate.getDay() == 6) {
                        start = e.getSaturday().getOpen();
                        end = e.getSaturday().getClose();
                    }
                    // formatting start and finish of each employee
                    String[] formatedStart = start.split(":");
                    String[] formatedEnd = end.split(":");
                    int hourEnd = Integer.parseInt(formatedEnd[0]);
                    int minuteEnd = Integer.parseInt(formatedEnd[1]);
                    int hourStart = Integer.parseInt(formatedStart[0]);
                    int minuteStart = Integer.parseInt(formatedStart[1]);
                    Date pickedDateEnd = picedDate;
                    picedDate.setHours(hourStart);
                    picedDate.setMinutes(minuteStart);
                    pickedDateEnd.setHours(hourEnd);
                    pickedDateEnd.setMinutes(minuteEnd);

                    // filtering orders for the same date as picked date
                    List<Order> filteredOrders = shop.getOrders().stream().filter(order1 ->
                                    order1.getDate().getDay()== picedDate.getDay() &&
                                    order1.getDate().getMonth()==picedDate.getMonth() &&
                                    order1.getDate().getYear()==picedDate.getYear() &&
                                    order1.getEmployee().getId() == e.getId()
                            ).collect(Collectors.toList());
                    // sorting orders by their time
                    filteredOrders.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                    LocalTime startTime = LocalTime.of(hourStart, minuteStart);
                    LocalTime endTime = LocalTime.of(hourEnd, minuteEnd);
                    System.out.println("Start: " + startTime + ", End: " + endTime);
                    System.out.println("Size: " + filteredOrders.size());

                    startTime = LocalTime.of(hourStart, minuteStart);
                    endTime = LocalTime.of(hourEnd, minuteEnd);
                    while (startTime.isBefore(endTime)) {
                        Order order = new Order();
                        Date date = new Date();
                        date.setTime(picedDate.getTime());
                        date.setHours(startTime.getHour());
                        date.setMinutes(startTime.getMinute());
                        order.setDate(date);
                        order.setEmployee(e);
                        order.setUsers(users);
                        order.setService(service);
                        order.setShop(shop);
                        availableOrders.add(order);
                        startTime = startTime.plusMinutes(15);
                    }
                    for (Order filteredOrder : filteredOrders) {
                        Iterator<Order> listIterator = availableOrders.iterator();
                        while (listIterator.hasNext()) {
                            Order order = listIterator.next();
                            LocalTime filterStart = LocalTime.of(filteredOrder.getDate().getHours(), filteredOrder.getDate().getMinutes());
                            LocalTime filterEnd = LocalTime.of(filteredOrder.getDate().getHours(), filteredOrder.getDate().getMinutes());
                            Duration durationFiltered = Duration.parse(filteredOrder.getService().getDuration());
                            Duration durationAvailable = Duration.parse(order.getService().getDuration());
                            filterEnd = filterEnd.plusMinutes(durationFiltered.toMinutes());
                            LocalTime available = LocalTime.of(order.getDate().getHours(), order.getDate().getMinutes());
                            if (available.plusMinutes(durationAvailable.toMinutes()).isBefore(filterStart) || available.isAfter(filterEnd) ){
                                Log.d("Ok: ", "Available between: " + filterStart + " and " + filterEnd + ", available: " + available);
                            } else {
                                Log.e("Fail: ", "Not available between: " + filterStart + " and " + filterEnd + ", available: " + available);
                                listIterator.remove();
                            }
                        }
                    }
                }
                Set<Order> sortedAndFilteredOrders = new HashSet<>(availableOrders);
                availableOrders = new ArrayList<>(sortedAndFilteredOrders);
                availableOrders.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));

            OrderAdapter aItems = new OrderAdapter(context, availableOrders);
            TwoWayView lvTest = root.findViewById(R.id.lvItems);
            lvTest.setAdapter(aItems);
            lvTest.setOnItemClickListener((parent, view, position, id) -> {
                Order order = availableOrders.get(position);
                handleOrder(order);
            });
        });
    }

    private void handleOrder(Order order1) {
        Log.e("ORDER: ", String.valueOf(order.getDate()));
        String pattern = "yyyy-MM-dd, HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("pl","PL"));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Order");
        alertDialog.setMessage("Are you sure that you want to order:\n"
                + order1.getService().getName()
                + "\nAt " + simpleDateFormat.format(order1.getDate()) + "?");
        alertDialog.setIcon(R.drawable.common_full_open_on_phone);
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Shop> call = apiInterface.addOrder(shop.getId(), order1, order1.getDate().getTime());
            Log.e("Date: ", order1.getDate().toString());
            call.enqueue(new Callback<Shop>() {
                @Override
                public void onResponse(Call<Shop> call, Response<Shop> response) {
                    Toast.makeText(context, "Your order has been placed", Toast.LENGTH_SHORT).show();
                    Log.e("Shop", "INFO");
                    shop.getOrders().add(order1);
                    response.body().getOrders().forEach(System.out::println);
                }

                @Override
                public void onFailure(Call<Shop> call, Throwable t) {
                    Toast.makeText(context, "There was problem with your order", Toast.LENGTH_SHORT).show();
                }
            });

            Call<List<Shop>> call1 = apiInterface.getAllShops();
            List<Order> orders = new ArrayList<>();
            call1.enqueue(new Callback<List<Shop>>() {
                @Override
                public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                    for (Shop shop : response.body()) {
                        for (Order order: shop.getOrders()) {
                            if (order.getUsers().getId().equals(users.getId())) {
                                orders.add(order);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Shop>> call, Throwable t) {
                    Log.e("FAIL WITH OREDERS", t.getMessage());
                }
            });
            orders.forEach(System.out::println);
        });
        alertDialog.setNegativeButton("Cancel", null);
        alertDialog.show();

    }
}
