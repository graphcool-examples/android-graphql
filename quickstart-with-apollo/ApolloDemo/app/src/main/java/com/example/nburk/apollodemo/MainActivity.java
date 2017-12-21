package com.example.nburk.apollodemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.AllPostsQuery;
import com.CreatePostMutation;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

// https://laughingsquid.com/wp-content/uploads/2013/07/QjJjgS0.jpg
// https://i.pinimg.com/originals/6d/90/af/6d90af5a16ed15f7e2c4a092b8884700.jpg

public class MainActivity extends AppCompatActivity {

    ApolloDemoApplication application;
    MainRecyclerViewAdapter postsAdapter;
    ViewGroup content;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Log.d(ApolloDemoApplication.TAG, "Creating activity...");

        application = (com.example.nburk.apollodemo.ApolloDemoApplication) getApplication();

        content = (ViewGroup) findViewById(R.id.content_holder);
        progressBar = (ProgressBar) findViewById(R.id.loading_bar);

        RecyclerView postsRecyclerView = (RecyclerView) findViewById(R.id.rv_posts_list);
        postsAdapter = new MainRecyclerViewAdapter(application.getApplicationContext());
        postsRecyclerView.setAdapter(postsAdapter);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchPosts();
    }

    private ApolloCall.Callback<CreatePostMutation.Data> createPostMutationCallback = new ApolloCall.Callback<CreatePostMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreatePostMutation.Data> dataResponse) {
            Log.d(ApolloDemoApplication.TAG, "Executed mutation: " + dataResponse.data());
            fetchPosts();
        }
        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(ApolloDemoApplication.TAG, "Error:" + e.toString());
        }
    };

    private ApolloCall.Callback<AllPostsQuery.Data> allPostsQueryCallback = new ApolloCall.Callback<AllPostsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<AllPostsQuery.Data> dataResponse) {
            Log.d(ApolloDemoApplication.TAG, "Received posts: " + dataResponse.data().allPosts().size());
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override public void run() {
                    postsAdapter.setPosts(dataResponse.data().allPosts());
                    progressBar.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.d(ApolloDemoApplication.TAG, "Error:" + e.toString());
        }
    };

    private void fetchPosts() {
        Log.d(ApolloDemoApplication.TAG, "Fetch posts ....");
        application.apolloClient().query(
                AllPostsQuery.builder()
                        .build()
        ).httpCachePolicy(HttpCachePolicy.CACHE_FIRST)
                .enqueue(allPostsQueryCallback);
    }

    private void createPost(String description, String imageUrl) {
        application.apolloClient().mutate(
                CreatePostMutation.builder()
                        .description(description)
                        .imageUrl(imageUrl)
                        .build())
                .enqueue(createPostMutationCallback);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_post, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_create_post, null);
        final EditText descriptionInput = (EditText) viewInflated.findViewById(R.id.description);
        final EditText imageUrlInput = (EditText) viewInflated.findViewById(R.id.imageUrl);

        builder.setView(viewInflated)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String description = descriptionInput.getText().toString();
                        String imageUrl = imageUrlInput.getText().toString();
                        Log.d(ApolloDemoApplication.TAG, "Create post with: " + description + ", " + imageUrl);
                        createPost(description, imageUrl);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
        return super.onOptionsItemSelected(item);
    }



}
