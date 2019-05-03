package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.MyJobAdverts;

public class MyJobAdvertsAdapter extends RecyclerView.Adapter<MyJobAdvertsAdapter.MyViewHolder>   {
    private List<MyJobAdverts> jobsList;
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
        }
    }

    public MyJobAdvertsAdapter(Context context, List<MyJobAdverts> jobsList) {
        this.jobsList = jobsList;
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
        final MyJobAdverts advert = jobsList.get(position);

        @SuppressLint("SimpleDateFormat")
        String date = new java.text.SimpleDateFormat("E, dd MMMM yyyy").format(new java.util.Date (Long.parseLong(advert.getDeadline())));

        holder.deadline.setText("Deadline: " + date);
        holder.company_name.setText("Company: " + advert.getCompanyName());
        holder.title.setText("Title: " + advert.getTitle());
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }
}
