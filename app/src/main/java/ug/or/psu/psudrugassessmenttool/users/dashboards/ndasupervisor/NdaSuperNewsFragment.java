package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ug.or.psu.psudrugassessmenttool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NdaSuperNewsFragment extends Fragment {

    private View view;

    public NdaSuperNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nda_super_news, container, false);
        return view;
    }

}
