package ug.or.psu.psudrugassessmenttool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.EcpdFeed;

public class EcpdFeedAdapter extends RecyclerView.Adapter<EcpdFeedAdapter.MyViewHolder>
        implements Filterable {

    private List<EcpdFeed> formList;
    private List<EcpdFeed> formListFiltered;
    private EcpdFeedAdapter.EcpdFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ecpd_feed_title, ecpd_feed_description, ecpd_feed_timestamp, ecpd_feed_author;

        MyViewHolder(View view) {
            super(view);
            ecpd_feed_title = view.findViewById(R.id.ecpd_feed_title);
            ecpd_feed_description = view.findViewById(R.id.ecpd_feed_description);
            ecpd_feed_timestamp = view.findViewById(R.id.ecpd_feed_timestamp);
            ecpd_feed_author = view.findViewById(R.id.ecpd_feed_author);

            view.setOnClickListener(view1 -> listener.onFormSelected(formListFiltered.get(getAdapterPosition())));
        }
    }

    public EcpdFeedAdapter(List<EcpdFeed> formList, EcpdFeedAdapter.EcpdFeedAdapterListener listener) {
        this.listener = listener;
        this.formList = formList;
        this.formListFiltered = formList;
    }

    @NonNull
    @Override
    public EcpdFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ecpd_feed, parent, false);

        return new EcpdFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EcpdFeedAdapter.MyViewHolder holder, final int position) {
        final EcpdFeed form = formListFiltered.get(position);
        holder.ecpd_feed_title.setText(form.getTitle());
        holder.ecpd_feed_description.setText(form.getDescription());
        holder.ecpd_feed_author.setText(form.getAuthor());

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(form.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.ecpd_feed_timestamp.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return formListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    formListFiltered = formList;
                } else {
                    List<EcpdFeed> filteredList = new ArrayList<>();
                    for (EcpdFeed row : formList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getAuthor().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    formListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = formListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                formListFiltered = (ArrayList<EcpdFeed>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EcpdFeedAdapterListener {
        void onFormSelected(EcpdFeed form);
    }
}
