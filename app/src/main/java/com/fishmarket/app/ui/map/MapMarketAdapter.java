package com.fishmarket.app.ui.map;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fishmarket.app.data.model.MarketLocation;
import com.fishmarket.app.databinding.ItemMarketLocationBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapMarketAdapter extends RecyclerView.Adapter<MapMarketAdapter.ViewHolder> {

    private List<MarketLocation> items = new ArrayList<>();
    private Map<String, Boolean> statusMap = new HashMap<>();
    private OnMarketActionListener listener;

    public interface OnMarketActionListener {
        void onNavigateClick(MarketLocation market);
        void onViewPricesClick(MarketLocation market);
    }

    public void setOnMarketActionListener(OnMarketActionListener listener) {
        this.listener = listener;
    }

    public void setData(List<MarketLocation> items, Map<String, Boolean> statusMap) {
        this.items = items;
        if (statusMap != null) this.statusMap = statusMap;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMarketLocationBinding binding = ItemMarketLocationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketLocation market = items.get(position);
        holder.binding.tvMarketName.setText(market.getMarketName() + "漁市場");
        holder.binding.tvAddress.setText(market.getAddress());

        boolean isClosed = statusMap.containsKey(market.getMarketName());
        holder.binding.tvStatus.setText(isClosed ? "今日休市" : "營業中");

        GradientDrawable bg = (GradientDrawable) holder.binding.tvStatus.getBackground();
        if (isClosed) {
            bg.setColor(Color.parseColor("#E53935")); // Red
        } else {
            bg.setColor(Color.parseColor("#43A047")); // Green
        }

        holder.binding.btnNavigate.setOnClickListener(v -> {
            if (listener != null) listener.onNavigateClick(market);
        });

        holder.binding.btnViewPrices.setOnClickListener(v -> {
            if (listener != null) listener.onViewPricesClick(market);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemMarketLocationBinding binding;
        ViewHolder(ItemMarketLocationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
