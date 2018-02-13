package com.demo.locationwatcher.network;

import com.demo.locationwatcher.model.SignUpResponseDataModel;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by webwerks on 19/6/17.
 */

public interface ApiInterface {

    @POST(ApiClient.ApiMethod.SIGN_UP)
    @FormUrlEncoded
    Call<SignUpResponseDataModel> postSignUp(@Field("uuid") String deviceId,
                                             @Field("code") String tokenCode);
    @POST(ApiClient.ApiMethod.TRACKER_REGISTER)
    @FormUrlEncoded
    Call<ResponseBody> postRegisterTracker(@Field("uuid") String deviceId,
                                           @Field("vcode") String tokenCode,
                                           @Field("vendorid") String vendorId,
                                           @Field("enggid") String enggId);

    @POST(ApiClient.ApiMethod.TRACKER)
    @FormUrlEncoded
    Call<ResponseBody> postTracker(@Field("data") String dataString);

    @POST(ApiClient.ApiMethod.SHIFT)
    @FormUrlEncoded
    Call<ResponseBody> postShift(@Field("uuid") String deviceId,
                                             @Field("clicktype") String clickType,
                                            @Field("date") String date);

}



