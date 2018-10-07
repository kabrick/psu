package ug.or.psu.psudrugassessmenttool;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class NewsViewActivity extends AppCompatActivity {

    CollapsingToolbarLayout mCollapsingToolbarLayout;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    CoordinatorLayout mRootLayout;
    ImageView image;
    TextView title, text, author, timestamp;
    String image_string, title_string, text_string, author_string, timestamp_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        image = (ImageView)findViewById(R.id.news_feed_image_single);
        //title = (TextView)findViewById(R.id.news_feed_text_single);
        text = (TextView)findViewById(R.id.news_feed_text_single);
        author = (TextView)findViewById(R.id.news_feed_author_single);
        timestamp = (TextView)findViewById(R.id.news_feed_timestamp_single);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            image_string = extras.getString("image", "");
            title_string = extras.getString("title", "");
            text_string = extras.getString("text", "");
            author_string = extras.getString("author", "");
            timestamp_string = extras.getString("timestamp", "");
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(timestamp_string),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        Glide.with(this)
                .load(image_string)
                .apply(RequestOptions.fitCenterTransform())
                .into(image);

        text.setText(text_string);
        author.setText(author_string);
    }
}
