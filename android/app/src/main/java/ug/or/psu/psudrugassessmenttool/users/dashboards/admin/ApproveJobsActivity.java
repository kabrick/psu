package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.ApproveJobsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.ApproveJobsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ApproveJobsActivity extends AppCompatActivity implements ApproveJobsFeedAdapter.ApproveJobsFeedAdapterListener {

    private List<ApproveJobsFeed> jobsList;
    private ApproveJobsFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_jobs);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_approve_jobs);
        jobsList = new ArrayList<>();
        mAdapter = new ApproveJobsFeedAdapter(this, jobsList, this);

        progressBar = findViewById(R.id.progressBarApproveJobs);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchJobs();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void fetchJobs() {
        String url = helperFunctions.getIpAddress() + "get_approve_jobs.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    progressBar.setVisibility(View.GONE);

                    List<ApproveJobsFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<ApproveJobsFeed>>() {
                    }.getType());

                    jobsList.clear();
                    jobsList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
            // error in getting json, so recursive call till successful
            fetchJobs();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * function to obtain selected jobs item and go to full view activity
     *
     * @param job model for item clicked
     */
    @Override
    public void onJobsItemSelected(ApproveJobsFeed job) {
        Intent intent = new Intent(this, ApproveJobsItemActivity.class);
        intent.putExtra("text", job.getText());
        intent.putExtra("title", job.getTitle());
        intent.putExtra("author", job.getAuthor());
        intent.putExtra("timestamp", job.getTimeStamp());
        intent.putExtra("source", job.getSource());
        intent.putExtra("id", job.getId());
        startActivity(intent);
    }
}
