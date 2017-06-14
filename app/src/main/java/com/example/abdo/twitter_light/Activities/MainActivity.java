package com.example.abdo.twitter_light.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String twitter_key = "sop7FgxeonfPcUh5Xs6gBaDte";
    String twitter_secret = "xA0YKURETtDDYU2WD4f8hqsnUPFSgF1Rl5Y9g6lG5u6WdXJiwZ";
    TwitterLoginButton  loginButton;
    String username;
    String id="id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitTwitter();
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);
        Login();

    }

    private void InitTwitter()
    {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(twitter_key, twitter_secret))
                .debug(true)
                .build();
        Twitter.initialize(config);

    }
    private void Login()
    {
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                id = String.valueOf(result.data.getUserId());
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
        Intent intent = new Intent(MainActivity.this,FollowersActivity.class);
        startActivity(intent);
    }

}
