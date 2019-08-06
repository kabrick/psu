package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.SingleTextCardAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.FilePath;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.SingleTextCard;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EResourcesViewActivity extends AppCompatActivity implements SingleTextCardAdapter.SingleTextCardAdapterListener {

    String category, title_string;
    private List<SingleTextCard> list;
    private SingleTextCardAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eresources_view);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            category = String.valueOf(extras.getInt("category", 1));
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_eresources);
        list = new ArrayList<>();
        mAdapter = new SingleTextCardAdapter(list, this);

        progressBar = findViewById(R.id.progressBarEResources);

        fab = findViewById(R.id.eresources_fab);

        fab.setOnClickListener(view -> addResource());

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchResources();
    }

    private void fetchResources() {
        String url = helperFunctions.getIpAddress() + "get_eresources.php?id=" + category;

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    //news has been got so stop progress bar
                    progressBar.setVisibility(View.GONE);

                    List<SingleTextCard> items = new Gson().fromJson(response.toString(), new TypeToken<List<SingleTextCard>>() {
                    }.getType());

                    list.clear();
                    list.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
            // error in getting json, so recursive call till successful
            fetchResources();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void addResource(){
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_add_eresource, null);

        EditText title = view.findViewById(R.id.title);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        builder.setCancelable(false)
                .setPositiveButton("Select attachment", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {

                title_string = title.getText().toString();

                if(TextUtils.isEmpty(title_string)) {
                    helperFunctions.genericDialog("Please fill in the title to continue");
                } else {
                    dialog.dismiss();

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-excel", "application/ms-powerpoint"};
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                    startActivityForResult(Intent.createChooser(intent, "Select attachment"), 1);
                }
            });
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == 1 && data != null && data.getData() != null) {

            String path = FilePath.getPath(this, data.getData());

            helperFunctions.genericDialog("Your resource is being uploaded and will be added to the list soon.");

            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                String upload_URL = helperFunctions.getIpAddress() + "add_resource.php";

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, upload_URL)
                        .addFileToUpload(path, "pdf")
                        .addParameter("name", title_string.toLowerCase().replace(" ", "_"))
                        .addParameter("title", title_string)
                        .addParameter("author", preferenceManager.getPsuId())
                        .addParameter("category", category)
                        .addParameter("timestamp", String.valueOf(System.currentTimeMillis()))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();

            } catch (Exception exc) {
                // Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(SingleTextCard item) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(helperFunctions.getIpAddress() + item.getId()));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
