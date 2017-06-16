package com.example.abdo.twitter_light.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.abdo.twitter_light.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    String twitter_key = "sop7FgxeonfPcUh5Xs6gBaDte";
    String twitter_secret = "xA0YKURETtDDYU2WD4f8hqsnUPFSgF1Rl5Y9g6lG5u6WdXJiwZ";
    TwitterLoginButton  loginButton;
    String username;
    Long id = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitTwitter();
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
                id = result.data.getUserId();
                username = String.valueOf(result.data.getUserName());
                try {
                    TwitterAdvancedSetup();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
    }
    private void TwitterAdvancedSetup() throws IOException {
        TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        OAuthSigning oauthSigning = new OAuthSigning(authConfig, authToken);
        Map<String, String> authHeaders = oauthSigning.getOAuthEchoHeadersForVerifyCredentials();
        URL url = new URL("https://api.twitter.com/1.1/check_credentials.json");
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            Toast.makeText(MainActivity.this,entry.getKey() + " " + entry.getValue(),Toast.LENGTH_SHORT).show();
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

}
