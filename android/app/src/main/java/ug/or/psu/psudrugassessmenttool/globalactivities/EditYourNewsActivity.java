package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.UserNewsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.UserNewsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditYourNewsActivity extends AppCompatActivity implements UserNewsFeedAdapter.UserNewsFeedAdapterListener {

    private List<UserNewsFeed> newsList;
    private UserNewsFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_news);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_your_news);
        newsList = new ArrayList<>();
        mAdapter = new UserNewsFeedAdapter(this, newsList, this);

        progressBar = findViewById(R.id.progressBarYourNews);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchNews();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchNews() {
        String url = helperFunctions.getIpAddress() + "get_your_news.php?id=" + preferenceManager.getPsuId();

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

        VolleySingleton.getInstance(EditYourNewsActivity.this).addToRequestQueue(request);
    }

    /**
     * function to obtain selected news item and go to full view activity
     *
     * @param news news feed model for item clicked
     */
    @Override
    public void onNewsItemSelected(final UserNewsFeed news) {

        String[] mStringArray = {"Edit News Article", "Delete News Article"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    Intent intent = new Intent(EditYourNewsActivity.this, EditNewsActivity.class);
                    intent.putExtra("id", news.getId());
                    startActivity(intent);
                } else if (i == 1){
                    new AlertDialog.Builder(EditYourNewsActivity.this)
                            .setMessage("Are you sure you want to delete this news article")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    helperFunctions.genericProgressBar("Removing news article..");
                                    String network_address = helperFunctions.getIpAddress() + "delete_news.php?id=" + news.getId();

                                    // Request a string response from the provided URL
                                    StringRequest request = new StringRequest(network_address,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    helperFunctions.stopProgressBar();

                                                    if(response.equals("1")){
                                                        new AlertDialog.Builder(EditYourNewsActivity.this)
                                                                .setMessage("News article deleted successfully")
                                                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        // reload the news view
                                                                        newsList.clear();
                                                                        progressBar.setVisibility(View.VISIBLE);
                                                                        fetchNews();
                                                                    }
                                                                }).show();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            helperFunctions.stopProgressBar();

                                            if (error instanceof TimeoutError || error instanceof NetworkError) {
                                                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                                            } else {
                                                helperFunctions.genericDialog("Something went wrong. Please try again later");
                                            }
                                        }
                                    });

                                    //add to request queue in singleton class
                                    VolleySingleton.getInstance(EditYourNewsActivity.this).addToRequestQueue(request);
                                }
                            }).show();
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
