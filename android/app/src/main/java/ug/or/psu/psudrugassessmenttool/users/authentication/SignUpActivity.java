package ug.or.psu.psudrugassessmenttool.users.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleyMultipartRequest;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class SignUpActivity extends AppCompatActivity {

    AwesomeValidation mAwesomeValidation;
    PreferenceManager prefManager;
    HelperFunctions helperFunctions;
    MaterialSpinner sign_up_mem_status;
    Bitmap bitmap;
    ImageView profile_picture;
    private final int IMAGE_REQUEST_CODE = 1;
    private RequestQueue rQueue;
    EditText sign_up_password, sign_up_username, sign_up_phone, sign_up_email, sign_up_full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        prefManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.psu_logo);

        profile_picture = findViewById(R.id.sign_up_profile_picture);

        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_username = findViewById(R.id.sign_up_username);
        sign_up_phone = findViewById(R.id.sign_up_phone);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_full_name = findViewById(R.id.sign_up_full_name);
        sign_up_mem_status = findViewById(R.id.sign_up_mem_status);

        sign_up_mem_status.setItems("Pharmacist", "Pharmacy Owner","Other");

        //add validation for the fields
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.sign_up_password, "[a-zA-Z0-9\\s]+", R.string.missing_password);
        mAwesomeValidation.addValidation(this, R.id.sign_up_username,"[a-zA-Z0-9\\s]+", R.string.missing_username);
        mAwesomeValidation.addValidation(this, R.id.sign_up_phone, "[a-zA-Z0-9\\s]+", R.string.missing_telephone);
        mAwesomeValidation.addValidation(this, R.id.sign_up_full_name, "[a-zA-Z0-9\\s]+", R.string.missing_fullname);
        // missing_mem_type
    }

    public void changeProfilePicture(View view){
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
                    profile_picture.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void signUp(View view){
        if (mAwesomeValidation.validate()){
            if(helperFunctions.getConnectionStatus()){

                //show progress dialog
                helperFunctions.genericProgressBar("Signing you up...");

                String mem_status = sign_up_mem_status.getText().toString();

                switch (mem_status) {
                    case "Pharmacist":
                        mem_status = "pharmacists";
                        break;
                    case "Pharmacy Owner":
                        mem_status = "pharmdirector";
                        break;
                    case "Other":
                        mem_status = "other";
                        break;
                    default:
                        helperFunctions.stopProgressBar();
                        helperFunctions.genericDialog("Select membership status");
                        return;
                }

                String upload_URL = helperFunctions.getIpAddress() + "user_sign_up.php";

                final String finalMem_status = mem_status;
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                        response -> {
                            rQueue.getCache().clear();
                            helperFunctions.stopProgressBar();

                            switch (response) {
                                case "0": {
                                    helperFunctions.genericDialog("Something went wrong. Please try again later");
                                    break;
                                }
                                case "1": {
                                    // user registered successfully
                                    AlertDialog.Builder alert = new AlertDialog.Builder(SignUpActivity.this);

                                    alert.setMessage("Your profile has been created. Sign in to continue").setPositiveButton("Okay", (dialogInterface, i) -> {
                                        Intent sign_in_intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(sign_in_intent);
                                    }).show();
                                    break;
                                }
                                case "2": {
                                    // email already taken
                                    helperFunctions.genericDialog("Email address has already been registered");
                                    break;
                                }
                                case "3": {
                                    // username already taken
                                    helperFunctions.genericDialog("User name has already been registered");
                                    break;
                                }
                                case "4": {
                                    // phone number already taken
                                    helperFunctions.genericDialog("Phone number has already been registered");
                                    break;
                                }
                                default:
                                    helperFunctions.genericDialog("Something went wrong. Please try again later");
                                    break;
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
                        params.put("full_names", sign_up_full_name.getText().toString());
                        params.put("email", sign_up_email.getText().toString());
                        params.put("phone", sign_up_phone.getText().toString());
                        params.put("password", sign_up_password.getText().toString());
                        params.put("username", sign_up_username.getText().toString());
                        params.put("mem_status", finalMem_status);
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
                rQueue = Volley.newRequestQueue(this);
                rQueue.add(volleyMultipartRequest);
            } else {
                helperFunctions.genericDialog("Internet connection has been lost!");
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void signIn(View view){
        Intent sign_in_intent = new Intent(this, SignInActivity.class);
        startActivity(sign_in_intent);
    }
}
