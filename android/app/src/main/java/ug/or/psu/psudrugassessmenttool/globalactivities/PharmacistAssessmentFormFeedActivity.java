package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.PharmacistAssessmentFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class PharmacistAssessmentFormFeedActivity extends AppCompatActivity implements PharmacistAssessmentFeedAdapter.PharmacistAssessmentFeedAdapterListener  {

    private List<PharmacistAssessmentFeed> formList;
    private PharmacistAssessmentFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    TextView pharmacist_assessment_name, pharmacist_assessment_position;
    ImageView pharmacist_assessment_profile_picture;

    String pharmacist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_assessment_form_feed);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacist_id = extras.getString("id", "1");
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_assessment);
        formList = new ArrayList<>();
        mAdapter = new PharmacistAssessmentFeedAdapter(formList, this);

        progressBar = findViewById(R.id.progressBarAssessment);
        pharmacist_assessment_name = findViewById(R.id.pharmacist_assessment_name);
        pharmacist_assessment_position = findViewById(R.id.pharmacist_assessment_position);
        pharmacist_assessment_profile_picture = findViewById(R.id.pharmacist_assessment_profile_picture);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchUserDetails();
    }

    private void fetchUserDetails() {
        helperFunctions.genericProgressBar("Fetching profile details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_profile_details.php?id=" + pharmacist_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getProfilePicture();

                        try {
                            pharmacist_assessment_name.setText(response.getString("name"));
                            pharmacist_assessment_position.setText(response.getString("phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void getProfilePicture(){

        String network_address = helperFunctions.getIpAddress()
                + "get_profile_picture.php?id=" + pharmacist_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            helperFunctions.stopProgressBar();

                            String picture = helperFunctions.getIpAddress() + response.getString("photo");
                            Glide.with(PharmacistAssessmentFormFeedActivity.this)
                                    .load(picture)
                                    .into(pharmacist_assessment_profile_picture);

                            fetchForms();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        return true;
    }

    private void fetchForms() {
        String url = helperFunctions.getIpAddress() + "get_pharmacist_assessment_forms.php?id=" + pharmacist_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        List<PharmacistAssessmentFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<PharmacistAssessmentFeed>>() {
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
    public void onFormSelected(PharmacistAssessmentFeed form) {
        Intent intent = new Intent(this, ViewPharmacistAssessmentFormActivity.class);
        intent.putExtra("id", form.getId());
        startActivity(intent);
    }
}
