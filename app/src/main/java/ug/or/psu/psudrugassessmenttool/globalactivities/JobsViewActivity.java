package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp, phone, email,
            salary, deadline, location, contract, company;
    String id, psu_id;
    long deadline_timestamp;

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
        phone = findViewById(R.id.jobs_feed_phone_single);
        email = findViewById(R.id.jobs_feed_email_single);
        salary = findViewById(R.id.jobs_feed_salary_single);
        deadline = findViewById(R.id.jobs_feed_deadline_single);
        location = findViewById(R.id.jobs_feed_location_single);
        contract = findViewById(R.id.jobs_feed_contract_single);
        company = findViewById(R.id.jobs_feed_company_single);

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
                            email.setText("Email: " + response.getString("email"));
                            phone.setText("Phone: " + response.getString("contact"));
                            salary.setText("Salary range: " + response.getString("salary_range"));
                            location.setText("Location: " + response.getString("location"));
                            contract.setText("Contract type: " + response.getString("contract_type"));
                            company.setText("Company name: " + response.getString("company_name"));

                            deadline_timestamp = Long.parseLong(response.getString("deadline"));

                            @SuppressLint("SimpleDateFormat")
                            String date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date (deadline_timestamp));
                            deadline.setText("Deadline: " + date);
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

        if (System.currentTimeMillis() > deadline_timestamp){
            helperFunctions.genericDialog("The application deadline has passed");
            return;
        }

        helperFunctions.genericProgressBar("Posting your application...");

        String network_address = helperFunctions.getIpAddress()
                + "post_job_application.php?id=" + id
                + "&psu_id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if (response.equals("2")){
                            helperFunctions.genericDialog("You have already applied for this job");
                        }else if(response.equals("1")){
                            //saved successfully
                            AlertDialog.Builder alert = new AlertDialog.Builder(JobsViewActivity.this);

                            alert.setMessage("Job application has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // redirect to jobs view
                                    // finish();
                                    helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                }
                            }).show();
                        } else {
                            helperFunctions.genericDialog("Something went wrong! Please try again");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.genericDialog("Something went wrong! Please try again");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
