package pl.rupniewski.ui.personaldata;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;



import pl.rupniewski.R;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.AlertDialogs;
import pl.rupniewski.staticcontent.DatabaseStatics;
import pl.rupniewski.staticcontent.GlobalSharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalDataFragment extends Fragment {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPhoneNumber;
    private EditText etZipCode;
    private EditText etCity;
    private EditText etStreetName;
    private EditText etHouseNumber;
    private EditText etApartmentNumber;
    private Button btnConfirm;
    private Users users;
    private GlobalSharedPreferences globalSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_personal_data, container, false);
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        etFirstName = root.findViewById(R.id.et_firstname_personal);
        etLastName = root.findViewById(R.id.et_lastname_personal);
        etPhoneNumber = root.findViewById(R.id.et_phone_personal);
        etZipCode = root.findViewById(R.id.et_zip_personal);
        etCity = root.findViewById(R.id.et_city_personal);
        etStreetName = root.findViewById(R.id.et_street_personal);
        etHouseNumber = root.findViewById(R.id.et_house_personal);
        etApartmentNumber = root.findViewById(R.id.et_apartment_personal);
        btnConfirm = root.findViewById(R.id.btn_submit_personal);
        btnConfirm.setOnClickListener(handleConfirm());
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String username = globalSharedPreferences.readPreference(getString(R.string.user_username));
        Call<Users> call = apiInterface.findUserByUsername(username);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                Log.e("Ok: ", response.message());
                users = response.body();
                etFirstName.setText(users.getFirstName());
                etLastName.setText(users.getLastName());
                etPhoneNumber.setText(users.getPhoneNumber());
                etZipCode.setText(users.getZipCode());
                etCity.setText(users.getCity());
                etStreetName.setText(users.getStreetName());
                etHouseNumber.setText(users.getHouseNumber());
                etApartmentNumber.setText(users.getApartmentNumber());
                Log.e("UserId: ", String.valueOf(users.getId()));
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Log.e("Fail: ", t.getMessage());
            }
        });
    }

    private View.OnClickListener handleConfirm() {
        return v -> {
            users.setFirstName(etFirstName.getText().toString());
            users.setLastName(etLastName.getText().toString());
            users.setPhoneNumber(etPhoneNumber.getText().toString());
            users.setZipCode(etZipCode.getText().toString());
            users.setCity(etCity.getText().toString());
            users.setStreetName(etStreetName.getText().toString());
            users.setHouseNumber(etHouseNumber.getText().toString());
            users.setApartmentNumber(etApartmentNumber.getText().toString());

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String username = globalSharedPreferences.readPreference(getString(R.string.user_username));
            String password = globalSharedPreferences.readPreference(getString(R.string.user_password));
            String auth = "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
            Call<Users> call = apiInterface.updateUserData(auth, users.getId(), users);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        AlertDialogs.showDialog(
                                getActivity().getString(R.string.dialog_success_title),
                                getActivity().getString(R.string.dialog_success_message_personal),
                                getActivity(),
                                R.drawable.info
                        );
                    } else {
                        AlertDialogs.showDialog(
                                getActivity().getString(R.string.dialog_failure_title),
                                getActivity().getString(R.string.dialog_failure_message_personal),
                                getActivity(),
                                R.drawable.failure
                        );
                    }
                }
                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    AlertDialogs.showDialog(
                            getActivity().getString(R.string.dialog_failure_title),
                            getActivity().getString(R.string.dialog_failure_message_personal),
                            getActivity(),
                            R.drawable.failure
                    );
                }
            });
        };
    }
}
