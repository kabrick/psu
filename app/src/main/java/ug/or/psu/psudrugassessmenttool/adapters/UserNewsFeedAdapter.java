package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.UserNewsFeed;

public class UserNewsFeedAdapter extends RecyclerView.Adapter<UserNewsFeedAdapter.MyViewHolder>  {
    private List<UserNewsFeed> newsList;
    private UserNewsFeedAdapterListener listener;
    private HelperFunctions helperFunctions;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, author, timestamp;
        ImageView image, user_news_attachment_image;
        View read_status;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.user_news_feed_title_list);
            text = view.findViewById(R.id.user_news_feed_text_list);
            author = view.findViewById(R.id.user_news_feed_author_list);
            timestamp = view.findViewById(R.id.user_news_feed_timestamp_list);
            read_status = view.findViewById(R.id.user_read_status);
            image = view.findViewById(R.id.user_news_feed_image);
            user_news_attachment_image = view.findViewById(R.id.user_news_attachment_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsItemSelected(newsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public UserNewsFeedAdapter(Context context, List<UserNewsFeed> newsList, UserNewsFeedAdapterListener listener) {
        this.listener = listener;
        this.newsList = newsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_news_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final UserNewsFeed news = newsList.get(position);
        holder.text.setText(news.getText());
        holder.title.setText(news.getTitle());
        holder.author.setText(news.getAuthor());

        //verify whether the person has read the article before
        try {
            if (helperFunctions.isNewsRead(Integer.parseInt(news.getId()))){
                holder.read_status.setBackgroundColor(Color.parseColor("#f4bb41"));
            } else {
                holder.read_status.setBackgroundColor(Color.parseColor("#adf442"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(news.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);

        if(news.getImage().equals("0")){
            //
        } else {
            String image_url = helperFunctions.getIpAddress() + news.getImage();

            Glide.with(context)
                    .load(image_url)
                    .into(holder.image);

            holder.image.setVisibility(View.VISIBLE);
        }

        switch (news.getAttachment()) {
            case "pdf":
                holder.user_news_attachment_image.setImageResource(R.drawable.pdf_logo);
                holder.user_news_attachment_image.setVisibility(View.VISIBLE);
                break;
            case "word":
                holder.user_news_attachment_image.setImageResource(R.drawable.word_logo);
                holder.user_news_attachment_image.setVisibility(View.VISIBLE);
                break;
            case "excel":
                holder.user_news_attachment_image.setImageResource(R.drawable.excel_logo);
                holder.user_news_attachment_image.setVisibility(View.VISIBLE);
                break;
            case "powerpoint":
                holder.user_news_attachment_image.setImageResource(R.drawable.powerpoint_logo);
                holder.user_news_attachment_image.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface UserNewsFeedAdapterListener {
        void onNewsItemSelected(UserNewsFeed news);
    }
}
