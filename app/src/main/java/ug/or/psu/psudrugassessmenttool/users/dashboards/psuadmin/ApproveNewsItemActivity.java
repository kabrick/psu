package ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ApproveNewsItemActivity extends AppCompatActivity {

    TextView title, text, author, timestamp;
    ImageView news_image;
    String title_string, text_string, author_string, timestamp_string, id;

    FloatingActionButton approve_news_fab, reject_news_fab, download_news_attachment_fab;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    String pdf_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_news_item);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        approve_news_fab = findViewById(R.id.approve_news_fab);
        download_news_attachment_fab = findViewById(R.id.download_news_attachment_fab);
        reject_news_fab = findViewById(R.id.reject_news_fab);

        approve_news_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveNews();
            }
        });

        download_news_attachment_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdf();
            }
        });

        reject_news_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rejectNews();
            }
        });

        title = findViewById(R.id.approve_news_feed_title_single);
        text = findViewById(R.id.approve_news_feed_text_single);
        author = findViewById(R.id.approve_news_feed_author_single);
        timestamp = findViewById(R.id.approve_news_feed_timestamp_single);

        news_image = findViewById(R.id.approve_news_image);

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

        fetchNewsImage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchNewsImage(){
        helperFunctions.genericProgressBar("Fetching news...");

        String network_address = helperFunctions.getIpAddress()
                + "get_news_picture.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fetchPdfUrl();

                        try {
                            // check if image is null
                            if(response.getString("photo").equals("0")){
                                //
                            } else {
                                String picture = helperFunctions.getIpAddress() + response.getString("photo");

                                Glide.with(ApproveNewsItemActivity.this)
                                        .load(picture)
                                        .into(news_image);

                                news_image.setVisibility(View.VISIBLE);
                            }
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

    public void fetchPdfUrl(){
        String network_address = helperFunctions.getIpAddress()
                + "get_news_pdf.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        helperFunctions.stopProgressBar();

                        try {
                            // check if image is null
                            if(response.getString("pdf").equals("0")){
                                //
                            } else {
                                pdf_url = helperFunctions.getIpAddress() + response.getString("pdf");

                                download_news_attachment_fab.setVisibility(View.VISIBLE);
                            }
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

    public void downloadPdf(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(pdf_url));
        startActivity(intent);
    }

    public void approveNews(){
        //show progress dialog
        helperFunctions.genericProgressBar("Approving news article...");

        String network_address = helperFunctions.getIpAddress() + "approve_news.php?id=" + id;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        Intent approve_news_intent = new Intent(ApproveNewsItemActivity.this, ApproveNewsActivity.class);
                        startActivity(approve_news_intent);
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

    public void rejectNews(){
        //show progress dialog
        helperFunctions.genericProgressBar("Rejecting news article...");

        String network_address = helperFunctions.getIpAddress() + "reject_news.php?id=" + id;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        Intent approve_news_intent = new Intent(ApproveNewsItemActivity.this, ApproveNewsActivity.class);
                        startActivity(approve_news_intent);
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
