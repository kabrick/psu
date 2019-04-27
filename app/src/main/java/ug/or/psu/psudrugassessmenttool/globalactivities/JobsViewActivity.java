package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp;
    String id;
    String email = null;
    String phone = null;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    View activityView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_email_job:
                    if (!TextUtils.isEmpty(email)){
                        // email is provided
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + email));
                        startActivity(emailIntent);
                    } else {
                        // email is not provided
                        helperFunctions.genericSnackbar("Email is not available", activityView);
                    }
                    return false;
                case R.id.action_phone_job:
                    if (!TextUtils.isEmpty(phone)){
                        // phone number is provided
                        Intent intent_phone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(intent_phone);
                    } else {
                        // phone number is not provided
                        helperFunctions.genericSnackbar("Phone number is not available", activityView);
                    }
                    return false;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_view);

        progressBar = findViewById(R.id.progressBarJobDetails);

        activityView = findViewById(R.id.news_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        title = findViewById(R.id.jobs_feed_title_single);
        text = findViewById(R.id.jobs_feed_text_single);
        author = findViewById(R.id.jobs_feed_author_single);
        timestamp = findViewById(R.id.jobs_feed_timestamp_single);

        BottomNavigationView navigation = findViewById(R.id.bottom_nav_jobs);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getString("id", "1");
        }

        getJobDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getJobDetails(){
        String network_address = helperFunctions.getIpAddress()
                + "get_job_details.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            Log.e("ss", response.toString());
                            //covert timestamp to readable format
                            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                    Long.parseLong(response.getString("timestamp")),
                                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                            timestamp.setText(timeAgo);
                            title.setText(response.getString("title"));
                            text.setText(response.getString("text"));
                            author.setText(response.getString("author"));

                            email = response.getString("email");
                            phone = response.getString("contact");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getJobDetails();
            }
        });

        VolleySingleton.getInstance(JobsViewActivity.this).addToRequestQueue(request);
    }
}
