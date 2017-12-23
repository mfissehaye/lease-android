package ahadoo.com.collect.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ahadoo.com.collect.CollectApplication;
import ahadoo.com.collect.R;
import ahadoo.com.collect.model.Survey;
import ahadoo.com.collect.model.SurveyChoice;
import ahadoo.com.collect.model.SurveyDao;
import ahadoo.com.collect.model.SurveyQuestion;

public class CollectHelpers {

    public static void showDialog(final Context context, String message) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // Yes button clicked
                        break;
                }
            }
        };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.app_name))

                .setMessage(message)

                .setPositiveButton(context.getString(R.string.ok), dialogClickListener).show();
    }

    public static boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo!=null && activeNetworkInfo.isConnected();
    }

    public static void showAlertDialog(Context context, String message, final OnAlertResponse response) {

        new AlertDialog.Builder(context)

                .setMessage(message)

                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        response.onPositiveResponse();

                    }
                }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}
