package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

public class ViewSubmittedCpdActivity extends AppCompatActivity {

    CardView card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submitted_cpd);

        card_view = findViewById(R.id.card_view);

        card_view.setOnClickListener(view -> {
            String[] mStringArray = {"Edit || Delete E-CPD", "View E-CPD Test Results"};

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewSubmittedCpdActivity.this);
            builder.setTitle("Choose your action");

            builder.setItems(mStringArray, (dialogInterface, i) -> {
                if (i == 0){
                    //
                } else if (i == 1){
                    //
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
