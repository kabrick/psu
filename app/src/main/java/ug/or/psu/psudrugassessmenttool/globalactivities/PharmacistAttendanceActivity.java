package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import lib.kingja.switchbutton.SwitchMultiButton;
import ug.or.psu.psudrugassessmenttool.R;

public class PharmacistAttendanceActivity extends AppCompatActivity {

    String pharmacy_id;
    String pharmacist_id;
    Fragment summary_fragment = new AttendanceSummaryFragment();

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
}
