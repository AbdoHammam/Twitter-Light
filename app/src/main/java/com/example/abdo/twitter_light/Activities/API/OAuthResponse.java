package com.example.abdo.twitter_light.Activities.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdo on 7/18/17.
 */

public class OAuthResponse {

    @SerializedName("token_type")
    String tokenType;

    @SerializedName("access_token")
    String accessToken;

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
