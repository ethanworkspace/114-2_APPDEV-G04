package com.fishmarket.app.ui.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fishmarket.app.FishMarketApp;
import com.fishmarket.app.databinding.DialogAddRecordBinding;
import com.fishmarket.app.databinding.FragmentPurchaseRecordBinding;

public class PurchaseRecordFragment extends Fragment {

    private FragmentPurchaseRecordBinding binding;
    private PurchaseRecordViewModel viewModel;
    private PurchaseRecordAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPurchaseRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new PurchaseRecordViewModel.Factory(app.getRepository()))
                .get(PurchaseRecordViewModel.class);

        adapter = new PurchaseRecordAdapter();
        binding.rvRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecords.setAdapter(adapter);

        viewModel.getAllRecords().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            binding.tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        });

        adapter.setOnDeleteClickListener(record -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("刪除紀錄")
                    .setMessage("確定要刪除這筆購買紀錄嗎？")
                    .setPositiveButton("刪除", (dialog, which) -> viewModel.deleteRecord(record))
                    .setNegativeButton("取消", null)
                    .show();
        });

        binding.fabAdd.setOnClickListener(v -> showAddRecordDialog());
    }

    private void showAddRecordDialog() {
        DialogAddRecordBinding dialogBinding = DialogAddRecordBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .create();

        dialogBinding.btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.btnSave.setOnClickListener(v -> {
            String fishName = dialogBinding.etFishName.getText().toString().trim();
            String marketName = dialogBinding.etMarketName.getText().toString().trim();
            String priceStr = dialogBinding.etPrice.getText().toString().trim();

            if (fishName.isEmpty() || marketName.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(requireContext(), "請填寫完整資訊", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                viewModel.insertRecord(fishName, marketName, price);
                dialog.dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "請輸入正確的金額", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
