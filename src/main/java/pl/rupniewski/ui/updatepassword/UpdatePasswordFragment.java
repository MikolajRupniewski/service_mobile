package pl.rupniewski.ui.updatepassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import pl.rupniewski.R;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.AlertDialogs;
import pl.rupniewski.staticcontent.DatabaseStatics;
import pl.rupniewski.staticcontent.GlobalSharedPreferences;
import pl.rupniewski.staticcontent.ValidateStrings;
import pl.rupniewski.ui.authentication.PasswordForgetActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordFragment extends Fragment {

    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etNewPassword2;
    private Button btnConfirm;
    private Users users;
    private GlobalSharedPreferences globalSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_update_password, container, false);

        initialize(root);
        return root;
    }

    private void initialize(View root) {
        etCurrentPassword = root.findViewById(R.id.et_curr_password_update_password);
        etNewPassword = root.findViewById(R.id.et_new_password_update_password);
        etNewPassword2 = root.findViewById(R.id.et_new_password2_update_password);
        btnConfirm = root.findViewById(R.id.btn_confirm_update_password);
        btnConfirm.setOnClickListener(handleConfirm());
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        users = DatabaseStatics.getCurrentUser(this);
    }

    private View.OnClickListener handleConfirm() {
        return v -> {
            String currentPassword = etCurrentPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String newPassword2 = etNewPassword2.getText().toString();
            boolean isValid = true;
            if(!ValidateStrings.isPasswordTheSame(newPassword, newPassword2)) {
                etNewPassword2.setError(getString(R.string.invalid_password2));
                isValid = false;
            }
            if(!ValidateStrings.isValidPassword(newPassword)) {
                etNewPassword.setError(getString(R.string.invalid_password));
                isValid = false;
            }
            if(!isValid) {
                return;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Users> call = apiInterface.updatePassword(users.getId() ,currentPassword, newPassword);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.isSuccessful()) {
                        AlertDialogs.showDialog(
                                getActivity().getString(R.string.dialog_success_title),
                                getActivity().getString(R.string.dialog_success_message_email),
                                getActivity(),
                                R.drawable.info
                        );
                        globalSharedPreferences.writePreference(getString(R.string.user_password), newPassword);
                    } else {
                        AlertDialogs.showDialog(
                                getActivity().getString(R.string.dialog_failure_title),
                                getActivity().getString(R.string.dialog_failure_message_email),
                                getActivity(),
                                R.drawable.failure
                        );
                    }
                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    AlertDialogs.showDialog(
                            getActivity().getString(R.string.dialog_failure_title),
                            getActivity().getString(R.string.dialog_failure_message_email),
                            getActivity(),
                            R.drawable.failure
                    );
                }
            });
        };
    }
}