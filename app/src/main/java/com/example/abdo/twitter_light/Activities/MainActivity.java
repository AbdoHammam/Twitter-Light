package com.example.abdo.twitter_light.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    Long id = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitTwitter();
        setContentView(R.layout.activity_main);
        Login();
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
        startActivity(intent);
        finish();
    }
}


