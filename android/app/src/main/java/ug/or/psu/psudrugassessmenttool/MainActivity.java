package ug.or.psu.psudrugassessmenttool;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.users.authentication.SignInActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    PreferenceManager prefManager;
    HelperFunctions helperFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        prefManager = new PreferenceManager(this);
        helperFunctions = new HelperFunctions(this);

        // check if permission has been accepted
        if(checkPermission()){
            // all permissions accepted so work on authentication
            userAuthentication();
        } else {
            // permissions not accepted, show permission dialog
            requestPermission();
        }
    }

    /**
     * user authentication method for sign in or sign up depending on user status
     */
    public void userAuthentication(){
        // allow play store to handle app update
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            1);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else {
                if(prefManager.isSignedIn()){
                    // user is signed in so check member category and go to respective dashboard
                    helperFunctions.getDefaultDashboard(prefManager.getMemberCategory());
                } else {
                    // user is not signed in so go to sign in page
                    Intent intent_sign_in = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent_sign_in);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode != RESULT_OK) {
                // close the app if update is not applied
                finish();
            }
        }
    }

    /**
     * check if all required permissions have been granted by user
     *
     * @return boolean state of permissions granted; true or false
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *  request permissions from user using android inbuilt dialogs
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    /**
     *
     * @param requestCode request code
     * @param permissions parameter list of permissions to present to user
     * @param grantResults permissions acceptance status
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (!locationAccepted && !storageAccepted) {
                    // location permission has not been granted so request for it
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            userPromptPermissions(
                                    (dialog, which) -> requestPermissions(new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE},
                                            PERMISSION_REQUEST_CODE), (dialog, which) -> {
                                        // close the app if user has not accepted permissions
                                        finish();
                                    });
                        }
                    }
                } else {
                    //location permission has been granted so continue to authentication
                    userAuthentication();
                }
            }
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
                .setMessage("You need to allow access to location and storage permissions to continue")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancel)
                .create()
                .show();
    }
}
