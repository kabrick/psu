package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.AttendanceSessionAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.AttendanceSession;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class AttendanceSessionsFragment extends Fragment {

    View view;
    Spinner session_spinner;
    private List<AttendanceSession> attendanceList;
    private AttendanceSessionAdapter mAdapter;
    String pharmacy_id, pharmacist_id;
    HelperFunctions helperFunctions;

    public AttendanceSessionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_sessions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.attendance_sessions_recycler);
        attendanceList = new ArrayList<>();
        mAdapter = new AttendanceSessionAdapter(attendanceList);

        helperFunctions = new HelperFunctions(getContext());

        // spinner logic
        session_spinner = view.findViewById(R.id.attendance_sessions_spinner);

        session_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                attendanceList.clear();
                mAdapter.notifyDataSetChanged();
                fetchSessions(String.valueOf(position + 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        //get pharmacist id from the activity
        pharmacy_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacy_id;
        pharmacist_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacist_id;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        calculateMonths();

        return view;
    }

    private void calculateMonths(){
        List<String> months_spinner = new ArrayList<>();

        //this is to keep track of current month from first month
        int month_id = 0;

        Calendar c = Calendar.getInstance();

        //get current date parameters
        int current_year = c.get(Calendar.YEAR);
        int current_month = c.get(Calendar.MONTH);

        //start year to stop at
        int start_year = helperFunctions.getStartYear();

        //start loop by incrementing start year to the current year
        while (start_year <= current_year){

            //january as 0
            int start_month = 0;

            //last month to stop at
            int last_month_in_year = 12;

            //check if this is last year and set last month to current month + 1
            if(start_year == current_year){
                last_month_in_year = current_month + 1;
            }

            //loop the months
            while (start_month < last_month_in_year){
                switch (start_month){
                    case 0:
                        //add to the spinner adapter
                        months_spinner.add("January " + start_year);
                        break;
                    case 1:
                        //add to the spinner adapter
                        months_spinner.add("Feb " + start_year);
                        break;
                    case 2:
                        //add to the spinner adapter
                        months_spinner.add("March " + start_year);
                        break;
                    case 3:
                        //add to the spinner adapter
                        months_spinner.add("April " + start_year);
                        break;
                    case 4:
                        //add to the spinner adapter
                        months_spinner.add("May " + start_year);
                        break;
                    case 5:
                        //add to the spinner adapter
                        months_spinner.add("June " + start_year);
                        break;
                    case 6:
                        //add to the spinner adapter
                        months_spinner.add("July " + start_year);
                        break;
                    case 7:
                        //add to the spinner adapter
                        months_spinner.add("Aug " + start_year);
                        break;
                    case 8:
                        //add to the spinner adapter
                        months_spinner.add("Sept " + start_year);
                        break;
                    case 9:
                        //add to the spinner adapter
                        months_spinner.add("Oct " + start_year);
                        break;
                    case 10:
                        //add to the spinner adapter
                        months_spinner.add("Nov " + start_year);
                        break;
                    case 11:
                        //add to the spinner adapter
                        months_spinner.add("Dec " + start_year);
                        break;
                    default:
                        //
                        break;
                }
                month_id++;
                start_month++;
            }

            start_year++;
        }

        //set adapter
        ArrayAdapter<String> months_adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, months_spinner);

        // Drop down layout style - list view with radio button
        months_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        session_spinner.setAdapter(months_adapter);

        //send the last month to fetch sessions
        fetchSessions(String.valueOf(month_id));
    }

    private void fetchSessions(final String month_id) {

        String url = helperFunctions.getIpAddress()
                + "get_pharmacist_session.php?id=" + pharmacy_id
                + "&psu_id=" + pharmacist_id
                + "&month_id=" + month_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<AttendanceSession> items = new Gson().fromJson(response.toString(), new TypeToken<List<AttendanceSession>>() {
                        }.getType());

                        Log.e("ff", response.toString());

                        attendanceList.clear();
                        attendanceList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchSessions(month_id);
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
