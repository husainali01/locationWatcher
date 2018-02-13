package com.demo.locationwatcher;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements LifecycleRegistryOwner {
    protected T mBinding;
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowCreated();
        mBinding = DataBindingUtil.setContentView(this,getContentViewLayout());
        onBinding();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    protected abstract void onBinding();
    public <S extends LifecycleObserver>void addObserver(S observer){
        getLifecycle().addObserver(observer);
    }
    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    protected abstract int getContentViewLayout();

    protected abstract void onWindowCreated();

    public void showProgressLoading(String title ,String msg) {
        showProgressLoading(title, msg, false);
    }
    public void showProgressLoading(String title ,String msg, boolean isCancelable) {
        progressDialog = ProgressDialog.show(this, title, msg);
        progressDialog.setCancelable(isCancelable);
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void stopLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void startActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }

    public void openFragment(android.support.v4.app.Fragment fragment, int containerId, boolean toBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        try{
            if(toBackStack) {
                fragmentManager.beginTransaction()
                        .replace(containerId, fragment).addToBackStack(fragment.getClass().getName()).commit();
            }else {
                fragmentManager.beginTransaction()
                        .replace(containerId, fragment).commit();
            }
        }catch (Exception e){
           e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
