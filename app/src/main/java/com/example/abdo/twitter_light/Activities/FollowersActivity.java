package com.example.abdo.twitter_light.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abdo.twitter_light.Activities.API.ApiClient;
import com.example.abdo.twitter_light.Activities.API.ApiInterface;
import com.example.abdo.twitter_light.Activities.API.GetFollowersResponse;
import com.example.abdo.twitter_light.Activities.API.GetUserInfoResponse;
import com.example.abdo.twitter_light.Activities.API.OAuthResponse;
import com.example.abdo.twitter_light.Activities.Adapters.FollowersAdapter;
import com.example.abdo.twitter_light.Activities.Classes.Follower;
import com.example.abdo.twitter_light.Activities.Classes.UserInfo;
import com.example.abdo.twitter_light.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {
    Long id;
    int numofFollowers = 0;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FollowersAdapter adapter;
    public static final String consumer_key = "Dme1JtqTXCTPssqoQTUEnIwSK";
    public static final String consumer_secret = "5tOImBv1N5VIZXspuqcygPDmSRjGKboaQE1Lj6RFM8sda20yOk";
    public static String authorizationHeaderGetFollowers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        recyclerView = (RecyclerView) findViewById(R.id.followersList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FollowersAdapter(this);
        recyclerView.setAdapter(adapter);
        String encodedBase64 = base64Encode(consumer_key + ":" + consumer_secret);
        final String authorizationHeader = "Basic " + encodedBase64;
        String contentTypeHeader = "application/x-www-form-urlencoded;charset=UTF-8";
        String body = "client_credentials";

        final ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
        Call<OAuthResponse> call = apiService.authenticate(authorizationHeader, contentTypeHeader, body);
        call.enqueue(new Callback<OAuthResponse>() {
            @Override
            public void onResponse(Call<OAuthResponse> call, Response<OAuthResponse> response) {
                String tokenType = null;
                String accessToken = null;

                if (response != null && response.body() != null && response.isSuccessful()) {
                    tokenType = response.body().getTokenType();
                    accessToken = response.body().getAccessToken();
                }

                if (tokenType != null && accessToken != null) {


                    authorizationHeaderGetFollowers = "Bearer " + accessToken;
                    Integer cursor = -1;

                    ApiInterface apiServiceGetFollowers = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<GetFollowersResponse> callGetFollowers = apiServiceGetFollowers.getFollowers(authorizationHeaderGetFollowers, cursor, id);
                    callGetFollowers.enqueue(new Callback<GetFollowersResponse>() {
                        @Override
                        public void onResponse(Call<GetFollowersResponse> call, Response<GetFollowersResponse> response) {

                            List<Follower> followers = null;
                            Integer nextCursor = null;
                            String nextCursorStr = null;
                            Integer previousCursor = null;
                            String previousCursorStr = null;

                            if (response != null && response.body() != null && response.isSuccessful()) {
                                followers = response.body().getUsers();
                                nextCursor = response.body().getNextCursor();
                                nextCursorStr = response.body().getNextCursorStr();
                                previousCursor = response.body().getPreviousCursor();
                                previousCursorStr = response.body().getPreviousCursorStr();
                                numofFollowers = followers.size();
                            }

                            if (followers != null && nextCursor != null && nextCursorStr != null && previousCursor != null && previousCursorStr != null) {

                                if (adapter != null) {
                                    adapter.updateAdapter(followers);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetFollowersResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
                        }
                    });

                    Call<GetUserInfoResponse> userInfo = apiService.getUserInfo(authorizationHeaderGetFollowers, id);
                    userInfo.enqueue(new Callback<GetUserInfoResponse>() {
                        CircleImageView imgProfilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
                        ImageView background_photo = (ImageView) findViewById(R.id.background_photo);
                        TextView numOfFollowers = (TextView) findViewById(R.id.numOfFollowers);

                        @Override
                        public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                            if (response != null && response.body() != null && response.isSuccessful()) {
                                UserInfo userInfo = new UserInfo(response.body().getName(), response.body().getProfile_image_url_https(), response.body().getProfile_background_image_url_https());

                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_image_url_https()).into(imgProfilePicture);
                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_background_image_url_https()).into(background_photo);

                                numOfFollowers.setText(String.valueOf(numofFollowers) + " Followers");
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OAuthResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Can't connect to server", Toast.LENGTH_LONG).show();
            }
        });

    }


    public static String base64Encode(String token) {
        String result = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);
        result = result.replace("\n", "");
        return result;
    }
}

