package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EditNewsActivity extends AppCompatActivity {

    TextView news_title, news_text;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    View activityView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        news_text = findViewById(R.id.news_text_edit);
        news_title = findViewById(R.id.news_title_edit);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        activityView = findViewById(R.id.edit_news);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            id = extras.getString("id", "1");
            getNewsDetails(id);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getNewsDetails(String id){
        String network_address = helperFunctions.getIpAddress()
                + "edit_news_details.php?id=" + id;

        helperFunctions.genericProgressBar("Getting news details...");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        helperFunctions.stopProgressBar();
                        try {
                            news_text.setText(response.getString("text"));
                            news_title.setText(response.getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void postNews(View view){

        String title = news_title.getText().toString();
        String text = news_text.getText().toString();

        if(TextUtils.isEmpty(title)) {
            news_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(text)) {
            news_text.setError("Please fill in the text");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Editing your news article...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "edit_news.php?title=" + title
                + "&text=" + text
                + "&id=" + id
                + "&timestamp=" + timestamp_long.toString();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dismiss progress dialog
                        helperFunctions.stopProgressBar();

                        if(response.equals("1")){
                            //saved article successfully
                            AlertDialog.Builder alert = new AlertDialog.Builder(EditNewsActivity.this);

                            alert.setMessage("News edited successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                }
                            }).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
