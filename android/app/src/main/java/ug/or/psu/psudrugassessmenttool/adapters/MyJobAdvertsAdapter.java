package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditJobAdvertActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.JobApplicationsActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.MyJobAdvertsActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.MyJobAdverts;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class MyJobAdvertsAdapter extends RecyclerView.Adapter<MyJobAdvertsAdapter.MyViewHolder> {
    private List<MyJobAdverts> advertsList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, company_name, deadline, edit, view_applications, delete;

        MyViewHolder(View view) {
            super(view);

            // Text views
            title = view.findViewById(R.id.job_title);
            company_name = view.findViewById(R.id.company_name);
            deadline = view.findViewById(R.id.deadline);
            edit = view.findViewById(R.id.edit_job);
            view_applications = view.findViewById(R.id.view_applications);
            delete = view.findViewById(R.id.delete_job);

            edit.setOnClickListener(view1 -> {
                int position = getAdapterPosition();
                final MyJobAdverts advert = advertsList.get(position);

                Intent intent = new Intent(context, EditJobAdvertActivity.class);
                intent.putExtra("id", advert.getId());
                context.startActivity(intent);
            });

            view_applications.setOnClickListener(view12 -> {
                int position = getAdapterPosition();
                final MyJobAdverts advert = advertsList.get(position);

                Intent intent = new Intent(context, JobApplicationsActivity.class);
                intent.putExtra("id", advert.getId());
                context.startActivity(intent);
            });

            delete.setOnClickListener(view13 -> {
                int position = getAdapterPosition();

                final MyJobAdverts advert = advertsList.get(position);

                final HelperFunctions helperFunctions = new HelperFunctions(context);

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to delete this advert")
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            //
                        })
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            helperFunctions.genericProgressBar("Removing job advert..");
                            String network_address = helperFunctions.getIpAddress() + "delete_job_advert.php?id=" + advert.getId();

                            // Request a string response from the provided URL
                            StringRequest request = new StringRequest(network_address,
                                    response -> {
                                        helperFunctions.stopProgressBar();

                                        if(response.equals("1")){
                                            new AlertDialog.Builder(context)
                                                    .setMessage("Advert deleted successfully")
                                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface1, int i1) {
                                                            ((MyJobAdvertsActivity)context).getAdverts();
                                                        }
                                                    }).show();
                                        }

                                    }, error -> {
                                        helperFunctions.stopProgressBar();

                                        if (error instanceof TimeoutError || error instanceof NetworkError) {
                                            helperFunctions.genericDialog("Something went wrong. Please make sure you are connected to a working internet connection.");
                                        } else {
                                            helperFunctions.genericDialog("Something went wrong. Please try again later");
                                        }
                                    });

                            //add to request queue in singleton class
                            VolleySingleton.getInstance(context).addToRequestQueue(request);
                        }).show();
            });
        }
    }

    public MyJobAdvertsAdapter(Context context, List<MyJobAdverts> advertsList) {
        this.advertsList = advertsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyJobAdvertsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_my_job_adverts, parent, false);

        return new MyJobAdvertsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyJobAdvertsAdapter.MyViewHolder holder, final int position) {
        final MyJobAdverts advert = advertsList.get(position);

        @SuppressLint("SimpleDateFormat")
        String date = new java.text.SimpleDateFormat("E, dd MMMM yyyy").format(new java.util.Date (Long.parseLong(advert.getDeadline())));

        holder.deadline.setText("Deadline: " + date);
        holder.company_name.setText("Company: " + advert.getCompanyName());
        holder.title.setText("Title: " + advert.getTitle());
    }

    @Override
    public int getItemCount() {
        return advertsList.size();
    }
}
