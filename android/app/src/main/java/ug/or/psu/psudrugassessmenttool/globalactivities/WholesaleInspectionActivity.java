package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class WholesaleInspectionActivity extends AppCompatActivity {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    EditText additional_notes, pharmacy_name, contact_name, contact,
            supervising_pharmacist, reg_number, support_supervision_date, location;
    TextView section_a_total_score, section_a_percentage, section_b_total_score, section_b_percentage,
            overall_total_score, overall_percentage_score, pharmacy_category, main_title;
    RadioGroup section_a_1, section_a_2, section_a_3, section_a_4, section_a_5,
            section_b_1, section_b_2, section_b_3, section_b_4, section_b_5;
    int section_a_1_int, section_a_2_int, section_a_3_int, section_a_4_int, section_a_5_int,
            section_b_1_int, section_b_2_int, section_b_3_int, section_b_4_int, section_b_5_int, section_a_total, section_b_total = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesale_inspection);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        main_title = findViewById(R.id.main_title);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            main_title.setText("SUPPORT SUPERVISION CHECKLIST FOR PHARMACIES " + extras.getString("text", "Wholesale Pharmacies") + " -HUMAN AND VETERINARY");
            main_title.setAllCaps(true);
        }

        overall_total_score = findViewById(R.id.overall_total_score);
        overall_percentage_score = findViewById(R.id.overall_percentage_score);
        pharmacy_category = findViewById(R.id.pharmacy_category);
        section_a_total_score = findViewById(R.id.section_a_total_score);
        section_a_percentage = findViewById(R.id.section_a_percentage);
        section_b_total_score = findViewById(R.id.section_b_total_score);
        section_b_percentage = findViewById(R.id.section_b_percentage);
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

        section_a_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_a_1_no){
                    section_a_1_int = 0;
                } else if(checked_id == R.id.section_a_1_yes){
                    section_a_1_int = 1;
                }

                calculateSectionA();
            }
        });

        section_a_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_a_2_no){
                    section_a_2_int = 0;
                } else if(checked_id == R.id.section_a_2_yes){
                    section_a_2_int = 1;
                }

                calculateSectionA();
            }
        });

        section_a_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_a_3_no){
                    section_a_3_int = 0;
                } else if(checked_id == R.id.section_a_3_yes){
                    section_a_3_int = 1;
                }

                calculateSectionA();
            }
        });

        section_a_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_a_4_no){
                    section_a_4_int = 0;
                } else if(checked_id == R.id.section_a_4_yes){
                    section_a_4_int = 1;
                }

                calculateSectionA();
            }
        });

        section_a_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_a_5_no){
                    section_a_5_int = 0;
                } else if(checked_id == R.id.section_a_5_yes){
                    section_a_5_int = 1;
                }

                calculateSectionA();
            }
        });

        section_b_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_b_1_no){
                    section_b_1_int = 0;
                } else if(checked_id == R.id.section_b_1_yes){
                    section_b_1_int = 1;
                }

                calculateSectionB();
            }
        });

        section_b_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_b_2_no){
                    section_b_2_int = 0;
                } else if(checked_id == R.id.section_b_2_yes){
                    section_b_2_int = 1;
                }

                calculateSectionB();
            }
        });

        section_b_3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_b_3_no){
                    section_b_3_int = 0;
                } else if(checked_id == R.id.section_b_3_yes){
                    section_b_3_int = 1;
                }

                calculateSectionB();
            }
        });

        section_b_4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_b_4_no){
                    section_b_4_int = 0;
                } else if(checked_id == R.id.section_b_4_yes){
                    section_b_4_int = 1;
                }

                calculateSectionB();
            }
        });

        section_b_5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                if(checked_id == R.id.section_b_5_no){
                    section_b_5_int = 0;
                } else if(checked_id == R.id.section_b_5_yes){
                    section_b_5_int = 1;
                }

                calculateSectionB();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void calculateSectionA(){
        // calculate the average score
        section_a_total = section_a_1_int + section_a_2_int +
                section_a_3_int + section_a_4_int + section_a_5_int;

        double percentage = ((double) section_a_total / 5) * 100;

        section_a_percentage.setText(percentage + " %");
        section_a_total_score.setText(String.valueOf(section_a_total));

        calculateTotals();
    }

    @SuppressLint("SetTextI18n")
    public void calculateSectionB(){
        // calculate the average score
        section_b_total = section_b_1_int + section_b_2_int +
                section_b_3_int + section_b_4_int + section_b_5_int;

        double percentage = ((double) section_b_total / 5) * 100;

        section_b_percentage.setText(percentage + " %");
        section_b_total_score.setText(String.valueOf(section_b_total));

        calculateTotals();
    }

    @SuppressLint("SetTextI18n")
    public void calculateTotals(){
        int overall_total = section_a_total + section_b_total;

        double overall_percentage = ((double) overall_total / 10) * 100;

        overall_percentage_score.setText(overall_percentage + " %");
        overall_total_score.setText(String.valueOf(overall_total));

        // color code the average score
        if (overall_percentage < 60){
            pharmacy_category.setText("Red");
            pharmacy_category.setTextColor(Color.RED);
        } else if (overall_percentage <= 74){
            pharmacy_category.setText("Orange");
            pharmacy_category.setTextColor(Color.parseColor("#FFA500"));
        } else {
            pharmacy_category.setText("Green");
            pharmacy_category.setTextColor(Color.GREEN);
        }
    }

    public void submitForm(View view){
        //show progress dialog
        helperFunctions.genericProgressBar("Posting support supervision checklist...");

        //get the current timestamp
        final Long timestamp_long = System.currentTimeMillis();

        String url = helperFunctions.getIpAddress() + "post_supervision_checklist_wholesale.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Posting failed. Please try again later");
                } else {
                    helperFunctions.stopProgressBar();
                    AlertDialog.Builder alert = new AlertDialog.Builder(WholesaleInspectionActivity.this);

                    alert.setMessage("Your support supervision checklist has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        }
                    }).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();

                String section_a_checklist = section_a_1_int + "," + section_a_2_int + "," + section_a_3_int + "," + section_a_4_int + "," + section_a_5_int;
                String section_b_checklist = section_b_1_int + "," + section_b_2_int + "," + section_b_3_int + "," + section_b_4_int + "," + section_b_5_int;

                data.put("additional_notes", additional_notes.getText().toString());
                data.put("pharmacy_name", pharmacy_name.getText().toString());
                data.put("contact_name", contact_name.getText().toString());
                data.put("contact", contact.getText().toString());
                data.put("supervising_pharmacist", supervising_pharmacist.getText().toString());
                data.put("reg_number", reg_number.getText().toString());
                data.put("support_supervision_date", support_supervision_date.getText().toString());
                data.put("location", location.getText().toString());
                data.put("section_a_checklist", section_a_checklist);
                data.put("section_b_checklist", section_b_checklist);
                data.put("submitted_by", preferenceManager.getPsuId());
                data.put("timestamp", timestamp_long.toString());
                return data;
            }
        };

        requestQueue.add(MyStringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
