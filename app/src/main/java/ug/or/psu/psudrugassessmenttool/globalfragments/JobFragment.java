package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    HelperFunctions helperFunctions;

    ProgressBar progressBar;
    FloatingActionButton fab;

    public JobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_job, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_jobs);
        jobsList = new ArrayList<>();
        mAdapter = new JobsFeedAdapter(jobsList, this);

        progressBar = view.findViewById(R.id.progressBarJobs);

        fab = view.findViewById(R.id.add_jobs_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent post_jobs_intent = new Intent(getContext(), CreateJobsActivity.class);
                Objects.requireNonNull(getContext()).startActivity(post_jobs_intent);
            }
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
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // stop progress bar
                        progressBar.setVisibility(View.GONE);

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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchJobs();
            }
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
