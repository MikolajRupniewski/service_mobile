package pl.rupniewski.retrofit;

import android.util.Base64;

import java.util.HashMap;
import java.util.List;

import pl.rupniewski.models.Category;
import pl.rupniewski.models.Order;
import pl.rupniewski.models.Shop;
import pl.rupniewski.models.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("category/")
    Call<List<Category>> getAllCategories();

    @POST("authenticate/register")
    Call<Users> registerUser(@Body Users users);

    @GET("authenticate/sign-in")
    Call<Users> signIn(@Query("username") String username, @Query("password") String password);

    @GET("authenticate/reset-password")
    Call<Users> restorePassword(@Query("email") String email);

    @PUT("authenticate/update-email/{id}")
    Call<Users> updateEmail(
            @Path("id") Long id,
            @Query("oldEmail") String oldEmail,
            @Query("newEmail") String newEmail
    );

    @PUT("authenticate/update-password/{id}")
    Call<Users> updatePassword(
            @Path("id") Long id,
            @Query("oldPassword") String oldPassword,
            @Query("newPassword") String newPassword
    );

    @GET("authenticate/get-user-by-username")
    Call<Users> findUserByUsername(@Query("username") String username);

    @PUT("users/{id}")
    Call<Users> updateUserData(
            @Header("Authorization") String auth,
            @Path("id") Long id,
            @Body Users users
            );

    @PUT("shops/{id}")
    Call<Shop> addOrder (
            @Path("id") Long id,
            @Body Order order,
            @Query("timestamp") long timestamp
            );

    @GET("shops/")
    Call<List<Shop>> getAllShops();

    @GET("orders/")
    Call<List<Order>> getAllOrders();

    @PUT("shops/addOrderFeedback/{id}")
    Call<Shop> updateShop(@Path("id") Long id,
                           @Body Order order);
}