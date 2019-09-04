package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.ForumTopic;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;
import ug.or.psu.psudrugassessmenttool.users.dashboards.admin.ApproveForumTopicActivity;

public class ForumTopicApprovalAdapter extends RecyclerView.Adapter<ForumTopicApprovalAdapter.MyViewHolder> {
    private List<ForumTopic> topicsList;
    private Context context;
    private HelperFunctions helperFunctions;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView forum_topic_title, forum_topic_document_link, forum_topic_posted_by, forum_topic_timestamp,
                forum_topic_approve, forum_topic_reject;
        ImageView forum_topic_profile_picture, forum_topic_picture;

        MyViewHolder(View view) {
            super(view);

            forum_topic_title = view.findViewById(R.id.forum_topic_title);
            forum_topic_document_link = view.findViewById(R.id.forum_topic_document_link);
            forum_topic_posted_by = view.findViewById(R.id.forum_topic_posted_by);
            forum_topic_timestamp = view.findViewById(R.id.forum_topic_timestamp);
            forum_topic_profile_picture = view.findViewById(R.id.forum_topic_profile_picture);
            forum_topic_picture = view.findViewById(R.id.forum_topic_picture);
            forum_topic_approve = view.findViewById(R.id.forum_topic_approve);
            forum_topic_reject = view.findViewById(R.id.forum_topic_reject);

            forum_topic_document_link.setOnClickListener(v -> {
                ForumTopic topic = topicsList.get(getAdapterPosition());

                String url = helperFunctions.getIpAddress() + topic.getDocument_url();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            });

            forum_topic_approve.setOnClickListener(v -> {
                ForumTopic topic = topicsList.get(getAdapterPosition());

                helperFunctions.genericProgressBar("Approving forum topic...");

                String network_address = helperFunctions.getIpAddress() + "approve_forum_topic.php?id=" + topic.getId();

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        response -> {
                            helperFunctions.stopProgressBar();
                            Toast.makeText(context, "Forum topic approved", Toast.LENGTH_LONG).show();
                            ((ApproveForumTopicActivity)context).fetchTopics();
                        }, error -> {
                    helperFunctions.stopProgressBar();
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                });

                //add to request queue in singleton class
                VolleySingleton.getInstance(context).addToRequestQueue(request);
            });

            forum_topic_reject.setOnClickListener(v -> {
                ForumTopic topic = topicsList.get(getAdapterPosition());

                helperFunctions.genericProgressBar("Rejecting forum topic...");

                String network_address = helperFunctions.getIpAddress() + "reject_forum_topic.php?id=" + topic.getId();

                // Request a string response from the provided URL.
                StringRequest request = new StringRequest(network_address,
                        response -> {
                            helperFunctions.stopProgressBar();
                            Toast.makeText(context, "Forum topic rejected", Toast.LENGTH_LONG).show();
                            ((ApproveForumTopicActivity)context).fetchTopics();
                        }, error -> {
                    helperFunctions.stopProgressBar();
                    Toast.makeText(context, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                });

                //add to request queue in singleton class
                VolleySingleton.getInstance(context).addToRequestQueue(request);
            });
        }
    }

    public ForumTopicApprovalAdapter(Context context, List<ForumTopic> topicsList) {
        this.topicsList = topicsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public ForumTopicApprovalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_forum_topic_approval, parent, false);

        return new ForumTopicApprovalAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForumTopicApprovalAdapter.MyViewHolder holder, final int position) {
        final ForumTopic topic = topicsList.get(position);
        holder.forum_topic_title.setText(topic.getTitle());
        holder.forum_topic_posted_by.setText("Posted by: " + topic.getName());
        holder.forum_topic_timestamp.setText(topic.getName());

        if (!topic.getDocument_url().equals("0")){
            holder.forum_topic_document_link.setText(topic.getDocument_name());
            holder.forum_topic_document_link.setVisibility(View.VISIBLE);
        }

        if (!topic.getPicture_url().equals("0")){
            Glide.with(context)
                    .load(helperFunctions.getIpAddress() + topic.getPicture_url())
                    .into(holder.forum_topic_picture);

            holder.forum_topic_picture.setVisibility(View.VISIBLE);
        }

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
