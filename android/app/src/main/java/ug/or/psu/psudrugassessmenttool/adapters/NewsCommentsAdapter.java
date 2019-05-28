package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
import ug.or.psu.psudrugassessmenttool.models.NewsComments;

public class NewsCommentsAdapter extends RecyclerView.Adapter<NewsCommentsAdapter.MyViewHolder>  {
    private List<NewsComments> newsCommentsList;
    private HelperFunctions helperFunctions;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_comments_comment, news_comments_author, news_comments_timestamp;
        ImageView news_comments_profile_picture;

        MyViewHolder(View view) {
            super(view);
            news_comments_comment = view.findViewById(R.id.news_comments_comment);
            news_comments_author = view.findViewById(R.id.news_comments_author);
            news_comments_timestamp = view.findViewById(R.id.news_comments_timestamp);
            news_comments_profile_picture = view.findViewById(R.id.news_comments_profile_picture);
        }
    }

    public NewsCommentsAdapter(List<NewsComments> newsCommentsList, Context context) {
        this.newsCommentsList = newsCommentsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
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
        holder.news_comments_comment.setText(comments.getComment());
        holder.news_comments_author.setText(comments.getAuthor());

        @SuppressLint("SimpleDateFormat")
        String timeAgo = new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date (Long.parseLong(comments.getTimestamp())));

        holder.news_comments_timestamp.setText(timeAgo);

        String image_url = helperFunctions.getIpAddress() + comments.getPhoto();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.news_comments_profile_picture);
    }

    @Override
    public int getItemCount() {
        return newsCommentsList.size();
    }
}
