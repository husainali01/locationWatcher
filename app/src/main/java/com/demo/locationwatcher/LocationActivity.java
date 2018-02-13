package com.demo.locationwatcher;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.demo.locationwatcher.databinding.ActivityLocationBinding;
import com.demo.locationwatcher.network.ApiClient;
import com.demo.locationwatcher.network.NetworkHelper;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import okhttp3.ResponseBody;

public class LocationActivity extends BaseActivity<ActivityLocationBinding> implements OnLocationUpdatedListener ,OnActivityUpdatedListener {
    private TextView latitudeTv ;
    private LocationGooglePlayServicesProvider provider;
    private int timeInterval;
    private static final int LOCATION_PERMISSION_ID = 1001;
    private Location mLocation;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();
    private String address;
    private String state;
    String carrierName;
    int interval;

    @Override
    protected void onBinding() {
        mBinding.companyNameTv.setText(SharedPreferencesManager.getStringPreference("vendor_name",""));
        mBinding.cityTv.setText(SharedPreferencesManager.getStringPreference("vendor_city",""));
        TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();
        interval = SharedPreferencesManager.getIntPreference("interval",30);
        Glide.with(this)
                .load(SharedPreferencesManager.getStringPreference("vendor_logo",""))
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(mBinding.vendorLogoIv);
        mBinding.startStopToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Location permission not granted
                if (ContextCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
                    if(isChecked){
                        mBinding.startStopToggle.setChecked(false);
                    }else {
                        mBinding.startStopToggle.setChecked(true);
                    }
                    return;
                }
                postShift(isChecked);

            }
        });
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_location;
    }

    @Override
    protected void onWindowCreated() {

    }


    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);
    }

    private void stopLocation() {
        SmartLocation.with(this).location().stop();
        Log.d("Location Status :","Location stopped!");

        SmartLocation.with(this).activity().stop();
        Log.d("Location Status :","Activity Recognition stopped!");

        SmartLocation.with(this).geofencing().stop();
        Log.d("Location Status :","Geofencing stopped!");
    }

    private void showLocation(Location location) {
        if (location != null) {
            Log.d("Location Status :","Geofencing stopped!");

            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder("");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
//                        locationText.setText(builder.toString());
                        address = builder.toString();
                        mBinding.locationTv.setText(builder.toString());

                    }
                }
            });
        } else {
            mBinding.locationTv.setText("Null location");
        }
    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
        showLast();
        mLocation = location;
        mBinding.longitudeTv.setText("Longitude : "+location.getLongitude());
        mBinding.latitudeTv.setText("Latitude : "+location.getLatitude());
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }
    }
    private void stopTimer(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
        }
    }

    private void startTimer(){
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        Long tsLong = System.currentTimeMillis()/1000;
                        String ts = tsLong.toString();
                        if(mLocation != null){
                            String data = "41289649aa6a1f1446716da39637c19f"+"~"+SharedPreferencesManager.getStringPreference("uuid","")+"~"+mLocation.getLatitude()+"~"+mLocation.getLongitude()+"~"+ts+"~"+state+"~"+carrierName+"~"+ address+"~"+SharedPreferencesManager.getStringPreference("vendor_id","");
                            postTracker(data);
                        }
                    }
                });
            }
        };
//        SharedPreferencesManager.getIntPreference("interval",30)
        mTimer.schedule(mTimerTask, 1, interval*1000);
    }

    private void postTracker(String data){
        NetworkHelper.create(this).setMessage("Please Wait...").callWebService(ApiClient.getClient().postTracker(data), new NetworkHelper.NetworkCallBack<ResponseBody>() {
            @Override
            public void onSyncData(ResponseBody data) {
                Log.d("tracker","isSuccessfull");
            }

            @Override
            public void onFailed(ResponseBody error) {

            }

            @Override
            public void noInternetConnection() {

            }
        },false);
    }

    private void showLast() {
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (mLocation != null) {
//            locationText.setText(
//                    String.format("[From Cache] Latitude %.6f, Longitude %.6f",
//                            lastLocation.getLatitude(),
//                            lastLocation.getLongitude())
//            );
        }

        DetectedActivity detectedActivity = SmartLocation.with(this).activity().getLastActivity();
        if (detectedActivity != null) {
//            activityText.setText(
//                    String.format("[From Cache] Activity %s with %d%% confidence",
//                            getNameFromType(detectedActivity),
//                            detectedActivity.getConfidence())
//            );
            state = getNameFromType(detectedActivity);
        }
    }
    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }

    private void postShift(final boolean isChecked){
        String clickType = "";
        if(isChecked){
            clickType = "start";
        }else {
            clickType = "stop";
        }
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        NetworkHelper.create(this).setMessage("Please Wait...").callWebService(ApiClient.getClient().postShift(SharedPreferencesManager.getStringPreference("uuid",""),clickType,ts), new NetworkHelper.NetworkCallBack<ResponseBody>() {
            @Override
            public void onSyncData(ResponseBody data) {
                if(isChecked){
                    startLocation();
                    startTimer();
                }else {
                    stopLocation();
                    stopTimer();
                }
            }

            @Override
            public void onFailed(ResponseBody error) {
                if(isChecked){
                   mBinding.startStopToggle.setChecked(false);
                }else {
                    mBinding.startStopToggle.setChecked(true);
                }
            }

            @Override
            public void noInternetConnection() {

            }
        },true);
    }
}
