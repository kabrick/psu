package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.ApproveJobsFeed;

public class ApproveJobsFeedAdapter  extends RecyclerView.Adapter<ApproveJobsFeedAdapter.MyViewHolder> {
    private List<ApproveJobsFeed> jobsList;
    private ApproveJobsFeedAdapter.ApproveJobsFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, author, timestamp;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.approve_jobs_feed_title_list);
            text = view.findViewById(R.id.approve_jobs_feed_text_list);
            author = view.findViewById(R.id.approve_jobs_feed_author_list);
            timestamp = view.findViewById(R.id.approve_jobs_feed_timestamp_list);

            view.setOnClickListener(view1 -> listener.onJobsItemSelected(jobsList.get(getAdapterPosition())));
        }
    }

    public ApproveJobsFeedAdapter(Context context, List<ApproveJobsFeed> jobsList, ApproveJobsFeedAdapter.ApproveJobsFeedAdapterListener listener) {
        this.listener = listener;
        this.jobsList = jobsList;
    }

    @NonNull
    @Override
    public ApproveJobsFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_approve_jobs_feed_items, parent, false);

        return new ApproveJobsFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveJobsFeedAdapter.MyViewHolder holder, final int position) {
        final ApproveJobsFeed job = jobsList.get(position);
        holder.text.setText(job.getText());
        holder.title.setText(job.getTitle());
        holder.author.setText(job.getAuthor());

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(job.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }

    public interface ApproveJobsFeedAdapterListener {
        void onJobsItemSelected(ApproveJobsFeed jobs);
    }
}
