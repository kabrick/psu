package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.SingleTextCardAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.SingleTextCard;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class EcpdAddQuestionsActivity extends AppCompatActivity implements SingleTextCardAdapter.SingleTextCardAdapterListener {

    String ecpd_id;
    private List<SingleTextCard> list;
    private SingleTextCardAdapter mAdapter;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    ProgressBar progressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecpd_add_questions);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            ecpd_id = extras.getString("id", "1");
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_add_questions);
        list = new ArrayList<>();
        mAdapter = new SingleTextCardAdapter(list, this);

        progressBar = findViewById(R.id.progressBarAddQuestions);

        fab = findViewById(R.id.add_questions_fab);

        fab.setOnClickListener(view -> addQuestions());

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchQuestions();
    }

    private void fetchQuestions() {
        String url = helperFunctions.getIpAddress() + "get_cpd_questions.php?id=" + ecpd_id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    //news has been got so stop progress bar
                    progressBar.setVisibility(View.GONE);

                    List<SingleTextCard> items = new Gson().fromJson(response.toString(), new TypeToken<List<SingleTextCard>>() {
                    }.getType());

                    list.clear();
                    list.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchQuestions();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void addQuestions(){
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_add_questions_dialog, null);

        final EditText cpd_question = view.findViewById(R.id.cpd_question);
        final EditText cpd_correct = view.findViewById(R.id.cpd_correct_answer);
        final EditText cpd_incorrect1 = view.findViewById(R.id.cpd_incorrect_answer_one);
        final EditText cpd_incorrect2 = view.findViewById(R.id.cpd_incorrect_answer_two);
        final EditText cpd_incorrect3 = view.findViewById(R.id.cpd_incorrect_answer_three);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle("Add Question");

        builder.setCancelable(false)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {

            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {

                String cpd_question_string = cpd_question.getText().toString();
                String cpd_correct_answer_string = cpd_correct.getText().toString();
                String cpd_incorrect1_string = cpd_incorrect1.getText().toString();
                String cpd_incorrect2_string = cpd_incorrect2.getText().toString();
                String cpd_incorrect3_string = cpd_incorrect3.getText().toString();

                if(TextUtils.isEmpty(cpd_question_string)) {
                    helperFunctions.genericDialog("Please fill in the question");
                    return;
                }

                if(TextUtils.isEmpty(cpd_correct_answer_string)) {
                    helperFunctions.genericDialog("Please fill in the correct answer");
                    return;
                }

                if(TextUtils.isEmpty(cpd_incorrect1_string)) {
                    helperFunctions.genericDialog("Please fill in the first incorrect answer");
                    return;
                }

                if(TextUtils.isEmpty(cpd_incorrect2_string)) {
                    helperFunctions.genericDialog("Please fill in the second incorrect answer");
                    return;
                }

                if(TextUtils.isEmpty(cpd_incorrect3_string)) {
                    helperFunctions.genericDialog("Please fill in the third incorrect answer");
                    return;
                }

                //show progress dialog
                helperFunctions.genericProgressBar("Posting question...");

                String url = helperFunctions.getIpAddress() + "add_cpd_question.php";

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response -> {
                    helperFunctions.stopProgressBar();
                    dialog.dismiss();
                    progressBar.setVisibility(View.VISIBLE);
                    fetchQuestions();
                }, error -> {
                    helperFunctions.stopProgressBar();
                    helperFunctions.genericDialog("Something went wrong. Please try again later");
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> data = new HashMap<>();
                        data.put("question", cpd_question_string);
                        data.put("answer", cpd_correct_answer_string);
                        data.put("incorrect_one", cpd_incorrect1_string);
                        data.put("incorrect_two", cpd_incorrect2_string);
                        data.put("ecpd_id", ecpd_id);
                        data.put("incorrect_three", cpd_incorrect3_string);
                        return data;
                    }
                };

                requestQueue.add(MyStringRequest);
            });
        });

        dialog.show();
    }

    @Override
    public void onItemSelected(SingleTextCard item) {

        String[] mStringArray = {"View || Edit Question", "Delete Question"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EcpdAddQuestionsActivity.this);
        builder.setTitle("Choose your action");

        builder.setItems(mStringArray, (dialogInterface, i) -> {
            if (i == 0){
                editQuestion(item.getId());
            } else if (i == 1){
                deleteQuestion(item.getId());
            }
        });

        // create and show the alert dialog
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editQuestion(String id){
        helperFunctions.genericProgressBar("Fetching question details...");

        String network_address = helperFunctions.getIpAddress() + "get_cpd_question.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
                    helperFunctions.stopProgressBar();

                    try {
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams")
                        View view = inflater.inflate(R.layout.layout_add_questions_dialog, null);

                        final EditText cpd_question = view.findViewById(R.id.cpd_question);
                        final EditText cpd_correct = view.findViewById(R.id.cpd_correct_answer);
                        final EditText cpd_incorrect1 = view.findViewById(R.id.cpd_incorrect_answer_one);
                        final EditText cpd_incorrect2 = view.findViewById(R.id.cpd_incorrect_answer_two);
                        final EditText cpd_incorrect3 = view.findViewById(R.id.cpd_incorrect_answer_three);

                        cpd_question.setText(response.getString("question"));
                        cpd_correct.setText(response.getString("correct"));
                        cpd_incorrect1.setText(response.getString("incorrect1"));
                        cpd_incorrect2.setText(response.getString("incorrect2"));
                        cpd_incorrect3.setText(response.getString("incorrect3"));

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setView(view);
                        builder.setTitle("Edit Question");

                        builder.setCancelable(false)
                                .setPositiveButton("Save", null)
                                .setNegativeButton("Cancel", (dialog, ids) -> dialog.cancel());

                        final AlertDialog dialog = builder.create();

                        dialog.setOnShowListener(dialogInterface -> {

                            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            button.setOnClickListener(view1 -> {

                                String cpd_question_string = cpd_question.getText().toString();
                                String cpd_correct_answer_string = cpd_correct.getText().toString();
                                String cpd_incorrect1_string = cpd_incorrect1.getText().toString();
                                String cpd_incorrect2_string = cpd_incorrect2.getText().toString();
                                String cpd_incorrect3_string = cpd_incorrect3.getText().toString();

                                if(TextUtils.isEmpty(cpd_question_string)) {
                                    helperFunctions.genericDialog("Please fill in the question");
                                    return;
                                }

                                if(TextUtils.isEmpty(cpd_correct_answer_string)) {
                                    helperFunctions.genericDialog("Please fill in the correct answer");
                                    return;
                                }

                                if(TextUtils.isEmpty(cpd_incorrect1_string)) {
                                    helperFunctions.genericDialog("Please fill in the first incorrect answer");
                                    return;
                                }

                                if(TextUtils.isEmpty(cpd_incorrect2_string)) {
                                    helperFunctions.genericDialog("Please fill in the second incorrect answer");
                                    return;
                                }

                                if(TextUtils.isEmpty(cpd_incorrect3_string)) {
                                    helperFunctions.genericDialog("Please fill in the third incorrect answer");
                                    return;
                                }

                                //show progress dialog
                                helperFunctions.genericProgressBar("Editing question...");

                                String url = helperFunctions.getIpAddress() + "edit_cpd_question.php";

                                RequestQueue requestQueue = Volley.newRequestQueue(this);

                                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, response1 -> {
                                    helperFunctions.stopProgressBar();
                                    dialog.dismiss();
                                    progressBar.setVisibility(View.VISIBLE);
                                    fetchQuestions();
                                }, error -> {
                                    helperFunctions.stopProgressBar();
                                    helperFunctions.genericDialog("Something went wrong. Please try again later");
                                }) {
                                    protected Map<String, String> getParams() {
                                        Map<String, String> data = new HashMap<>();
                                        data.put("question", cpd_question_string);
                                        data.put("answer", cpd_correct_answer_string);
                                        data.put("incorrect_one", cpd_incorrect1_string);
                                        data.put("incorrect_two", cpd_incorrect2_string);
                                        data.put("incorrect_three", cpd_incorrect3_string);
                                        data.put("question_id", id);
                                        return data;
                                    }
                                };

                                requestQueue.add(MyStringRequest);
                            });
                        });

                        dialog.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            helperFunctions.stopProgressBar();
            helperFunctions.genericDialog("Failed to get question");
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void deleteQuestion(String id){
        helperFunctions.genericProgressBar("Deleting question...");
        String network_address = helperFunctions.getIpAddress() + "delete_cpd_question.php?id=" + id;

        // Request a string response from the provided URL
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("1")){
                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(EcpdAddQuestionsActivity.this);

                        alert.setMessage("Question deleted").setPositiveButton("Okay", (dialogInterface, i) -> {
                            progressBar.setVisibility(View.VISIBLE);
                            fetchQuestions();
                        }).show();
                    }

                }, error -> {
            helperFunctions.stopProgressBar();
            helperFunctions.genericDialog("Something went wrong. Please try again later");
                });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
