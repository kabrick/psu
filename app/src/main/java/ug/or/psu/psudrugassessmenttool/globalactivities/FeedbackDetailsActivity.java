package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class FeedbackDetailsActivity extends AppCompatActivity {

    TextView title, text, author_name, timestamp, model, brand, sdk, version, product;
    String id;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        title = findViewById(R.id.feedback_details_title);
        text = findViewById(R.id.feedback_details_text);
        author_name = findViewById(R.id.feedback_details_author_name);
        timestamp = findViewById(R.id.feedback_details_timestamp);
        model = findViewById(R.id.feedback_details_model);
        brand = findViewById(R.id.feedback_details_brand);
        sdk = findViewById(R.id.feedback_details_sdk);
        version = findViewById(R.id.feedback_details_version);
        product = findViewById(R.id.feedback_details_product);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getString("id", "1");
        }

        getFeedbackDetails();
    }

    public void getFeedbackDetails(){
        String network_address = helperFunctions.getIpAddress()
                + "get_feedback_details.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            title.setText(response.getString("title"));
                            text.setText(response.getString("text"));
                            author_name.setText(response.getString("author_name"));
                            model.setText(response.getString("model"));
                            brand.setText(response.getString("brand"));
                            sdk.setText(response.getString("sdk"));
                            version.setText(response.getString("version"));
                            product.setText(response.getString("product"));

                            //covert timestamp to readable format
                            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                    Long.parseLong(response.getString("timestamp")),
                                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

                            timestamp.setText(timeAgo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getFeedbackDetails();
            }
        });

        VolleySingleton.getInstance(FeedbackDetailsActivity.this).addToRequestQueue(request);
    }
}
