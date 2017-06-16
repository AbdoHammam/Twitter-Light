package com.example.abdo.twitter_light.Activities.API;

import com.example.abdo.twitter_light.Activities.Classes.Followers;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abdo on 6/14/17.
 */

public class ApiClient extends TwitterApiClient {

    private static final String BASE_URL = "https://api.twitter.com/1.1/";
    private static Retrofit retrofit;
    public static Retrofit getApiClient()
    {
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
    public ApiClient(TwitterSession session) {
        super(session);
    }
}

