package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.SearchView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.PharmacistsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.Pharmacists;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ForumChooseModeratorActivity extends AppCompatActivity implements PharmacistsAdapter.PharmacistsAdapterListener {

    private List<Pharmacists> pharmacistsList;
    private PharmacistsAdapter mAdapter;
    HelperFunctions helperFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_choose_moderator);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_forum_choose_moderator);
        pharmacistsList = new ArrayList<>();
        mAdapter = new PharmacistsAdapter(pharmacistsList, this);

        helperFunctions = new HelperFunctions(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        SearchView searchView = findViewById(R.id.search_forum_choose_moderator);

        searchView.setQueryHint("Search users");

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

        fetchUsers();
    }

    private void fetchUsers() {
        helperFunctions.genericProgressBar("Fetching users...");

        String url = helperFunctions.getIpAddress() + "get_users.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    helperFunctions.stopProgressBar();
                    List<Pharmacists> items = new Gson().fromJson(response.toString(), new TypeToken<List<Pharmacists>>() {
                    }.getType());

                    pharmacistsList.clear();
                    pharmacistsList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    helperFunctions.stopProgressBar();
                    // error in getting json, so recursive call till successful
                    fetchUsers();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onPharmacistSelected(Pharmacists pharmacist) {
        Intent i = new Intent();
        i.putExtra("user_name", pharmacist.getName());
        i.putExtra("user_id", pharmacist.getPsu_id());
        setResult(1, i);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
