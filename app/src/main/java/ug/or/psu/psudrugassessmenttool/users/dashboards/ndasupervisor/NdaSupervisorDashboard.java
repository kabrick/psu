package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditProfileActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.FeedbackActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.ViewPharmaciesLocationFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.ViewPharmacistAttendanceFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class NdaSupervisorDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nda_supervisor_dashboard);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_nda_supervisor);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        drawer = findViewById(R.id.nda_supervisor_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get header view
        View header_view = navigationView.getHeaderView(0);

        //add user name to drawer
        TextView user_name = header_view.findViewById(R.id.supervisor_name);
        user_name.setText(preferenceManager.getPsuName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container_nda_supervisor);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(4);

        TabLayout mTabLayout = findViewById(R.id.tab_nda_supervisor);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NewsFragment();
                case 1:
                    return new NdaSuperSetLocationsFragment();
                case 2:
                    return new ViewPharmaciesLocationFragment();
                case 3:
                    return new ViewPharmacistAttendanceFragment();
                case 4:
                    return new JobFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position) {
                case 0:
                    return "News";
                case 1:
                    return "Set Locations";
                case 2:
                    return "View Locations";
                case 3:
                    return "View Attendance";
                case 4:
                    return "Job Adverts";
            }
            return super.getPageTitle(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_nda_supervisor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_supervisor_dashboard) {
            helperFunctions.signAdminUsersOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nda_supervisor_post_news:
                Intent post_news_intent = new Intent(this, CreateNewsActivity.class);
                startActivity(post_news_intent);
                break;
            case R.id.nda_supervisor_log_out:
                helperFunctions.signAdminUsersOut();
                break;
            case R.id.nda_supervisor_edit_profile:
                Intent edit_profile = new Intent(this, EditProfileActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.nda_supervisor_feedback:
                Intent give_feedback_intent = new Intent(this, FeedbackActivity.class);
                startActivity(give_feedback_intent);
                break;
            default:
                //
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
