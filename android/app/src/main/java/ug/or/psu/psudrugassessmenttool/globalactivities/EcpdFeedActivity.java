package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;

public class EcpdFeedActivity extends AppCompatActivity {

    CardView card_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_feed);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        card_view = findViewById(R.id.card_view);

        card_view.setOnClickListener(view -> {
            startActivity(new Intent(EcpdFeedActivity.this, EcpdViewQuestionsActivity.class));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
