package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import ug.or.psu.psudrugassessmenttool.models.EcpdResults;

public class EcpdResultsAdapter extends RecyclerView.Adapter<EcpdResultsAdapter.MyViewHolder>
        implements Filterable {

    private List<EcpdResults> formList;
    private List<EcpdResults> formListFiltered;
    private EcpdResultsAdapter.EcpdResultsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, title, score, timestamp;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            title = view.findViewById(R.id.title);
            score = view.findViewById(R.id.score);
            timestamp = view.findViewById(R.id.timestamp);

            view.setOnClickListener(view1 -> listener.onFormSelected(formListFiltered.get(getAdapterPosition())));
        }
    }

    public EcpdResultsAdapter(List<EcpdResults> formList, EcpdResultsAdapter.EcpdResultsAdapterListener listener) {
        this.listener = listener;
        this.formList = formList;
        this.formListFiltered = formList;
    }

    @NonNull
    @Override
    public EcpdResultsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ecpd_results, parent, false);

        return new EcpdResultsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EcpdResultsAdapter.MyViewHolder holder, final int position) {
        final EcpdResults form = formListFiltered.get(position);
        holder.name.setText(form.getName());
        holder.title.setText(form.getTitle());
        holder.score.setText("Score: " + form.getScore());

        if (form.getPassed().equals("1")){
            holder.score.setTextColor(Color.GREEN);
        } else {
            holder.score.setTextColor(Color.RED);
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(form.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText("Test taken: " + timeAgo);
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
                    List<EcpdResults> filteredList = new ArrayList<>();
                    for (EcpdResults row : formList) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                formListFiltered = (ArrayList<EcpdResults>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EcpdResultsAdapterListener {
        void onFormSelected(EcpdResults form);
    }
}
