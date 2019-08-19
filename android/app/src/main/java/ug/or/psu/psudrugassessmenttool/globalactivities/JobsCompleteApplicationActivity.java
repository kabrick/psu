package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;

public class JobsCompleteApplicationActivity extends AppCompatActivity {

    private RequestQueue rQueue;
    private Uri filePath;
    private String fileExtension;
    private final int CV_REQUEST_CODE = 1;
    private boolean is_cv_attached = false;
    private String jobs_id;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    EditText cover_letter;
    TextView resume_name;
    ImageView cancel_resume;
    LinearLayout cv_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_complete_application);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            jobs_id = extras.getString("id", "1");
        }

        cover_letter = findViewById(R.id.cover_letter);
        resume_name = findViewById(R.id.resume_name);
        cancel_resume = findViewById(R.id.cancel_resume);
        cv_details = findViewById(R.id.cv_details);

        cancel_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resume_name.setText("");
                cv_details.setVisibility(View.GONE);
                is_cv_attached = false;
                Toast.makeText(JobsCompleteApplicationActivity.this, "CV has been removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == CV_REQUEST_CODE && data != null && data.getData() != null) {
            filePath = data.getData();

            helperFunctions.genericDialog("CV has been added successfully");

            is_cv_attached = true;

            String path = getRealPathFromURI(this, filePath);
            String filename = path.substring(path.lastIndexOf("/")+1);

            // for the file extension
            int i = filename.lastIndexOf('.');
            if (i > 0) {
                fileExtension = filename.substring(i+1);
            }

            // for the file name
            if (filename.indexOf(".") > 0) {
                filename = filename.substring(0, filename.lastIndexOf("."));
            }

            cv_details.setVisibility(View.VISIBLE);

            resume_name.setText(filename);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void addCV(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mime_types = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mime_types);
        startActivityForResult(Intent.createChooser(intent, "Select resume"), CV_REQUEST_CODE);
    }

    public void submitApplication(View view){
        if(is_cv_attached){
            submitWithCV();
        } else {
            helperFunctions.genericDialog("Please add your CV before submitting");
        }
    }

    public void submitWithCV(){
        helperFunctions.genericProgressBar("Posting your application...");

        String upload_URL = helperFunctions.getIpAddress() + "post_job_application.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        helperFunctions.stopProgressBar();

                        switch (response) {
                            case "2":
                                helperFunctions.genericDialog("You have already applied for this job");
                                break;
                            case "1":
                                //saved successfully
                                AlertDialog.Builder alert = new AlertDialog.Builder(JobsCompleteApplicationActivity.this);

                                alert.setMessage("Job application has been posted successfully").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
                                    }
                                }).show();
                                break;
                            default:
                                helperFunctions.genericDialog("Something went wrong! Please try again");
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        helperFunctions.stopProgressBar();

                        if (error instanceof TimeoutError || error instanceof NetworkError) {
                            helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                        } else {
                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", jobs_id);
                params.put("psu_id", preferenceManager.getPsuId());
                params.put("cover_letter", cover_letter.getText().toString());
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long resume_name = System.currentTimeMillis();
                byte[] inputData = new byte[0];
                try {
                    InputStream iStream =   getContentResolver().openInputStream(filePath);
                    inputData = getBytes(iStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                params.put("filename", new DataPart(resume_name + "." + fileExtension, inputData));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(JobsCompleteApplicationActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    /*public void submitWithoutCV(){
        //
    }*/

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
}
