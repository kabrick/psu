package ug.or.psu.psudrugassessmenttool.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "psu_preferences";
    private static final String IS_SIGNED_IN = "logged_in";
    private static final String MEMBER_CATEGORY = "member_category";
    private static final String PSU_ID = "psu_id";

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

    /**
     * check user sign in status
     *
     * @return boolean state of user sign in
     */
    public boolean isSignedIn(){
        return this.pref.getBoolean(IS_SIGNED_IN, false);
    }

    /**
     * set user sign in status
     *
     * @param isSignedIn boolean state of user sign in
     */
    public void setSignedIn(boolean isSignedIn){
        this.editor.putBoolean(IS_SIGNED_IN, isSignedIn);
        this.editor.commit();
    }

    /**
     * get member category
     *
     * @return member category
     */
    public String getMemberCategory(){
        return this.pref.getString(MEMBER_CATEGORY, "");
    }

    /**
     * set member category
     *
     * @param category member category
     *
     * <bold>Member Categories</bold>
     * 1 -> Systems Administrator
     * 2 -> PSU Administrator
     * 3 -> Pharmacist
     * 4 -> Pharmacy Owner
     * 5 -> NDA Administrator
     * 6 -> NDA Supervisor
     */
    public void setMemberCategory(String category){
        this.editor.putString(MEMBER_CATEGORY, category);
        this.editor.commit();
    }

    public String getPsuId() {
        return this.pref.getString(PSU_ID, "");
    }

    public void setPsuId(String id) {
        this.editor.putString(PSU_ID, id);
        this.editor.commit();
    }

}
