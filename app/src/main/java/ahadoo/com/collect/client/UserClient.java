package ahadoo.com.collect.client;

import ahadoo.com.collect.model.User;
import ahadoo.com.collect.util.LoginCredentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api-token-auth/")
    Call<User> login(@Body LoginCredentials credentials);
}
