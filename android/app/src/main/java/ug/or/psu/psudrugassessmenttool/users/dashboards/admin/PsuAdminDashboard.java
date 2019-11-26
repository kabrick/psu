package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EResourcesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EcpdCreateActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditProfileActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditYourNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ForumPostActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ForumTopicsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.PrivacyPolicyActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.SearchPharmacistAssessmentFormsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewAllPharmacyCoordinatesActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewEcpdResultsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.ViewSubmittedCpdActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.WholesaleInspectionFeedActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.MyAttendanceFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

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
        } else {
            super.onBackPressed();
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

    @SuppressLint("InflateParams")
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
                Intent submit_ecpd_intent = new Intent(this, EcpdCreateActivity.class);
                startActivity(submit_ecpd_intent);
                break;
            case R.id.psu_admin_view_cpd_results:
                Intent view_cpd_results_intent = new Intent(this, ViewEcpdResultsActivity.class);
                startActivity(view_cpd_results_intent);
                break;
            case R.id.psu_admin_edit_cpd_passmark:
                helperFunctions.genericProgressBar("Fetching passmark details...");

                String network_address = helperFunctions.getIpAddress() + "get_settings.php";

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                        response -> {
                            helperFunctions.stopProgressBar();

                            try {
                                LayoutInflater inflater = getLayoutInflater();

                                View view = inflater.inflate(R.layout.layout_add_eresource, null);

                                final EditText passmark = view.findViewById(R.id.title);

                                passmark.setText(response.getString("passmark"));
                                passmark.setHint("Passmark");

                                AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                                builder.setView(view);
                                builder.setTitle("Edit Passmark");

                                builder.setCancelable(false)
                                        .setPositiveButton("Save", null)
                                        .setNegativeButton("Cancel", (dialog, ids) -> dialog.cancel());

                                final AlertDialog dialog = builder.create();

                                dialog.setOnShowListener(dialogInterface -> {

                                    Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                    button.setOnClickListener(view1 -> {
                                        String passmark_string = passmark.getText().toString();

                                        if(TextUtils.isEmpty(passmark_string)) {
                                            helperFunctions.genericDialog("Please fill in the passmark");
                                            return;
                                        }

                                        //show progress dialog
                                        helperFunctions.genericProgressBar("Editing passmark...");

                                        String url = helperFunctions.getIpAddress() + "update_passmark.php";

                                        RequestQueue requestQueue = Volley.newRequestQueue(this);

                                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response1 -> {
                                            helperFunctions.stopProgressBar();
                                            helperFunctions.genericDialog("Passmark has been updated");
                                            dialog.dismiss();
                                        }, error -> {
                                            helperFunctions.stopProgressBar();
                                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                                        }) {
                                            protected Map<String, String> getParams() {
                                                Map<String, String> data = new HashMap<>();
                                                data.put("mark", passmark_string);
                                                return data;
                                            }
                                        };

                                        requestQueue.add(MyStringRequest);
                                    });
                                });

                                dialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Failed to get passmark");
                });

                VolleySingleton.getInstance(this).addToRequestQueue(request);
                break;
            case R.id.psu_admin_communication:
                startActivity(new Intent(this, AdminCommunicationsActivity.class));
                break;
            case R.id.psu_admin_log_out:
                helperFunctions.signAdminUsersOut();
                break;
            case R.id.psu_admin_privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.psu_admin_post_forum:
                startActivity(new Intent(this, ForumPostActivity.class));
                break;
            case R.id.psu_admin_review_forum_topic:
                startActivity(new Intent(this, ApproveForumTopicActivity.class));
                break;
            case R.id.psu_admin_view_forum_topic:
                startActivity(new Intent(this, ForumTopicsActivity.class));
                break;
            case R.id.psu_admin_approve_job_posts:
                Intent approve_jobs_intent = new Intent(this, ApproveJobsActivity.class);
                startActivity(approve_jobs_intent);
                break;
            default:
                //
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
