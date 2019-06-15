package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalfragments.IntroFragment;
import ug.or.psu.psudrugassessmenttool.globalfragments.QuestionsFragment;

public class EcpdViewQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_view_questions);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        enableFragment(1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void enableFragment(int step_counter){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (step_counter == 0){
            onBackPressed();
        } else if (step_counter == 1){
            transaction.replace(R.id.ecpd_fragment, new IntroFragment()).commit();
        } else if (step_counter == 2){
            transaction.replace(R.id.ecpd_fragment, new QuestionsFragment()).commit();
        }
    }
}
