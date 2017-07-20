package com.example.abdo.twitter_light.Activities.Classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abdo on 7/19/17.
 */

public class UserInfo implements Parcelable
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

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getProfile_background_image_url_https() {
        return profile_background_image_url_https;
    }

    public void setProfile_background_image_url_https(String profile_background_image_url_https) {
        this.profile_background_image_url_https = profile_background_image_url_https;
    }

    public UserInfo() {
    }

    public UserInfo(String name, String profile_image_url_https, String profile_background_image_url_https) {
        this.name = name;
        this.profile_image_url_https = profile_image_url_https;
        this.profile_background_image_url_https = profile_background_image_url_https;
    }

    protected UserInfo(Parcel in) {
        name = in.readString();
        profile_image_url_https = in.readString();
        profile_background_image_url_https = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(profile_image_url_https);
        dest.writeString(profile_background_image_url_https);
    }
}
