package com.example.nburk.apollodemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.AllPostsQuery;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by nburk on 26.10.17.
 */

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.PostViewHolder> {

    private List<AllPostsQuery.AllPost> posts = Collections.emptyList();
    private Context context;

    MainRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setPosts(List<AllPostsQuery.AllPost> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
        Log.d(ApolloDemoApplication.TAG, "Updated posts in adapter: " + posts.size());
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.post_item_entry, parent, false);

        return new PostViewHolder(itemView, context);
    }

    @Override public void onBindViewHolder(PostViewHolder holder, int position) {
        final AllPostsQuery.AllPost post = this.posts.get(position);
        holder.setPost(post);
    }

    @Override public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView postDescription;
        private ImageView postImage;
        private View postEntryContainer;
        private Context context;

        PostViewHolder(View itemView, Context context) {
            super(itemView);
            postDescription = (TextView) itemView.findViewById(R.id.post_description);
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            postEntryContainer = itemView.findViewById(R.id.post_entry_container);
            this.context = context;
        }

        void setPost(final AllPostsQuery.AllPost post) {
            postDescription.setText(post.description());
            Picasso.with(context).load(post.imageUrl()).into(postImage);

            postEntryContainer.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                }
            });

        }
    }

}
