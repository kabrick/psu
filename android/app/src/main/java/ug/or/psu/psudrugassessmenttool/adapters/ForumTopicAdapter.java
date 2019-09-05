package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.ForumTopicViewActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.ForumTopic;

public class ForumTopicAdapter extends RecyclerView.Adapter<ForumTopicAdapter.MyViewHolder> {
    private List<ForumTopic> topicsList;
    private Context context;
    private HelperFunctions helperFunctions;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView forum_topic_title, forum_topic_posted_by, forum_topic_timestamp;
        ImageView forum_topic_profile_picture;

        MyViewHolder(View view) {
            super(view);

            forum_topic_title = view.findViewById(R.id.forum_topic_title);
            forum_topic_posted_by = view.findViewById(R.id.forum_topic_posted_by);
            forum_topic_timestamp = view.findViewById(R.id.forum_topic_timestamp);
            forum_topic_profile_picture = view.findViewById(R.id.forum_topic_profile_picture);

            view.setOnClickListener(v -> {
                ForumTopic topic = topicsList.get(getAdapterPosition());

                context.startActivity(new Intent(context, ForumTopicViewActivity.class).putExtra("id", topic.getId()));
            });
        }
    }

    public ForumTopicAdapter(Context context, List<ForumTopic> topicsList) {
        this.topicsList = topicsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public ForumTopicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_forum_topic, parent, false);

        return new ForumTopicAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForumTopicAdapter.MyViewHolder holder, final int position) {
        final ForumTopic topic = topicsList.get(position);
        holder.forum_topic_title.setText(topic.getTitle());
        holder.forum_topic_posted_by.setText("Posted by: " + topic.getName());
        holder.forum_topic_timestamp.setText(topic.getName());

        // time posted
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(topic.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.forum_topic_timestamp.setText(timeAgo);

        // get the profile picture
        Glide.with(context)
                .load(helperFunctions.getIpAddress() + topic.getPhoto())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.forum_topic_profile_picture);
    }

    @Override
    public int getItemCount() {
        return topicsList.size();
    }
}
