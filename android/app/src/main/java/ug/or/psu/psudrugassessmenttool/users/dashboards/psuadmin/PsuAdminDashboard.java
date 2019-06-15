package ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.AdrReportFormActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.AdrReportFormFeedActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EResourcesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditProfileActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.SearchPharmacistAssessmentFormsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewAllPharmacyCoordinatesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewEcpdResultsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewSubmittedCpdActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.WholesaleInspectionActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.WholesaleInspectionFeedActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.MyAttendanceFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;

public class PsuAdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HelperFunctions helperFunctions;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu_admin_dashboard);

        helperFunctions = new HelperFunctions(this);

        Toolbar toolbar = findViewById(R.id.toolbar_psu_admin);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        drawer = findViewById(R.id.psu_admin_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container_psu_admin);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(3);

        TabLayout mTabLayout = findViewById(R.id.tab_psu_admin);
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
                    return new JobFragment();
                case 2:
                    return new MyAttendanceFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position) {
                case 0:
                    return "News";
                case 1:
                    return "Job-Careers";
                case 2:
                    return "Practice";
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
        getMenuInflater().inflate(R.menu.menu_activity_psu_admin_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_psu_admin_dashboard) {
            helperFunctions.signAdminUsersOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.psu_admin_approve_news_posts:
                Intent approve_news_intent = new Intent(this, ApproveNewsActivity.class);
                startActivity(approve_news_intent);
                break;
            case R.id.psu_admin_view_pharmacist_assessment_form:
                Intent view_pharmacist_assessment_form_intent = new Intent(this, SearchPharmacistAssessmentFormsActivity.class);
                startActivity(view_pharmacist_assessment_form_intent);
                break;
            case R.id.psu_admin_set_pharmacy_locations:
                Intent set_pharmacy_location = new Intent(this, SetPharmacyLocationActivity.class);
                startActivity(set_pharmacy_location);
                break;
            case R.id.psu_admin_view_pharmacy_location:
                Intent view_pharmacy_location = new Intent(this, ViewPharmacyActivity.class);
                startActivity(view_pharmacy_location);
                break;
            case R.id.psu_admin_view_pharmacy_coordinates:
                Intent view_pharmacy_coordinates = new Intent(this, ViewAllPharmacyCoordinatesActivity.class);
                startActivity(view_pharmacy_coordinates);
                break;
            case R.id.psu_admin_post_news:
                Intent post_news_intent = new Intent(this, CreateNewsActivity.class);
                startActivity(post_news_intent);
                break;
            case R.id.psu_admin_edit_profile:
                Intent edit_profile = new Intent(this, EditProfileActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.psu_admin_eresources:
                Intent eresources_intent = new Intent(this, EResourcesActivity.class);
                startActivity(eresources_intent);
                break;
            /*case R.id.psu_admin_my_job_adverts:
                Intent my_job_adverts_intent = new Intent(this, MyJobAdvertsActivity.class);
                startActivity(my_job_adverts_intent);
                break;*/
            case R.id.psu_admin_edit_news_posts:
                Intent edit_news_intent = new Intent(this, EditYourNewsActivity.class);
                startActivity(edit_news_intent);
                break;
            case R.id.psu_admin_view_support_supervision_checklist:
                Intent view_support_supervision_checklist_intent = new Intent(this, WholesaleInspectionFeedActivity.class);
                startActivity(view_support_supervision_checklist_intent);
                break;
            case R.id.psu_admin_view_submitted_cpd:
                Intent view_submitted_intent = new Intent(this, ViewSubmittedCpdActivity.class);
                startActivity(view_submitted_intent);
                break;
            case R.id.psu_admin_submit_cpd:
                Toast.makeText(this, "Feature not ready", Toast.LENGTH_SHORT).show();
                break;
            case R.id.psu_admin_view_cpd_results:
                Intent view_cpd_results_intent = new Intent(this, ViewEcpdResultsActivity.class);
                startActivity(view_cpd_results_intent);
                break;
            case R.id.psu_admin_log_out:
                helperFunctions.signAdminUsersOut();
                break;
            default:
                //
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
