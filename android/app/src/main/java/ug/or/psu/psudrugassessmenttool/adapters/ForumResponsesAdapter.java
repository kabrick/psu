package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.ForumTopicViewActivity;
import ug.or.psu.psudrugassessmenttool.models.ForumResponses;

public class ForumResponsesAdapter extends RecyclerView.Adapter<ForumResponsesAdapter.MyViewHolder> {
    private List<ForumResponses> newsCommentsList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView forum_response_text, forum_response_timestamp;
        LinearLayout linear_layout;

        MyViewHolder(View view) {
            super(view);
            linear_layout = view.findViewById(R.id.linear_layout);
            forum_response_text = view.findViewById(R.id.forum_response_text);
            forum_response_timestamp = view.findViewById(R.id.forum_response_timestamp);
        }
    }

    public ForumResponsesAdapter(List<ForumResponses> newsCommentsList, Context context) {
        this.newsCommentsList = newsCommentsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ForumResponsesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_forum_response, parent, false);

        return new ForumResponsesAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForumResponsesAdapter.MyViewHolder holder, final int position) {
        final ForumResponses comments = newsCommentsList.get(position);

        //set comment text
        holder.forum_response_text.setText(comments.getComment());

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(comments.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.forum_response_timestamp.setText(comments.getAuthor() + " " + timeAgo);

        String moderator = ((ForumTopicViewActivity)context).moderator;

        if (moderator.equals(comments.getId())){
            holder.linear_layout.setBackgroundColor(context.getResources().getColor(R.color.text_green));
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.linear_layout.getLayoutParams();
            params.gravity = GravityCompat.END;
            holder.linear_layout.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return newsCommentsList.size();
    }
}
