package com.example.abdo.twitter_light.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.widget.Toast;

import com.example.abdo.twitter_light.Activities.API.ApiClient;
import com.example.abdo.twitter_light.Activities.API.ApiInterface;
import com.example.abdo.twitter_light.Activities.API.GetFollowersResponse;
import com.example.abdo.twitter_light.Activities.API.OAuthResponse;
import com.example.abdo.twitter_light.Activities.Adapters.FollowersAdapter;
import com.example.abdo.twitter_light.Activities.Classes.Follower;
import com.example.abdo.twitter_light.Activities.Classes.Followers;
import com.example.abdo.twitter_light.R;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {
    Long id;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FollowersAdapter adapter;
    public static final String consumer_key = "Dme1JtqTXCTPssqoQTUEnIwSK";
    public static final String consumer_secret = "5tOImBv1N5VIZXspuqcygPDmSRjGKboaQE1Lj6RFM8sda20yOk";

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

        String authorizationHeader = "Basic " + encodedBase64;
        String contentTypeHeader = "application/x-www-form-urlencoded;charset=UTF-8";
        String body = "client_credentials";

        ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
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


                    String authorizationHeaderGetFollowers = "Bearer " + accessToken;
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

