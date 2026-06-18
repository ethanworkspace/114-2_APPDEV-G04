package com.fishmarket.app.ui.market;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.fishmarket.app.R;
import com.fishmarket.app.databinding.ItemMarketBinding;
import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    private List<MarketListViewModel.MarketInfo> items = new ArrayList<>();
    private OnMarketClickListener listener;

    public interface OnMarketClickListener {
        void onMarketClick(String marketName);
    }

    public void setOnMarketClickListener(OnMarketClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<MarketListViewModel.MarketInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMarketBinding binding = ItemMarketBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketListViewModel.MarketInfo item = items.get(position);
        holder.binding.tvMarketName.setText(item.getName() + "漁市場");
        if (item.isClosedToday()) {
            holder.binding.tvMarketStatus.setText("今日休市");
            holder.binding.tvMarketStatus.setBackgroundResource(R.drawable.bg_closed_badge);
            holder.binding.tvMarketStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
        } else {
            holder.binding.tvMarketStatus.setText("營業中");
            holder.binding.tvMarketStatus.setBackgroundResource(R.drawable.bg_open_badge);
            holder.binding.tvMarketStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.secondary));
        }
        String closedText = item.getClosedDates().isEmpty() ? "無休市資料" : "本月休市：" + item.getClosedDates();
        holder.binding.tvClosedDates.setText(closedText);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onMarketClick(item.getName());
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMarketBinding binding;
        ViewHolder(ItemMarketBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
