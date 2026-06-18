package com.fishmarket.app.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fishmarket.app.databinding.FragmentRecipeBinding;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private RecipeViewModel viewModel;
    private RecipeAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        adapter = new RecipeAdapter();
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecipes.setAdapter(adapter);

        viewModel.getRecipes().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            if (list.isEmpty() && !binding.etSearch.getText().toString().isEmpty()) {
                binding.tvEmpty.setText("找不到相關食譜");
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else if (list.isEmpty()) {
                binding.tvEmpty.setText("請輸入關鍵字搜尋食譜\n資料來源：愛料理 icook.tw");
                binding.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            if (loading) binding.tvEmpty.setVisibility(View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });

        adapter.setOnRecipeClickListener(recipe -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(recipe.getRecipeUrl()));
            startActivity(intent);
        });

        binding.btnSearch.setOnClickListener(v -> performSearch());

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                performSearch();
                return true;
            }
            return false;
        });

        // Check if query is passed from other fragment
        if (getArguments() != null) {
            String query = getArguments().getString("query", "");
            if (!query.isEmpty()) {
                binding.etSearch.setText(query);
                performSearch();
            }
        }
    }

    private void performSearch() {
        String query = binding.etSearch.getText().toString().trim();
        if (!query.isEmpty()) {
            viewModel.searchRecipes(query);
            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
