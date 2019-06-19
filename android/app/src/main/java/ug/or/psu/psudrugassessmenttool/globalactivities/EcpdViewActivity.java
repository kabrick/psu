package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EcpdViewActivity extends AppCompatActivity {

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
    }

    public void getDetails(){

        helperFunctions.genericProgressBar("Fetching E-CPD...");

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
            helperFunctions.genericDialog("Failed to get E-CPD form");
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void editForm(View view){
        String[] mStringArray = {"View || Edit Questions", "Edit E-CPD"};

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
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(EcpdViewActivity.this);

                        alert.setMessage("E-cpd deleted").setPositiveButton("Okay", (dialogInterface, i) -> {
                           finish();
                           onBackPressed();
                        }).show();
                    }

                }, error -> {
            helperFunctions.stopProgressBar();
            helperFunctions.genericDialog("Something went wrong. Please try again later");
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
