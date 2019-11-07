package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
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
import ug.or.psu.psudrugassessmenttool.adapters.SupervisionChecklistFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.SupervisionChecklistFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class WholesaleInspectionFeedActivity extends AppCompatActivity implements SupervisionChecklistFeedAdapter.SupervisionChecklistFeedAdapterListener  {

    private List<SupervisionChecklistFeed> formList;
    private SupervisionChecklistFeedAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesale_inspection_feed);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_wholesale);
        formList = new ArrayList<>();
        mAdapter = new SupervisionChecklistFeedAdapter(formList, this);

        progressBar = findViewById(R.id.progressBarWholesale);

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
        String url = helperFunctions.getIpAddress() + "get_supervision_checklist_wholesale.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        List<SupervisionChecklistFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<SupervisionChecklistFeed>>() {
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
    public void onFormSelected(SupervisionChecklistFeed form) {
        Intent intent = new Intent(this, ViewWholesaleInspectionChecklistActivity.class);
        intent.putExtra("id", form.getId());
        startActivity(intent);
    }
}
