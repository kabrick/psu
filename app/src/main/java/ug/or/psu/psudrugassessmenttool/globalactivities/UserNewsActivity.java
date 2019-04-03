package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.UserNewsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.UserNewsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class UserNewsActivity extends AppCompatActivity implements UserNewsFeedAdapter.UserNewsFeedAdapterListener {

    private List<UserNewsFeed> newsList;
    private UserNewsFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    String user_id;

    TextView user_news_name, user_news_position;
    ImageView user_news_profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            user_id = extras.getString("user_id", "");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_user_news);
        newsList = new ArrayList<>();
        mAdapter = new UserNewsFeedAdapter(this, newsList, this);

        progressBar = findViewById(R.id.progressBarUserNews);
        user_news_name = findViewById(R.id.user_news_name);
        user_news_position = findViewById(R.id.user_news_position);
        user_news_profile_picture = findViewById(R.id.user_news_profile_picture);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchUserDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchUserDetails() {
        helperFunctions.genericProgressBar("Fetching profile details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_profile_details.php?id=" + user_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getProfilePicture();

                        try {
                            user_news_name.setText(response.getString("name"));
                            user_news_position.setText(response.getString("phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(UserNewsActivity.this).addToRequestQueue(request);
    }

    public void getProfilePicture(){

        String network_address = helperFunctions.getIpAddress()
                + "get_profile_picture.php?id=" + user_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            helperFunctions.stopProgressBar();

                            String picture = helperFunctions.getIpAddress() + response.getString("photo");
                            Glide.with(UserNewsActivity.this)
                                    .load(picture)
                                    .into(user_news_profile_picture);

                            fetchNews();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchNews() {
        String url = helperFunctions.getIpAddress() + "get_user_news.php?id=" + user_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //news has been got so stop progress bar
                        progressBar.setVisibility(View.GONE);

                        List<UserNewsFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<UserNewsFeed>>() {
                        }.getType());

                        newsList.clear();
                        newsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchNews();
            }
        });

        VolleySingleton.getInstance(UserNewsActivity.this).addToRequestQueue(request);
    }

    /**
     * function to obtain selected news item and go to full view activity
     *
     * @param news news feed model for item clicked
     */
    @Override
    public void onNewsItemSelected(UserNewsFeed news) {
        Intent intent = new Intent(UserNewsActivity.this, NewsViewActivity.class);
        intent.putExtra("text", news.getText());
        intent.putExtra("title", news.getTitle());
        intent.putExtra("author", news.getAuthor());
        intent.putExtra("timestamp", news.getTimeStamp());
        intent.putExtra("id", news.getId());
        startActivity(intent);
    }
}
