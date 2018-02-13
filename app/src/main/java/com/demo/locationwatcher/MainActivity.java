package com.demo.locationwatcher;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesManager.getStringPreference("code","").equals("")){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this,LocationActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
