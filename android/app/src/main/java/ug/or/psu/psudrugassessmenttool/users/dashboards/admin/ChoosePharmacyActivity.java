package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.PharmacyAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.Pharmacy;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ChoosePharmacyActivity extends AppCompatActivity implements PharmacyAdapter.SupervisorPharmacyAdapterListener {

    FloatingActionButton add_pharmacy_fab;
    EditText pharmacy_name, pharmacy_location;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    List<Pharmacy> pharmaciesList;
    PharmacyAdapter mAdapter;
    ShimmerFrameLayout shimmer;
    LinearLayout activity_layout;

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

        add_pharmacy_fab = findViewById(R.id.add_pharmacy_fab);
        shimmer = findViewById(R.id.choose_pharmacy_shimmer_view_container);
        activity_layout = findViewById(R.id.choose_primary_linear_layout);

        add_pharmacy_fab.setOnClickListener(view -> {
            LayoutInflater inflater1 = LayoutInflater.from(ChoosePharmacyActivity.this);

            View view1 = inflater1.inflate(R.layout.new_pharmacy_view, null);

            pharmacy_name = view1.findViewById(R.id.pharmacy_name);
            pharmacy_location = view1.findViewById(R.id.pharmacy_location);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChoosePharmacyActivity.this);
            alertDialog.setTitle("New Pharmacy Dialog");
            alertDialog.setView(view1);

            alertDialog.setCancelable(false)
                    .setPositiveButton("Okay",
                            (dialog, id) -> createNewPharmacy(pharmacy_name.getText().toString(), pharmacy_location.getText().toString()))
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog1 = alertDialog.create();

            alertDialog1.show();
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_choose_pharmacy);
        pharmaciesList = new ArrayList<>();
        mAdapter = new PharmacyAdapter(this, pharmaciesList, this);

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
                response -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();

                    //check if location has been saved successfully
                    if(response.equals("1")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(ChoosePharmacyActivity.this);

                        alert.setMessage("Pharmacy info saved successfully").setPositiveButton("Okay", (dialogInterface, i) -> fetchPharmacies()).show();
                    } else {
                        //did not save
                        helperFunctions.genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Something went wrong! Please try again");
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void fetchPharmacies() {
        String url = helperFunctions.getIpAddress() + "get_all_pharmacies.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    shimmer.setVisibility(View.GONE);
                    activity_layout.setVisibility(View.VISIBLE);

                    if (response.length() < 1) {
                        helperFunctions.genericDialog("No pharmacies are available. You can create one using the plus symbol");
                        return;
                    }

                    List<Pharmacy> items = new Gson().fromJson(response.toString(), new TypeToken<List<Pharmacy>>() {
                    }.getType());

                    pharmaciesList.clear();
                    pharmaciesList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchPharmacies();
                    Toast.makeText(this, "Something went wrong with the network connection", Toast.LENGTH_SHORT).show();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onPharmacySelected(Pharmacy pharmacy) {
        final String pharmacy_id = pharmacy.getId();
        final String pharmacy_name = pharmacy.getName();
        final String location_set = pharmacy.getLocationSet();

        //check if location has already been set
        if(location_set.equals("0")){
            String confirm_text = "Are you sure you want to assign yourself to " + pharmacy_name + "?";

            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setMessage(confirm_text).setPositiveButton("Yes", (dialogInterface, i) -> updatePharmacyInfo(pharmacy_id, preferenceManager.getPsuId())).show();
        } else {
            helperFunctions.genericDialog("A pharmacist has already been assigned to this pharmacy");
        }
    }

    private void updatePharmacyInfo(String id, String pharmacist){
        //start progress bar
        helperFunctions.genericProgressBar("Assigning you a pharmacy...");

        String network_address = helperFunctions.getIpAddress()
                + "edit_pharmacy_info.php?id=" + id
                + "&pharmacist=" + pharmacist;

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();

                    //check if location has been saved successfully
                    if(response.equals("1")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(ChoosePharmacyActivity.this);

                        alert.setMessage("Pharmacy info saved successfully").setPositiveButton("Okay", (dialogInterface, i) -> onBackPressed()).show();
                    } else {
                        helperFunctions.genericDialog("Something went wrong! Please try again");
                    }
                }, error -> {
                    //stop progress bar
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Something went wrong! Please try again");
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
