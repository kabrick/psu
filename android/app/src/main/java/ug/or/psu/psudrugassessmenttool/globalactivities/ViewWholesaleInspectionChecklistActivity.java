package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewWholesaleInspectionChecklistActivity extends AppCompatActivity {

    String id;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    TextView additional_notes,pharmacy_name, contact_name,support_supervision_date,
            section_a_1, section_a_2, section_a_3, section_a_4, section_a_5,
            section_b_1, section_b_2, section_b_3, section_b_4, section_b_5,
            contact,supervising_pharmacist,reg_number,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wholesale_inspection_checklist);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        additional_notes = findViewById(R.id.additional_notes);
        pharmacy_name = findViewById(R.id.pharmacy_name);
        contact_name = findViewById(R.id.contact_name);
        contact = findViewById(R.id.contact);
        supervising_pharmacist = findViewById(R.id.supervising_pharmacist);
        reg_number = findViewById(R.id.reg_number);
        support_supervision_date = findViewById(R.id.support_supervision_date);
        location = findViewById(R.id.location);

        section_a_1 = findViewById(R.id.section_a_1);
        section_a_2 = findViewById(R.id.section_a_2);
        section_a_3 = findViewById(R.id.section_a_3);
        section_a_4 = findViewById(R.id.section_a_4);
        section_a_5 = findViewById(R.id.section_a_5);
        section_b_1 = findViewById(R.id.section_b_1);
        section_b_2 = findViewById(R.id.section_b_2);
        section_b_3 = findViewById(R.id.section_b_3);
        section_b_4 = findViewById(R.id.section_b_4);
        section_b_5 = findViewById(R.id.section_b_5);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("id", "1");

            fetchRecord();
        } else {
            // go to the default dashboard
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchRecord(){

        helperFunctions.genericProgressBar("Retrieving form details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_supervision_checklist_wholesale_single.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            additional_notes.setText(response.getString("additional_notes"));
                            pharmacy_name.setText(response.getString("pharmacy_name"));
                            contact_name.setText(response.getString("contact_name"));
                            contact.setText(response.getString("contact"));
                            supervising_pharmacist.setText(response.getString("supervising_pharmacist"));
                            reg_number.setText(response.getString("reg_number"));
                            support_supervision_date.setText(response.getString("support_supervision_date"));
                            location.setText(response.getString("location"));

                            String[] section_a_checklist = response.getString("section_a_checklist").split(",");
                            String[] section_b_checklist = response.getString("section_b_checklist").split(",");

                            if(section_a_checklist[0].equals("1")){
                                section_a_1.setText("Yes");
                            } else {
                                section_a_1.setText("No");
                            }

                            if(section_a_checklist[1].equals("1")){
                                section_a_2.setText("Yes");
                            } else {
                                section_a_2.setText("No");
                            }

                            if(section_a_checklist[2].equals("1")){
                                section_a_3.setText("Yes");
                            } else {
                                section_a_3.setText("No");
                            }

                            if(section_a_checklist[3].equals("1")){
                                section_a_4.setText("Yes");
                            } else {
                                section_a_4.setText("No");
                            }

                            if(section_a_checklist[4].equals("1")){
                                section_a_5.setText("Yes");
                            } else {
                                section_a_5.setText("No");
                            }

                            if(section_a_checklist[0].equals("1")){
                                section_b_1.setText("Yes");
                            } else {
                                section_b_1.setText("No");
                            }

                            if(section_b_checklist[1].equals("1")){
                                section_b_2.setText("Yes");
                            } else {
                                section_b_2.setText("No");
                            }

                            if(section_b_checklist[2].equals("1")){
                                section_b_3.setText("Yes");
                            } else {
                                section_b_3.setText("No");
                            }

                            if(section_b_checklist[3].equals("1")){
                                section_b_4.setText("Yes");
                            } else {
                                section_b_4.setText("No");
                            }

                            if(section_b_checklist[4].equals("1")){
                                section_b_5.setText("Yes");
                            } else {
                                section_b_5.setText("No");
                            }

                            helperFunctions.stopProgressBar();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helperFunctions.stopProgressBar();
                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();
                helperFunctions.genericDialog("Something went wrong. Please try again");
                onBackPressed();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
