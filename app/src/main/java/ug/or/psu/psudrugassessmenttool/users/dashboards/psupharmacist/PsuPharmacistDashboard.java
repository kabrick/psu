package ug.or.psu.psudrugassessmenttool.users.dashboards.psupharmacist;

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
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.CreateNewsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.FeedbackActivity;
import ug.or.psu.psudrugassessmenttool.globalfragments.JobFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.NewsFragment;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.services.TrackPharmacistService;

public class PsuPharmacistDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PreferenceManager preferenceManager;
    HelperFunctions helperFunctions;
    ArrayList<String> pharmacy_names;
    ArrayList<String> pharmacy_id;
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

        //create array list objects
        pharmacy_names = new ArrayList<>();
        pharmacy_id = new ArrayList<>();

        //check if location is already set
        if(!preferenceManager.isPharmacyLocationSet()){
            //start dialog
            helperFunctions.genericProgressBar("Getting your allocated pharmacies...");
            //not so start procedure to set it
            getPharmacies();
        }

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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
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
                    return "News";
                case 1:
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
        getMenuInflater().inflate(R.menu.menu_activity_psu_pharmacist_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_log_out_psu_pharmacist_dashboard) {
            helperFunctions.signPharmacistOut();
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
            case R.id.pharmacist_feedback:
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

    public void getPharmacies(){
        String network_address = helperFunctions.getIpAddress() + "get_pharmacist_pharmacies.php?id=" + preferenceManager.getPsuId();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //close the generic progressbar
                        helperFunctions.stopProgressBar();

                        JSONObject obj;

                        for (int i = 0; i < response.length(); i++){

                            try {
                                obj = response.getJSONObject(i);

                                pharmacy_names.add(obj.getString("pharmacy_name"));
                                pharmacy_id.add(obj.getString("pharmacy_id"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //continue to display
                        choosePharmacy();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    public void choosePharmacy(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your pharmacy");

        //convert array list to string array
        String[] mStringArray = new String[pharmacy_names.size()];
        mStringArray = pharmacy_names.toArray(mStringArray);

        builder.setItems(mStringArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //open working on something dialog
                helperFunctions.genericProgressBar("Setting pharmacy location...");

                //set the selected pharmacy in prefs
                preferenceManager.setPharmacyId(pharmacy_id.get(i));
                preferenceManager.setPharmacyName(pharmacy_names.get(i));

                //fetch the coordinates for the pharmacy
                String network_address = helperFunctions.getIpAddress()
                        + "get_pharmacy_coordinates.php?id=" + pharmacy_id.get(i);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    preferenceManager.setPharmacyLatitude(Double.parseDouble(response.getString("latitude")));
                                    preferenceManager.setPharmacyLongitude(Double.parseDouble(response.getString("longitude")));

                                    //set pharmacy location
                                    preferenceManager.setIsPharmacyLocationSet(true);

                                    //start service
                                    Intent intent = new Intent(PsuPharmacistDashboard.this, TrackPharmacistService.class);
                                    intent.setAction("start");
                                    startService(intent);

                                    //dismiss dialog and snack success
                                    helperFunctions.stopProgressBar();

                                    helperFunctions.genericSnackbar("Pharmacy set successfully", activity_view);
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

                VolleySingleton.getInstance(PsuPharmacistDashboard.this).addToRequestQueue(request);
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
