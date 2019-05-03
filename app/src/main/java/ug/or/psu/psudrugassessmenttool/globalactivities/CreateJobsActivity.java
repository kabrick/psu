package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class CreateJobsActivity extends AppCompatActivity {

    EditText jobs_title, jobs_description, jobs_phone, jobs_email, jobs_company;
    String salary, contract, location = "N/A";
    String deadline = String.valueOf(System.currentTimeMillis());
    TextView jobs_deadline, jobs_location, jobs_contract, jobs_salary;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        jobs_title = findViewById(R.id.jobs_title);
        jobs_company = findViewById(R.id.jobs_company);
        jobs_description = findViewById(R.id.jobs_description);
        jobs_phone = findViewById(R.id.jobs_phone);
        jobs_email = findViewById(R.id.jobs_email);
        jobs_salary = findViewById(R.id.jobs_salary);
        jobs_contract = findViewById(R.id.jobs_contract);
        jobs_location = findViewById(R.id.jobs_location);
        jobs_deadline = findViewById(R.id.jobs_deadline);

        jobs_salary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] options = {"0 - 300,000", "300,001 - 700,000", "700,001 - 1,000,000", "1,000,001 - 3,000,000", "3,000,001+", "Confidential"};

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateJobsActivity.this);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateJobsActivity.this);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateJobsActivity.this);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateJobsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel_create_jobs) {
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }

        return super.onOptionsItemSelected(item);
    }

    public void postNews(View view){

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
        helperFunctions.genericProgressBar("Posting your job advert...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_jobs.php?title=" + title
                + "&text=" + text
                + "&contact=" + jobs_phone.getText().toString()
                + "&email=" + jobs_email.getText().toString()
                + "&author_id=" + preferenceManager.getPsuId()
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
                            AlertDialog.Builder alert = new AlertDialog.Builder(CreateJobsActivity.this);

                            alert.setMessage("Job has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // redirect to jobs view
                                    // finish();
                                    helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
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
