package ug.or.psu.psudrugassessmenttool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.Pharmacies;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.MyViewHolder>
        implements Filterable {
    private List<Pharmacies> pharmaciesList;
    private List<Pharmacies> pharmaciesListFiltered;
    private PharmaciesAdapter.PharmaciesAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPharmacySelected(pharmaciesListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public PharmaciesAdapter(List<Pharmacies> pharmaciesList, PharmaciesAdapter.PharmaciesAdapterListener listener) {
        this.listener = listener;
        this.pharmaciesList = pharmaciesList;
        this.pharmaciesListFiltered = pharmaciesList;
    }

    @NonNull
    @Override
    public PharmaciesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacy_row_item, parent, false);

        return new PharmaciesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmaciesAdapter.MyViewHolder holder, final int position) {
        final Pharmacies pharmacy = pharmaciesListFiltered.get(position);
        holder.name.setText(pharmacy.getName());
    }

    @Override
    public int getItemCount() {
        return pharmaciesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pharmaciesListFiltered = pharmaciesList;
                } else {
                    List<Pharmacies> filteredList = new ArrayList<>();
                    for (Pharmacies row : pharmaciesList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    pharmaciesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pharmaciesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pharmaciesListFiltered = (ArrayList<Pharmacies>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PharmaciesAdapterListener {
        void onPharmacySelected(Pharmacies pharmacy);
    }
}
