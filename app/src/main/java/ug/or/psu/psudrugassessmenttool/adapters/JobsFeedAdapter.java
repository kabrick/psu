package ug.or.psu.psudrugassessmenttool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.JobsFeed;

public class JobsFeedAdapter extends RecyclerView.Adapter<JobsFeedAdapter.MyViewHolder> {
    private List<JobsFeed> jobsList;
    private JobsFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, author, timestamp;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.jobs_feed_title_list);
            text = view.findViewById(R.id.jobs_feed_text_list);
            author = view.findViewById(R.id.jobs_feed_author_list);
            timestamp = view.findViewById(R.id.jobs_feed_timestamp_list);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onJobsItemSelected(jobsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public JobsFeedAdapter(List<JobsFeed> jobsList, JobsFeedAdapterListener listener) {
        this.listener = listener;
        this.jobsList = jobsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobs_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final JobsFeed jobs = jobsList.get(position);
        holder.text.setText(jobs.getText());
        holder.title.setText(jobs.getTitle());
        holder.author.setText(jobs.getAuthor());

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(jobs.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public interface JobsFeedAdapterListener {
        void onJobsItemSelected(JobsFeed job);
    }
}
