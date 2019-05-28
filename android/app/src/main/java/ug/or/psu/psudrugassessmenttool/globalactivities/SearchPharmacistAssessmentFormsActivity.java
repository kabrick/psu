package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

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
import ug.or.psu.psudrugassessmenttool.adapters.PharmacistAssessmentSearchAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentSearch;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class SearchPharmacistAssessmentFormsActivity extends AppCompatActivity implements PharmacistAssessmentSearchAdapter.PharmacistAssessmentSearchAdapterListener {

    private List<PharmacistAssessmentSearch> pharmacistsList;
    private PharmacistAssessmentSearchAdapter mAdapter;

    HelperFunctions helperFunctions;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pharmacist_assessment_forms);

        RecyclerView recyclerView = findViewById(R.id.recycler_pharmacist_assessment);
        pharmacistsList = new ArrayList<>();
        mAdapter = new PharmacistAssessmentSearchAdapter(pharmacistsList, this);

        helperFunctions = new HelperFunctions(this);

        progressBar = findViewById(R.id.progressBarPharmacistAssessment);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(this), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        SearchView searchView = findViewById(R.id.search_pharmacist_assessment);

        searchView.setQueryHint("Search pharmacists");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        fetchPharmacies();
    }

    private void fetchPharmacies() {
        String url = helperFunctions.getIpAddress() + "get_pharmacists_search_assessment_forms.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //hide the progress bar
                        progressBar.setVisibility(View.GONE);

                        List<PharmacistAssessmentSearch> items = new Gson().fromJson(response.toString(), new TypeToken<List<PharmacistAssessmentSearch>>() {
                        }.getType());

                        pharmacistsList.clear();
                        pharmacistsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchPharmacies();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onPharmacistSelected(PharmacistAssessmentSearch pharmacist) {
        String id = pharmacist.getId();

        Intent intent = new Intent(SearchPharmacistAssessmentFormsActivity.this, PharmacistAssessmentFormFeedActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
