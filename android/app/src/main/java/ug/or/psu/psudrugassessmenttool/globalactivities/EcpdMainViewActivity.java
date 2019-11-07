package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EcpdMainViewActivity extends AppCompatActivity {

    Button btn_take_test, btn_cancel, btn_read_resource;
    CardView resource_text_cardview;
    String ecpd_id, has_test, resource_type, resource_text, resource_url;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    TextView ecpd_title, ecpd_description, ecpd_timestamp, resource_text_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_main_view);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        ecpd_title = findViewById(R.id.ecpd_title);
        ecpd_description = findViewById(R.id.ecpd_description);
        ecpd_timestamp = findViewById(R.id.ecpd_timestamp);
        btn_read_resource = findViewById(R.id.btn_read_resource);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_take_test = findViewById(R.id.btn_take_test);
        resource_text_cardview = findViewById(R.id.resource_text_cardview);
        resource_text_textview = findViewById(R.id.resource_text_textview);

        btn_read_resource.setOnClickListener(view -> {
            if (resource_type.equals("1")){
                // text
                resource_text_textview.setText(resource_text);
                resource_text_cardview.setVisibility(View.VISIBLE);
                btn_read_resource.setVisibility(View.GONE);
                btn_take_test.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.VISIBLE);
            } else if (resource_type.equals("2")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(helperFunctions.getIpAddress() + resource_url));
                startActivity(intent);
            }
        });

        btn_cancel.setOnClickListener(view -> {
            resource_text_cardview.setVisibility(View.GONE);
            btn_read_resource.setVisibility(View.VISIBLE);
            btn_take_test.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
        });

        btn_take_test.setOnClickListener(view -> {
            if (has_test.equals("1")){
                Intent intent = new Intent(this, EcpdTakeExamActivity.class);
                intent.putExtra("id", ecpd_id);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No test available yet", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ecpd_id = extras.getString("id", "1");
        }

        getDetails();
    }

    public void getDetails(){

        helperFunctions.genericProgressBar("Fetching e-CPD...");

        String network_address = helperFunctions.getIpAddress() + "get_cpd_form.php?id=" + ecpd_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {

                    helperFunctions.stopProgressBar();

                    try {
                        //covert timestamp to readable format
                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(response.getString("timestamp")),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                        ecpd_title.setText(response.getString("title"));
                        ecpd_description.setText(response.getString("description"));
                        has_test = response.getString("has_test");
                        resource_type = response.getString("resource_type");
                        resource_text = response.getString("resource_text");
                        resource_url = response.getString("resource_url");

                        String info = "Submitted by " + response.getString("author") + " - " + timeAgo;
                        ecpd_timestamp.setText(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
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
