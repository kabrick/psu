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
import ug.or.psu.psudrugassessmenttool.models.NewsComments;

public class NewsCommentsAdapter extends RecyclerView.Adapter<NewsCommentsAdapter.MyViewHolder>  {
    private List<NewsComments> newsCommentsList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_comments_author, news_comments_text;

        MyViewHolder(View view) {
            super(view);
            news_comments_author = view.findViewById(R.id.news_comments_author);
            news_comments_text = view.findViewById(R.id.news_comments_text);
        }
    }

    public NewsCommentsAdapter(List<NewsComments> newsCommentsList) {
        this.newsCommentsList = newsCommentsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_comments_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NewsComments comments = newsCommentsList.get(position);

        //set comment text
        holder.news_comments_text.setText(comments.getComment());

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(comments.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        String author_info = "By " + comments.getAuthor() + " " + timeAgo;

        holder.news_comments_author.setText(author_info);
    }

    @Override
    public int getItemCount() {
        return newsCommentsList.size();
    }
}
