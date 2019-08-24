package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;

public class EcpdCreateActivity extends AppCompatActivity {

    TextView resource_document_name;
    EditText cpd_title, cpd_description, resource_text;
    RadioGroup resource_type;
    int resource_type_int = 0;
    boolean document_uploaded = false;
    Button add_resource;
    CardView resource_text_cardview, resource_document_cardview;
    private String fileExtension;
    String filename;
    private Uri filePath;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_create);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        preferenceManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        resource_document_name = findViewById(R.id.resource_document_name);
        cpd_title = findViewById(R.id.cpd_title);
        cpd_description = findViewById(R.id.cpd_description);
        resource_text = findViewById(R.id.resource_text);
        add_resource = findViewById(R.id.add_resource);
        resource_type = findViewById(R.id.resource_type);
        resource_text_cardview = findViewById(R.id.resource_text_cardview);
        resource_document_cardview = findViewById(R.id.resource_document_cardview);

        add_resource.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-excel", "application/ms-powerpoint"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            startActivityForResult(Intent.createChooser(intent, "Select attachment"), 1);
        });

        resource_type.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.resource_type_text){
                resource_text_cardview.setVisibility(View.VISIBLE);
                resource_document_cardview.setVisibility(View.GONE);
                resource_type_int = 1;
            } else if (checkedId == R.id.resource_type_document){
                resource_text_cardview.setVisibility(View.GONE);
                resource_document_cardview.setVisibility(View.VISIBLE);
                resource_type_int = 2;
            }
        });
    }

    public void submitEcpd(View view){
        String title = cpd_title.getText().toString();
        String description = cpd_description.getText().toString();
        String resource_text_string = resource_text.getText().toString();

        if(TextUtils.isEmpty(title)) {
            cpd_title.setError("Please fill in the title");
            return;
        }

        if(TextUtils.isEmpty(description)) {
            cpd_description.setError("Please fill in the description");
            return;
        }

        if (resource_type_int == 1 && TextUtils.isEmpty(resource_text_string)){
            // make sure the text has been filled
            resource_text.setError("Please fill in the text");
            return;
        }

        if (resource_type_int == 2 && !document_uploaded) {
            // make sure a document is uploaded
            helperFunctions.genericDialog("Please attach a document");
            return;
        }

        if (resource_type_int == 0) {
            // prompt the user to select an option
            helperFunctions.genericDialog("Please select a resource type");
            return;
        }

        //show progress dialog
        helperFunctions.genericProgressBar("Posting e-cpd...");

        //get the current timestamp
        final Long timestamp_long = System.currentTimeMillis();

        String url = helperFunctions.getIpAddress() + "post_cpd.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response -> {
            if(response.equals("0")){
                helperFunctions.genericDialog("Posting e-cpd failed!");
                helperFunctions.stopProgressBar();
            } else {
                if(resource_type_int == 2){
                    uploadAttachment(response);
                }
                helperFunctions.stopProgressBar();
                AlertDialog.Builder alert = new AlertDialog.Builder(EcpdCreateActivity.this);

                alert.setMessage("Your e-cpd submission has been made").setPositiveButton("Okay", (dialogInterface, i) -> helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory())).show();
            }
        }, error -> {
            helperFunctions.stopProgressBar();

            if (error instanceof TimeoutError || error instanceof NetworkError) {
                helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
            } else {
                helperFunctions.genericDialog("Something went wrong. Please try again later");
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<>();
                data.put("title", title);
                data.put("description", description);
                data.put("resource_type", String.valueOf(resource_type_int));
                data.put("resource_text", resource_text_string);
                data.put("author_id", preferenceManager.getPsuId());
                data.put("timestamp", timestamp_long.toString());
                return data;
            }
        };

        requestQueue.add(MyStringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // check if the user has selected an attachment
        if (requestCode == 1 && data != null && data.getData() != null) {
            filePath = data.getData();

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

            document_uploaded = true;

            resource_document_name.setVisibility(View.VISIBLE);

            resource_document_name.setText(filename);
        }
    }

    private String getRealPathFromURI(Context context, Uri uri) {
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            assert inputStream != null;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            //
        }
        return file.getPath();
    }

    public void uploadAttachment(String id) {

        String upload_URL = helperFunctions.getIpAddress() + "upload_cpd_attachment.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
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
                params.put("id", id);
                params.put("name", filename + "."  + fileExtension);
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();
                params.put("filename", new DataPart(filename + "."  + fileExtension, getBytesFromFile(filePath)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(EcpdCreateActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getBytesFromFile(Uri uri) throws IOException {
        try (InputStream iStream = getContentResolver().openInputStream(uri)) {
            assert iStream != null;

            byte[] bytesResult;
            try (ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream()) {
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int len;
                while ((len = iStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                bytesResult = byteBuffer.toByteArray();
            }

            return bytesResult;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
