package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.JobApplicationsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.JobApplications;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobApplicationsActivity extends AppCompatActivity {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    String job_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_applications);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            job_id = extras.getString("id", "1");
        }

        getApplications();
    }

    @Override
    public boolean onSupportNavigateUp() {
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        return true;
    }

    public void getApplications(){

        helperFunctions.genericProgressBar("Fetching applications...");

        final ArrayList<JobApplications> applications = new ArrayList<>();

        String network_address = helperFunctions.getIpAddress() + "get_job_applications.php?id=" + job_id;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        for(int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);
                                applications.add(new JobApplications(obj.getString("photo"), obj.getString("name"), obj.getString("email"), obj.getString("phone"), obj.getString("cover_letter"), obj.getString("cv")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // continue to display
                        RecyclerView recyclerView = findViewById(R.id.recycler_job_applications);

                        JobApplicationsAdapter mAdapter = new JobApplicationsAdapter(JobApplicationsActivity.this, applications);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(JobApplicationsActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(JobApplicationsActivity.this).addToRequestQueue(request);
    }
}
