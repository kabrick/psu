package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsCommentsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.NewsComments;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class ForumTopicViewActivity extends AppCompatActivity {

    String id, document_url, picture_url;
    TextView forum_topic_view_author, forum_topic_view_timestamp, forum_topic_view_title,
            forum_topic_view_document_link, no_responses_textview;
    ImageView forum_topic_view_picture;
    EditText topic_response;

    HelperFunctions helperFunctions;
    PreferenceManager preferenceManager;

    private List<NewsComments> responsesList;
    private NewsCommentsAdapter mAdapter;

    private Animator currentAnimator;
    private int shortAnimationDuration;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_topic_view);

        // add icon to the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        topic_response = findViewById(R.id.topic_response);
        forum_topic_view_author = findViewById(R.id.forum_topic_view_author);
        forum_topic_view_timestamp = findViewById(R.id.forum_topic_view_timestamp);
        forum_topic_view_title = findViewById(R.id.forum_topic_view_title);
        forum_topic_view_document_link = findViewById(R.id.forum_topic_view_document_link);
        forum_topic_view_picture = findViewById(R.id.forum_topic_view_picture);
        no_responses_textview = findViewById(R.id.no_responses_textview);

        forum_topic_view_picture.setOnClickListener(v -> zoomImageFromThumb(forum_topic_view_picture));

        forum_topic_view_document_link.setOnClickListener(v -> {
            String url = helperFunctions.getIpAddress() + document_url;

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            id = extras.getString("id", "1");
        }

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        recyclerView = findViewById(R.id.responses_recycler);
        responsesList = new ArrayList<>();
        mAdapter = new NewsCommentsAdapter(responsesList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchTopic();
        fetchResponses();
    }

    public void fetchTopic() {
        helperFunctions.genericProgressBar("Fetching forum topic...");
        String network_address = helperFunctions.getIpAddress()
                + "get_forum_post.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {
                    try {
                        forum_topic_view_author.setText(response.getString("author"));
                        forum_topic_view_title.setText(response.getString("title"));

                        if (!response.getString("document_url").equals("0")){
                            forum_topic_view_document_link.setText(response.getString("document_name"));
                            forum_topic_view_document_link.setVisibility(View.VISIBLE);
                            document_url = response.getString("document_url");
                        }

                        if (!response.getString("picture_url").equals("0")){

                            picture_url = helperFunctions.getIpAddress() + response.getString("picture_url");

                            Glide.with(this)
                                    .load(picture_url)
                                    .into(forum_topic_view_picture);

                            forum_topic_view_picture.setVisibility(View.VISIBLE);
                        }

                        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                                Long.parseLong(response.getString("timestamp")),
                                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                        forum_topic_view_timestamp.setText(timeAgo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    helperFunctions.stopProgressBar();
                }, error -> {
                    helperFunctions.stopProgressBar();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void zoomImageFromThumb(final View thumbView) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = findViewById(R.id.expanded_image);

        // use glide to load the image
        Glide.with(this)
                .load(picture_url)
                .into(expandedImageView);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.forum_topic_view_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(view -> {
            if (currentAnimator != null) {
                currentAnimator.cancel();
            }

            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator
                    .ofFloat(expandedImageView, View.X, startBounds.left))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.Y,startBounds.top))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_X, startScaleFinal))
                    .with(ObjectAnimator
                            .ofFloat(expandedImageView,
                                    View.SCALE_Y, startScaleFinal));
            set1.setDuration(shortAnimationDuration);
            set1.setInterpolator(new DecelerateInterpolator());
            set1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    currentAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    currentAnimator = null;
                }
            });
            set1.start();
            currentAnimator = set1;
        });
    }

    public void fetchResponses(){
        String url = helperFunctions.getIpAddress() + "get_forum_responses.php?id=" + id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

            if (response.length() > 0){
                List<NewsComments> items = new Gson().fromJson(response.toString(), new TypeToken<List<NewsComments>>() {
                }.getType());

                responsesList.clear();
                responsesList.addAll(items);

                // refreshing recycler view
                mAdapter.notifyDataSetChanged();
                no_responses_textview.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                no_responses_textview.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
                }, error -> {
            no_responses_textview.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void sendResponse(View view){

        String response_text = topic_response.getText().toString();

        if(TextUtils.isEmpty(response_text)) {
            Toast.makeText(this, "Please fill in a response before submitting", Toast.LENGTH_LONG).show();
            return;
        }

        // show progress dialog
        helperFunctions.genericProgressBar("Posting your response...");

        String network_address = helperFunctions.getIpAddress()
                + "post_forum_response.php?id=" + id
                + "&comment=" + response_text
                + "&psu_id=" + preferenceManager.getPsuId()
                + "&timestamp=" + System.currentTimeMillis();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("1")){
                        //saved article successfully
                        topic_response.setText("");
                        topic_response.clearFocus();
                        Toast.makeText(this, "Posting response successful!", Toast.LENGTH_LONG).show();
                        fetchResponses();
                    } else {
                        Toast.makeText(this, "Posting response failed!", Toast.LENGTH_LONG).show();
                    }
                }, error -> {
            helperFunctions.stopProgressBar();
            Toast.makeText(this, "Posting response failed!", Toast.LENGTH_LONG).show();
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
