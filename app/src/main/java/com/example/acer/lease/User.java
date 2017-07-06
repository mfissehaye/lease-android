package com.example.acer.lease;

import com.google.gson.annotations.SerializedName;

/**
 * Created by merhawifissehaye@gmail.com on 7/5/2017.
 */

class User {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;
}
