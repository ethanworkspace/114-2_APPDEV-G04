package com.fishmarket.app.ui.fish;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fishmarket.app.databinding.ItemFishBinding;
import com.fishmarket.app.data.model.TransactionData;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FishAdapter extends RecyclerView.Adapter<FishAdapter.ViewHolder> {

    private List<TransactionData> items = new ArrayList<>();
    private OnFishClickListener listener;

    public interface OnFishClickListener {
        void onFishClick(TransactionData fish);
    }

    public void setOnFishClickListener(OnFishClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<TransactionData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFishBinding binding = ItemFishBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionData item = items.get(position);
        holder.binding.tvFishName.setText(item.getFishName());
        holder.binding.tvAvgPrice.setText(String.format(Locale.US, "$%.1f/kg", item.getAvgPrice()));
        holder.binding.tvVolume.setText(String.format(Locale.US, "%.1f kg", item.getVolume()));
        // Price change can be computed if we have yesterday's data; for now show price range
        String range = String.format(Locale.US, "$%.1f ~ $%.1f", item.getLowPrice(), item.getHighPrice());
        holder.binding.tvPriceChange.setText(range);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onFishClick(item);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemFishBinding binding;
        ViewHolder(ItemFishBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
