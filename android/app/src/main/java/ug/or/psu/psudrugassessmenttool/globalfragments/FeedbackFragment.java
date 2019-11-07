package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
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

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.FeedbackAdapter;
import ug.or.psu.psudrugassessmenttool.globalactivities.FeedbackDetailsActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.Feedback;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class FeedbackFragment extends Fragment implements FeedbackAdapter.FeedbackAdapterListener {

    private View view;
    private List<Feedback> feedbackList;
    private FeedbackAdapter mAdapter;

    HelperFunctions helperFunctions;

    ProgressBar progressBar;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feedback, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_feedback);
        feedbackList = new ArrayList<>();
        mAdapter = new FeedbackAdapter(feedbackList, this);

        progressBar = view.findViewById(R.id.progressBarFeedback);

        helperFunctions = new HelperFunctions(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchFeedback();

        return view;
    }

    private void fetchFeedback() {
        String url = helperFunctions.getIpAddress() + "get_feedback.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // stop progress bar
                        progressBar.setVisibility(View.GONE);

                        if (response == null) {
                            // toast message about information not being found
                            helperFunctions.genericSnackbar("No feedback available", view);
                            return;
                        }

                        List<Feedback> items = new Gson().fromJson(response.toString(), new TypeToken<List<Feedback>>() {
                        }.getType());

                        feedbackList.clear();
                        feedbackList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchFeedback();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * function to obtain selected feedback item and go to full view activity
     *
     * @param feedback feedback model for item clicked
     */
    @Override
    public void onFeedbackItemSelected(Feedback feedback) {
        Intent intent = new Intent(getContext(), FeedbackDetailsActivity.class);
        intent.putExtra("id", feedback.getId());
        startActivity(intent);
    }

}
