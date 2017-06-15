package com.example.abdo.twitter_light.Activities.API;

import com.example.abdo.twitter_light.Activities.Classes.Followers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abdo on 6/14/17.
 */

public interface ApiInterface
{
    @GET("followers/list.json")
    Call<Followers> getFollowers(@Query("user_id")Long id);
}
