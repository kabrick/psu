package ug.or.psu.psudrugassessmenttool.users.dashboards.psuadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.PharmaciesAdapter;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.Pharmacies;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ChoosePharmacyActivity extends AppCompatActivity implements PharmaciesAdapter.PharmaciesAdapterListener {

    FloatingActionButton add_pharmacy_fab;
    EditText pharmacy_name, pharmacy_location;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private List<Pharmacies> pharmaciesList;
    private PharmaciesAdapter mAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pharmacy);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        progressBar = findViewById(R.id.progressBarChoosePharmacy);

        add_pharmacy_fab = findViewById(R.id.add_pharmacy_fab);

        add_pharmacy_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater1 = LayoutInflater.from(ChoosePharmacyActivity.this);

                View view1 = inflater1.inflate(R.layout.new_pharmacy_view, null);

                pharmacy_name = view1.findViewById(R.id.pharmacy_name);
                pharmacy_location = view1.findViewById(R.id.pharmacy_location);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChoosePharmacyActivity.this);
                alertDialog.setTitle("New Pharmacy Dialog");
                alertDialog.setView(view1);

                alertDialog.setCancelable(false)
                        .setPositiveButton("Okay",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        createNewPharmacy(pharmacy_name.getText().toString(), pharmacy_location.getText().toString());
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
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_choose_pharmacy);
        pharmaciesList = new ArrayList<>();
        mAdapter = new PharmaciesAdapter(pharmaciesList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        SearchView searchView = findViewById(R.id.search_choose_pharmacy);

        searchView.setQueryHint("Search pharmacies");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        fetchPharmacies();
    }

    private void createNewPharmacy(String name, String location){
        //start progress bar
        helperFunctions.genericProgressBar("Saving pharmacy location...");

        String network_address = helperFunctions.getIpAddress()
                + "set_new_pharmacy.php?name=" + name
                + "&location=" + location
                + "&id=" + preferenceManager.getPsuId();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        helperFunctions.stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Pharmacy saved successfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                        }
                                    })
                                    .show();
                        } else {
                            //did not save
                            new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong! Please try again")
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                helperFunctions.stopProgressBar();
                new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong! Please try again")
                        .show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchPharmacies() {
        String url = helperFunctions.getIpAddress() + "get_unset_pharmacies.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //hide the progress bar
                        progressBar.setVisibility(View.GONE);

                        if (response == null) {
                            return;
                        }

                        List<Pharmacies> items = new Gson().fromJson(response.toString(), new TypeToken<List<Pharmacies>>() {
                        }.getType());

                        pharmaciesList.clear();
                        pharmaciesList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchPharmacies();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onPharmacySelected(Pharmacies pharmacy) {
        final String pharmacy_id = pharmacy.getId();

        String confirm_text = "Assign yourself to " + pharmacy.getName() + "?";

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText(confirm_text)
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        updatePharmacyInfo(pharmacy_id, preferenceManager.getPsuId());
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void updatePharmacyInfo(String id, String pharmacist){
        //start progress bar
        helperFunctions.genericProgressBar("Assigning you a pharmacy...");

        String network_address = helperFunctions.getIpAddress()
                + "edit_pharmacy_info.php?id=" + id
                + "&pharmacist=" + pharmacist;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress bar
                        helperFunctions.stopProgressBar();

                        //check if location has been saved successfully
                        if(response.equals("1")){
                            new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Pharmacy info saved successfully")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                        }
                                    })
                                    .show();
                        } else {
                            //did not save
                            new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Something went wrong! Please try again")
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //stop progress bar
                helperFunctions.stopProgressBar();
                new SweetAlertDialog(ChoosePharmacyActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong! Please try again")
                        .show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
