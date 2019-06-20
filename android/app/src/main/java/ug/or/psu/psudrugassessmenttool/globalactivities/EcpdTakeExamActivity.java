package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EcpdTakeExamActivity extends AppCompatActivity {

    String ecpd_id;
    ArrayList<String> questions, question_ids;
    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;
    TextView question;
    RadioGroup radio_group;
    RadioButton option1, option2, option3, option4;
    Button control_button;
    int counter = 0;
    int answer_position;
    int questions_number;
    int current_score = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_take_exam);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ecpd_id = extras.getString("id", "1");
        }

        preferenceManager = new PreferenceManager(this);

        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        control_button = findViewById(R.id.control_button);
        radio_group = findViewById(R.id.radio_group);

        control_button.setOnClickListener(view -> {
            if ((counter + 1) == questions_number){
                helperFunctions.genericProgressBar("Saving test scores...");

                double percentage_score = Math.round(((double) current_score / questions_number) * 100);

                String network_address = helperFunctions.getIpAddress()
                        + "update_user_tests.php?cpd_id=" + ecpd_id
                        + "&user_id=" + preferenceManager.getPsuId()
                        + "&timestamp=" + System.currentTimeMillis()
                        + "&score=" + percentage_score;

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        response -> {
                            //dismiss progress dialog
                            helperFunctions.stopProgressBar();
                            android.support.v7.app.AlertDialog.Builder alert = new AlertDialog.Builder(EcpdTakeExamActivity.this);

                            String message;

                            if (preferenceManager.getPassmark() > percentage_score){
                                // failed
                                message = "You got a score of " + percentage_score + "%. Better luck next time";
                            } else {
                                // passed
                                message = "Congratulations, you got a score of " + percentage_score + "% and passed";
                            }
                            alert.setMessage(message).setPositiveButton("Okay", (dialogInterface, i) -> helperFunctions.getDefaultDashboard(preferenceManager.getMemberCategory())).show();
                        }, error -> {
                            //dismiss progress dialog
                            helperFunctions.stopProgressBar();
                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                        });

                //add to request queue in singleton class
                VolleySingleton.getInstance(this).addToRequestQueue(request);
            } else {
                if ((counter + 1) == (questions_number - 1)){
                    control_button.setText("Complete");
                }
                nextQuestion();
            }
        });

        radio_group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.option1 && answer_position == 1){
                current_score++;
            } else if (checkedId == R.id.option2 && answer_position == 2){
                current_score++;
            } else if (checkedId == R.id.option3 && answer_position == 3){
                current_score++;
            } else if (checkedId == R.id.option4 && answer_position == 4){
                current_score++;
            }
        });

        helperFunctions = new HelperFunctions(this);

        questions = new ArrayList<>();
        question_ids = new ArrayList<>();

        helperFunctions.genericProgressBar("Fetching question...");

        String url = helperFunctions.getIpAddress() + "get_cpd_questions.php?id=" + ecpd_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    helperFunctions.stopProgressBar();

                    questions_number = response.length();

                    for(int i = 0; i < response.length(); i++){

                        try {
                            JSONObject obj = response.getJSONObject(i);
                            questions.add(obj.getString("text"));
                            question_ids.add(obj.getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    nextQuestion();
                }, error -> {
            // error in getting json, so recursive call till successful
            helperFunctions.stopProgressBar();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void nextQuestion(){
        helperFunctions.genericProgressBar("Fetching next question...");

        radio_group.clearCheck();

        String network_address = helperFunctions.getIpAddress() + "get_cpd_question.php?id=" + question_ids.get(counter);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
                    helperFunctions.stopProgressBar();

                    counter++;

                    try {

                        question.setText(response.getString("question"));

                        int randomNumber = new Random().nextInt(4);

                        if (randomNumber == 0){
                            answer_position = 1;
                            option1.setText(response.getString("correct"));
                            option2.setText(response.getString("incorrect3"));
                            option3.setText(response.getString("incorrect2"));
                            option4.setText(response.getString("incorrect1"));
                        } else if (randomNumber == 1){
                            answer_position = 2;
                            option2.setText(response.getString("correct"));
                            option1.setText(response.getString("incorrect1"));
                            option3.setText(response.getString("incorrect3"));
                            option4.setText(response.getString("incorrect2"));
                        } else if (randomNumber == 2){
                            answer_position = 3;
                            option3.setText(response.getString("correct"));
                            option1.setText(response.getString("incorrect2"));
                            option2.setText(response.getString("incorrect1"));
                            option4.setText(response.getString("incorrect3"));
                        } else if (randomNumber == 3){
                            answer_position = 4;
                            option4.setText(response.getString("correct"));
                            option1.setText(response.getString("incorrect3"));
                            option2.setText(response.getString("incorrect2"));
                            option3.setText(response.getString("incorrect1"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            helperFunctions.stopProgressBar();
            helperFunctions.genericDialog("Failed to get question");
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
