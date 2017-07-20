package com.example.abdo.twitter_light.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.abdo.twitter_light.R;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TweetsActivity extends AppCompatActivity {

    long id;
    ListView tweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .userId(id)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        tweets = (ListView) findViewById(R.id.list_tweets);
        tweets.setAdapter(adapter);

    }
}
