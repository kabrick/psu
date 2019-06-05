package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
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
import ug.or.psu.psudrugassessmenttool.adapters.PharmacyCoordinatesAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.PharmacyCoordinates;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewAllPharmacyCoordinatesActivity extends AppCompatActivity {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private List<PharmacyCoordinates> attendanceList;
    private PharmacyCoordinatesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_pharmacy_coordinates);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.all_pharmacy_coordinates_recycler);
        attendanceList = new ArrayList<>();
        mAdapter = new PharmacyCoordinatesAdapter(attendanceList);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getCoordinates();
    }

    public void getCoordinates(){
        helperFunctions.genericProgressBar("Fetching pharmacies");

        String network_address = helperFunctions.getIpAddress() + "get_pharmacies_locations.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        helperFunctions.stopProgressBar();

                        List<PharmacyCoordinates> items = new Gson().fromJson(response.toString(), new TypeToken<List<PharmacyCoordinates>>() {
                        }.getType());

                        attendanceList.clear();
                        attendanceList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
