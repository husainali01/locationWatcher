package com.demo.locationwatcher;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by webwerks1 on 25/10/17.
 */

public class Tools {

    /**
     * print error log
     */
    public static void printError(String tag, String msg) {
        Log.e(tag, msg);
    }

    /**
     * print error Debug msg
     */
    public static void printDebug(String tag, String msg) {
        Log.d(tag, msg);
    }

    /**
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    /**
     * Checks if is connected.
     *
     * @param context the context
     * @return true, if is connected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();


        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showDialogWhenException(Context context) {
        showToast(context, context.getString(R.string.server_error));
    }


    public static void showNoInternetMsg(Context context) {
        showToast(context, context.getString(R.string.no_internet));
    }

    public static void showFailureMsg(Context context) {
        showToast(context, context.getString(R.string.server_error));
    }

    public static String getGender(int value){
        String gender;
        if(value== 0){
            return gender = "male";
        }else if(value == 1){
            return gender = "female";
        }else {
            return gender = "other";
        }
    }
    public static List<Address> getAddress(Activity activity, double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String formatedDateTime(String date){
        if(date != null&&date.equals("")){
            Date actualDate = new Date();
            try{
                actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            }catch (Exception e){
                e.printStackTrace();
            }
            return new SimpleDateFormat("dd-MMM-yy").format(actualDate).toString();
        }
        return "";
    }
}
