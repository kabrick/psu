package ug.or.psu.psudrugassessmenttool.users.dashboards.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.AdminCommunicationsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.AdminCommunications;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class AdminCommunicationsActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    ShimmerFrameLayout shimmer;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private List<AdminCommunications> itemsList;
    private AdminCommunicationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_communications);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        recycler_view = findViewById(R.id.admin_communication_recycler_view);
        shimmer = findViewById(R.id.admin_communication_shimmer_view_container);

        itemsList = new ArrayList<>();
        mAdapter = new AdminCommunicationsAdapter(this, itemsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(mAdapter);

        fetchItems();
    }

    private void fetchItems() {
        String url = helperFunctions.getIpAddress() + "get_admin_communications.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    shimmer.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);

                    List<AdminCommunications> items = new Gson().fromJson(response.toString(), new TypeToken<List<AdminCommunications>>() {
                    }.getType());

                    itemsList.clear();
                    itemsList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    Toast.makeText(this, "An error occurred with your network", Toast.LENGTH_SHORT).show();
                    fetchItems();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_admin_communications, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create_new_communication) {
            LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.layout_send_communication, null);

            EditText title = view.findViewById(R.id.communication_title);
            EditText message = view.findViewById(R.id.communication_text);
            MaterialSpinner category = view.findViewById(R.id.communication_category);

            category.setItems("All", "Administrators", "Pharmacists", "Pharmacy Owners", "Intern Pharmacists");

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setView(view);
            builder1.setTitle("Send Communication");

            builder1.setCancelable(false)
                    .setPositiveButton("Send", null)
                    .setNegativeButton("Cancel", (dialog, ids) -> dialog.cancel());

            final AlertDialog dialog1 = builder1.create();

            dialog1.setOnShowListener(dialogInterface -> {
                Button button = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(view1 -> {
                    String title_string = title.getText().toString();
                    String message_string = message.getText().toString();

                    if(TextUtils.isEmpty(title_string)) {
                        helperFunctions.genericDialog("Please fill in the title");
                        return;
                    }

                    if(TextUtils.isEmpty(message_string)) {
                        helperFunctions.genericDialog("Please fill in the message");
                        return;
                    }

                    String category_text = category.getText().toString();

                    switch (category_text) {
                        case "Administrators":
                            category_text = "1";
                            break;
                        case "Pharmacist":
                            category_text = "2";
                            break;
                        case "Pharmacy Owner":
                            category_text = "3";
                            break;
                        case "Intern Pharmacist":
                            category_text = "4";
                            break;
                        case "All":
                        default:
                            category_text = "0";
                            break;
                    }

                    final String category_text_final = category_text;

                    //show progress dialog
                    helperFunctions.genericProgressBar("Sending communication...");

                    String url = helperFunctions.getIpAddress() + "send_communication.php";

                    RequestQueue requestQueue = Volley.newRequestQueue(this);

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response1 -> {
                        helperFunctions.stopProgressBar();
                        helperFunctions.genericDialog("Communication has been sent");
                        dialog1.dismiss();
                        fetchItems();
                    }, error -> {
                        helperFunctions.stopProgressBar();
                        helperFunctions.genericDialog(error.toString());
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> data = new HashMap<>();
                            data.put("title", title_string);
                            data.put("message", message_string);
                            data.put("category", category_text_final);
                            data.put("psu_id", preferenceManager.getPsuId());
                            return data;
                        }
                    };

                    requestQueue.add(MyStringRequest);
                });
            });

            dialog1.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
