package com.example.abdo.twitter_light.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
public class FollowersActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Long id;
    int numofFollowers = 0;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FollowersAdapter adapter;
    private String consumer_key;
    private String consumer_secret;
    public static String authorizationHeaderGetFollowers;
    TextView username;
    String encodedBase64;
    String authorizationHeader;
    SwipeRefreshLayout swipeRefreshLayout;
    String contentTypeHeader;
    String body;
    ApiInterface apiService;
    Call<OAuthResponse> call;
    String tokenType = null;
    String accessToken = null;
    Integer cursor;
    ApiInterface apiServiceGetFollowers;
    Call<GetFollowersResponse> callGetFollowers;
    List<Follower> followers;
    Integer nextCursor = null;
    String nextCursorStr = null;
    Integer previousCursor = null;
    String previousCursorStr = null;
    Call<GetUserInfoResponse> userInfo;
    SharedPreferences pref;
    UserInfo currentuserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        username = (TextView) findViewById(R.id.username);
        Bundle bundle = getIntent().getExtras();
        consumer_key = getResources().getString(R.string.consumer_key);
        consumer_secret = getResources().getString(R.string.consumer_secret);
        id = bundle.getLong("id");
        recyclerView = (RecyclerView) findViewById(R.id.followersList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FollowersAdapter(this);
        recyclerView.setAdapter(adapter);
        encodedBase64 = base64Encode(consumer_key + ":" + consumer_secret);
        authorizationHeader = "Basic " + encodedBase64;
        contentTypeHeader = getResources().getString(R.string.contentTypeHeader);
        body = getResources().getString(R.string.auth_body);
        if(isNetworkAvailable())
        {
            CallAPI();
            CacheData();
        }
        else
        {
            getCachedData();
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        ShowData();

    }

    private void CacheData()
    {
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name),0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("numOfFollowers",numofFollowers);
        editor.putString("currentUsername",currentuserInfo.getName());
        editor.putString("currentUserProfilePic",currentuserInfo.getProfile_image_url_https()); // Picasso caches the image if the url has been used before
        editor.putString("currentUserBackgroundImage",currentuserInfo.getProfile_background_image_url_https());
        for(int i=0 ; i < numofFollowers ; i++)
        {
            editor.putString("Follower#"+String.valueOf(i)+"username",followers.get(i).getUsername());
            editor.putString("Follower#"+String.valueOf(i)+"bio",followers.get(i).getBio());
            editor.putString("Follower#"+String.valueOf(i)+"profilePic",followers.get(i).getProfilePicURL());
            editor.putLong("Follower#"+String.valueOf(i)+"id",followers.get(i).getId());

        }
        editor.apply();
    }
    private void getCachedData()
    {
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name),0);
        numofFollowers = pref.getInt("numOfFollowers",0);
        currentuserInfo.setName(pref.getString("currentUsername",""));
        currentuserInfo.setProfile_image_url_https(pref.getString("currentUserProfilePic",""));
        currentuserInfo.setProfile_background_image_url_https(pref.getString("currentUserBackgroundImage",""));
        followers.clear();
        for(int i=0 ; i < numofFollowers ; i++)
        {
            Follower temp = new Follower();
            temp.setUsername(pref.getString("Follower#"+String.valueOf(i)+"username",""));
            temp.setBio(pref.getString("Follower#"+String.valueOf(i)+"bio",""));
            temp.setProfilePicURL(pref.getString("Follower#"+String.valueOf(i)+"profilePic",""));
            temp.setId(pref.getLong("Follower#"+String.valueOf(i)+"id",0));
            followers.add(temp);
        }


    }
    public static String base64Encode(String token) {
        String result = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);
        result = result.replace("\n", "");
        return result;
    }
    private void CallAPI()
    {
        apiService = ApiClient.getApiClient().create(ApiInterface.class);
        call = apiService.authenticate(authorizationHeader, contentTypeHeader, body);
        call.enqueue(new Callback<OAuthResponse>() {
            @Override
            public void onResponse(Call<OAuthResponse> call, Response<OAuthResponse> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    tokenType = response.body().getTokenType();
                    accessToken = response.body().getAccessToken();
                }
                if (tokenType != null && accessToken != null) {
                    authorizationHeaderGetFollowers = "Bearer " + accessToken;
                    cursor = -1;
                    apiServiceGetFollowers = ApiClient.getApiClient().create(ApiInterface.class);
                    callGetFollowers = apiServiceGetFollowers.getFollowers(authorizationHeaderGetFollowers, cursor, id);
                    callGetFollowers.enqueue(new Callback<GetFollowersResponse>() {
                        @Override
                        public void onResponse(Call<GetFollowersResponse> call, Response<GetFollowersResponse> response) {


                            if (response != null && response.body() != null && response.isSuccessful()) {
                                followers = response.body().getUsers();
                                nextCursor = response.body().getNextCursor();
                                nextCursorStr = response.body().getNextCursorStr();
                                previousCursor = response.body().getPreviousCursor();
                                previousCursorStr = response.body().getPreviousCursorStr();
                                numofFollowers = followers.size();
                            }

                        }

                        @Override
                        public void onFailure(Call<GetFollowersResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
                        }
                    });

                    userInfo = apiService.getUserInfo(authorizationHeaderGetFollowers, id);
                    userInfo.enqueue(new Callback<GetUserInfoResponse>() {
                        @Override
                        public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                            if (response != null && response.body() != null && response.isSuccessful()) {
                                currentuserInfo = new UserInfo(response.body().getName(), response.body().getProfile_image_url_https(), response.body().getProfile_background_image_url_https());
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUserInfoResponse> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OAuthResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        });


    }
    @SuppressLint("SetTextI18n")
    private void ShowData()
    {
        if (followers != null && nextCursor != null && nextCursorStr != null && previousCursor != null && previousCursorStr != null) {

            if (adapter != null) {
                adapter.updateAdapter(followers);

            }
        } else {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_message) , Toast.LENGTH_LONG).show();
        }
        CircleImageView imgProfilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
        ImageView background_photo = (ImageView) findViewById(R.id.background_photo);
        TextView numOfFollowers = (TextView) findViewById(R.id.numOfFollowers);
        Picasso.with(FollowersActivity.this).load(currentuserInfo.getProfile_image_url_https())
                .into(imgProfilePicture);
        Picasso.with(FollowersActivity.this).load(currentuserInfo.getProfile_background_image_url_https()).into(background_photo);

        numOfFollowers.setText(String.valueOf(numofFollowers) + " Followers");
        username.setText(currentuserInfo.getName());
    }
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if(isNetworkAvailable()) {
                    CallAPI();
                }
                else
                    Toast.makeText(FollowersActivity.this,"Check your internet connection",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
