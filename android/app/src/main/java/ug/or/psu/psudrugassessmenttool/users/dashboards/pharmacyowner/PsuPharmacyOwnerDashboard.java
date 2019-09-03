package ug.or.psu.psudrugassessmenttool.users.dashboards.pharmacyowner;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EResourcesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditProfileActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.FeedbackActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ForumPostActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAssessmentFormFeedOwnerActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.PrivacyPolicyActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.MyAttendanceFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class PsuPharmacyOwnerDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu_pharmacy_owner_dashboard);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar_pharmacy_owner);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        drawer = findViewById(R.id.psu_pharmacy_owner_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get header view
        View header_view = navigationView.getHeaderView(0);

        //add user name to drawer
        TextView user_name = header_view.findViewById(R.id.pharmacy_owner_name);
        user_name.setText(preferenceManager.getPsuName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container_pharmacy_owner);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //set fixed cache so that tabs are not reloaded
        mViewPager.setOffscreenPageLimit(2);

        TabLayout mTabLayout = findViewById(R.id.tab_pharmacy_owner);
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
        getMenuInflater().inflate(R.menu.menu_activity_pharmacy_owner_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_pharmacy_owner_dashboard) {
            helperFunctions.signAdminUsersOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.pharmacy_owner_post_news:
                Intent post_news_intent = new Intent(this, CreateNewsActivity.class);
                startActivity(post_news_intent);
                break;
            case R.id.pharmacy_owner_feedback:
                Intent give_feedback_intent = new Intent(this, FeedbackActivity.class);
                startActivity(give_feedback_intent);
                break;
            case R.id.pharmacy_owner_attendance:
                viewPharmacistAttendance();
                break;
            case R.id.pharmacy_owner_edit_profile:
                Intent edit_profile = new Intent(this, EditProfileActivity.class);
                startActivity(edit_profile);
                break;
            case R.id.pharmacy_owner_pharmacist_assessment_form:
                Intent owner_pharmacist_assessment_form_intent = new Intent(this, PharmacistAssessmentFormFeedOwnerActivity.class);
                startActivity(owner_pharmacist_assessment_form_intent);
                break;
            case R.id.pharmacy_owner_log_out:
                helperFunctions.signAdminUsersOut();
                break;
            case R.id.pharmacy_owner_eresources:
                Intent eresources_intent = new Intent(this, EResourcesActivity.class);
                startActivity(eresources_intent);
                break;
            /*case R.id.pharmacy_owner_my_job_adverts:
                Intent my_job_adverts_intent = new Intent(this, MyJobAdvertsActivity.class);
                startActivity(my_job_adverts_intent);
                break;*/
            case R.id.pharmacy_owner_edit_news_posts:
                Intent edit_news_intent = new Intent(this, EditYourNewsActivity.class);
                startActivity(edit_news_intent);
                break;
            case R.id.pharmacy_owner_privacy_policy:
                Intent privacy_policy_intent = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(privacy_policy_intent);
                break;
            case R.id.pharmacy_owner_post_forum:
                Intent forum_intent = new Intent(this, ForumPostActivity.class);
                startActivity(forum_intent);
                break;
            default:
                //
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void viewPharmacistAttendance(){

        helperFunctions.genericProgressBar("Retrieving pharmacy records...");

        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacy_owner_details.php?id=" + preferenceManager.getPsuId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            helperFunctions.stopProgressBar();

                            Intent intent = new Intent(PsuPharmacyOwnerDashboard.this, PharmacistAttendanceActivity.class);
                            intent.putExtra("pharmacy_id", response.getString("pharmacy_id"));
                            intent.putExtra("pharmacist_id", response.getString("pharmacist_id"));
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();

                helperFunctions.genericDialog("Something went wrong. Please try again");
            }
        });

        VolleySingleton.getInstance(PsuPharmacyOwnerDashboard.this).addToRequestQueue(request);
    }
}
