package ug.or.psu.psudrugassessmenttool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ug.or.psu.psudrugassessmenttool.R;
import ug.or.psu.psudrugassessmenttool.models.SingleTextCard;

public class SingleTextCardAdapter extends RecyclerView.Adapter<SingleTextCardAdapter.MyViewHolder> {
    private List<SingleTextCard> list;
    private SingleTextCardAdapter.SingleTextCardAdapterListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.single_text_card_text);

            view.setOnClickListener(view1 -> listener.onItemSelected(list.get(getAdapterPosition())));
        }
    }

    public SingleTextCardAdapter(List<SingleTextCard> list, SingleTextCardAdapter.SingleTextCardAdapterListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @NonNull
    @Override
    public SingleTextCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_single_text_card, parent, false);

        return new SingleTextCardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleTextCardAdapter.MyViewHolder holder, final int position) {
        final SingleTextCard item = list.get(position);
        holder.text.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SingleTextCardAdapterListener {
        void onItemSelected(SingleTextCard item);
    }
}
