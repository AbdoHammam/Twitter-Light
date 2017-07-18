package com.example.abdo.twitter_light.Activities.API;

import com.example.abdo.twitter_light.Activities.Classes.Follower;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abdo on 7/18/17.
 */

public class GetFollowersResponse {

    @SerializedName("users")
    private List<Follower> users;

    @SerializedName("next_cursor")
    private Integer nextCursor;

    @SerializedName("next_cursor_str")
    private String nextCursorStr;

    @SerializedName("previous_cursor")
    private Integer previousCursor;

    @SerializedName("previous_cursor_str")
    private String previousCursorStr;


    public List<Follower> getUsers() {
        return users;
    }

    public Integer getNextCursor() {
        return nextCursor;
    }

    public String getNextCursorStr() {
        return nextCursorStr;
    }

    public Integer getPreviousCursor() {
        return previousCursor;
    }

    public String getPreviousCursorStr() {
        return previousCursorStr;
    }
}
