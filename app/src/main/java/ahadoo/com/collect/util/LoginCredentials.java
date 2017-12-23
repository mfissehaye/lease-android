package ahadoo.com.collect.util;

import android.content.Context;

import ahadoo.com.collect.CollectApplication;
import ahadoo.com.collect.R;
import ahadoo.com.collect.client.UserClient;
import ahadoo.com.collect.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginCredentials {

    public String username;

    public String password;

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validate() {
        return username.isEmpty() || password.isEmpty();
    }

    public void login(final Context context, final LoginResult loginResult) {

        UserClient client = RetrofitServiceGenerator.createService(UserClient.class);

        client.login(this).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {

                    User user = response.body();

                    user.setPreferences(context);

                    loginResult.onSuccess();

                } else {

                    loginResult.onFailure(context.getString(R.string.error_credentials));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                loginResult.onFailure(context.getString(R.string.error_connection));
            }
        });
    }
}
