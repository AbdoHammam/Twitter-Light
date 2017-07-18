package com.example.abdo.twitter_light.Activities.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdo on 6/14/17.
 */

public class Follower implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String username;

    @SerializedName("screen_name")
    private String screenname;

    @SerializedName("profile_image_url_https")
    private String profilePicURL;

    @SerializedName("profile_background_image_url_https")
    private String backgroundImageURL;

    @SerializedName("description")
    private String bio;
    public Follower(){}
    protected Follower(Parcel in) {
        username = in.readString();
        screenname = in.readString();
        profilePicURL = in.readString();
        backgroundImageURL = in.readString();
        bio = in.readString();
    }

    public static final Creator<Follower> CREATOR = new Creator<Follower>() {
        @Override
        public Follower createFromParcel(Parcel in) {
            return new Follower(in);
        }

        @Override
        public Follower[] newArray(int size) {
            return new Follower[size];
        }
    };

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getScreenname() {
        return screenname;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public String getBackgroundImageURL() {
        return backgroundImageURL;
    }

    public String getBio() {
        return bio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScreenname(String screenname) {
        this.screenname = screenname;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setBackgroundImageURL(String backgroundImageURL) {
        this.backgroundImageURL = backgroundImageURL;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(screenname);
        parcel.writeString(profilePicURL);
        parcel.writeString(backgroundImageURL);
        parcel.writeString(bio);
    }
}
