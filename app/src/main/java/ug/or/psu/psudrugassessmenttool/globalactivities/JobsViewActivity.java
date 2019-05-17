package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp, source;
    String jobs_id, psu_id, image_string, attachment_url;
    ImageView jobs_image;
    CardView attachment_card;
    TextView attachment_name;

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

        attachment_card = findViewById(R.id.attachment_card);
        attachment_name = findViewById(R.id.attachment_name);

        attachment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAttachment();
            }
        });

        title = findViewById(R.id.jobs_feed_title_single);
        text = findViewById(R.id.jobs_feed_text_single);
        author = findViewById(R.id.jobs_feed_author_single);
        timestamp = findViewById(R.id.jobs_feed_timestamp_single);
        source = findViewById(R.id.jobs_view_source);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            jobs_id = extras.getString("id", "1");
        }

        jobs_image = findViewById(R.id.jobs_image);

        getJobDetails();
        fetchJobsImage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchJobsImage(){
        String network_address = helperFunctions.getIpAddress()
                + "get_jobs_image.php?id=" + jobs_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fetchAttachmentUrl();

                        try {
                            // check if image is null
                            if(response.getString("photo").equals("0")){
                                //
                            } else {
                                image_string = helperFunctions.getIpAddress() + response.getString("photo");

                                Glide.with(JobsViewActivity.this)
                                        .load(image_string)
                                        .into(jobs_image);

                                jobs_image.setVisibility(View.VISIBLE);
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
                            source.setText("Source: " + response.getString("source"));
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

    public void fetchAttachmentUrl(){
        String network_address = helperFunctions.getIpAddress()
                + "get_jobs_attachment.php?id=" + jobs_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.getString("attachment_url").equals("0")){
                                //
                            } else {
                                attachment_url = helperFunctions.getIpAddress() + response.getString("attachment_url");
                                attachment_name.setText("Download " + response.getString("attachment_name"));
                                attachment_card.setVisibility(View.VISIBLE);
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

    public void downloadAttachment(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(attachment_url));
        startActivity(intent);
    }
}
