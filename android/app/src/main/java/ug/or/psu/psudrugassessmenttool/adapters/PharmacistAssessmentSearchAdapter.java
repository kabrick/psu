package ug.or.psu.psudrugassessmenttool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentSearch;

public class PharmacistAssessmentSearchAdapter extends RecyclerView.Adapter<PharmacistAssessmentSearchAdapter.MyViewHolder>
        implements Filterable {
    private List<PharmacistAssessmentSearch> pharmacistsList;
    private List<PharmacistAssessmentSearch> pharmacistListFiltered;
    private PharmacistAssessmentSearchAdapter.PharmacistAssessmentSearchAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.pharmacist_assessment_search_name);

            view.setOnClickListener(view1 -> listener.onPharmacistSelected(pharmacistListFiltered.get(getAdapterPosition())));
        }
    }

    public PharmacistAssessmentSearchAdapter(List<PharmacistAssessmentSearch> pharmacistsList, PharmacistAssessmentSearchAdapter.PharmacistAssessmentSearchAdapterListener listener) {
        this.listener = listener;
        this.pharmacistsList = pharmacistsList;
        this.pharmacistListFiltered = pharmacistsList;
    }

    @NonNull
    @Override
    public PharmacistAssessmentSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacist_assessment_search_row_item, parent, false);

        return new PharmacistAssessmentSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacistAssessmentSearchAdapter.MyViewHolder holder, final int position) {
        final PharmacistAssessmentSearch pharmacy = pharmacistListFiltered.get(position);
        holder.name.setText(pharmacy.getName());
    }

    @Override
    public int getItemCount() {
        return pharmacistListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pharmacistListFiltered = pharmacistsList;
                } else {
                    List<PharmacistAssessmentSearch> filteredList = new ArrayList<>();
                    for (PharmacistAssessmentSearch row : pharmacistsList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    pharmacistListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pharmacistListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pharmacistListFiltered = (ArrayList<PharmacistAssessmentSearch>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PharmacistAssessmentSearchAdapterListener {
        void onPharmacistSelected(PharmacistAssessmentSearch pharmacist);
    }
}
