package com.example.abdo.twitter_light.Activities.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abdo on 6/14/17.
 */

public interface ApiInterface
{
    @GET("friends/ids")
    Call<List<Long> > getFriendsIDs(@Query("user_id")Long id);
}
