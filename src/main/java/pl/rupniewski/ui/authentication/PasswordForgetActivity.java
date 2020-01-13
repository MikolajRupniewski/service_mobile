package pl.rupniewski.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;

import pl.rupniewski.R;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.AlertDialogs;
import pl.rupniewski.staticcontent.LayoutSettings;
import pl.rupniewski.staticcontent.ValidateStrings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordForgetActivity extends AppCompatActivity {

    private EditText textEmail;
    private Button btnConfirm;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        LayoutSettings.setStatusBarGradiant(this);
        initialize();
    }

    private void initialize() {
        textEmail = findViewById(R.id.et_email_password_forget);
        btnConfirm = findViewById(R.id.btn_password_forget);
        btnBack = findViewById(R.id.btn_back_password_forget);

        btnBack.setOnClickListener(handleBack());
        btnConfirm.setOnClickListener(handleConfirm());
    }

    private View.OnClickListener handleConfirm() {
        return v -> {
            String email = textEmail.getText().toString();
            if(!ValidateStrings.isValidEmailAddress(email)) {
                textEmail.setError(this.getString(R.string.invalid_email));
                return;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<Users> call = apiInterface.restorePassword(email);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                    if (response.isSuccessful()) {
                        AlertDialogs.showDialog(
                                PasswordForgetActivity.this.getString(R.string.dialog_success_title),
                                PasswordForgetActivity.this.getString(R.string.dialog_success_message),
                                PasswordForgetActivity.this,
                                R.drawable.info
                        );
                    } else {
                        AlertDialogs.showDialog(
                                PasswordForgetActivity.this.getString(R.string.dialog_failure_title),
                                PasswordForgetActivity.this.getString(R.string.dialog_failure_message),
                                PasswordForgetActivity.this,
                                R.drawable.failure
                        );
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                    AlertDialogs.showDialog(
                            PasswordForgetActivity.this.getString(R.string.dialog_failure_title),
                            PasswordForgetActivity.this.getString(R.string.dialog_failure_message),
                            PasswordForgetActivity.this,
                            R.drawable.failure
                    );
                }
            });
        };
    }

    private View.OnClickListener handleBack() {
        return v -> finish();
    }
}
