package ug.or.psu.psudrugassessmenttool.users.dashboards.ndasupervisor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class SupervisorPharmacyAdapter extends RecyclerView.Adapter<SupervisorPharmacyAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<SupervisorPharmacy> pharmacyList;
    private List<SupervisorPharmacy> pharmacyListFiltered;
    private SupervisorPharmacyAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, location;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.pharmacy_name);
            location = view.findViewById(R.id.pharmacy_location);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPharmacySelected(pharmacyListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public SupervisorPharmacyAdapter(Context context, List<SupervisorPharmacy> pharmacyList, SupervisorPharmacyAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.pharmacyList = pharmacyList;
        this.pharmacyListFiltered = pharmacyList;
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
        final SupervisorPharmacy pharmacy = pharmacyListFiltered.get(position);
        holder.name.setText(pharmacy.getName());
        holder.location.setText(pharmacy.getLocation());

        Glide.with(context)
                .load(pharmacy.getStatus())
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
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pharmacyListFiltered = pharmacyList;
                } else {
                    List<SupervisorPharmacy> filteredList = new ArrayList<>();
                    for (SupervisorPharmacy row : pharmacyList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    pharmacyListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pharmacyListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pharmacyListFiltered = (ArrayList<SupervisorPharmacy>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface SupervisorPharmacyAdapterListener {
        void onPharmacySelected(SupervisorPharmacy pharmacy);
    }
}
