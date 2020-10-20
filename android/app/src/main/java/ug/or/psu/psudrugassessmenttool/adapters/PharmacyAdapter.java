package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.Pharmacy;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<Pharmacy> pharmacyList;
    private List<Pharmacy> pharmacyListFiltered;
    private SupervisorPharmacyAdapterListener listener;
    private HelperFunctions helperFunctions;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.pharmacy_name);
            location = view.findViewById(R.id.pharmacy_location);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(view1 -> listener.onPharmacySelected(pharmacyListFiltered.get(getAdapterPosition())));
        }
    }


    public PharmacyAdapter(Context context, List<Pharmacy> pharmacyList, SupervisorPharmacyAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.pharmacyList = pharmacyList;
        this.pharmacyListFiltered = pharmacyList;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacies_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Pharmacy pharmacy = pharmacyListFiltered.get(position);
        holder.name.setText(pharmacy.getName());
        holder.location.setText(pharmacy.getLocation());

        String image_url = helperFunctions.getIpAddress() + pharmacy.getStatusImage();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return pharmacyListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString().toLowerCase();
                if (charString.isEmpty()) {
                    filterResults.values = pharmacyList;
                } else {
                    List<Pharmacy> filteredList = new ArrayList<>();
                    for (Pharmacy row : pharmacyList) {
                        if (row.getName().toLowerCase().contains(charString) || row.getLocation().toLowerCase().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    filterResults.values = filteredList;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    pharmacyListFiltered = (ArrayList<Pharmacy>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public interface SupervisorPharmacyAdapterListener {
        void onPharmacySelected(Pharmacy pharmacy);
    }
}