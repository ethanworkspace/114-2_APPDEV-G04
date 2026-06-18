package com.fishmarket.app.ui.records;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fishmarket.app.data.local.PurchaseRecordEntity;
import com.fishmarket.app.databinding.ItemPurchaseRecordBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseRecordAdapter extends RecyclerView.Adapter<PurchaseRecordAdapter.ViewHolder> {

    private List<PurchaseRecordEntity> items = new ArrayList<>();
    private OnDeleteClickListener deleteListener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public interface OnDeleteClickListener {
        void onDeleteClick(PurchaseRecordEntity record);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public void setItems(List<PurchaseRecordEntity> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPurchaseRecordBinding binding = ItemPurchaseRecordBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseRecordEntity item = items.get(position);
        holder.binding.tvFishName.setText(item.getFishName());
        holder.binding.tvMarketName.setText(item.getMarketName() + "漁市場");
        holder.binding.tvPrice.setText(String.format(Locale.US, "$%.1f", item.getPrice()));
        holder.binding.tvDate.setText(sdf.format(new Date(item.getTimestamp())));

        holder.binding.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteClick(item);
        });
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemPurchaseRecordBinding binding;
        ViewHolder(ItemPurchaseRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
