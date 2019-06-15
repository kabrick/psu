package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.EcpdFeedAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.EcpdFeed;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ViewSubmittedCpdActivity extends AppCompatActivity implements EcpdFeedAdapter.EcpdFeedAdapterListener {

    private List<EcpdFeed> formList;
    private EcpdFeedAdapter mAdapter;
    HelperFunctions helperFunctions;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submitted_cpd);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_submitted_cpd);
        formList = new ArrayList<>();
        mAdapter = new EcpdFeedAdapter(formList, this);

        helperFunctions = new HelperFunctions(this);

        progressBar = findViewById(R.id.progressBarViewSubmittedCpd);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SearchView searchView = findViewById(R.id.search_view_submitted_cpd);

        searchView.setQueryHint("Search cpd's");

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
        String url = helperFunctions.getIpAddress() + "get_cpd_forms.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    //hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    if (response == null) {
                        helperFunctions.genericDialog("No CPDs were found");
                        return;
                    }

                    List<EcpdFeed> items = new Gson().fromJson(response.toString(), new TypeToken<List<EcpdFeed>>() {
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
    public void onFormSelected(EcpdFeed form) {
        String id = form.getId();

        String[] mStringArray = {"Edit || Delete E-CPD", "View E-CPD Test Results"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSubmittedCpdActivity.this);
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            if (i == 0){
                Intent intent = new Intent(ViewSubmittedCpdActivity.this, EcpdViewActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            } else if (i == 1){
                //
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
