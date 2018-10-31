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
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.NewsFeed;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.MyViewHolder> {

    private List<NewsFeed> newsList;
    private NewsFeedAdapterListener listener;
    private HelperFunctions helperFunctions;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, text, author, timestamp;
        View read_status;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.news_feed_title_list);
            text = view.findViewById(R.id.news_feed_text_list);
            author = view.findViewById(R.id.news_feed_author_list);
            timestamp = view.findViewById(R.id.news_feed_timestamp_list);
            read_status = view.findViewById(R.id.read_status);

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
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_feed_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final NewsFeed news = newsList.get(position);
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

        //String image_url = helperFunctions.getIpAddress() + news.getImage();

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(news.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);

        /*Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.fitCenterTransform())
                .into(holder.image);*/
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface NewsFeedAdapterListener {
        void onNewsItemSelected(NewsFeed news);
    }
}
