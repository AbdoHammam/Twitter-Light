package com.example.abdo.twitter_light.Activities.Classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abdo on 6/14/17.
 */

public class Followers
{
    @SerializedName("users")
    List<Follower> followers;
    public List<Follower> getFollowers() {
        return followers;
    }
    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }
}
