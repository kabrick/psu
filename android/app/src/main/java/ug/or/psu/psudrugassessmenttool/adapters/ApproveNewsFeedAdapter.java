package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.ApproveNewsFeed;

public class ApproveNewsFeedAdapter extends RecyclerView.Adapter<ApproveNewsFeedAdapter.MyViewHolder>  {
    private List<ApproveNewsFeed> newsList;
    private ApproveNewsFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, author, timestamp;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.approve_news_feed_title_list);
            text = view.findViewById(R.id.approve_news_feed_text_list);
            author = view.findViewById(R.id.approve_news_feed_author_list);
            timestamp = view.findViewById(R.id.approve_news_feed_timestamp_list);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsItemSelected(newsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public ApproveNewsFeedAdapter(Context context, List<ApproveNewsFeed> newsList, ApproveNewsFeedAdapterListener listener) {
        this.listener = listener;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approve_news_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final ApproveNewsFeed news = newsList.get(position);
        holder.text.setText(news.getText());
        holder.title.setText(news.getTitle());
        holder.author.setText(news.getAuthor());

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(news.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface ApproveNewsFeedAdapterListener {
        void onNewsItemSelected(ApproveNewsFeed news);
    }
}
