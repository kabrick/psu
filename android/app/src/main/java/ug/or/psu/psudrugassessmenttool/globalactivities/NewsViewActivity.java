package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.adapters.NewsCommentsAdapter;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.helpers.PreferenceManager;
import ug.or.psu.psudrugassessmenttool.models.NewsComments;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class NewsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp, source;
    ImageView news_image;
    String title_string, text_string, author_string, timestamp_string, id, image_string, source_string;
    HelperFunctions helperFunctions;
    CardView attachment_card;
    TextView attachment_name;
    FloatingActionButton create_comment_fab, share_news_fab;

    PreferenceManager preferenceManager;

    private List<NewsComments> newsCommentsList;
    private NewsCommentsAdapter mAdapter;

    View activityView;
    String attachment_url;

    private Animator currentAnimator;
    private int shortAnimationDuration;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        activityView = findViewById(R.id.news_view);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helperFunctions = new HelperFunctions(this);
        preferenceManager = new PreferenceManager(this);

        // Retrieve and cache the system's default "short" animation time.
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        create_comment_fab = findViewById(R.id.create_comment_fab);
        share_news_fab = findViewById(R.id.share_news_fab);
        attachment_card = findViewById(R.id.attachment_card);
        attachment_name = findViewById(R.id.attachment_name);

        attachment_card.setOnClickListener(view -> downloadAttachment());

        share_news_fab.setOnClickListener(view -> {
            String share_string = title_string + "\n\n" + text_string + "\n\n" + image_string;
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title_string);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, share_string);
            startActivity(Intent.createChooser(sharingIntent, "Share Article"));
        });

        create_comment_fab.setOnClickListener(view -> {
            LayoutInflater inflater1 = LayoutInflater.from(NewsViewActivity.this);
            View view1 = inflater1.inflate(R.layout.add_news_comment, null);

            final EditText editText = view1.findViewById(R.id.comment_text);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewsViewActivity.this);
            alertDialog.setTitle("Post your comment");
            alertDialog.setView(view1);
            alertDialog.setCancelable(false)
                    .setPositiveButton("Okay",
                            (dialog, id) -> {
                                // get user input and set it to result
                                postComment(editText.getText().toString());
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        });

        title = findViewById(R.id.news_feed_title_single);
        text = findViewById(R.id.news_feed_text_single);
        author = findViewById(R.id.news_feed_author_single);
        timestamp = findViewById(R.id.news_feed_timestamp_single);
        source = findViewById(R.id.news_view_source);

        news_image = findViewById(R.id.news_image);

        news_image.setOnClickListener(v -> zoomImageFromThumb(news_image));

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            title_string = extras.getString("title", "");
            text_string = extras.getString("text", "");
            author_string = extras.getString("author", "");
            timestamp_string = extras.getString("timestamp", "");
            id = extras.getString("id", "");
            source_string = extras.getString("source", "");
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(timestamp_string),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        timestamp.setText(timeAgo);
        title.setText(title_string);
        text.setText(text_string);
        author.setText(author_string);
        source.setText("Source: " + source_string);

        //add id to read news
        try {
            helperFunctions.addNewsRead(Integer.parseInt(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.comments_recycler);
        newsCommentsList = new ArrayList<>();
        mAdapter = new NewsCommentsAdapter(newsCommentsList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchComments();
        fetchNewsImage();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void fetchNewsImage(){
        helperFunctions.genericProgressBar("Fetching news...");

        String network_address = helperFunctions.getIpAddress()
                + "get_news_picture.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {

                    fetchAttachmentUrl();
                    helperFunctions.stopProgressBar();

                    try {
                        // check if image is null
                        if(response.getString("photo").equals("0")){
                            //
                        } else {
                            image_string = helperFunctions.getIpAddress() + response.getString("photo");

                            Glide.with(NewsViewActivity.this)
                                    .load(image_string)
                                    .into(news_image);

                            news_image.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    //
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void zoomImageFromThumb(final View thumbView) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = findViewById(R.id.expanded_image);

        // use glide to load the image
        Glide.with(NewsViewActivity.this)
                .load(image_string)
                .into(expandedImageView);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.news_view_container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
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

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
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

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
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
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
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
                set.start();
                currentAnimator = set;
            }
        });
    }

    public void fetchAttachmentUrl(){
        String network_address = helperFunctions.getIpAddress()
                + "get_news_attachment.php?id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, network_address, null,
                response -> {

                    try {

                        if(response.getString("attachment_url").equals("0")){
                            helperFunctions.stopProgressBar();
                        } else {
                            attachment_url = helperFunctions.getIpAddress() + response.getString("attachment_url");
                            attachment_name.setText("Download " + response.getString("attachment_name"));
                            helperFunctions.stopProgressBar();
                            attachment_card.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    //
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void downloadAttachment(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(attachment_url));
        startActivity(intent);
    }

    public void fetchComments(){
        String url = helperFunctions.getIpAddress() + "get_comments.php?id=" + id;

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {

                    List<NewsComments> items = new Gson().fromJson(response.toString(), new TypeToken<List<NewsComments>>() {
                    }.getType());

                    if(items.size() > 0){
                        findViewById(R.id.comments_title).setVisibility(View.VISIBLE);
                        findViewById(R.id.comments_line).setVisibility(View.VISIBLE);
                    }

                    newsCommentsList.clear();
                    newsCommentsList.addAll(items);

                    // refreshing recycler view
                    mAdapter.notifyDataSetChanged();
                }, error -> {
                    // error in getting json, so recursive call till successful
                    fetchComments();
                });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void postComment(String comment){
        // show progress dialog
        helperFunctions.genericProgressBar("Posting your comment...");

        // get the current timestamp
        Long timestamp_long = System.currentTimeMillis();

        String network_address = helperFunctions.getIpAddress()
                + "post_comments.php?news_id=" + id
                + "&comment=" + comment
                + "&psu_id=" + preferenceManager.getPsuId()
                + "&timestamp=" + timestamp_long.toString();

        // Request a string response from the provided URL.
        StringRequest request = new StringRequest(network_address,
                response -> {
                    //dismiss progress dialog
                    helperFunctions.stopProgressBar();

                    if(response.equals("1")){
                        //saved article successfully
                        helperFunctions.genericSnackbar("Posting comment successful!", activityView);
                        fetchComments();
                    } else {
                        helperFunctions.genericSnackbar("Posting comment failed!", activityView);
                    }
                }, error -> {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        helperFunctions.genericSnackbar("Connection Error. Please check your connection", activityView);
                    } else if (error instanceof AuthFailureError) {
                        helperFunctions.genericSnackbar("Authentication error", activityView);
                    } else if (error instanceof ServerError) {
                        helperFunctions.genericSnackbar("Server error", activityView);
                    } else if (error instanceof NetworkError) {
                        helperFunctions.genericSnackbar("Network error", activityView);
                    } else if (error instanceof ParseError) {
                        helperFunctions.genericSnackbar("Data from server not Available", activityView);
                    }
                });

        //add to request queue in singleton class
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
