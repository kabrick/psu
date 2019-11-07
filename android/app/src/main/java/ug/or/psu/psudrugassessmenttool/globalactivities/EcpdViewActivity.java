package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.EcpdResultsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.EcpdResults;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EcpdViewActivity extends AppCompatActivity implements EcpdResultsAdapter.EcpdResultsAdapterListener {

    String ecpd_id;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    TextView ecpd_title, ecpd_description, ecpd_timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_view);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        ecpd_title = findViewById(R.id.ecpd_title);
        ecpd_description = findViewById(R.id.ecpd_description);
        ecpd_timestamp = findViewById(R.id.ecpd_timestamp);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ecpd_id = extras.getString("id", "1");
        }

        getDetails();
        fetchForms();
    }

    public void getDetails(){

        helperFunctions.genericProgressBar("Fetching e-CPD...");

        String network_address = helperFunctions.getIpAddress() + "get_cpd_form.php?id=" + ecpd_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {

            helperFunctions.stopProgressBar();

                    try {
                        //covert timestamp to readable format
                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(response.getString("timestamp")),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                        ecpd_title.setText(response.getString("title"));
                        ecpd_description.setText(response.getString("description"));

                        String info = "Submitted by " + response.getString("author") + " - " + timeAgo;
                        ecpd_timestamp.setText(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void editForm(View view){
        String[] mStringArray = {"View || Edit Questions", "Edit e-CPD"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EcpdViewActivity.this);
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            if (i == 0){
                Intent intent = new Intent(EcpdViewActivity.this, EcpdAddQuestionsActivity.class);
                intent.putExtra("id", ecpd_id);
                startActivity(intent);
            } else if (i == 1){
                Toast.makeText(this, "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteForm(View view){
        helperFunctions.genericProgressBar("Deleting e-cpd...");
        String network_address = helperFunctions.getIpAddress() + "delete_cpd.php?id=" + ecpd_id;

        // Request a string response from the provided URL
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("1")){
                        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(EcpdViewActivity.this);

                        alert.setMessage("e-cpd deleted").setPositiveButton("Okay", (dialogInterface, i) -> {
                           finish();
                           onBackPressed();
                        }).show();
                    }

                }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchForms() {
        String url = helperFunctions.getIpAddress() + "get_cpd_tests.php";

        List<EcpdResults> formList;
        EcpdResultsAdapter mAdapter;
        TextView no_tests_found = findViewById(R.id.no_tests_found);

        RecyclerView recyclerView = findViewById(R.id.recycler_ecpd_results);
        formList = new ArrayList<>();
        mAdapter = new EcpdResultsAdapter(formList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    if (response.length() < 1) {
                        no_tests_found.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }

                    List<EcpdResults> items = new Gson().fromJson(response.toString(), new TypeToken<List<EcpdResults>>() {
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
    public void onFormSelected(EcpdResults form) {
        //
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
