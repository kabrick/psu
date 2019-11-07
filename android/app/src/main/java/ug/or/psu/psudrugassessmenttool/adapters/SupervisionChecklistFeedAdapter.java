package ug.or.psu.psudrugassessmenttool.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.SupervisionChecklistFeed;

public class SupervisionChecklistFeedAdapter extends RecyclerView.Adapter<SupervisionChecklistFeedAdapter.MyViewHolder>  {
    private List<SupervisionChecklistFeed> formList;
    private SupervisionChecklistFeedAdapter.SupervisionChecklistFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView supervisor_checklist_feed_text;

        MyViewHolder(View view) {
            super(view);
            supervisor_checklist_feed_text = view.findViewById(R.id.supervisor_checklist_feed_text);

            view.setOnClickListener(view1 -> listener.onFormSelected(formList.get(getAdapterPosition())));
        }
    }

    public SupervisionChecklistFeedAdapter(List<SupervisionChecklistFeed> formList, SupervisionChecklistFeedAdapter.SupervisionChecklistFeedAdapterListener listener) {
        this.listener = listener;
        this.formList = formList;
    }

    @NonNull
    @Override
    public SupervisionChecklistFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_supervision_checklist_feed, parent, false);

        return new SupervisionChecklistFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SupervisionChecklistFeedAdapter.MyViewHolder holder, final int position) {
        final SupervisionChecklistFeed forms = formList.get(position);

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(forms.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        String text = "Submitted by " + forms.getName() + " " + timeAgo;

        holder.supervisor_checklist_feed_text.setText(text);
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

    public interface SupervisionChecklistFeedAdapterListener {
        void onFormSelected(SupervisionChecklistFeed forms);
    }
}
