package com.example.abdo.twitter_light.Activities.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by abdo on 6/15/17.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("oauth2/token")
    Call<OAuthResponse> authenticate(@Header("Authorization") String authorization, @Header("Content-Type") String contentType, @Field("grant_type") String body);

    @GET("1.1/followers/list.json")
    Call<GetFollowersResponse> getFollowers(@Header("Authorization") String authorization, @Query("cursor") Integer cursor, @Query("user_id") Long userId);

    @GET("1.1/users/show.json")
    Call<GetUserInfoResponse> getUserInfo(@Header("Authorization") String authorization, @Query("id") Long userId);
}