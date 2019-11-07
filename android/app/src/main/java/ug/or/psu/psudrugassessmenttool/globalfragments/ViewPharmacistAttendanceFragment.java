package ug.or.psu.psudrugassessmenttool.globalfragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import ug.or.psu.psudrugassessmenttool.adapters.PharmacistsAdapter;
import ug.or.psu.psudrugassessmenttool.globalactivities.PharmacistAttendanceActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.Pharmacists;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewPharmacistAttendanceFragment extends Fragment implements PharmacistsAdapter.PharmacistsAdapterListener {

    View view;
    private List<Pharmacists> pharmacistsList;
    private PharmacistsAdapter mAdapter;

    HelperFunctions helperFunctions;

    ProgressBar progressBar;

    public ViewPharmacistAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_pharmacist_attendance, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_pharmacist_attendance);
        pharmacistsList = new ArrayList<>();
        mAdapter = new PharmacistsAdapter(pharmacistsList, this);

        helperFunctions = new HelperFunctions(getContext());

        progressBar = view.findViewById(R.id.progressBarViewPharmacistAttendance);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        SearchView searchView = view.findViewById(R.id.search_view_pharmacist_attendance);

        searchView.setQueryHint("Search pharmacists");

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
        String url = helperFunctions.getIpAddress() + "get_pharmacists.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //hide the progress bar
                        progressBar.setVisibility(View.GONE);

                        if (response == null) {
                            //toast message about information not being found
                            helperFunctions.genericSnackbar("No pharmacists were found", view);
                            return;
                        }

                        List<Pharmacists> items = new Gson().fromJson(response.toString(), new TypeToken<List<Pharmacists>>() {
                        }.getType());

                        pharmacistsList.clear();
                        pharmacistsList.addAll(items);

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
    public void onPharmacistSelected(Pharmacists pharmacist) {
        String pharmacy_id = pharmacist.getId();
        String pharmacist_id = pharmacist.getPsu_id();

        Intent intent = new Intent(getContext(), PharmacistAttendanceActivity.class);
        intent.putExtra("pharmacy_id", pharmacy_id);
        intent.putExtra("pharmacist_id", pharmacist_id);
        startActivity(intent);
    }

}
