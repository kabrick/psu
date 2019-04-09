package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ug.or.psu.psudrugassessmenttool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PharmacistAttendanceFragment extends Fragment {

    CardView view_attendance_pharmacist, login_attendance_pharmacist, log_out_attendance_pharmacist, set_location_pharmacist, select_pharmacy_pharmacist;

    public PharmacistAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pharmacist_attendance, container, false);

        set_location_pharmacist = view.findViewById(R.id.set_location_pharmacist);

        set_location_pharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        view_attendance_pharmacist = view.findViewById(R.id.view_attendance_pharmacist);

        view_attendance_pharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        login_attendance_pharmacist = view.findViewById(R.id.login_attendance_pharmacist);

        login_attendance_pharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        log_out_attendance_pharmacist = view.findViewById(R.id.log_out_attendance_pharmacist);

        log_out_attendance_pharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        select_pharmacy_pharmacist = view.findViewById(R.id.select_pharmacy_pharmacist);

        select_pharmacy_pharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
