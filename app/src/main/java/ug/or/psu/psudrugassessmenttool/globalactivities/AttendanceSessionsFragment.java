package ug.or.psu.psudrugassessmenttool.globalactivities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ug.or.psu.psudrugassessmenttool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceSessionsFragment extends Fragment {


    public AttendanceSessionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendance_sessions, container, false);
    }

}
