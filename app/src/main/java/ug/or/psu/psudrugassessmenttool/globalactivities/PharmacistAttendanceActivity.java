package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;
import ug.or.psu.psudrugassessmenttool.R;

public class PharmacistAttendanceActivity extends AppCompatActivity {

    String pharmacy_id;
    String pharmacist_id;
    Fragment summary_fragment = new AttendanceSummaryFragment();
    long start_date, end_date;
    TextView start_date_edit, end_date_edit;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_attendance);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacy_id = extras.getString("pharmacy_id", "1");
            pharmacist_id = extras.getString("pharmacist_id", "1");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // implement switch
        ((SwitchMultiButton) findViewById(R.id.switch_pharmacist_attendance)).setText("Summary", "Sessions").setOnSwitchListener
                (onSwitchListener);

        Calendar cal = Calendar.getInstance();
        end_date = cal.getTimeInMillis();

        cal.add(Calendar.YEAR, -1);
        start_date = cal.getTimeInMillis();

        enableFragment(summary_fragment);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private SwitchMultiButton.OnSwitchListener onSwitchListener = new SwitchMultiButton.OnSwitchListener() {
        @Override
        public void onSwitch(int position, String tabText) {
            if(position == 0){
                enableFragment(summary_fragment);
            } else if(position == 1){
                Fragment sessions_fragment = new AttendanceSessionsFragment();
                enableFragment(sessions_fragment);
            }
        }
    };

    private void enableFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //use replace instead of add to avoid unpredictable behaviour
        transaction.replace(R.id.frame_view_patients, fragment).commit();
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
            alertDialog.setTitle("Search");
            alertDialog.setView(view1);

            alertDialog.setCancelable(false)
                    .setPositiveButton("Search",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    enableFragment(summary_fragment);
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

        return super.onOptionsItemSelected(item);
    }
}
