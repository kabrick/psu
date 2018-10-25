package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

public class PharmacistAttendanceActivity extends AppCompatActivity {

    String pharmacist_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_attendance);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            pharmacist_id = extras.getString("pharmacist_id", "1");
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container_pharmacist_attendance);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(2);

        TabLayout mTabLayout = findViewById(R.id.tab_pharmacist_attendance);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new AttendanceSummaryFragment();
                case 1:
                    return new AttendanceSessionsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position) {
                case 0:
                    return "Summary";
                case 1:
                    return "Sessions";
            }
            return super.getPageTitle(position);
        }
    }
}
