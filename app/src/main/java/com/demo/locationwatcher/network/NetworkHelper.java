package com.demo.locationwatcher.network;

import android.content.Context;


import com.demo.locationwatcher.BaseActivity;
import com.demo.locationwatcher.Tools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by webwerks on 19/6/17.
 */

public class NetworkHelper {

    private static final String TAG = NetworkHelper.class.getSimpleName();

    private Context mContext;
    private String message = null;

    /**
     * this flag use for when you call api from service.
     */
    private boolean showProgressLoading = true;
    private boolean showOnFailureDialog = true;


    private NetworkHelper(Context mContext) {
        this.mContext = mContext;
    }

    public static NetworkHelper create(Context context) {
        return new NetworkHelper(context);
    }

    public NetworkHelper setMessage(String message) {
        this.message = message;
        return this;

    }

    public <T> void callWebService(Call<T> call, final NetworkCallBack networkCallBack,boolean isShowProgress) {

        if (Tools.isConnected(mContext)) {
            if(isShowProgress){
                showLoader();
            }
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    stopLoader();
                    if (response.isSuccessful()) {
                        try {
                            Tools.printError(TAG, "enter in onResponse:" + "" + response.body().toString());
                            networkCallBack.onSyncData(response.body());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Tools.printError(TAG, "Enter in Exception:");
                            if (showOnFailureDialog) {
                                Tools.showDialogWhenException(mContext);
                            }

                        }
                    } else {
                        Tools.printError(TAG, "server contact failed");
                        networkCallBack.onFailed(null);
                    }

                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    t.printStackTrace();
                    Tools.printError(TAG, "enter in onFailure:" + t.toString());
                    stopLoader();
                    if (showOnFailureDialog) {
                        showOnFailureDialogResponse();
                    }
                    networkCallBack.onFailed(null);
                }
            });

        } else {
            networkCallBack.noInternetConnection();
            if (showProgressLoading) {
                Tools.showNoInternetMsg(mContext);
            }
        }
    }

    private void showLoader() {
        if (showProgressLoading) {
            if (message == null) {
                ((BaseActivity) mContext).showProgressLoading("",message);
            } else {
                ((BaseActivity) mContext).showProgressLoading("",message);
            }
        }
    }


    public void stopLoader() {
        ((BaseActivity) mContext).stopLoading();
    }

    private void showOnFailureDialogResponse() {
        if (showOnFailureDialog) {
            Tools.showFailureMsg(mContext);

        }

    }
    
    public NetworkHelper setShowOnFailureDialog(boolean showOnFailureDialog) {
        this.showOnFailureDialog = showOnFailureDialog;
        return this;
    }

    public NetworkHelper setShowProgressLoading(boolean showProgressLoading) {
        this.showProgressLoading = showProgressLoading;
        return this;
    }


    public interface NetworkCallBack<T> {
        void onSyncData(T data);


        void onFailed(T error);

        void noInternetConnection();

    }
}
