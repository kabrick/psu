package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.EcpdViewQuestionsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {

    Button read_resource, take_test;


    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro, container, false);

        read_resource = view.findViewById(R.id.read_resource);
        take_test = view.findViewById(R.id.take_test);

        read_resource.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://psucop.000webhostapp.com/documents/test.pdf"));
            startActivity(intent);
        });

        take_test.setOnClickListener(view1 -> ((EcpdViewQuestionsActivity) Objects.requireNonNull(getActivity())).enableFragment(2));

        return view;
    }

}
