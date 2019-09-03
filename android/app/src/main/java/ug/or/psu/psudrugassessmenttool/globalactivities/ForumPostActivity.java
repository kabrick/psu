package ug.or.psu.psudrugassessmenttool.globalactivities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ug.or.psu.psudrugassessmenttool.R;

public class ForumPostActivity extends AppCompatActivity {

    ImageView picture_imageview, document_imageview, poll_imageview;
    TextView count_text, post_forum, cancel_forum;
    EditText forum_title;
    boolean correct_characters = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);

        picture_imageview = findViewById(R.id.picture_imageview);
        picture_imageview.setOnClickListener(pictureListener);

        document_imageview = findViewById(R.id.document_imageview);
        document_imageview.setOnClickListener(documentListener);

        poll_imageview = findViewById(R.id.poll_imageview);
        poll_imageview.setOnClickListener(pollListener);

        count_text = findViewById(R.id.count_text);

        post_forum = findViewById(R.id.post_forum);
        post_forum.setOnClickListener(postForumListener);

        cancel_forum = findViewById(R.id.cancel_forum);
        cancel_forum.setOnClickListener(cancelForumListener);

        forum_title = findViewById(R.id.forum_title);
        forum_title.addTextChangedListener(mTextEditorWatcher);
    }

    private final View.OnClickListener pictureListener = v -> {
        //
    };

    private final View.OnClickListener documentListener = v -> {
        //
    };

    private final View.OnClickListener pollListener = v -> {
        //
    };

    private final View.OnClickListener postForumListener = v -> {
        //
    };

    private final View.OnClickListener cancelForumListener = v -> {
        //
    };

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @SuppressLint("SetTextI18n")
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            count_text.setText(s.length() + " / 380");
            if (s.length() > 380 || s.length() < 1){
                correct_characters = false;
                Toast.makeText(ForumPostActivity.this, "You have gone over the allocated number of characters", Toast.LENGTH_SHORT).show();
                count_text.setTextColor(getResources().getColor(R.color.errorText));
            } else {
                correct_characters = true;
            }
        }

        public void afterTextChanged(Editable s) {}
    };
}
