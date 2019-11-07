package ug.or.psu.psudrugassessmenttool.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.Feedback;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {
    private List<Feedback> feedbackList;
    private FeedbackAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.feedback_feed_title_list);
            text = view.findViewById(R.id.feedback_feed_text_list);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFeedbackItemSelected(feedbackList.get(getAdapterPosition()));
                }
            });
        }
    }

    public FeedbackAdapter(List<Feedback> feedbackList, FeedbackAdapterListener listener) {
        this.listener = listener;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Feedback jobs = feedbackList.get(position);
        holder.text.setText(jobs.getText());
        holder.title.setText(jobs.getTitle());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public interface FeedbackAdapterListener {
        void onFeedbackItemSelected(Feedback feedback);
    }
}
