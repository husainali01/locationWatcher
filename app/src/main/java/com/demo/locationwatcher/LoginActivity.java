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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LoginActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private String TAG = LoginActivity.class.getSimpleName();
    private Class<?> mClss;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private ZXingScannerView mScannerView;
    private ViewGroup contentFrame;
    private Button enableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        enableBtn = findViewById(R.id.enable_btn);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
        launchScannerView();
        enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchScannerView();
            }
        });
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
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this,LocationActivity.class);
        intent.putExtra("QR Code:",rawResult.getBarcodeFormat().toString());
        startActivity(intent);
        finish();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(LoginActivity.this);
            }
        }, 2000);
    }

    public void launchScannerView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            enableBtn.setVisibility(View.VISIBLE);
            contentFrame.setVisibility(View.GONE);
            mScannerView = new ZXingScannerView(this);
            contentFrame.addView(mScannerView);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            enableBtn.setVisibility(View.GONE);
            contentFrame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableBtn.setVisibility(View.GONE);
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
