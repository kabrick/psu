package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditJobAdvertActivity extends AppCompatActivity {

    EditText jobs_title, jobs_description, jobs_phone, jobs_email, jobs_company;
    String deadline, salary, contract, location = "N/A";
    TextView jobs_deadline, jobs_location, jobs_contract, jobs_salary;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    final Calendar myCalendar = Calendar.getInstance();
    String job_id;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job_advert);

        jobs_title = findViewById(R.id.edit_jobs_title);
        jobs_company = findViewById(R.id.edit_jobs_company);
        jobs_description = findViewById(R.id.edit_jobs_description);
        jobs_phone = findViewById(R.id.edit_jobs_phone);
        jobs_email = findViewById(R.id.edit_jobs_email);
        jobs_salary = findViewById(R.id.edit_jobs_salary);
        jobs_contract = findViewById(R.id.edit_jobs_contract);
        jobs_location = findViewById(R.id.edit_jobs_location);
        jobs_deadline = findViewById(R.id.edit_jobs_deadline);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        jobs_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"0 - 300,000", "300,001 - 700,000", "700,001 - 1,000,000", "1,000,001 - 3,000,000", "3,000,001+", "Confidential"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditJobAdvertActivity.this);
                builder.setTitle("Choose salary range");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        salary = options[i];
                        jobs_salary.setText("Salary Range: " + options[i]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        jobs_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Internship", "Part time", "Full time"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditJobAdvertActivity.this);
                builder.setTitle("Choose contract type");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contract = options[i];
                        jobs_contract.setText("Contract type: " + options[i]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        jobs_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"Kampala", "Mbale", "Jinja", "Gulu", "Mbarara", "Entebbe", "Lira", "Luwero", "Kabale", "Mukono", "Wakiso", "Rest of Uganda"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditJobAdvertActivity.this);
                builder.setTitle("Choose location");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        location = options[i];
                        jobs_location.setText("Location: " + options[i]);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // set up onclick dialog
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                jobs_deadline.setText("Deadline: " + sdf.format(myCalendar.getTime()));
                deadline = String.valueOf(myCalendar.getTimeInMillis());
            }

        };

        jobs_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditJobAdvertActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            job_id = extras.getString("id", "1");
        }

        getJobDetails();
    }

    public void getJobDetails(){
        helperFunctions.genericProgressBar("Retrieving job details...");

        String network_address = helperFunctions.getIpAddress()
                + "get_job_details.php?id=" + job_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        helperFunctions.stopProgressBar();

                        try {
                            jobs_title.setText(response.getString("title"));
                            jobs_description.setText(response.getString("text"));
                            jobs_email.setText(response.getString("email"));
                            jobs_phone.setText(response.getString("contact"));
                            jobs_salary.setText("Salary range: " + response.getString("salary_range"));
                            jobs_location.setText("Location: " + response.getString("location"));
                            jobs_contract.setText("Contract type: " + response.getString("contract_type"));
                            jobs_company.setText(response.getString("company_name"));

                            deadline = response.getString("deadline");
                            salary = response.getString("salary_range");
                            contract = response.getString("contract_type");
                            location = response.getString("location");

                            Long deadline_timestamp = Long.parseLong(response.getString("deadline"));

                            @SuppressLint("SimpleDateFormat")
                            String date = new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date (deadline_timestamp));
                            jobs_deadline.setText("Deadline: " + date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();
                new AlertDialog.Builder(EditJobAdvertActivity.this)
                        .setMessage("Something went wrong. Please try again")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        }).show();
            }
        });

        VolleySingleton.getInstance(EditJobAdvertActivity.this).addToRequestQueue(request);
    }

    public void editNews(View view){
        String title = jobs_title.getText().toString();
        String text = jobs_description.getText().toString();

        if(TextUtils.isEmpty(title)) {
            jobs_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(text)) {
            jobs_description.setError("Please fill in the text");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Editing your job advert...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "edit_jobs.php?title=" + title
                + "&text=" + text
                + "&contact=" + jobs_phone.getText().toString()
                + "&email=" + jobs_email.getText().toString()
                + "&id=" + job_id
                + "&timestamp=" + timestamp_long.toString()
                + "&company_name=" + jobs_company.getText().toString()
                + "&salary_range=" + salary
                + "&contract_type=" + contract
                + "&location=" + location
                + "&deadline=" + deadline;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("1")){
                            //saved article successfully
                            AlertDialog.Builder alert = new AlertDialog.Builder(EditJobAdvertActivity.this);

                            alert.setMessage("Job has been edited successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    Intent intent = new Intent(EditJobAdvertActivity.this, MyJobAdvertsActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                        } else {
                            helperFunctions.genericDialog("Something went wrong! Please try again");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.genericDialog("Something went wrong! Please try again");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
