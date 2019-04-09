package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ug.or.psu.psudrugassessmenttool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PharmacyOwnerAttendanceFragment extends Fragment {

    CardView view_pharmacist_attendance_owner, add_new_pharmacy_owner, set_pharmacy_location_owner, add_pharmacist_pharmacy_owner;

    public PharmacyOwnerAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pharmacy_owner_attendance, container, false);

        view_pharmacist_attendance_owner = view.findViewById(R.id.view_pharmacist_attendance_owner);

        view_pharmacist_attendance_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        add_new_pharmacy_owner = view.findViewById(R.id.add_new_pharmacy_owner);

        add_new_pharmacy_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        set_pharmacy_location_owner = view.findViewById(R.id.set_pharmacy_location_owner);

        set_pharmacy_location_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        add_pharmacist_pharmacy_owner = view.findViewById(R.id.add_pharmacist_pharmacy_owner);

        add_pharmacist_pharmacy_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Feature not ready", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
