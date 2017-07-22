package com.example.abdo.twitter_light.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.abdo.twitter_light.Activities.API.ApiClient;
import com.example.abdo.twitter_light.Activities.API.ApiInterface;
import com.example.abdo.twitter_light.Activities.API.GetUserInfoResponse;
import com.example.abdo.twitter_light.Activities.Classes.UserInfo;
import com.example.abdo.twitter_light.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.abdo.twitter_light.Activities.FollowersActivity.authorizationHeaderGetFollowers;

public class TweetsActivity extends AppCompatActivity {

    long id;
    ListView tweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        final UserTimeline userTimeline = new UserTimeline.Builder().maxItemsPerRequest(10).includeRetweets(false)
                .userId(id)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        tweets = (ListView) findViewById(R.id.list_tweets);
        tweets.setAdapter(adapter);
        final ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GetUserInfoResponse> userInfo = apiService.getUserInfo(authorizationHeaderGetFollowers, id);
        userInfo.enqueue(new Callback<GetUserInfoResponse>() {
            CircleImageView imgProfilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
            ImageView background_photo = (ImageView) findViewById(R.id.background_photo);
            TextView username = (TextView) findViewById(R.id.username);

            @Override
            public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    UserInfo userInfo = new UserInfo(response.body().getName(), response.body().getProfile_image_url_https(), response.body().getProfile_background_image_url_https());

                    Picasso.with(TweetsActivity.this).load(userInfo.getProfile_image_url_https())
                            .resize(imgProfilePicture.getWidth(), imgProfilePicture.getHeight()).
                            into(imgProfilePicture);
                    Picasso.with(TweetsActivity.this).load(userInfo.getProfile_background_image_url_https()).
                    into(background_photo);
                    username.setText(userInfo.getName());
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {

            }
        });
    }
}
