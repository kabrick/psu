package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.NewsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NewsViewPostedActivity extends AppCompatActivity implements NewsFeedAdapter.NewsFeedAdapterListener {

    private List<NewsFeed> newsList;
    private NewsFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view_posted);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_news_posted);
        newsList = new ArrayList<>();
        mAdapter = new NewsFeedAdapter(this, newsList, this);

        progressBar = findViewById(R.id.progressBarNewsPosted);

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
        String url = helperFunctions.getIpAddress() + "get_news_posted.php?name=" + preferenceManager.getPsuName();

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //news has been got so stop progress bar
                        progressBar.setVisibility(View.GONE);

                        List<NewsFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<NewsFeed>>() {
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

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * function to obtain selected news item and go to full view activity
     *
     * @param news news feed model for item clicked
     */
    @Override
    public void onNewsItemSelected(final NewsFeed news) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("News Action");

        //convert array list to string array
        String[] mStringArray = {"Edit", "Delete"};

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    // go to edit
                    Intent intent = new Intent(NewsViewPostedActivity.this, EditNewsActivity.class);
                    intent.putExtra("id", news.getId());
                    startActivity(intent);
                } else if (i == 1){
                    // delete the record
                    delete_record(news.getId());
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void delete_record(String id){
        helperFunctions.genericProgressBar("Deleting news record");
        String network_address = helperFunctions.getIpAddress() + "delete_news.php?id=" + id;

        // Request a string response from the provided URL
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("1")){
                            new SweetAlertDialog(NewsViewPostedActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("News article deleted")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            fetchNews();
                                        }
                                    })
                                    .show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
