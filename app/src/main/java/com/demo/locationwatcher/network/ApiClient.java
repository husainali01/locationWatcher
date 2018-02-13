package com.demo.locationwatcher.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by webwerks on 19/6/17.
 */

public class ApiClient {

//       public static final String BASE_URL = "http://api.watslive.com/watslive/public/";
    public static String BASE_URL="http://srv.perfectinfodesk.com/api/";

    public String getBaseUrl() {
        return BASE_URL;
    }
    public static void setBaseUrl(String baseUrl){
        BASE_URL = baseUrl;
    }

    //production url
//    public static final String BASE_URL = "http://ec2-54-158-50-2.compute-1.amazonaws.com/watslive/public/";
    public static final String PATH_URL = "";


    private static Retrofit retrofit = null;

    public static ApiInterface getClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }


    /**
     * Web service method name
     */

    public abstract class ApiMethod {
        public static final String SIGN_UP = "signup";
        public static final String TRACKER_REGISTER = "registertracker";
        public static final String TRACKER = "tracker";
        public static final String SHIFT = "shift";
    }

    /**
     * Created by webwerks on 19/6/17.
     */

    public static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain)
                throws IOException {
//            String token = SharedPreferencesManager.getStringPreference(Constants.SharedPrefrenceData.TOKEN, null);
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    }
}
