package ug.or.psu.psudrugassessmenttool;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if permission has been accepted
        if(checkPermission()){
            // TODO: all permissions accepted so work on authentication
        } else {
            //permissions not accepted, show permission dialog
            requestPermission();
        }
    }

    /**
     * check if all required permissions have been granted by user
     *
     * @return boolean state of permissions granted; true or false
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *  request permissions from user using android inbuilt dialogs
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    /**
     *
     * @param requestCode request code
     * @param permissions parameter list of permissions to present to user
     * @param grantResults permissions acceptance status
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (!locationAccepted){
                        //location permission has not been granted so request for it
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                userPromptPermissions(
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                return;
                            }
                        }
                    } else {
                        //location permission has been granted so continue to authentication
                        // TODO: sign up logic here
                    }
                }
                break;
        }
    }


    /**
     * Dialog display when user rejects permissions prompt
     *
     * @param okListener action taken when user agrees to set permissions
     * @param cancel action taken when user does not agree to set permission
     */
    private void userPromptPermissions(DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancel) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("You need to allow access to location permission to continue")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancel)
                .create()
                .show();
    }
}
