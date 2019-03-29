package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import ug.or.psu.psudrugassessmenttool.adapters.PharmacistAssessmentFeedOwnerAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentFeedOwner;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class PharmacistAssessmentFormFeedOwnerActivity extends AppCompatActivity implements PharmacistAssessmentFeedOwnerAdapter.PharmacistAssessmentFeedOwnerAdapterListener {

    private List<PharmacistAssessmentFeedOwner> formList;
    private PharmacistAssessmentFeedOwnerAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_assessment_form_feed_owner);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_assessment_owner);
        formList = new ArrayList<>();
        mAdapter = new PharmacistAssessmentFeedOwnerAdapter(formList, this);

        progressBar = findViewById(R.id.progressBarAssessmentOwner);

        fab = findViewById(R.id.assessment_owner_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_form_intent = new Intent(PharmacistAssessmentFormFeedOwnerActivity.this, PharmacistAssessmentFormActivity.class);
                startActivity(new_form_intent);
            }
        });

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
        String url = helperFunctions.getIpAddress() + "get_assessment_form_owner.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        List<PharmacistAssessmentFeedOwner> items = new Gson().fromJson(response.toString(), new TypeToken<List<PharmacistAssessmentFeedOwner>>() {
                        }.getType());

                        formList.clear();
                        formList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchForms();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onFormSelected(PharmacistAssessmentFeedOwner form) {
        /*Intent intent = new Intent(this, UserNewsActivity.class);
        intent.putExtra("user_id", form.getId());
        startActivity(intent);*/

        Toast.makeText(this, form.getScore(), Toast.LENGTH_LONG).show();
    }
}
