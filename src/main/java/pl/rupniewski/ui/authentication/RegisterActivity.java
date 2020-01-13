package pl.rupniewski.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    private EditText textUsername;
    private EditText textPassword;
    private EditText textPassword2;
    private EditText textEmail;
    private Button btnRegister;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LayoutSettings.setStatusBarGradiant(this);
        initialize();
    }

    private void initialize() {
        textUsername = findViewById(R.id.et_username_register);
        textEmail = findViewById(R.id.et_email_register);
        textPassword = findViewById(R.id.et_password_register);
        textPassword2 = findViewById(R.id.et_password2_register);
        btnBack = findViewById(R.id.btn_back_register);
        btnRegister = findViewById(R.id.btn_register_register);

        btnBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();
        String password2 = textPassword2.getText().toString();
        String email = textEmail.getText().toString();
        boolean isValid = true;
        if(ValidateStrings.isValidUsername(username)) {
            textUsername.setError(this.getString(R.string.invalid_username));
            isValid = false;
        }
        if(!ValidateStrings.isValidPassword(password)) {
            textPassword.setError(this.getString(R.string.invalid_password));
            isValid = false;
        }
        if(!ValidateStrings.isValidEmailAddress(email)) {
            textEmail.setError(this.getString(R.string.invalid_email));
            isValid = false;
        }
        if(!ValidateStrings.isPasswordTheSame(password, password2)) {
            textPassword2.setError(this.getString(R.string.invalid_password2));
            isValid = false;
        }
        if(!isValid) {
            return;
        }
        Users users = new Users(username, password, email);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Users> call = apiInterface.registerUser(users);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
                if (response.isSuccessful()) {
                    AlertDialogs.showDialog(
                            RegisterActivity.this.getString(R.string.dialog_success_title),
                            RegisterActivity.this.getString(R.string.dialog_success_registration_message),
                            RegisterActivity.this,
                            R.drawable.info
                    );
                } else {
                    AlertDialogs.showDialog(
                            RegisterActivity.this.getString(R.string.dialog_failure_title),
                            RegisterActivity.this.getString(R.string.dialog_failure_registration_message),
                            RegisterActivity.this,
                            R.drawable.failure
                    );
                }
            }

            @Override
            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                AlertDialogs.showDialog(
                        RegisterActivity.this.getString(R.string.dialog_failure_title),
                        RegisterActivity.this.getString(R.string.dialog_failure_registration_message),
                        RegisterActivity.this,
                        R.drawable.failure
                );
            }
        });
    }
}
