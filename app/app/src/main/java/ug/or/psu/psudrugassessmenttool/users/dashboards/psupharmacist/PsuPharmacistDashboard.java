package ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacist;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EResourcesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditProfileActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.FeedbackActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.MyJobAdvertsActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.MyAttendanceFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;

public class PsuPharmacistDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PreferenceManager preferenceManager;
    HelperFunctions helperFunctions;
    View activity_view;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu_pharmacist_dashboard);

        activity_view = findViewById(R.id.psu_pharmacist_dashboard_view);

        Toolbar toolbar = findViewById(R.id.toolbar_pharmacist);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        drawer = findViewById(R.id.pharmacist_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get header view
        View header_view = navigationView.getHeaderView(0);

        //add user name to drawer
        TextView user_name = header_view.findViewById(R.id.pharmacist_name);
        user_name.setText(preferenceManager.getPsuName());

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container_pharmacist);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(2);

        TabLayout mTabLayout = findViewById(R.id.tab_pharmacist);
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
                    return "Job||Careers";
                case 2:
                    return "Attendance";
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
        getMenuInflater().inflate(R.menu.menu_activity_psu_pharmacist_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_psu_pharmacist_dashboard) {
            helperFunctions.signAdminUsersOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.pharmacist_post_news:
                Intent post_news_intent = new Intent(this, CreateNewsActivity.class);
                startActivity(post_news_intent);
                break;
            case R.id.pharmacist_log_out:
                helperFunctions.signAdminUsersOut();
                break;
            case R.id.pharmacist_edit_profile:
                Intent edit_profile = new Intent(this, EditProfileActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.pharmacist_feedback:
                Intent give_feedback_intent = new Intent(this, FeedbackActivity.class);
                startActivity(give_feedback_intent);
                break;
            case R.id.pharmacist_admin_eresources:
                Intent eresources_intent = new Intent(this, EResourcesActivity.class);
                startActivity(eresources_intent);
                break;
            /*case R.id.pharmacy_owner_my_job_adverts:
                Intent my_job_adverts_intent = new Intent(this, MyJobAdvertsActivity.class);
                startActivity(my_job_adverts_intent);
                break;*/
            case R.id.pharmacist_edit_news_posts:
                Intent edit_news_intent = new Intent(this, EditYourNewsActivity.class);
                startActivity(edit_news_intent);
                break;
            default:
                //
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}