package pl.rupniewski.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;

import pl.rupniewski.MainActivity;
import pl.rupniewski.R;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import pl.rupniewski.staticcontent.AlertDialogs;
import pl.rupniewski.staticcontent.GlobalSharedPreferences;
import pl.rupniewski.staticcontent.LayoutSettings;
import pl.rupniewski.staticcontent.ValidateStrings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = getClass().getName();
    GlobalSharedPreferences globalSharedPreferences;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LayoutSettings.setStatusBarGradiant(this);
        initialize();
        String username = globalSharedPreferences.readPreference(this.getString(R.string.user_username));
        String password = globalSharedPreferences.readPreference(this.getString(R.string.user_password));
        if (!username.isEmpty() && !password.isEmpty()) {
            Log.e("Nice", username + ": " + password);
            etUsername.setText(username);
            etPassword.setText(password);
        }
    }

    private void initialize() {
        etUsername = findViewById(R.id.et_username_login);
        etPassword = findViewById(R.id.et_password_login);
        Button btnSignIn = findViewById(R.id.btn_sign_in);
        Button btnSignOn = findViewById(R.id.btn_sign_on);
        Button btnForgetPassword = findViewById(R.id.btn_forget_password);
        btnSignIn.setOnClickListener(v -> handleSignIn());
        btnSignOn.setOnClickListener(v -> handleSignOn());
        btnForgetPassword.setOnClickListener(v -> handleForgetPassword());
        GlobalSharedPreferences.getInstance().initialize(getApplicationContext());
        globalSharedPreferences = GlobalSharedPreferences.getInstance();
        globalSharedPreferences.initialize(getApplicationContext());
    }

    private void handleForgetPassword() {
        Intent intent = new Intent(this, PasswordForgetActivity.class);
        startActivity(intent);
        // TODO implement in correct place
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<Users> call = apiInterface.updateEmail(1L, "karconko@gmail.com", "karconko@gmail.com");
//        call.enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
//                Log.d(TAG, String.valueOf(response.code()));
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
//                Log.e(TAG, t.getMessage());
//            }
//        });
        // TODO implement in correct place
//        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//        Call<Users> call = apiInterface.updatePassword(1L, "karconko123", "karconko123");
//        call.enqueue(new Callback<Users>() {
//            @Override
//            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {
//                Log.d(TAG, String.valueOf(response.code()));
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
//                Log.e(TAG, Objects.requireNonNull(t.getMessage()));
//            }
//        });
    }

    private void handleSignOn() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void handleSignIn() {
        boolean invalidForm = false;
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (ValidateStrings.isValidUsername(username)) {
            etUsername.setError(this.getString(R.string.username_error));
            invalidForm = true;
        }
        if (ValidateStrings.isValidUsername(password)) {
            etPassword.setError(this.getString(R.string.password_error));
            invalidForm = true;
        }
        if (invalidForm)
            return;

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Users> call = apiInterface.signIn(username, password);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NotNull Call<Users> call, @NotNull Response<Users> response) {

                if(globalSharedPreferences == null) {
                    System.out.println("Fail: shared are null");
                }
                globalSharedPreferences.writePreference(getString(R.string.user_username), username);
                globalSharedPreferences.writePreference(getString(R.string.user_password), password);
                Log.e("Response", response.message());
                if(response.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    AlertDialogs.showDialog(
                            LoginActivity.this.getString(R.string.dialog_failure_title),
                            LoginActivity.this.getString(R.string.dialog_failure_message),
                            LoginActivity.this,
                            R.drawable.failure
                    );
                }
            }

            @Override
            public void onFailure(@NotNull Call<Users> call, @NotNull Throwable t) {
                AlertDialogs.showDialog(
                        LoginActivity.this.getString(R.string.dialog_failure_title),
                        LoginActivity.this.getString(R.string.dialog_failure_message),
                        LoginActivity.this,
                        R.drawable.failure
                );
            }
        });
    }
}
