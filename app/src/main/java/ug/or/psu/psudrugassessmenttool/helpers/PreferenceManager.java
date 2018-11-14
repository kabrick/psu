package ug.or.psu.psudrugassessmenttool.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

public class PreferenceManager {

    private static final String PREF_NAME = "psu_preferences";
    private static final String IS_SIGNED_IN = "logged_in";
    private static final String MEMBER_CATEGORY = "member_category";
    private static final String PSU_ID = "psu_id";
    private static final String NEWS_READ = "news_read";
    private static final String TIME_IN = "time_in";
    private static final String CURRENT_LATITUDE = "current_latitude";
    private static final String CURRENT_LONGITUDE = "current_longitude";
    private static final String LAST_LATITUDE = "last_latitude";
    private static final String LAST_LONGITUDE = "last_longitude";
    private static final String IS_PHARMACY_LOCATION_SET = "pharmacy_location_set";
    private static final String PHARMACY_NAME = "pharmacy_name";
    private static final String PHARMACY_ID = "pharmacy_id";
    private static final String PHARMACY_LATITUDE = "pharmacy_latitude";
    private static final String PHARMACY_LONGITUDE = "pharmacy_longitude";
    private static final String DAY_IN = "day_in";
    private static final String MONTH_IN = "month_in";

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

    public JSONArray getNewsRead() throws JSONException {
        return new JSONArray(this.pref.getString(NEWS_READ, "[]"));
    }

    public void setNewsRead(JSONArray values) {
        this.editor.putString(NEWS_READ, values.toString());
        this.editor.commit();
    }

    public Long getTimeIn() {
        return this.pref.getLong(TIME_IN, 0);
    }

    public void setTimeIn(Long time_in) {
        this.editor.putLong(TIME_IN, time_in);
        this.editor.commit();
    }

    public Double getCurrentLatitude() {
        return Double.parseDouble(this.pref.getString(CURRENT_LATITUDE, "0.0"));
    }

    public void setCurrentLatitude(Double value) {
        this.editor.putString(CURRENT_LATITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public Double getCurrentLongitude() {
        return Double.parseDouble(this.pref.getString(CURRENT_LONGITUDE, "0.0"));
    }

    public void setCurrentLongitude(Double value) {
        this.editor.putString(CURRENT_LONGITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public Double getLastLatitude() {
        return Double.parseDouble(this.pref.getString(LAST_LATITUDE, "0.0"));
    }

    public void setLastLatitude(Double value) {
        this.editor.putString(LAST_LATITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public Double getLastLongitude() {
        return Double.parseDouble(this.pref.getString(LAST_LONGITUDE, "0.0"));
    }

    public void setLastLongitude(Double value) {
        this.editor.putString(LAST_LONGITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public Double getPharmacyLatitude() {
        return Double.parseDouble(this.pref.getString(PHARMACY_LATITUDE, "0.0"));
    }

    public void setPharmacyLatitude(Double value) {
        this.editor.putString(PHARMACY_LATITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public Double getPharmacyLongitude() {
        return Double.parseDouble(this.pref.getString(PHARMACY_LONGITUDE, "0.0"));
    }

    public void setPharmacyLongitude(Double value) {
        this.editor.putString(PHARMACY_LONGITUDE, String.valueOf(value));
        this.editor.commit();
    }

    public int getDayIn() {
        return this.pref.getInt(DAY_IN, 1);
    }

    public void setDayIn(int id) {
        this.editor.putInt(DAY_IN, id);
        this.editor.commit();
    }

    public int getMonthIn() {
        return this.pref.getInt(MONTH_IN, 0);
    }

    public void setMonthIn(int value) {
        this.editor.putInt(MONTH_IN, value);
        this.editor.commit();
    }

    public boolean isPharmacyLocationSet(){
        return this.pref.getBoolean(IS_PHARMACY_LOCATION_SET, false);
    }

    public void setIsPharmacyLocationSet(boolean value){
        this.editor.putBoolean(IS_PHARMACY_LOCATION_SET, value);
        this.editor.commit();
    }

    public String getPharmacyName(){
        return this.pref.getString(PHARMACY_NAME, "");
    }

    public void setPharmacyName(String value){
        this.editor.putString(PHARMACY_NAME, value);
        this.editor.commit();
    }

    public String getPharmacyId(){
        return this.pref.getString(PHARMACY_ID, "");
    }

    public void setPharmacyId(String value){
        this.editor.putString(PHARMACY_ID, value);
        this.editor.commit();
    }

}
