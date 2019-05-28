package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedback_text = findViewById(R.id.feedback_text);
        feedback_title = findViewById(R.id.feedback_title);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        activityView = findViewById(R.id.create_feedback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        if (id == R.id.action_post_create_news) {
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }

        return super.onOptionsItemSelected(item);
    }

    public void postFeedback(View view){

        String title = feedback_title.getText().toString();
        String text = feedback_text.getText().toString();

        if(TextUtils.isEmpty(title)) {
            feedback_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(text)) {
            feedback_text.setError("Please fill in the text");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting your feedback...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_feedback.php?title=" + title
                + "&text=" + text
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(FeedbackActivity.this);

                            alert.setMessage("Feedback has been posted").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                }
                            }).show();
                        } else {
                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
