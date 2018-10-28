package ug.or.psu.psudrugassessmenttool.globalactivities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
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

        //get pharmacist id from the activity
        pharmacy_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacy_id;
        pharmacist_id = ((PharmacistAttendanceActivity) Objects.requireNonNull(getActivity())).pharmacist_id;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchSessions();

        return view;
    }

    private void calculateMonths(){
        //
    }

    private void fetchSessions() {

        String url = helperFunctions.getIpAddress()
                + "get_pharmacist_session.php?id=" + pharmacy_id
                + "&psu_id=" + pharmacist_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<AttendanceSession> items = new Gson().fromJson(response.toString(), new TypeToken<List<AttendanceSession>>() {
                        }.getType());

                        attendanceList.clear();
                        attendanceList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json, so recursive call till successful
                fetchSessions();
            }
        });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

}
