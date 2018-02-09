package com.demo.locationwatcher.network;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by webwerks on 19/6/17.
 */

public interface ApiInterface {

    @POST(ApiClient.PATH_URL + ApiClient.ApiMethod.LOGIN)
    @FormUrlEncoded
    Call<Response> getLogin(@Field("social_type") String loginType,
                            @Field("social_token") String socialToken);


}



