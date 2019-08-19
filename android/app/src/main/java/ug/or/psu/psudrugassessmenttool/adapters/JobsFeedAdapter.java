package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.JobsFeed;

public class JobsFeedAdapter extends RecyclerView.Adapter<JobsFeedAdapter.MyViewHolder> {
    private List<JobsFeed> jobsList;
    private JobsFeedAdapterListener listener;
    private HelperFunctions helperFunctions;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, timestamp;
        ImageView profile_picture;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.jobs_feed_title_list);
            author = view.findViewById(R.id.jobs_feed_author_list);
            timestamp = view.findViewById(R.id.jobs_feed_timestamp_list);
            profile_picture = view.findViewById(R.id.jobs_feed_profile_picture);

            view.setOnClickListener(view1 -> listener.onJobsItemSelected(jobsList.get(getAdapterPosition())));
        }
    }

    public JobsFeedAdapter(List<JobsFeed> jobsList, JobsFeedAdapterListener listener, Context context) {
        this.listener = listener;
        this.jobsList = jobsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobs_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final JobsFeed jobs = jobsList.get(position);
        holder.title.setText(jobs.getTitle());
        holder.author.setText("Source: " + jobs.getText());

        //covert timestamp to readable format
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(jobs.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);*/

        @SuppressLint("SimpleDateFormat")
        String timeAgo = new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date (Long.parseLong(jobs.getTimestamp())));

        holder.timestamp.setText(timeAgo);

        String image_url = helperFunctions.getIpAddress() + jobs.getPhoto();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profile_picture);
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public interface JobsFeedAdapterListener {
        void onJobsItemSelected(JobsFeed job);
    }
}
