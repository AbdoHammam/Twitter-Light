package com.example.abdo.twitter_light.Activities.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdo on 7/19/17.
 */

public class GetUserInfoResponse
{
    @SerializedName("name")
    private String name;

    @SerializedName("profile_image_url_https")
    private String profile_image_url_https;

    @SerializedName("profile_background_image_url_https")
    private String profile_background_image_url_https;

    public String getName() {
        return name;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public String getProfile_background_image_url_https() {
        return profile_background_image_url_https;
    }
}
