package com.demo.locationwatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demo.locationwatcher.databinding.ActivityVendorBinding;
import com.demo.locationwatcher.model.VendorDataModel;
import com.demo.locationwatcher.network.ApiClient;
import com.demo.locationwatcher.network.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class VendorActivity extends BaseActivity<ActivityVendorBinding> {

    private VendorDataModel vendorDataModel;
    private List<String> enggList ;

    @Override
    protected void onBinding() {
        vendorDataModel = getIntent().getParcelableExtra("vendorModel");
        if(vendorDataModel != null){
            SharedPreferencesManager.setPreference("vendor_name",vendorDataModel.getCompany());
            SharedPreferencesManager.setPreference("vendor_logo",vendorDataModel.getLogo());
                SharedPreferencesManager.setPreference("vendor_city",vendorDataModel.getCity());
            SharedPreferencesManager.setPreference("vendor_id",vendorDataModel.getUserid());
            SharedPreferencesManager.setPreference("interval",Integer.parseInt(vendorDataModel.getInterval()));
            mBinding.setModel(vendorDataModel);
            mBinding.selectEnggMsg.setText("Hello "+vendorDataModel.getContactperson()+",\nPlease Select Engineer.");
            Glide.with(this)
                    .load(vendorDataModel.getLogo())
                    .fitCenter()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(mBinding.vendorLogoIv);
            List<String> list = new ArrayList<>();
            final Spinner spnEngList = findViewById(R.id.spn_engList);
            for(int i = 0;i<vendorDataModel.getEngineerList().size();i++){
                list.add(vendorDataModel.getEngineerList().get(i).getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
           spnEngList.setAdapter(adapter);
            mBinding.vendorSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postRegisterTracker(SharedPreferencesManager.getStringPreference("uuid",""),SharedPreferencesManager.getStringPreference("code",""),vendorDataModel.getUserid(),vendorDataModel.getEngineerList().get((int) spnEngList.getSelectedItemId()).getId());
                }
            });
        }
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_vendor;
    }

    @Override
    protected void onWindowCreated() {

    }

    private void postRegisterTracker(String uuid,String code,String vendorId,String enggId){
        NetworkHelper.create(this).setMessage("Please Wait...").callWebService(ApiClient.getClient().postRegisterTracker(uuid, code,vendorId,enggId), new NetworkHelper.NetworkCallBack<ResponseBody>() {
            @Override
            public void onSyncData(ResponseBody data) {
//                Intent intent = new Intent(VendorActivity.this,LocationActivity.class);
                startActivity(LocationActivity.class);
            }

            @Override
            public void onFailed(ResponseBody error) {
                Toast.makeText(VendorActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void noInternetConnection() {

            }
        },true);
    }
}
