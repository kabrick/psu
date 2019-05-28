package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.AttendanceSessionAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.AttendanceSession;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class PharmacistAttendanceActivity extends AppCompatActivity {

    String pharmacy_id, pharmacist_id;
    TextView total_hours, monday_hours, tuesday_hours, wednesday_hours, thursday_hours,
            friday_hours, saturday_hours, sunday_hours, name, phone, email, pharmacy,
            start_date_edit, end_date_edit, view_attendance_summary;
    boolean is_summary_shown = false;
    long start_date, end_date;
    private List<AttendanceSession> attendanceList;
    private AttendanceSessionAdapter mAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    HelperFunctions helperFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_attendance);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacy_id = extras.getString("pharmacy_id", "1");
            pharmacist_id = extras.getString("pharmacist_id", "1");
        } else {
            onBackPressed();
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);

        view_attendance_summary = findViewById(R.id.view_attendance_summary);

        view_attendance_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_summary_shown){
                    // hide the summary
                    findViewById(R.id.layout_attendance_summary).setVisibility(View.GONE);
                    is_summary_shown = false;
                    view_attendance_summary.setText("Click to view attendance summary");
                } else {
                    // show the summary
                    findViewById(R.id.layout_attendance_summary).setVisibility(View.VISIBLE);
                    is_summary_shown = true;
                    view_attendance_summary.setText("Click to hide attendance summary");
                }
            }
        });

        // summary attendance
        total_hours = findViewById(R.id.total_hours);
        monday_hours = findViewById(R.id.monday_hours);
        tuesday_hours = findViewById(R.id.tuesday_hours);
        wednesday_hours = findViewById(R.id.wednesday_hours);
        thursday_hours = findViewById(R.id.thursday_hours);
        friday_hours = findViewById(R.id.friday_hours);
        saturday_hours = findViewById(R.id.saturday_hours);
        sunday_hours = findViewById(R.id.sunday_hours);
        name = findViewById(R.id.attendance_name);
        phone = findViewById(R.id.attendance_phone);
        email = findViewById(R.id.attendance_email);
        pharmacy = findViewById(R.id.attendance_pharmacy);

        // sessions attendance
        RecyclerView recyclerView = findViewById(R.id.attendance_sessions_recycler);
        attendanceList = new ArrayList<>();
        mAdapter = new AttendanceSessionAdapter(attendanceList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        search_records();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_pharmacist_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search_pharmacist_attendance) {
            search_records();
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchSummary(){

        helperFunctions.genericProgressBar("Getting attendance records...");

        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacist_summary.php?id=" + pharmacy_id
                + "&start_date=" + start_date
                + "&end_date=" + end_date
                + "&psu_id=" + pharmacist_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fetchSessions();

                        try {
                            total_hours.setText(response.getString("total_hours"));
                            monday_hours.setText(response.getString("monday_hours"));
                            tuesday_hours.setText(response.getString("tuesday_hours"));
                            wednesday_hours.setText(response.getString("wednesday_hours"));
                            thursday_hours.setText(response.getString("thursday_hours"));
                            friday_hours.setText(response.getString("friday_hours"));
                            saturday_hours.setText(response.getString("saturday_hours"));
                            sunday_hours.setText(response.getString("sunday_hours"));
                            name.setText(response.getString("name"));
                            phone.setText(response.getString("phone"));
                            email.setText(response.getString("email"));
                            pharmacy.setText(response.getString("pharmacy"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchSessions() {

        String url = helperFunctions.getIpAddress()
                + "get_pharmacist_session.php?id=" + pharmacy_id
                + "&psu_id=" + pharmacist_id
                + "&start_date=" + start_date
                + "&end_date=" + end_date;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        helperFunctions.stopProgressBar();

                        List<AttendanceSession> items = new Gson().fromJson(response.toString(), new TypeToken<List<AttendanceSession>>() {
                        }.getType());

                        attendanceList.clear();
                        attendanceList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                helperFunctions.stopProgressBar();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void search_records(){
        LayoutInflater inflater1 = LayoutInflater.from(this);
        View view1 = inflater1.inflate(R.layout.layout_attendance_search, null);

        start_date_edit = view1.findViewById(R.id.start_date);
        end_date_edit = view1.findViewById(R.id.end_date);

        final DatePickerDialog.OnDateSetListener date_start = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                start_date_edit.setText(sdf.format(myCalendar.getTime()));
                start_date = myCalendar.getTimeInMillis();
            }

        };

        start_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PharmacistAttendanceActivity.this, date_start, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date_end = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                end_date_edit.setText(sdf.format(myCalendar.getTime()));
                end_date = myCalendar.getTimeInMillis();
            }

        };

        end_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PharmacistAttendanceActivity.this, date_end, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Select date range");
        alertDialog.setView(view1);

        alertDialog.setCancelable(false)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                fetchSummary();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog1 = alertDialog.create();

        alertDialog1.show();
    }
}
