package ug.or.psu.psudrugassessmenttool.users.dashboards.ndaadmin;

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

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;

public class NdaAdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HelperFunctions helperFunctions;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nda_admin_dashboard);

        helperFunctions = new HelperFunctions(this);

        Toolbar toolbar = findViewById(R.id.toolbar_nda_admin);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        drawer = findViewById(R.id.nda_admin_drawer_layout);
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
        ViewPager mViewPager = findViewById(R.id.container_nda_admin);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(3);

        TabLayout mTabLayout = findViewById(R.id.tab_nda_admin);
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
                    //return new NdaSuperNewsFragment();
                case 1:
                    //return new NdaSuperViewLocationsFragment();
                case 2:
                    //return new NdaSuperViewAttendanceFragment();
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
                    return "View Locations";
                case 2:
                    return "View Attendance";
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
        getMenuInflater().inflate(R.menu.menu_activity_nda_admin_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_admin_dashboard) {
            helperFunctions.signAdminUsersOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nda_admin_post_news:
                Intent post_news_intent = new Intent(this, CreateNewsActivity.class);
                startActivity(post_news_intent);
                break;
            case R.id.nda_admin_log_out:
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
