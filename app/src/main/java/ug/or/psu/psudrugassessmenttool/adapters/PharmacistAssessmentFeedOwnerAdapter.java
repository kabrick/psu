package ug.or.psu.psudrugassessmenttool.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentFeedOwner;

public class PharmacistAssessmentFeedOwnerAdapter extends RecyclerView.Adapter<PharmacistAssessmentFeedOwnerAdapter.MyViewHolder> {
    private List<PharmacistAssessmentFeedOwner> formList;
    private PharmacistAssessmentFeedOwnerAdapter.PharmacistAssessmentFeedOwnerAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from_period, to_period, score, timestamp;

        MyViewHolder(View view) {
            super(view);
            from_period = view.findViewById(R.id.pharmacist_assessment_feed_owner_from);
            to_period = view.findViewById(R.id.pharmacist_assessment_feed_owner_to);
            score = view.findViewById(R.id.pharmacist_assessment_feed_owner_score);
            timestamp = view.findViewById(R.id.pharmacist_assessment_feed_owner_timestamp);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFormSelected(formList.get(getAdapterPosition()));
                }
            });
        }
    }

    public PharmacistAssessmentFeedOwnerAdapter(List<PharmacistAssessmentFeedOwner> formList, PharmacistAssessmentFeedOwnerAdapter.PharmacistAssessmentFeedOwnerAdapterListener listener) {
        this.listener = listener;
        this.formList = formList;
    }

    @NonNull
    @Override
    public PharmacistAssessmentFeedOwnerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacist_assessment_form_feed_owner, parent, false);

        return new PharmacistAssessmentFeedOwnerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacistAssessmentFeedOwnerAdapter.MyViewHolder holder, final int position) {
        final PharmacistAssessmentFeedOwner forms = formList.get(position);
        holder.from_period.setText(forms.getFromPeriod());
        holder.to_period.setText(forms.getToPeriod());
        holder.score.setText(forms.getScore());

        // change string to int
        int average_score_number = Integer.parseInt(forms.getScore());

        // color code the average score
        if (average_score_number < 50){
            // red
            holder.score.setTextColor(Color.RED);
        } else if (average_score_number <= 74){
            // orange
            holder.score.setTextColor(Color.parseColor("#FFA500"));
        } else {
            // green
            holder.score.setTextColor(Color.GREEN);
        }

        //covert timestamp to readable format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(forms.getTimestamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

        holder.timestamp.setText(timeAgo);
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

    public interface PharmacistAssessmentFeedOwnerAdapterListener {
        void onFormSelected(PharmacistAssessmentFeedOwner forms);
    }
}
