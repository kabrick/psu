package ug.or.psu.psudrugassessmenttool.globalactivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.EcpdResultsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.EcpdResults;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewEcpdResultsActivity extends AppCompatActivity implements EcpdResultsAdapter.EcpdResultsAdapterListener {

    private List<EcpdResults> formList;
    private EcpdResultsAdapter mAdapter;
    RecyclerView recyclerView;
    TextView no_tests_found;
    HelperFunctions helperFunctions;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ecpd_results);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        recyclerView = findViewById(R.id.recycler_ecpd_results);
        no_tests_found = findViewById(R.id.no_tests_found);
        formList = new ArrayList<>();
        mAdapter = new EcpdResultsAdapter(formList, this);

        helperFunctions = new HelperFunctions(this);

        progressBar = findViewById(R.id.progressBarEcpdResults);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SearchView searchView = findViewById(R.id.search_ecpd_results);

        searchView.setQueryHint("Search results");

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

        fetchForms();
    }

    private void fetchForms() {
        String url = helperFunctions.getIpAddress() + "get_cpd_tests.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    //hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    if (response.length() < 1) {
                        no_tests_found.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }

                    List<EcpdResults> items = new Gson().fromJson(response.toString(), new TypeToken<List<EcpdResults>>() {
                    }.getType());

                    formList.clear();
                    formList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
            // error in getting json, so recursive call till successful
            fetchForms();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onFormSelected(EcpdResults form) {
        //
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
