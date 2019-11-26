package ug.or.psu.psudrugassessmenttool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.helpers.HelperFunctions;
import ug.or.psu.psudrugassessmenttool.models.AdminCommunications;

public class AdminCommunicationsAdapter extends RecyclerView.Adapter<AdminCommunicationsAdapter.MyViewHolder> {

    private List<AdminCommunications> list;
    private HelperFunctions helperFunctions;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, message;
        ImageView profile_picture;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.admin_communication_feed_name);
            message = view.findViewById(R.id.admin_communication_feed_message);
            profile_picture = view.findViewById(R.id.admin_communication_feed_photo);
        }
    }

    public AdminCommunicationsAdapter(Context context, List<AdminCommunications> list) {
        this.list = list;
        this.context = context;
        helperFunctions = new HelperFunctions(context);
    }

    @NonNull
    @Override
    public AdminCommunicationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_admin_communications, parent, false);

        return new AdminCommunicationsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCommunicationsAdapter.MyViewHolder holder, final int position) {
        final AdminCommunications communication = list.get(position);
        holder.name.setText(communication.getName());
        holder.message.setText(communication.getMessage());

        String image_url = helperFunctions.getIpAddress() + communication.getPhoto();

        Glide.with(context)
                .load(image_url)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profile_picture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
