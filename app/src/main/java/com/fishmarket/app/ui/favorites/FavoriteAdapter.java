package com.fishmarket.app.ui.favorites;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fishmarket.app.data.local.FavoriteEntity;
import com.fishmarket.app.databinding.ItemFavoriteBinding;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteEntity> items = new ArrayList<>();
    private OnFavoriteClickListener listener;
    private OnRemoveClickListener removeListener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(FavoriteEntity item);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(FavoriteEntity item);
    }

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.listener = listener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.removeListener = listener;
    }

    public void setItems(List<FavoriteEntity> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding binding = ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteEntity item = items.get(position);
        if ("MARKET".equals(item.getType())) {
            holder.binding.tvName.setText(item.getMarketName() + "漁市場");
            holder.binding.tvSubInfo.setText("收藏市場");
            holder.binding.tvPrice.setText("");
        } else {
            holder.binding.tvName.setText(item.getFishName());
            holder.binding.tvSubInfo.setText(item.getMarketName() + "漁市場");
            holder.binding.tvPrice.setText("品種代碼: " + item.getProductCode());
        }
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onFavoriteClick(item);
        });
        holder.binding.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onRemoveClick(item);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemFavoriteBinding binding;
        ViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
