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
import ug.or.psu.psudrugassessmenttool.models.Pharmacists;

public class PharmacistsAdapter extends RecyclerView.Adapter<PharmacistsAdapter.MyViewHolder>
        implements Filterable {

    private List<Pharmacists> pharmacistsList;
    private List<Pharmacists> pharmacistListFiltered;
    private PharmacistsAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, pharmacy;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.pharmacist_name);
            pharmacy = view.findViewById(R.id.pharmacist_pharmacy);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPharmacistSelected(pharmacistListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public PharmacistsAdapter(List<Pharmacists> pharmacistsList, PharmacistsAdapterListener listener) {
        this.listener = listener;
        this.pharmacistsList = pharmacistsList;
        this.pharmacistListFiltered = pharmacistsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacist_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Pharmacists pharmacy = pharmacistListFiltered.get(position);
        holder.name.setText(pharmacy.getName());
        holder.pharmacy.setText(pharmacy.getPharmacy());
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
                    List<Pharmacists> filteredList = new ArrayList<>();
                    for (Pharmacists row : pharmacistsList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPharmacy().toLowerCase().contains(charString.toLowerCase())) {
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
                pharmacistListFiltered = (ArrayList<Pharmacists>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PharmacistsAdapterListener {
        void onPharmacistSelected(Pharmacists pharmacist);
    }
}
