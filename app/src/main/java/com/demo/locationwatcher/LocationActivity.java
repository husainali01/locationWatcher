package com.demo.locationwatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {
    private TextView latitudeTv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        String QrCode = getIntent().getStringExtra("QR Code");
        latitudeTv = findViewById(R.id.latitude_tv);
        latitudeTv.setText(QrCode);
    }
}
