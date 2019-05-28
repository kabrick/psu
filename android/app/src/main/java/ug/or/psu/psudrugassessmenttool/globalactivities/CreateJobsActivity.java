package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.FilePath;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;

public class CreateJobsActivity extends AppCompatActivity {

    EditText jobs_title, jobs_description, jobs_source;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private RequestQueue rQueue;
    boolean is_picture_set = false;
    boolean is_attachment_set = false;
    private String fileExtension;
    String filename;
    String picture_name;
    ImageView image;
    TextView attachment_name;
    private final int IMAGE_REQUEST_CODE = 1;
    private final int ATTACHMENT_REQUEST_CODE = 2;
    private Uri filePath;
    Bitmap bitmap;
    FloatingActionMenu fam;
    FloatingActionButton add_picture_fab, add_attachments_fab, remove_attachments_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jobs);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        jobs_title = findViewById(R.id.jobs_title);
        jobs_description = findViewById(R.id.jobs_description);
        jobs_source = findViewById(R.id.jobs_source);

        attachment_name = findViewById(R.id.jobs_attachment_name);
        image = findViewById(R.id.create_jobs_image);

        add_attachments_fab = findViewById(R.id.add_attachments_fab);
        add_picture_fab = findViewById(R.id.add_picture_fab);
        remove_attachments_fab = findViewById(R.id.remove_attachments_fab);
        fam = findViewById(R.id.jobs_fam);

        add_picture_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPicture();
                fam.close(true);
            }
        });

        add_attachments_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAttachment();
                fam.close(true);
            }
        });

        remove_attachments_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_name.setText("");
                attachment_name.setVisibility(View.GONE);
                is_attachment_set = false;
                Toast.makeText(CreateJobsActivity.this, "Attachment has been removed", Toast.LENGTH_LONG).show();
            }
        });

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_create_jobs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_post_jobs) {
            postJobs();
        }

        return super.onOptionsItemSelected(item);
    }

    public void postJobs(){

        final String title = jobs_title.getText().toString();
        final String text = jobs_description.getText().toString();
        final String source = jobs_source.getText().toString();

        if(TextUtils.isEmpty(title)) {
            jobs_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(text)) {
            jobs_description.setError("Please fill in the text");
            return;
        }

        if(TextUtils.isEmpty(source)) {
            jobs_source.setError("Please fill in the source");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting your job advert...");

        //get the current timestamp
        final Long timestamp_long = System.currentTimeMillis();

        String url = helperFunctions.getIpAddress() + "post_jobs.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Posting job advert failed!");
                } else {
                    // check if image is selected
                    if(is_picture_set){
                        // upload picture
                        uploadProfilePicture(bitmap, response);
                    } else {
                        if(is_attachment_set){
                            uploadAttachment(response);
                        }
                        helperFunctions.stopProgressBar();
                        AlertDialog.Builder alert = new AlertDialog.Builder(CreateJobsActivity.this);

                        alert.setMessage("Job has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                            }
                        }).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                helperFunctions.stopProgressBar();
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("title", title);
                data.put("text", text);
                data.put("source", source);
                data.put("author_id", preferenceManager.getPsuId());
                data.put("timestamp", timestamp_long.toString());
                return data;
            }
        };

        requestQueue.add(MyStringRequest);
    }

    public void addPicture(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public void addAttachment() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-excel", "application/ms-powerpoint"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(Intent.createChooser(intent, "Select attachment"), ATTACHMENT_REQUEST_CODE);
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

                    image.setVisibility(View.VISIBLE);

                    image.setImageBitmap(bitmap);

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

                    Toast.makeText(this, "Picture has been added successfully", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // check if the user has selected an attachment
        if (requestCode == ATTACHMENT_REQUEST_CODE && data != null && data.getData() != null) {
            filePath = data.getData();
            is_attachment_set = true;

            Toast.makeText(this, "Document has been added successfully", Toast.LENGTH_LONG).show();

            String path = getRealPathFromURI(this, filePath);
            filename = path.substring(path.lastIndexOf("/")+1);

            // for the file extension
            int i = filename.lastIndexOf('.');
            if (i > 0) {
                fileExtension = filename.substring(i+1);
            }

            // for the file name
            if (filename.indexOf(".") > 0) {
                filename = filename.substring(0, filename.lastIndexOf("."));
            }

            attachment_name.setVisibility(View.VISIBLE);

            attachment_name.setText(filename);
        }
    }

    public void uploadAttachment(String id) {
        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            String upload_URL = helperFunctions.getIpAddress() + "upload_jobs_attachment.php";

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, upload_URL)
                    .addFileToUpload(path, "pdf")
                    .addParameter("id", id)
                    .addParameter("name", filename + "."  + fileExtension)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            // Toast.makeText(this, exc.getMessage(), Toast.LENGTH_LONG).show();
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
            assert cursor != null;
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
        String upload_URL = helperFunctions.getIpAddress() + "upload_jobs_picture.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();

                        if(is_attachment_set){
                            // upload pdf
                            uploadAttachment(id);
                        }

                        helperFunctions.stopProgressBar();
                        AlertDialog.Builder alert = new AlertDialog.Builder(CreateJobsActivity.this);

                        alert.setMessage("Job has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                            }
                        }).show();
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
        rQueue = Volley.newRequestQueue(CreateJobsActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}