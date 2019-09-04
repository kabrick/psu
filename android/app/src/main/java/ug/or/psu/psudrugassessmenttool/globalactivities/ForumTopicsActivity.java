package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.ForumTopicAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.ForumTopic;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ForumTopicsActivity extends AppCompatActivity {

    TextView review_forum_no_results_textview;
    private List<ForumTopic> topicsList;
    private ForumTopicAdapter mAdapter;
    RecyclerView recyclerView;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_topics);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        review_forum_no_results_textview = findViewById(R.id.view_forum_no_results_textview);
        recyclerView = findViewById(R.id.view_forum_topics_recycler);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        fetchTopics();
    }

    public void fetchTopics() {
        topicsList = new ArrayList<>();
        mAdapter = new ForumTopicAdapter(this, topicsList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        helperFunctions.genericProgressBar("Fetching forum topics...");
        String url = helperFunctions.getIpAddress() + "get_forum_posts.php";

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    helperFunctions.stopProgressBar();

                    if (response.length() > 0) {
                        List<ForumTopic> items = new Gson().fromJson(response.toString(), new TypeToken<List<ForumTopic>>() {
                        }.getType());

                        topicsList.clear();
                        topicsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        review_forum_no_results_textview.setVisibility(View.VISIBLE);
                    }
                }, error -> {
            //
            helperFunctions.stopProgressBar();
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}