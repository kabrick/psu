package ug.or.psu.psudrugassessmenttool.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.globalactivities.EditJobAdvertActivity;
import ug.or.psu.psudrugassessmenttool.globalactivities.MyJobAdvertsActivity;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.JobApplications;
import ug.or.psu.psudrugassessmenttool.models.MyJobAdverts;
import ug.or.psu.psudrugassessmenttool.network.VolleySingleton;

public class JobApplicationsAdapter extends RecyclerView.Adapter<JobApplicationsAdapter.MyViewHolder> {
    private List<JobApplications> applicationList;
    private Context context;
    HelperFunctions helperFunctions;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, cover_letter, cv;
        ImageView photo;

        MyViewHolder(View view) {
            super(view);

            // Text views
            photo = view.findViewById(R.id.job_application_photo);
            name = view.findViewById(R.id.job_application_name);
            email = view.findViewById(R.id.job_application_email);
            phone = view.findViewById(R.id.job_application_phone);
            cover_letter = view.findViewById(R.id.job_application_cover_letter);
            cv = view.findViewById(R.id.job_application_cv);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final JobApplications application = applicationList.get(position);

                    String url = helperFunctions.getIpAddress() + application.getCv();

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
        }
    }

    public JobApplicationsAdapter(Context context, List<JobApplications> applicationList) {
        this.applicationList = applicationList;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public JobApplicationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_job_applications, parent, false);

        return new JobApplicationsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull JobApplicationsAdapter.MyViewHolder holder, final int position) {
        final JobApplications application = applicationList.get(position);

        String image_url = helperFunctions.getIpAddress() + application.getPhoto();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.photo);

        holder.name.setText(application.getName());
        holder.email.setText(application.getEmail());
        holder.phone.setText(application.getPhone());
        holder.cover_letter.setText(application.getCoverLetter());
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }
}
