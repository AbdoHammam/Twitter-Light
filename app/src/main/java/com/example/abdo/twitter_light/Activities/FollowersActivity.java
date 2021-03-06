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
    List<Follower> followers = null;
    Integer nextCursor = null;
    String nextCursorStr = null;
    Integer previousCursor = null;
    String previousCursorStr = null;
    Call<GetUserInfoResponse> userInfo;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        // Initialize data and its holders
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
        CallAPI();
        swipeRefreshLayout.setOnRefreshListener(this);
        pref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_name),0);
    }

    // base64Encode to be encode twitter consumer key and secret to be able to make "application-only" authentication
    public static String base64Encode(String token) {
        String result = Base64.encodeToString(token.getBytes(), Base64.DEFAULT);
        result = result.replace("\n", "");
        return result;
    }
    // Call the API to load the data
    public void CallAPI()
    {
        apiService = ApiClient.getApiClient().create(ApiInterface.class);
        call = apiService.authenticate(authorizationHeader, contentTypeHeader, body);
        call.enqueue(new Callback<OAuthResponse>() {
            @Override
            // Read the authentication response to read the access token which allows the developer to get data using twitter API
            public void onResponse(Call<OAuthResponse> call, Response<OAuthResponse> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    tokenType = response.body().getTokenType();
                    accessToken = response.body().getAccessToken();
                }
                // Add a header to get followers of the logged in user
                if (tokenType != null && accessToken != null) {
                    authorizationHeaderGetFollowers = "Bearer " + accessToken;
                    cursor = -1;
                    apiServiceGetFollowers = ApiClient.getApiClient().create(ApiInterface.class);
                    callGetFollowers = apiServiceGetFollowers.getFollowers(authorizationHeaderGetFollowers, cursor, id);
                    callGetFollowers.enqueue(new Callback<GetFollowersResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<GetFollowersResponse> call, Response<GetFollowersResponse> response) {
                            TextView numOfFollowers = (TextView) findViewById(R.id.numOfFollowers);
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
                                    SharedPreferences.Editor editor = pref.edit();
                                    if(isNetworkAvailable())
                                    {
                                        //Cache the followers
                                        editor.putInt("numOfFollowers",numofFollowers);
                                        for(int i=0 ; i < numofFollowers ; i++)
                                        {
                                            editor.putString("Follower#"+String.valueOf(i)+"username",followers.get(i).getUsername());
                                            editor.putString("Follower#"+String.valueOf(i)+"bio",followers.get(i).getBio());
                                            editor.putString("Follower#"+String.valueOf(i)+"profilePic",followers.get(i).getProfilePicURL());
                                            editor.putLong("Follower#"+String.valueOf(i)+"id",followers.get(i).getId());

                                        }
                                        editor.apply();
                                    }
                                    else
                                    {
                                        Follower temp = new Follower();
                                        for(int i=0 ; i < numofFollowers ; i++)
                                        {
                                            temp.setUsername(pref.getString("Follower#"+String.valueOf(i)+"username",""));
                                            temp.setBio(pref.getString("Follower#"+String.valueOf(i)+"bio",""));
                                            temp.setProfilePicURL(pref.getString("Follower#"+String.valueOf(i)+"profilePic",""));
                                            temp.setId(pref.getLong("Follower#"+String.valueOf(i)+"id",0));
                                            followers.add(temp);
                                        }
                                    }
                                }
                            } else {
                                numofFollowers = pref.getInt("numOfFollowers",0);
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_message) , Toast.LENGTH_LONG).show();
                            }
                            numOfFollowers.setText(String.valueOf(numofFollowers) + " Followers");

                            adapter.updateAdapter(followers);
                        }

                        @Override
                        public void onFailure(Call<GetFollowersResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                    // Get the logged in user data
                    userInfo = apiService.getUserInfo(authorizationHeaderGetFollowers, id);
                    userInfo.enqueue(new Callback<GetUserInfoResponse>() {
                        CircleImageView imgProfilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
                        ImageView background_photo = (ImageView) findViewById(R.id.background_photo);

                        @Override
                        public void onResponse(Call<GetUserInfoResponse> call, Response<GetUserInfoResponse> response) {
                            if (response != null && response.body() != null && response.isSuccessful()) {
                                UserInfo userInfo = new UserInfo(response.body().getName(), response.body().getProfile_image_url_https(), response.body().getProfile_background_image_url_https());

                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_image_url_https())
                                        .into(imgProfilePicture);
                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_background_image_url_https()).into(background_photo);
                                SharedPreferences.Editor editor = pref.edit();

                                username.setText(userInfo.getName());
                                editor.putString("currentUsername",userInfo.getName());
                                editor.putString("currentUserProfilePic",userInfo.getProfile_image_url_https()); // Picasso caches the image if the url has been used before
                                editor.putString("currentUserBackgroundImage",userInfo.getProfile_background_image_url_https());
                                editor.apply();
                            }
                            else
                            {
                                UserInfo userInfo = new UserInfo();
                                userInfo.setName(pref.getString("currentUsername",""));
                                userInfo.setProfile_image_url_https(pref.getString("currentUserProfilePic",""));
                                userInfo.setProfile_background_image_url_https(pref.getString("currentUserBackgroundImage",""));

                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_image_url_https())
                                        .into(imgProfilePicture);
                                Picasso.with(FollowersActivity.this).load(userInfo.getProfile_background_image_url_https()).into(background_photo);
                                username.setText(userInfo.getName());
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

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                CallAPI();
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
