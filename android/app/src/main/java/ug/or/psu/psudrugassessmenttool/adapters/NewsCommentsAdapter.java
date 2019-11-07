package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        //ImageView news_comments_profile_picture;
        LinearLayout linear_layout;

        MyViewHolder(View view) {
            super(view);
            linear_layout = view.findViewById(R.id.linear_layout);
            news_comments_comment = view.findViewById(R.id.news_comments_comment);
            news_comments_author = view.findViewById(R.id.news_comments_author);
            news_comments_timestamp = view.findViewById(R.id.news_comments_timestamp);
            //news_comments_profile_picture = view.findViewById(R.id.news_comments_profile_picture);
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

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(comments.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.news_comments_timestamp.setText(timeAgo);

        /*// for forums only
        if (comments.getPhoto().equals("0")){
            String moderator = ((ForumTopicViewActivity)context).moderator;

            if (moderator.equals(comments.getId())){
                holder.linear_layout.setGravity(Gravity.END);
            }
        }*/

        /*if (comments.getPhoto().equals("0")){
            // remove profile picture
            holder.news_comments_profile_picture.setVisibility(View.GONE);
            holder.news_comments_relative_layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            String image_url = helperFunctions.getIpAddress() + comments.getPhoto();

            Glide.with(context)
                    .load(image_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.news_comments_profile_picture);
        }*/
    }

    @Override
    public int getItemCount() {
        return newsCommentsList.size();
    }
}
