package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.EditYourPharmaciesAdapter;
import ug.or.psu.psudrugassessmenttool.adapters.MyJobAdvertsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.MyJobAdverts;
import ug.or.psu.psudrugassessmenttool.models.Pharmacies;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class MyJobAdvertsActivity extends AppCompatActivity {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_adverts);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        getAdverts();
    }

    @Override
    public boolean onSupportNavigateUp() {
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        return true;
    }

    public void getAdverts(){

        helperFunctions.genericProgressBar("Fetching your adverts");

        final ArrayList<MyJobAdverts> adverts = new ArrayList<>();

        String network_address = helperFunctions.getIpAddress() + "get_my_job_adverts.php?psu_id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        for(int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);
                                adverts.add(new MyJobAdverts(obj.getString("title"), obj.getString("company_name"), obj.getString("deadline"), obj.getString("id")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // continue to display
                        RecyclerView recyclerView = findViewById(R.id.recycler_my_job_adverts);

                        MyJobAdvertsAdapter mAdapter = new MyJobAdvertsAdapter(MyJobAdvertsActivity.this, adverts);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyJobAdvertsActivity.this);
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(MyJobAdvertsActivity.this).addToRequestQueue(request);
    }
}
