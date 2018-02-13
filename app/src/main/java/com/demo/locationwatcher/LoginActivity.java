package com.demo.locationwatcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.demo.locationwatcher.databinding.ActivityLoginBinding;
import com.demo.locationwatcher.model.SignUpResponseDataModel;
import com.demo.locationwatcher.network.ApiClient;
import com.demo.locationwatcher.network.ApiInterface;
import com.demo.locationwatcher.network.NetworkHelper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements ZXingScannerView.ResultHandler{
    private String TAG = LoginActivity.class.getSimpleName();
    private Class<?> mClss;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private ZXingScannerView mScannerView;
    private ViewGroup contentFrame;
    private Button enableBtn;
    private ApiInterface networkCall;
    private String deviceId;


    @Override
    protected void onBinding() {
        networkCall =
                ApiClient.getClient();
        deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        enableBtn = mBinding.enableBtn;
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        contentFrame.setVisibility(View.GONE);
        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchScannerView();
            }
        });
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onWindowCreated() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mScannerView != null){
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mScannerView != null) {
            mScannerView.stopCamera();
        }
    }

    @Override
    public void handleResult(final Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getText() +
//                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        NetworkHelper.create(this).setMessage("Please Wait...").callWebService(ApiClient.getClient().postSignUp(deviceId, rawResult.getText()), new NetworkHelper.NetworkCallBack<SignUpResponseDataModel>() {
            @Override
            public void onSyncData(SignUpResponseDataModel data) {
                if(data != null){
                    if (data.getStatus().getErrcode() == 0){
                        SharedPreferencesManager.setPreference("uuid",deviceId);
                        SharedPreferencesManager.setPreference("code",rawResult.getText());
                        ApiClient.setBaseUrl(data.getVendordata().getServer());
                        Intent intent = new Intent(LoginActivity.this,VendorActivity.class);
                        intent.putExtra("vendorModel",data.getVendordata());
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(LoginActivity.this, data.getStatus().getMsg(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(SignUpResponseDataModel error) {

            }

            @Override
            public void noInternetConnection() {

            }
        },true);
    }

    public void launchScannerView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            contentFrame.setVisibility(View.GONE);
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            mBinding.registrationLL.setVisibility(View.GONE);
            mBinding.copyrightTv.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mBinding.registrationLL.setVisibility(View.GONE);
                    mBinding.copyrightTv.setVisibility(View.GONE);
                    contentFrame.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                return;
        }
    }
}
