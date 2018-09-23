package ug.or.psu.psudrugassessmenttool.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "psu_preferences";

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    /**
     * Class constructor to set privacy mode and create new editor
     *
     * @param context application context
     *
     * suppress for adding commit to created prefs editor
     */
    @SuppressLint("CommitPrefEdits")
    public PreferenceManager(Context context) {
        int PRIVATE_MODE = 0;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = this.pref.edit();
    }
}
