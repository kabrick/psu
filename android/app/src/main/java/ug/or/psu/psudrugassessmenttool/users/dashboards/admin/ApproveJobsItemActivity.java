package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ApproveJobsItemActivity extends AppCompatActivity {

    TextView title, text, author, timestamp, source;
    ImageView jobs_image;
    String title_string, text_string, author_string, timestamp_string, id, source_string;

    FloatingActionButton approve_jobs_fab, reject_jobs_fab;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_jobs_item);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        approve_jobs_fab = findViewById(R.id.approve_jobs_fab);
        reject_jobs_fab = findViewById(R.id.reject_jobs_fab);

        approve_jobs_fab.setOnClickListener(view -> approveJob());
        reject_jobs_fab.setOnClickListener(view -> rejectJob());

        title = findViewById(R.id.approve_jobs_feed_title_single);
        text = findViewById(R.id.approve_jobs_feed_text_single);
        author = findViewById(R.id.approve_jobs_feed_author_single);
        timestamp = findViewById(R.id.approve_jobs_feed_timestamp_single);
        source = findViewById(R.id.approve_jobs_source);

        jobs_image = findViewById(R.id.approve_jobs_image);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            title_string = extras.getString("title", "");
            text_string = extras.getString("text", "");
            author_string = extras.getString("author", "");
            timestamp_string = extras.getString("timestamp", "");
            source_string = extras.getString("source", "");
            id = extras.getString("id", "");
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(timestamp_string),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        timestamp.setText(timeAgo);
        title.setText(title_string);
        text.setText(text_string);
        author.setText("Posted By: " + author_string);
        source.setText("Source: " + source_string);

        fetchJobsImage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchJobsImage(){
        helperFunctions.genericProgressBar("Fetching job...");

        String network_address = helperFunctions.getIpAddress()
                + "get_jobs_image.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
                    helperFunctions.stopProgressBar();

                    try {
                        // check if image is null
                        if (!response.getString("photo").equals("0")) {
                            String picture = helperFunctions.getIpAddress() + response.getString("photo");

                            Glide.with(ApproveJobsItemActivity.this)
                                    .load(picture)
                                    .into(jobs_image);

                            jobs_image.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            //
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void approveJob(){
        //show progress dialog
        helperFunctions.genericProgressBar("Approving job advert...");

        String network_address = helperFunctions.getIpAddress() + "approve_job.php?id=" + id + "&psu_id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();
                    helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                }, error -> {
            //
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void rejectJob(){
        //show progress dialog
        helperFunctions.genericProgressBar("Rejecting job advert...");

        String network_address = helperFunctions.getIpAddress() + "reject_job.php?id=" + id + "&psu_id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();
                    finish();

                    startActivity(new Intent(ApproveJobsItemActivity.this, ApproveJobsActivity.class));
                }, error -> {
            //
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
