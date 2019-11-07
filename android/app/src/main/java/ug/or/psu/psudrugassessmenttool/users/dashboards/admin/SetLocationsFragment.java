package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class SetLocationsFragment extends Fragment implements PharmacyAdapter.SupervisorPharmacyAdapterListener {

    View view;
    private List<Pharmacy> pharmacyList;
    private PharmacyAdapter mAdapter;

    HelperFunctions helperFunctions;

    ProgressBar progressBar;

    public SetLocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_set_locations, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_nda_supervisor_pharmacies);
        pharmacyList = new ArrayList<>();
        mAdapter = new PharmacyAdapter(getContext(), pharmacyList, this);

        helperFunctions = new HelperFunctions(getContext());

        progressBar = view.findViewById(R.id.progressBarSupervisorSetLocations);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SearchView searchView = view.findViewById(R.id.search_view);

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

        return view;
    }

    private void fetchPharmacies() {
        String url = helperFunctions.getIpAddress() + "get_all_pharmacies.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //hide the progress bar
                        progressBar.setVisibility(View.GONE);

                        if (response == null) {
                            //toast message about information not being found
                            helperFunctions.genericSnackbar("No pharmacies were found", view);
                            return;
                        }

                        List<Pharmacy> items = new Gson().fromJson(response.toString(), new TypeToken<List<Pharmacy>>() {
                        }.getType());

                        pharmacyList.clear();
                        pharmacyList.addAll(items);

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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    @Override
    public void onPharmacySelected(Pharmacy pharmacy) {

        final String pharmacy_name = pharmacy.getName();
        final String pharmacy_id = pharmacy.getId();
        final String location_set = pharmacy.getLocationSet();

        //check if location has already been set
        if(location_set.equals("0")){
            //location is not set, do not show alert
            continueToSetLocation(pharmacy_name, pharmacy_id, location_set);
        } else {
            //display dialog to edit location
            final AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

            alert.setMessage("Pharmacy location is already set. Do you want to edit it?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    continueToSetLocation(pharmacy_name, pharmacy_id, location_set);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do nothing
                }
            }).show();
        }
    }

    public void continueToSetLocation(String name, String id, String status){
        Intent intent = new Intent(getContext(), GetLocationActivity.class);
        intent.putExtra("pharmacy_name", name);
        intent.putExtra("pharmacy_id", id);
        intent.putExtra("status", status);
        startActivity(intent);
    }

}
