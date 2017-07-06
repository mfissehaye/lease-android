package com.example.acer.lease;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by merhawifissehaye@gmail.com on 7/5/2017.
 */

public interface LeaseService {
    @GET("/lease")
    Call<List<User>> getNames();

    @GET("/lease/description.php")
    Call<LeaseDescription> getDescription(@Query("id") int id);
}
