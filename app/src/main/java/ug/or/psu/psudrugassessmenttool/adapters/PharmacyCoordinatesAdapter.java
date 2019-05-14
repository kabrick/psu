package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.PharmacyCoordinates;

public class PharmacyCoordinatesAdapter extends RecyclerView.Adapter<PharmacyCoordinatesAdapter.MyViewHolder> {
    private List<PharmacyCoordinates> attendanceList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pharmacy_name, latitude, longitude, location;

        MyViewHolder(View view) {
            super(view);
            pharmacy_name = view.findViewById(R.id.pharmacy_name);
            latitude = view.findViewById(R.id.latitude);
            longitude = view.findViewById(R.id.longitude);
            location = view.findViewById(R.id.location);
        }
    }

    public PharmacyCoordinatesAdapter(List<PharmacyCoordinates> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public PharmacyCoordinatesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacy_coordinates_row, parent, false);

        return new PharmacyCoordinatesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacyCoordinatesAdapter.MyViewHolder holder, final int position) {
        final PharmacyCoordinates attendanceSession = attendanceList.get(position);

        holder.pharmacy_name.setText(attendanceSession.getPharmacy());
        holder.latitude.setText(attendanceSession.getLatitude());
        holder.longitude.setText(attendanceSession.getLongitude());
        holder.location.setText(attendanceSession.getLocation());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}
