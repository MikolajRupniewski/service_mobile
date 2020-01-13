package pl.rupniewski.staticcontent;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import pl.rupniewski.R;
import pl.rupniewski.models.Users;
import pl.rupniewski.retrofit.ApiClient;
import pl.rupniewski.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabaseStatics {
    public static Users users;
    public static Users getCurrentUser(Fragment context) {
        GlobalSharedPreferences globalSharedPreferences = GlobalSharedPreferences.getInstance();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        String username = globalSharedPreferences.readPreference(context.getString(R.string.user_username));
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
        return users;
    }
    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float) (earthRadius * c / 1000);
    }
}
