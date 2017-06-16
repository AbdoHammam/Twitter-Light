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
    private List<Follower> followers;
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
        Call<Followers> call = apiInterface.getFollowers(id);
        call.enqueue(new Callback<Followers>() {
            @Override
            public void onResponse(Call<Followers> call, Response<Followers> response) {
                followers = response.body().getFollowers();
                Toast.makeText(FollowersActivity.this,followers.size(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Followers> call, Throwable t) {

            }
        });

    }

}
