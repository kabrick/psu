package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;

public class ForumPostActivity extends AppCompatActivity {

    ImageView picture_imageview, document_imageview, poll_imageview;
    TextView count_text, post_forum, cancel_forum;
    EditText forum_title;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    private RequestQueue rQueue;
    boolean is_picture_set = false;
    boolean is_attachment_set = false;
    private String fileExtension;
    String filename;
    String picture_name;
    private Uri filePath;
    Bitmap bitmap;
    boolean correct_characters = false;
    int target_audience = 0;
    String moderator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        moderator = preferenceManager.getPsuId();

        picture_imageview = findViewById(R.id.picture_imageview);
        picture_imageview.setOnClickListener(pictureListener);

        document_imageview = findViewById(R.id.document_imageview);
        document_imageview.setOnClickListener(documentListener);

        poll_imageview = findViewById(R.id.poll_imageview);
        poll_imageview.setOnClickListener(pollListener);

        count_text = findViewById(R.id.count_text);

        post_forum = findViewById(R.id.post_forum);
        post_forum.setOnClickListener(postForumListener);

        cancel_forum = findViewById(R.id.cancel_forum);
        cancel_forum.setOnClickListener(cancelForumListener);

        forum_title = findViewById(R.id.forum_title);
        forum_title.addTextChangedListener(mTextEditorWatcher);
    }

    private final View.OnClickListener pictureListener = v -> {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    };

    private final View.OnClickListener documentListener = v -> {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword", "application/pdf", "application/vnd.ms-excel", "application/ms-powerpoint"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(Intent.createChooser(intent, "Select attachment"), 2);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        // check if user has selected an image
        if (requestCode == 1) {
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

                    Toast.makeText(this, "Picture has been added successfully", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // check if the user has selected an attachment
        if (requestCode == 2 && data != null && data.getData() != null) {
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
        }

        if(requestCode == 3){
            moderator = data.getStringExtra("user_id");
            Toast.makeText(this, data.getStringExtra("user_name") + " has been assigned as moderator", Toast.LENGTH_LONG).show();
        }
    }

    private final View.OnClickListener pollListener = v -> {
        String[] mStringArray = {"Choose Moderator", "Specify Target Audience"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose action");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            if (i == 0){
                startActivityForResult(new Intent(this, ForumChooseModeratorActivity.class), 3);
            } else if (i == 1){
                String[] mStringArray1 = {"All", "Administrators", "Pharmacists", "Pharmacy Owners"};

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Choose action");

                builder1.setItems(mStringArray1, (dialogInterface1, i1) -> {
                    target_audience = i1;
                    Toast.makeText(this, "Target audience has been set", Toast.LENGTH_LONG).show();
                });

                // create and show the alert dialog
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    };

    private final View.OnClickListener postForumListener = v -> {
        if (correct_characters){
            helperFunctions.genericProgressBar("Posting your forum topic");
            String upload_URL = helperFunctions.getIpAddress() + "post_forum_topic.php";

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    response -> {
                        rQueue.getCache().clear();

                        helperFunctions.stopProgressBar();

                        if (response.equals("1")){
                            AlertDialog.Builder alert = new AlertDialog.Builder(ForumPostActivity.this);
                            alert.setMessage("Your discussion forum topic has been posted and will be reviewed later for approval").setPositiveButton("Okay", (dialogInterface, i) -> helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory())).show();
                        } else {
                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                        }
                    },
                    error -> {
                        helperFunctions.stopProgressBar();

                        if (error instanceof TimeoutError || error instanceof NetworkError) {
                            helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                        } else {
                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    // add the psu_id
                    params.put("title", forum_title.getText().toString());
                    params.put("author_id", preferenceManager.getPsuId());
                    params.put("moderator", moderator);
                    params.put("target_audience", String.valueOf(target_audience));
                    params.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    return params;
                }

                /*
                 *pass files using below method
                 * */
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
            rQueue = Volley.newRequestQueue(ForumPostActivity.this);
            rQueue.add(volleyMultipartRequest);
        } else {
            helperFunctions.genericDialog("Please ensure that your topic title is correctly formatted");
        }
    };

    private final View.OnClickListener cancelForumListener = v -> {
        finish();
        helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory());
    };

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            count_text.setText(s.length() + " / 380");
            if (s.length() > 380 || s.length() < 1){
                correct_characters = false;
                Toast.makeText(ForumPostActivity.this, "You have gone over the allocated number of characters", Toast.LENGTH_SHORT).show();
                count_text.setTextColor(getResources().getColor(R.color.errorText));
            } else {
                correct_characters = true;
            }
        }

        public void afterTextChanged(Editable s) {}
    };

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
