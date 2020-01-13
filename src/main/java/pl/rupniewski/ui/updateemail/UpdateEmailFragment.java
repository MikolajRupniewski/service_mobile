package pl.rupniewski.ui.updateemail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import pl.rupniewski.staticcontent.ValidateStrings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateEmailFragment extends Fragment {

    private EditText etOldEmail;
    private EditText etNewEmail;
    private Button btnConfirm;
    private Users users;
    private GlobalSharedPreferences globalSharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_update_email, container, false);
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        etOldEmail = root.findViewById(R.id.et_curr_email_update_email);
        etNewEmail = root.findViewById(R.id.et_new_email_update_email);
        btnConfirm = root.findViewById(R.id.btn_update_email);
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        users = DatabaseStatics.getCurrentUser(this);
        btnConfirm.setOnClickListener(handleConfirm());
    }

    private View.OnClickListener handleConfirm() {
        return v -> {
            String currEmail = etOldEmail.getText().toString();
            String newEmail = etNewEmail.getText().toString();
            if (!ValidateStrings.isValidEmailAddress(newEmail)) {
                etNewEmail.setError(getString(R.string.invalid_email));
                return;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Users> call = apiInterface.updateEmail(users.getId() ,currEmail, newEmail);
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