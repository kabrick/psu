package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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

        add_picture_fab.setOnClickListener(view -> {
            addPicture();
            fam.close(true);
        });

        add_attachments_fab.setOnClickListener(view -> {
            addAttachment();
            fam.close(true);
        });

        remove_attachments_fab.setOnClickListener(view -> {
            attachment_name.setText("");
            attachment_name.setVisibility(View.GONE);
            is_attachment_set = false;
            Toast.makeText(CreateJobsActivity.this, "Attachment has been removed", Toast.LENGTH_LONG).show();
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

        String url = helperFunctions.getIpAddress() + "post_jobs.php";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    rQueue.getCache().clear();

                    helperFunctions.stopProgressBar();

                    if (response.equals("1")){
                        AlertDialog.Builder alert = new AlertDialog.Builder(CreateJobsActivity.this);

                        alert.setMessage("Job has been posted successfully and will be reviewed later").setPositiveButton("Okay", (dialogInterface, i) -> helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory())).show();
                    } else {
                        helperFunctions.genericDialog("Something went wrong. Please try again later");
                    }
                },
                error -> {
                    helperFunctions.stopProgressBar();

                    if (error instanceof TimeoutError || error instanceof NetworkError) {
                        helperFunctions.genericDialog("Connection Error! Please make sure you are connected to a working internet connection.");
                    } else {
                        helperFunctions.genericDialog("Something went wrong. Please try again later");
                    }
                }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("text", text);
                params.put("source", source);
                params.put("author_id", preferenceManager.getPsuId());
                params.put("timestamp", String.valueOf(System.currentTimeMillis()));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws IOException {
                Map<String, DataPart> params = new HashMap<>();

                if (is_picture_set){
                    params.put("picture_filename", new DataPart(picture_name + ".png", getFileDataFromDrawable(bitmap)));
                }

                if (is_attachment_set){
                    params.put("document_filename", new DataPart(filename + "."  + fileExtension, getBytesFromFile(filePath)));
                }

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

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
            int maxBufferSize = 1024 * 1024;
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
}
