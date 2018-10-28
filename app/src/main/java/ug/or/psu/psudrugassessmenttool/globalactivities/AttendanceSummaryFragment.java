package ug.or.psu.psudrugassessmenttool.globalactivities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class AttendanceSummaryFragment extends Fragment {

    View view;
    String pharmacy_id, pharmacist_id;
    TextView total_hours, monday_hours, tuesday_hours, wednesday_hours, thursday_hours,
        friday_hours, saturday_hours, sunday_hours, name, phone, email, pharmacy;
    HelperFunctions helperFunctions;

    public AttendanceSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_summary, container, false);

        //get pharmacist id from the activity
        pharmacy_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacy_id;
        pharmacist_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacist_id;

        //cast a spell to all the text_views
        total_hours = view.findViewById(R.id.total_hours);
        monday_hours = view.findViewById(R.id.monday_hours);
        tuesday_hours = view.findViewById(R.id.tuesday_hours);
        wednesday_hours = view.findViewById(R.id.wednesday_hours);
        thursday_hours = view.findViewById(R.id.thursday_hours);
        friday_hours = view.findViewById(R.id.friday_hours);
        saturday_hours = view.findViewById(R.id.saturday_hours);
        sunday_hours = view.findViewById(R.id.sunday_hours);
        name = view.findViewById(R.id.attendance_name);
        phone = view.findViewById(R.id.attendance_phone);
        email = view.findViewById(R.id.attendance_email);
        pharmacy = view.findViewById(R.id.attendance_pharmacy);

        //call 911 for help
        helperFunctions = new HelperFunctions(getContext());

        display_summary();

        return view;
    }

    public void display_summary(){
        String network_address = helperFunctions.getIpAddress()
                + "get_pharmacist_summary.php?id=" + pharmacy_id
                + "&psu_id=" + pharmacist_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            total_hours.setText(response.getString("total_hours"));
                            monday_hours.setText(response.getString("monday_hours"));
                            tuesday_hours.setText(response.getString("tuesday_hours"));
                            wednesday_hours.setText(response.getString("wednesday_hours"));
                            thursday_hours.setText(response.getString("thursday_hours"));
                            friday_hours.setText(response.getString("friday_hours"));
                            saturday_hours.setText(response.getString("saturday_hours"));
                            sunday_hours.setText(response.getString("sunday_hours"));
                            name.setText(response.getString("name"));
                            phone.setText(response.getString("phone"));
                            email.setText(response.getString("email"));
                            pharmacy.setText(response.getString("pharmacy"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                display_summary();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
