package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
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
import ug.or.psu.psudrugassessmenttool.models.NewsFeed;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder> {

    private List<NewsFeed> newsList;
    private NewsFeedAdapterListener listener;
    private HelperFunctions helperFunctions;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, timestamp;
        ImageView profile_picture;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.news_feed_title_list);
            author = view.findViewById(R.id.news_feed_author_list);
            timestamp = view.findViewById(R.id.news_feed_timestamp_list);
            profile_picture = view.findViewById(R.id.news_feed_profile_picture);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNewsItemSelected(newsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public NewsFeedAdapter(Context context, List<NewsFeed> newsList, NewsFeedAdapterListener listener) {
        this.listener = listener;
        this.newsList = newsList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NewsFeed news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.author.setText("Source: " + news.getSource());

        //covert timestamp to readable format
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(news.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);*/

        @SuppressLint("SimpleDateFormat")
        String timeAgo = new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date (Long.parseLong(news.getTimeStamp())));

        holder.timestamp.setText(timeAgo);

        String image_url = helperFunctions.getIpAddress() + news.getImage();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profile_picture);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface NewsFeedAdapterListener {
        void onNewsItemSelected(NewsFeed news);
    }
}
