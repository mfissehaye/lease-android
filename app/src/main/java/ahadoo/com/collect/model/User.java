package ahadoo.com.collect.model;

import android.content.Context;

import ahadoo.com.collect.util.UserPreferencesManager;

public class User {

    String lastName;

    String username;

    String uuid;

    String token;

    public void setPreferences(Context context) {

        UserPreferencesManager.setRegisteredUser(context, true);

        UserPreferencesManager.setUserLastname(context, lastName);

        UserPreferencesManager.setUserUsername(context, username);

        UserPreferencesManager.setUserID(context, uuid);

        UserPreferencesManager.setUserToken(context, token);
    }
}
