package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NdaSuperSetLocationsFragment extends Fragment implements SupervisorPharmacyAdapter.SupervisorPharmacyAdapterListener {

    View view;
    private static final String TAG = NdaSuperSetLocationsFragment.class.getSimpleName();
    private List<SupervisorPharmacy> pharmacyList;
    private SupervisorPharmacyAdapter mAdapter;

    HelperFunctions helperFunctions;

    public NdaSuperSetLocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nda_super_set_locations, container, false);

        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_nda_supervisor_pharmacies);
        pharmacyList = new ArrayList<>();
        mAdapter = new SupervisorPharmacyAdapter(getContext(), pharmacyList, this);

        helperFunctions = new HelperFunctions(getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
        searchView.setQueryHint("Search View");

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
                        if (response == null) {
                            //toast message about information not being found
                            helperFunctions.genericSnackbar("No pharmacies were found", view);
                            return;
                        }

                        List<SupervisorPharmacy> items = new Gson().fromJson(response.toString(), new TypeToken<List<SupervisorPharmacy>>() {
                        }.getType());

                        pharmacyList.clear();
                        pharmacyList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nda_supervisor_set_locations, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nda_set_location_action_sign_out:
                //do something
                return true;

            case R.id.nda_set_location_action_search:
                //do something
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPharmacySelected(SupervisorPharmacy pharmacy) {
        Toast.makeText(getContext(), "Selected: " + pharmacy.getId(), Toast.LENGTH_LONG).show();
    }

}
