package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class FeedbackActivity extends AppCompatActivity {

    TextView feedback_title, feedback_text;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    View activityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        feedback_text = findViewById(R.id.feedback_text);
        feedback_title = findViewById(R.id.feedback_title);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        activityView = findViewById(R.id.create_feedback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel_create_news) {
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }

        return super.onOptionsItemSelected(item);
    }

    public void postNews(View view){
        //show progress dialog
        helperFunctions.genericProgressBar("Posting your feedback...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_feedback.php?title=" + feedback_title.getText().toString()
                + "&text=" + feedback_text.getText().toString()
                + "&author_id=" + preferenceManager.getPsuId()
                + "&model=" + Build.MODEL
                + "&brand=" + Build.BRAND
                + "&sdk=" + String.valueOf(Build.VERSION.SDK_INT)
                + "&version=" + String.valueOf(Build.VERSION.RELEASE)
                + "&product=" + Build.PRODUCT
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
                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        } else {
                            helperFunctions.genericSnackbar("Posting feedback failed!", activityView);
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
                    helperFunctions.genericSnackbar("Data from server not available", activityView);
                }
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
