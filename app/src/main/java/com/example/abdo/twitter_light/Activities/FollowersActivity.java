package com.example.abdo.twitter_light.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.abdo.twitter_light.Activities.API.ApiClient;
import com.example.abdo.twitter_light.Activities.API.ApiInterface;
import com.example.abdo.twitter_light.Activities.Adapters.FollowersAdapter;
import com.example.abdo.twitter_light.Activities.Classes.Follower;
import com.example.abdo.twitter_light.R;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {
    Long id;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Follower> followers;
    private List<Long> followersIDs;
    private FollowersAdapter adapter;
    private ApiInterface apiInterface;
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
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Long> > call = apiInterface.getFriendsIDs(id);
        call.enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                followersIDs = response.body();
                Toast.makeText(FollowersActivity.this,followersIDs.size(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {

            }
        });
        //TODO : get followers from API
        //adapter = new FollowersAdapter(followers,this);
        //recyclerView.setAdapter(adapter);
    }

}
