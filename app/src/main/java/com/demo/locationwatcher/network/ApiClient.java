package com.demo.locationwatcher.network;

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
    public static final String BASE_URL="http://api.watslive.com/";
       //production url
//    public static final String BASE_URL = "http://ec2-54-158-50-2.compute-1.amazonaws.com/watslive/public/";
    public static final String PATH_URL = "api/v1/";


    private static Retrofit retrofit = null;

    public static ApiInterface getClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
//                .addNetworkInterceptor(new StethoInterceptor())
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
        public static final String LOGIN = "login";
        public static final String REGISTER = "register";
        public static final String SEARCH = "videos/search";
        public static final String PLACES = "places";
        public static final String USER_PROFILE_DETAILS = "userProfileDetails";
        public static final String CATEGORY = "categories";
        public static final String FEEDBACK = "feedback";
        public static final String LIKE = "like";
        public static final String FOLLOW = "videos/follow";
        public static final String FOLLOWING = "following";
        public static final String FOLLOWING_VIDEOS = "following_videos";
        public static final String VIEW = "videos/view";
        public static final String LIKED_VIDEOS = "liked_videos";
        public static final String EDIT_USER_PROFILE = "editUserProfile";
        public static final String VIDEOS = "videos";
        public static final String VIDEO_FOLLOWING = "videos/following";
        public static final String USER_DETAIL_DATA = "videos";
        public static final String DELETE = "videos/delete";
        public static final String VIDEO_FOLLOWER = "videos/follower";
        public static final String ADD_VENUE = "videos/add_venue";
        public static final String GET_VENUES = "videos/get_venues";
        public static final String SEARCH_BY_PLACE = "videos/search_by_place";
        public static final String VIDEOS_FOLLOWING_VIDEOS = "videos/following_videos";
        public static final String SUGGESTIONS = "videos/suggestions";
        public static final String VIDEOS_VIDEO_VIEWS = "videos/video_views";
        public static final String SEARCH_USER_PROFILE = "searchUserProfile";
        public static final String VIDEOS_VIEW = "videos/view";
        public static final String REPORT = "report";
        public static final String UNFOLLOW = "videos/unfollow";
        public static final String GET_FOLLOWING_VENUES = "videos/get_venues_list";
        public static final String UPDATE_COVER_PIC = "updateCoverPic";
        public static final String UNLIKE_VENUE = "videos/remove_venue";
        public static final String REPORT_PROBLEM = "report_problem";
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

//    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vd2F0c2xpdmUucGhwLWRldi5pbjo4ODQ1L2FwaS92MS9yZWdpc3RlciIsImlhdCI6MTUxMDYzNTUyMCwibmJmIjoxNTEwNjM1NTIwLCJqdGkiOiJvTXlLRnlIS3RuaWtkYlF5Iiwic3ViIjoiN2U4ZWEwZmQtNTcyYy00MmM4LTk1MmEtNWE2YTg0ZmMwOTE1IiwicHJ2IjoiODdlMGFmMWVmOWZkMTU4MTJmZGVjOTcxNTNhMTRlMGIwNDc1NDZhYSIsInVzZXIiOnsiaWQiOiI3ZThlYTBmZC01NzJjLTQyYzgtOTUyYS01YTZhODRmYzA5MTUifX0.uCoUsFvMTalQAdzKbbG-OV_ZhHhKOFmUeqeAt3Oj-CA
}
