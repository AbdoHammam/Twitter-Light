package com.example.abdo.twitter_light.Activities.API;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by abdo on 6/14/17.
 */

public class ApiClient extends TwitterApiClient {

    private static final String BASE_URL = "https://api.twitter.com/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public ApiClient(TwitterSession session) {
        super(session);
    }
}

