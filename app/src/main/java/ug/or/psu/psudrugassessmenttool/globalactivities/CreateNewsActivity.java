package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class CreateNewsActivity extends AppCompatActivity {

    TextView news_title, news_text;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private RequestQueue rQueue;
    View activityView;
    boolean is_picture_set = false;
    String picture_name;
    private final int IMAGE_REQUEST_CODE = 1;
    Bitmap bitmap;
    FloatingActionMenu fam;
    FloatingActionButton view_attachments_fab, add_attachments_fab, post_news_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        news_text = findViewById(R.id.news_text);
        news_title = findViewById(R.id.news_title);

        post_news_fab = findViewById(R.id.post_news_fab);
        add_attachments_fab = findViewById(R.id.add_attachments_fab);
        view_attachments_fab = findViewById(R.id.view_attachments_fab);
        fam = findViewById(R.id.news_fam);

        // apply listeners
        post_news_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);
                postNews();
            }
        });

        add_attachments_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPicture();
                fam.close(true);
            }
        });

        view_attachments_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.close(true);

                if(is_picture_set){
                    Toast.makeText(CreateNewsActivity.this, picture_name, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateNewsActivity.this, "No picture selected", Toast.LENGTH_LONG).show();
                }
            }
        });

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        activityView = findViewById(R.id.create_news);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel_create_news) {
            helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
        }

        return super.onOptionsItemSelected(item);
    }

    public void postNews(){

        String title = news_title.getText().toString();
        String text = news_text.getText().toString();

        if(TextUtils.isEmpty(title)) {
            news_title.setError("Please fill in the title");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting your news article...");

        //get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_news.php?title=" + title
                + "&text=" + text
                + "&author_id=" + preferenceManager.getPsuId()
                + "&timestamp=" + timestamp_long.toString();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("0")){
                            helperFunctions.genericSnackbar("Posting news article failed!", activityView);
                            helperFunctions.stopProgressBar();
                        } else {
                            // check if image is selected
                            if(is_picture_set){
                                // upload picture and pass the buck to that function
                                uploadProfilePicture(bitmap, response);
                            } else {
                                // saved article successfully
                                helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                helperFunctions.stopProgressBar();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    helperFunctions.genericSnackbar("Connection Error. Please check your connection", activityView);
                } else if (error instanceof AuthFailureError) {
                    helperFunctions.genericSnackbar("Authentication error", activityView);
                } else if (error instanceof ServerError) {
                    helperFunctions.genericSnackbar("Server error", activityView);
                } else if (error instanceof NetworkError) {
                    helperFunctions.genericSnackbar("Network error", activityView);
                } else if (error instanceof ParseError) {
                    helperFunctions.genericSnackbar("Data from server not available", activityView);
                }
            }
        });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void addPicture(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // check if user has selected an image
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    Uri uri = getImageUri(this, bitmap);
                    String path = getRealPathFromURI(this, uri);
                    String filename = path.substring(path.lastIndexOf("/")+1);

                    if (filename.indexOf(".") > 0) {
                        picture_name = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        picture_name =  filename;
                    }

                    // set that the picture has been received
                    is_picture_set = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void uploadProfilePicture(final Bitmap bitmap, final String id){
        String upload_URL = helperFunctions.getIpAddress() + "upload_news_picture.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                        helperFunctions.stopProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                // add the psu_id
                params.put("id", id);
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("filename", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(CreateNewsActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
