package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import ug.or.psu.psudrugassessmenttool.R;

public class NewsViewActivity extends AppCompatActivity {

    TextView title, text, author, timestamp;
    String title_string, text_string, author_string, timestamp_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        title = findViewById(R.id.news_feed_title_single);
        text = findViewById(R.id.news_feed_text_single);
        author = findViewById(R.id.news_feed_author_single);
        timestamp = findViewById(R.id.news_feed_timestamp_single);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
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
        title.setText(title_string);
        text.setText(text_string);
        author.setText(author_string);
    }
}
