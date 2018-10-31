package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsCommentsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.NewsComments;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NewsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp;
    String title_string, text_string, author_string, timestamp_string, id;
    HelperFunctions helperFunctions;
    FloatingActionButton create_comment_fab;

    PreferenceManager preferenceManager;

    private List<NewsComments> newsCommentsList;
    private NewsCommentsAdapter mAdapter;

    View activityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        activityView = findViewById(R.id.news_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        create_comment_fab = findViewById(R.id.create_comment_fab);

        create_comment_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater1 = LayoutInflater.from(NewsViewActivity.this);
                View view1 = inflater1.inflate(R.layout.add_news_comment, null);

                final EditText editText = view1.findViewById(R.id.comment_text);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewsViewActivity.this);
                alertDialog.setTitle("Post your comment");
                alertDialog.setView(view1);
                alertDialog.setCancelable(false)
                        .setPositiveButton("Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        postComment(editText.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog1 = alertDialog.create();
                alertDialog1.show();
            }
        });

        title = findViewById(R.id.news_feed_title_single);
        text = findViewById(R.id.news_feed_text_single);
        author = findViewById(R.id.news_feed_author_single);
        timestamp = findViewById(R.id.news_feed_timestamp_single);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            title_string = extras.getString("title", "");
            text_string = extras.getString("text", "");
            author_string = extras.getString("author", "");
            timestamp_string = extras.getString("timestamp", "");
            id = extras.getString("id", "");
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(timestamp_string),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        timestamp.setText(timeAgo);
        title.setText(title_string);
        text.setText(text_string);
        author.setText(author_string);

        //add id to read news
        try {
            helperFunctions.addNewsRead(Integer.parseInt(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.comments_recycler);
        newsCommentsList = new ArrayList<>();
        mAdapter = new NewsCommentsAdapter(newsCommentsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchComments();
    }

    public void fetchComments(){
        String url = helperFunctions.getIpAddress() + "get_comments.php?id=" + id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<NewsComments> items = new Gson().fromJson(response.toString(), new TypeToken<List<NewsComments>>() {
                        }.getType());

                        newsCommentsList.clear();
                        newsCommentsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchComments();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void postComment(String comment){
        //show progress dialog
        helperFunctions.genericProgressBar("Posting your comment...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_comments.php?news_id=" + id
                + "&comment=" + comment
                + "&psu_id=" + preferenceManager.getPsuId()
                + "&timestamp=" + timestamp_long.toString();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("1")){
                            //saved article successfully
                            helperFunctions.genericSnackbar("Posting comment successful!", activityView);
                            fetchComments();
                        } else {
                            helperFunctions.genericSnackbar("Posting comment failed!", activityView);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    helperFunctions.genericSnackbar("Connection Error. Please check your connection", activityView);
                } else if (error instanceof AuthFailureError) {
                    helperFunctions.genericSnackbar("Authentication error", activityView);
                } else if (error instanceof ServerError) {
                    helperFunctions.genericSnackbar("Server error", activityView);
                } else if (error instanceof NetworkError) {
                    helperFunctions.genericSnackbar("Network error", activityView);
                } else if (error instanceof ParseError) {
                    helperFunctions.genericSnackbar("Data from server not Available", activityView);
                }
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
