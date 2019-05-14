package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
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
    String jobs_id, psu_id;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_view);

        progressBar = findViewById(R.id.progressBarJobDetails);

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            jobs_id = extras.getString("id", "1");
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
                + "get_job_details.php?id=" + jobs_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //covert timestamp to readable format
                            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                    Long.parseLong(response.getString("timestamp")),
                                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                            psu_id = response.getString("author_id");

                            timestamp.setText(timeAgo);
                            title.setText(response.getString("title"));
                            text.setText(response.getString("text"));
                            author.setText(response.getString("author"));
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

    public void apply(View view){

        if(psu_id.equals(preferenceManager.getPsuId())){
            helperFunctions.genericDialog("You can not apply to a job that you posted");
            return;
        }

        Intent intent = new Intent(this, JobsCompleteApplicationActivity.class);
        intent.putExtra("id", jobs_id);
        startActivity(intent);
    }
}
