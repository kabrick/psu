package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.SupervisionChecklistFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.SupervisionChecklistFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class AdrReportFormFeedActivity extends AppCompatActivity implements SupervisionChecklistFeedAdapter.SupervisionChecklistFeedAdapterListener {

    private List<SupervisionChecklistFeed> formList;
    private SupervisionChecklistFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adr_report_form_feed);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_adr);
        formList = new ArrayList<>();
        mAdapter = new SupervisionChecklistFeedAdapter(formList, this);

        progressBar = findViewById(R.id.progressBarADR);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchForms();
    }

    @Override
    public boolean onSupportNavigateUp() {
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        return true;
    }

    private void fetchForms() {
        String url = helperFunctions.getIpAddress() + "get_adr_form.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    progressBar.setVisibility(View.GONE);

                    List<SupervisionChecklistFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<SupervisionChecklistFeed>>() {
                    }.getType());

                    formList.clear();
                    formList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchForms();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onFormSelected(SupervisionChecklistFeed form) {
        Intent intent = new Intent(this, AdrReportFormDetailsActivity.class);
        intent.putExtra("id", form.getId());
        startActivity(intent);
    }
}
