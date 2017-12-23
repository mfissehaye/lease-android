package ahadoo.com.collect.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferencesManager {

    public static int PRIVATE_MODE = 0;

    private static final String PREFERENCE_NAME = "ahadoocollect";
    private static final String USER_LANGUAGE = "user_language";
    private static final String USER_NAME = "user_name";
    private static final String USER_FIRST_NAME = "user_first_name";
    private static final String USER_LAST_NAME = "user_last_name";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_ID = "user_id";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_IS_REGISTERED = "user_is_registered";

    public static void setStringPreference(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPreference(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).getString(key, defaultValue);
    }

    public static void setBooleanPreference(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE).getBoolean(key, defaultValue);
    }

    public static void setUserLanguage(Context context, String userLanguage) {
        setStringPreference(context, USER_LANGUAGE, userLanguage);
    }

    public static String getUserLanguage(Context context) {
        return getStringPreference(context, USER_LANGUAGE, "am");
    }

    public static void setRegisteredUser(Context context, boolean isRegistered) {
        setBooleanPreference(context, USER_IS_REGISTERED, isRegistered);
    }

    public static boolean isARegisteredUser(Context context) {
        return getBooleanPreference(context, USER_IS_REGISTERED, false);
    }

    public static void setUserID(Context context, String userID) {
        setStringPreference(context, USER_ID, userID);
    }

    public static String getUserID(Context context) {
        return getStringPreference(context, USER_ID, "");
    }

    public static void setUserUsername(Context context, String username) {
        setStringPreference(context, USER_NAME, username);
    }

    public static String getUserName(Context context) {
        return getStringPreference(context, USER_NAME, "");
    }

    public static void setUserPassword(Context context, String userPassword) {
        setStringPreference(context, USER_PASSWORD, userPassword);
    }

    public static String getUserPassword(Context context) {
        return getStringPreference(context, USER_PASSWORD, "");
    }


    public static void setUserFirstName(Context context, String userFirstName) {
        setStringPreference(context, USER_FIRST_NAME, userFirstName);
    }

    public static String getUserFirstName(Context context) {
        return getStringPreference(context, USER_FIRST_NAME, "");
    }

    public static void setUserLastname(Context context, String userLastname) {
        setStringPreference(context, USER_LANGUAGE, userLastname);
    }

    public static String getUserLastName(Context context) {
        return getStringPreference(context, USER_LAST_NAME, "");
    }

    public static void setUserToken(Context context, String token) {
        setStringPreference(context, USER_TOKEN, token);
    }

    public static String getUserToken(Context context) {
        return getStringPreference(context, USER_TOKEN, "");
    }

    public static void clearPreference(Context context) {
        setRegisteredUser(context, false);
        setUserToken(context, "");
        setUserLastname(context, "");
        setUserFirstName(context, "");
        setUserPassword(context, "");
        setUserUsername(context, "");
    }
}
