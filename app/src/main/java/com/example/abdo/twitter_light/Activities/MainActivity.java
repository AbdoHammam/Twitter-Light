package com.example.abdo.twitter_light.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.abdo.twitter_light.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {
    TwitterLoginButton loginButton;
    private String consumer_key;
    private String consumer_secret;
    String username;
    Long id = -1L;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitTwitter(); // Initialize twitter API
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name),0); // 0 means private mode
        id = pref.getLong("user_id",-1L); // Cache the user id to be able to login automatically without authentication in every time
        if(id!=-1L)
        {
            Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            finish();
        }
        else if(isNetworkAvailable())
            Login();
        else
            Toast.makeText(MainActivity.this,"Check your internet connection",Toast.LENGTH_SHORT).show();
    }

    private void InitTwitter() {
        consumer_key = getResources().getString(R.string.consumer_key);
        consumer_secret = getResources().getString(R.string.consumer_secret);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(consumer_key, consumer_secret))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void Login() {
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                id = result.data.getUserId();
                username = String.valueOf(result.data.getUserName());

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, FollowersActivity.class);
        intent.putExtra("id", id);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("user_id",id);
        editor.apply();
        startActivity(intent);
        finish();
    }
    //Check if there's internet connection or no to make the user able to login and get his data
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


