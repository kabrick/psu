package ug.or.psu.psudrugassessmenttool.globalfragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends Fragment {

    public QuestionsFragment() {
        // Required empty public constructor
    }

    RadioGroup question1, question2, question3, question4, question5,
            question6, question7, question8, question9, question10;
    int value1, value2, value3, value4, value5,
            value6, value7, value8, value9, value10 = 0;
    Button submit_test;
    TextView correct1, correct2, correct3, correct4, correct5,
            correct6, correct7, correct8, correct9, correct10;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_questions, container, false);

        question1 = view.findViewById(R.id.question1);
        question2 = view.findViewById(R.id.question2);
        question3 = view.findViewById(R.id.question3);
        question4 = view.findViewById(R.id.question4);
        question5 = view.findViewById(R.id.question5);
        question6 = view.findViewById(R.id.question6);
        question7 = view.findViewById(R.id.question7);
        question8 = view.findViewById(R.id.question8);
        question9 = view.findViewById(R.id.question9);
        question10 = view.findViewById(R.id.question10);
        submit_test = view.findViewById(R.id.submit_test);
        correct1 = view.findViewById(R.id.correct1);
        correct2 = view.findViewById(R.id.correct2);
        correct3 = view.findViewById(R.id.correct3);
        correct4 = view.findViewById(R.id.correct4);
        correct5 = view.findViewById(R.id.correct5);
        correct6 = view.findViewById(R.id.correct6);
        correct7 = view.findViewById(R.id.correct7);
        correct8 = view.findViewById(R.id.correct8);
        correct9 = view.findViewById(R.id.correct9);
        correct10 = view.findViewById(R.id.correct10);

        question1.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer1){
                value1 = 1;
            }
        });

        question2.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer2){
                value2 = 1;
            }
        });

        question3.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer3){
                value3 = 1;
            }
        });

        question4.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer4){
                value4 = 1;
            }
        });

        question5.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer5){
                value5 = 1;
            }
        });

        question6.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer6){
                value6 = 1;
            }
        });

        question7.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer7){
                value7 = 1;
            }
        });

        question8.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer8){
                value8 = 1;
            }
        });

        question9.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer9){
                value9 = 1;
            }
        });

        question10.setOnCheckedChangeListener((radioGroup, checked_id) -> {
            if(checked_id == R.id.answer10){
                value10 = 1;
            }
        });

        submit_test.setOnClickListener(view1 -> {

            int total = value1 + value2 + value3 + value4 + value5 + value6 + value7 + value8 + value9 + value10;

            AlertDialog.Builder alert = new AlertDialog.Builder(Objects.requireNonNull(getContext()));

            alert.setMessage("You scored " + total + " out of 10.").setPositiveButton("Okay", (dialogInterface, i) -> {
                //
            }).show();

            correct1.setVisibility(View.VISIBLE);
            correct2.setVisibility(View.VISIBLE);
            correct3.setVisibility(View.VISIBLE);
            correct4.setVisibility(View.VISIBLE);
            correct5.setVisibility(View.VISIBLE);
            correct6.setVisibility(View.VISIBLE);
            correct7.setVisibility(View.VISIBLE);
            correct8.setVisibility(View.VISIBLE);
            correct9.setVisibility(View.VISIBLE);
            correct10.setVisibility(View.VISIBLE);
        });

        return view;
    }

}
