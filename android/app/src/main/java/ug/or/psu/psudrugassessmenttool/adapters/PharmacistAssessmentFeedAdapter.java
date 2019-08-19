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
import ug.or.psu.psudrugassessmenttool.models.PharmacistAssessmentFeed;

public class PharmacistAssessmentFeedAdapter extends RecyclerView.Adapter<PharmacistAssessmentFeedAdapter.MyViewHolder>  {
    private List<PharmacistAssessmentFeed> formList;
    private PharmacistAssessmentFeedAdapter.PharmacistAssessmentFeedAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView from_period, to_period, score, timestamp, pharmacy_name;

        MyViewHolder(View view) {
            super(view);
            from_period = view.findViewById(R.id.pharmacist_assessment_feed_from);
            to_period = view.findViewById(R.id.pharmacist_assessment_feed_to);
            score = view.findViewById(R.id.pharmacist_assessment_feed_score);
            pharmacy_name = view.findViewById(R.id.pharmacist_assessment_feed_pharmacy_name);
            timestamp = view.findViewById(R.id.pharmacist_assessment_feed_timestamp);

            view.setOnClickListener(view1 -> listener.onFormSelected(formList.get(getAdapterPosition())));
        }
    }

    public PharmacistAssessmentFeedAdapter(List<PharmacistAssessmentFeed> formList, PharmacistAssessmentFeedAdapter.PharmacistAssessmentFeedAdapterListener listener) {
        this.listener = listener;
        this.formList = formList;
    }

    @NonNull
    @Override
    public PharmacistAssessmentFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pharmacist_assessment_form_feed, parent, false);

        return new PharmacistAssessmentFeedAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmacistAssessmentFeedAdapter.MyViewHolder holder, final int position) {
        final PharmacistAssessmentFeed forms = formList.get(position);
        holder.from_period.setText(forms.getFromPeriod());
        holder.pharmacy_name.setText(forms.getPharmacyName());
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

    public interface PharmacistAssessmentFeedAdapterListener {
        void onFormSelected(PharmacistAssessmentFeed forms);
    }
}
