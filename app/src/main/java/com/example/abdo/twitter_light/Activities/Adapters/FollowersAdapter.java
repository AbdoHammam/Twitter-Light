package com.example.abdo.twitter_light.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abdo.twitter_light.Activities.Classes.Follower;
import com.example.abdo.twitter_light.Activities.TweetsActivity;
import com.example.abdo.twitter_light.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by abdo on 6/14/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.MyViewHolder> {
    private List<Follower> followers;

    private static Context context;

    public FollowersAdapter(Context context) {
        this.context = context;
    }

    public void updateAdapter(List<Follower> followers) {
        if (followers != null) {
            this.followers = followers;
        } else {
            this.followers = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.username.setText(followers.get(position).getUsername());
        holder.screenname.setText(followers.get(position).getScreenname());
        Picasso.with(context).load(followers.get(position).getProfilePicURL()).
                into(holder.imgProfilePicture);
        holder.bio.setText(followers.get(position).getBio());
        holder.id = followers.get(position).getId();
        holder.follower = followers.get(position);
    }

    @Override
    public int getItemCount() {
        return followers == null ? 0 : followers.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfilePicture;
        TextView username;
        TextView screenname;
        TextView bio;
        Long id;
        Follower follower;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgProfilePicture = (CircleImageView) itemView.findViewById(R.id.imgProfilePicture);
            username = (TextView) itemView.findViewById(R.id.username);
            screenname = (TextView) itemView.findViewById(R.id.screenname);
            bio = (TextView) itemView.findViewById(R.id.bio);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TweetsActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}