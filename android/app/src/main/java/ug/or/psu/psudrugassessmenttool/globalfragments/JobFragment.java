package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.JobsFeedAdapter;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateJobsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.JobsViewActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.JobsFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobFragment extends Fragment implements JobsFeedAdapter.JobsFeedAdapterListener {

    private View view;
    private List<JobsFeed> jobsList;
    private JobsFeedAdapter mAdapter;

    private HelperFunctions helperFunctions;
    private ShimmerFrameLayout shimmer_view_container;
    private RecyclerView recyclerView;

    public JobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_job, container, false);
        shimmer_view_container = view.findViewById(R.id.jobs_shimmer_view_container);

        recyclerView = view.findViewById(R.id.recycler_view_jobs);
        jobsList = new ArrayList<>();
        mAdapter = new JobsFeedAdapter(jobsList, this, getContext());

        FloatingActionButton fab = view.findViewById(R.id.add_jobs_fab);

        fab.setOnClickListener(view -> {
            Intent post_jobs_intent = new Intent(getContext(), CreateJobsActivity.class);
            Objects.requireNonNull(getContext()).startActivity(post_jobs_intent);
        });

        helperFunctions = new HelperFunctions(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchJobs();
        return view;
    }

    private void fetchJobs() {
        String url = helperFunctions.getIpAddress() + "get_jobs.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    // stop progress bar
                    shimmer_view_container.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    if (response == null) {
                        // toast message about information not being found
                        helperFunctions.genericSnackbar("No jobs available", view);
                        return;
                    }

                    List<JobsFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<JobsFeed>>() {
                    }.getType());

                    jobsList.clear();
                    jobsList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchJobs();
                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * function to obtain selected job item and go to full view activity
     *
     * @param job job feed model for item clicked
     */
    @Override
    public void onJobsItemSelected(JobsFeed job) {
        Intent intent = new Intent(getContext(), JobsViewActivity.class);
        intent.putExtra("id", job.getId());
        startActivity(intent);
    }

}
