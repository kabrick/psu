package ug.or.psu.psudrugassessmenttool.globalactivities;

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

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class CreateJobsActivity extends AppCompatActivity {

    TextView jobs_title, jobs_text, jobs_phone, jobs_email;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    View activityView;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        jobs_title = findViewById(R.id.jobs_title);
        jobs_text = findViewById(R.id.jobs_text);
        jobs_phone = findViewById(R.id.jobs_phone);
        jobs_email = findViewById(R.id.jobs_email);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        activityView = findViewById(R.id.create_jobs);
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

        String title = jobs_title.getText().toString();
        String text = jobs_text.getText().toString();

        if(TextUtils.isEmpty(title)) {
            jobs_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(text)) {
            jobs_text.setError("Please fill in the text");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting your job advert...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_jobs.php?title=" + title
                + "&text=" + text
                + "&contact=" + jobs_phone.getText().toString()
                + "&email=" + jobs_email.getText().toString()
                + "&author_id=" + preferenceManager.getPsuId()
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
                            new SweetAlertDialog(CreateJobsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Job has been posted")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(CreateJobsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong! Please try again")
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new SweetAlertDialog(CreateJobsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong! Please try again")
                        .show();
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
