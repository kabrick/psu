package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

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
            section_c_1, section_c_2, section_c_3, section_c_4, section_c_5,
            section_c_6, section_c_7, section_c_8, section_c_9, section_c_10,
            section_d_1, section_d_2, section_d_3, section_d_4, section_d_5,
            section_d_6, section_d_7, section_d_8, section_d_9, section_d_10, section_d_11,
            section_e_1, section_e_2, section_e_3, section_e_4, section_e_5,
            section_e_6, section_e_7, section_e_8, section_e_9, section_e_10, section_e_11, section_e_12,
            section_f_1, section_f_2, section_f_3, section_f_4, section_f_5,
            section_f_6, section_f_7,
            contact,supervising_pharmacist,reg_number,location, overall_percentage_score, overall_total_score,
            section_b_total_score, section_a_total_score, section_b_percentage, section_a_percentage,
            section_c_total_score, section_d_total_score, section_c_percentage, section_d_percentage,
            section_e_total_score, section_f_total_score, section_e_percentage, section_f_percentage;

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
        section_c_1 = findViewById(R.id.section_c_1);
        section_c_2 = findViewById(R.id.section_c_2);
        section_c_3 = findViewById(R.id.section_c_3);
        section_c_4 = findViewById(R.id.section_c_4);
        section_c_5 = findViewById(R.id.section_c_5);
        section_c_6 = findViewById(R.id.section_c_6);
        section_c_7 = findViewById(R.id.section_c_7);
        section_c_8 = findViewById(R.id.section_c_8);
        section_c_9 = findViewById(R.id.section_c_9);
        section_c_10 = findViewById(R.id.section_c_10);
        section_d_1 = findViewById(R.id.section_d_1);
        section_d_2 = findViewById(R.id.section_d_2);
        section_d_3 = findViewById(R.id.section_d_3);
        section_d_4 = findViewById(R.id.section_d_4);
        section_d_5 = findViewById(R.id.section_d_5);
        section_d_6 = findViewById(R.id.section_d_6);
        section_d_7 = findViewById(R.id.section_d_7);
        section_d_8 = findViewById(R.id.section_d_8);
        section_d_9 = findViewById(R.id.section_d_9);
        section_d_10 = findViewById(R.id.section_d_10);
        section_d_11 = findViewById(R.id.section_d_11);
        section_e_1 = findViewById(R.id.section_e_1);
        section_e_2 = findViewById(R.id.section_e_2);
        section_e_3 = findViewById(R.id.section_e_3);
        section_e_4 = findViewById(R.id.section_e_4);
        section_e_5 = findViewById(R.id.section_e_5);
        section_e_6 = findViewById(R.id.section_e_6);
        section_e_7 = findViewById(R.id.section_e_7);
        section_e_8 = findViewById(R.id.section_e_8);
        section_e_9 = findViewById(R.id.section_e_9);
        section_e_10 = findViewById(R.id.section_e_10);
        section_e_11 = findViewById(R.id.section_e_11);
        section_e_12 = findViewById(R.id.section_e_12);
        section_f_1 = findViewById(R.id.section_f_1);
        section_f_2 = findViewById(R.id.section_f_2);
        section_f_3 = findViewById(R.id.section_f_3);
        section_f_4 = findViewById(R.id.section_f_4);
        section_f_5 = findViewById(R.id.section_f_5);
        section_f_6 = findViewById(R.id.section_f_6);
        section_f_7 = findViewById(R.id.section_f_7);
        overall_percentage_score = findViewById(R.id.overall_percentage_score);
        overall_total_score = findViewById(R.id.overall_total_score);
        section_b_total_score = findViewById(R.id.section_b_total_score);
        section_a_total_score = findViewById(R.id.section_a_total_score);
        section_b_percentage = findViewById(R.id.section_b_percentage);
        section_a_percentage = findViewById(R.id.section_a_percentage);
        section_c_total_score = findViewById(R.id.section_c_total_score);
        section_d_total_score = findViewById(R.id.section_d_total_score);
        section_e_percentage = findViewById(R.id.section_e_percentage);
        section_f_percentage = findViewById(R.id.section_f_percentage);
        section_e_total_score = findViewById(R.id.section_e_total_score);
        section_f_total_score = findViewById(R.id.section_f_total_score);
        section_c_percentage = findViewById(R.id.section_c_percentage);
        section_d_percentage = findViewById(R.id.section_d_percentage);

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

        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
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
                        String[] section_c_checklist = response.getString("section_c_checklist").split(",");
                        String[] section_d_checklist = response.getString("section_d_checklist").split(",");
                        String[] section_e_checklist = response.getString("section_e_checklist").split(",");
                        String[] section_f_checklist = response.getString("section_f_checklist").split(",");

                        int section_a_total = Integer.parseInt(section_a_checklist[0]) + Integer.parseInt(section_a_checklist[1]) +
                                Integer.parseInt(section_a_checklist[2]) + Integer.parseInt(section_a_checklist[3]) + Integer.parseInt(section_a_checklist[4]);

                        double percentage_a = ((double) section_a_total / 5) * 100;

                        section_a_percentage.setText(percentage_a + " %");
                        section_a_total_score.setText(String.valueOf(section_a_total));

                        // calculate the average score
                        int section_b_total = Integer.parseInt(section_b_checklist[0]) + Integer.parseInt(section_b_checklist[1]) +
                                Integer.parseInt(section_b_checklist[2]) + Integer.parseInt(section_b_checklist[3]) + Integer.parseInt(section_b_checklist[4]);

                        double percentage_b = ((double) section_b_total / 5) * 100;

                        section_b_percentage.setText(percentage_b + " %");
                        section_b_total_score.setText(String.valueOf(section_b_total));

                        // calculate the average score
                        int section_c_total = Integer.parseInt(section_c_checklist[0]) + Integer.parseInt(section_c_checklist[1]) +
                                Integer.parseInt(section_c_checklist[2]) + Integer.parseInt(section_c_checklist[3]) + Integer.parseInt(section_c_checklist[4]) +
                                Integer.parseInt(section_c_checklist[5]) + Integer.parseInt(section_c_checklist[6]) + Integer.parseInt(section_c_checklist[7]) +
                                Integer.parseInt(section_c_checklist[8]) + Integer.parseInt(section_c_checklist[9]);

                        double percentage_c = ((double) section_c_total / 10) * 100;

                        section_c_percentage.setText(percentage_c + " %");
                        section_c_total_score.setText(String.valueOf(section_c_total));

                        // calculate the average score
                        int section_d_total = Integer.parseInt(section_d_checklist[0]) + Integer.parseInt(section_d_checklist[1]) +
                                Integer.parseInt(section_d_checklist[2]) + Integer.parseInt(section_d_checklist[3]) + Integer.parseInt(section_d_checklist[4]) +
                                Integer.parseInt(section_d_checklist[5]) + Integer.parseInt(section_d_checklist[6]) + Integer.parseInt(section_d_checklist[7]) +
                                Integer.parseInt(section_d_checklist[8]) + Integer.parseInt(section_d_checklist[9]) + Integer.parseInt(section_d_checklist[10]);

                        double percentage_d = ((double) section_d_total / 11) * 100;

                        section_d_percentage.setText(percentage_d + " %");
                        section_d_total_score.setText(String.valueOf(section_d_total));

                        // calculate the average score
                        int section_e_total = Integer.parseInt(section_e_checklist[0]) + Integer.parseInt(section_e_checklist[1]) +
                                Integer.parseInt(section_e_checklist[2]) + Integer.parseInt(section_e_checklist[3]) + Integer.parseInt(section_e_checklist[4]) +
                                Integer.parseInt(section_e_checklist[5]) + Integer.parseInt(section_e_checklist[6]) + Integer.parseInt(section_e_checklist[7]) +
                                Integer.parseInt(section_e_checklist[8]) + Integer.parseInt(section_e_checklist[9]) + Integer.parseInt(section_e_checklist[10]) +
                                Integer.parseInt(section_e_checklist[11]);

                        double percentage_e = ((double) section_e_total / 12) * 100;

                        section_e_percentage.setText(percentage_e + " %");
                        section_e_total_score.setText(String.valueOf(section_e_total));

                        // calculate the average score
                        int section_f_total = Integer.parseInt(section_f_checklist[0]) + Integer.parseInt(section_f_checklist[1]) +
                                Integer.parseInt(section_f_checklist[2]) + Integer.parseInt(section_f_checklist[3]) + Integer.parseInt(section_f_checklist[4]) +
                                Integer.parseInt(section_f_checklist[5]) + Integer.parseInt(section_f_checklist[6]);

                        double percentage_f = ((double) section_f_total / 7) * 100;

                        section_f_percentage.setText(percentage_f + " %");
                        section_f_total_score.setText(String.valueOf(section_f_total));

                        int overall_total = section_a_total + section_b_total + section_c_total + section_d_total + section_e_total + section_f_total;

                        double overall_percentage = ((double) overall_total / 50) * 100;

                        overall_percentage_score.setText(overall_percentage + " %");
                        overall_total_score.setText(String.valueOf(overall_total));

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

                        if(section_b_checklist[0].equals("1")){
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

                        if(section_c_checklist[0].equals("1")){
                            section_c_1.setText("Yes");
                        } else {
                            section_c_1.setText("No");
                        }

                        if(section_c_checklist[1].equals("1")){
                            section_c_2.setText("Yes");
                        } else {
                            section_c_2.setText("No");
                        }

                        if(section_c_checklist[2].equals("1")){
                            section_c_3.setText("Yes");
                        } else {
                            section_c_3.setText("No");
                        }

                        if(section_c_checklist[3].equals("1")){
                            section_c_4.setText("Yes");
                        } else {
                            section_c_4.setText("No");
                        }

                        if(section_c_checklist[4].equals("1")){
                            section_c_5.setText("Yes");
                        } else {
                            section_c_5.setText("No");
                        }

                        if(section_c_checklist[5].equals("1")){
                            section_c_6.setText("Yes");
                        } else {
                            section_c_6.setText("No");
                        }

                        if(section_c_checklist[6].equals("1")){
                            section_c_7.setText("Yes");
                        } else {
                            section_c_7.setText("No");
                        }

                        if(section_c_checklist[7].equals("1")){
                            section_c_8.setText("Yes");
                        } else {
                            section_c_8.setText("No");
                        }

                        if(section_c_checklist[8].equals("1")){
                            section_c_9.setText("Yes");
                        } else {
                            section_c_9.setText("No");
                        }

                        if(section_c_checklist[9].equals("1")){
                            section_c_10.setText("Yes");
                        } else {
                            section_c_10.setText("No");
                        }

                        if(section_d_checklist[0].equals("1")){
                            section_d_1.setText("Yes");
                        } else {
                            section_d_1.setText("No");
                        }

                        if(section_d_checklist[1].equals("1")){
                            section_d_2.setText("Yes");
                        } else {
                            section_d_2.setText("No");
                        }

                        if(section_d_checklist[2].equals("1")){
                            section_d_3.setText("Yes");
                        } else {
                            section_d_3.setText("No");
                        }

                        if(section_d_checklist[3].equals("1")){
                            section_d_4.setText("Yes");
                        } else {
                            section_d_4.setText("No");
                        }

                        if(section_d_checklist[4].equals("1")){
                            section_d_5.setText("Yes");
                        } else {
                            section_d_5.setText("No");
                        }

                        if(section_d_checklist[5].equals("1")){
                            section_d_6.setText("Yes");
                        } else {
                            section_d_6.setText("No");
                        }

                        if(section_d_checklist[6].equals("1")){
                            section_d_7.setText("Yes");
                        } else {
                            section_d_7.setText("No");
                        }

                        if(section_d_checklist[7].equals("1")){
                            section_d_8.setText("Yes");
                        } else {
                            section_d_8.setText("No");
                        }

                        if(section_d_checklist[8].equals("1")){
                            section_d_9.setText("Yes");
                        } else {
                            section_d_9.setText("No");
                        }

                        if(section_d_checklist[9].equals("1")){
                            section_d_10.setText("Yes");
                        } else {
                            section_d_10.setText("No");
                        }

                        if(section_d_checklist[10].equals("1")){
                            section_d_11.setText("Yes");
                        } else {
                            section_d_11.setText("No");
                        }

                        if(section_e_checklist[0].equals("1")){
                            section_e_1.setText("Yes");
                        } else {
                            section_e_1.setText("No");
                        }

                        if(section_e_checklist[1].equals("1")){
                            section_e_2.setText("Yes");
                        } else {
                            section_e_2.setText("No");
                        }

                        if(section_e_checklist[2].equals("1")){
                            section_e_3.setText("Yes");
                        } else {
                            section_e_3.setText("No");
                        }

                        if(section_e_checklist[3].equals("1")){
                            section_e_4.setText("Yes");
                        } else {
                            section_e_4.setText("No");
                        }

                        if(section_e_checklist[4].equals("1")){
                            section_e_5.setText("Yes");
                        } else {
                            section_e_5.setText("No");
                        }

                        if(section_e_checklist[5].equals("1")){
                            section_e_6.setText("Yes");
                        } else {
                            section_e_6.setText("No");
                        }

                        if(section_e_checklist[6].equals("1")){
                            section_e_7.setText("Yes");
                        } else {
                            section_e_7.setText("No");
                        }

                        if(section_e_checklist[7].equals("1")){
                            section_e_8.setText("Yes");
                        } else {
                            section_e_8.setText("No");
                        }

                        if(section_e_checklist[8].equals("1")){
                            section_e_9.setText("Yes");
                        } else {
                            section_e_9.setText("No");
                        }

                        if(section_e_checklist[9].equals("1")){
                            section_e_10.setText("Yes");
                        } else {
                            section_e_10.setText("No");
                        }

                        if(section_e_checklist[10].equals("1")){
                            section_e_11.setText("Yes");
                        } else {
                            section_e_11.setText("No");
                        }

                        if(section_e_checklist[11].equals("1")){
                            section_e_12.setText("Yes");
                        } else {
                            section_e_12.setText("No");
                        }

                        if(section_f_checklist[0].equals("1")){
                            section_f_1.setText("Yes");
                        } else {
                            section_f_1.setText("No");
                        }

                        if(section_f_checklist[1].equals("1")){
                            section_f_2.setText("Yes");
                        } else {
                            section_f_2.setText("No");
                        }

                        if(section_f_checklist[2].equals("1")){
                            section_f_3.setText("Yes");
                        } else {
                            section_f_3.setText("No");
                        }

                        if(section_f_checklist[3].equals("1")){
                            section_f_4.setText("Yes");
                        } else {
                            section_f_4.setText("No");
                        }

                        if(section_f_checklist[4].equals("1")){
                            section_f_5.setText("Yes");
                        } else {
                            section_f_5.setText("No");
                        }

                        if(section_f_checklist[5].equals("1")){
                            section_f_6.setText("Yes");
                        } else {
                            section_f_6.setText("No");
                        }

                        if(section_f_checklist[6].equals("1")){
                            section_f_7.setText("Yes");
                        } else {
                            section_f_7.setText("No");
                        }

                        helperFunctions.stopProgressBar();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        helperFunctions.stopProgressBar();
                        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                    }
                }, error -> {
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Something went wrong. Please try again");
                    onBackPressed();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
