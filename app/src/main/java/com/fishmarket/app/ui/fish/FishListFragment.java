package com.fishmarket.app.ui.fish;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fishmarket.app.FishMarketApp;
import com.fishmarket.app.R;
import com.fishmarket.app.databinding.FragmentFishListBinding;

public class FishListFragment extends Fragment {

    private FragmentFishListBinding binding;
    private FishListViewModel viewModel;
    private FishAdapter adapter;
    private String marketName;
    private boolean isFavorite = false;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFishListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        marketName = getArguments() != null ? getArguments().getString("marketName", "") : "";

        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new FishListViewModel.Factory(app.getRepository()))
                .get(FishListViewModel.class);

        binding.tvMarketTitle.setText(marketName + "漁市場");
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        adapter = new FishAdapter();
        binding.rvFish.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFish.setAdapter(adapter);

        adapter.setOnFishClickListener(fish -> {
            Bundle args = new Bundle();
            args.putString("marketName", marketName);
            args.putString("fishName", fish.getFishName());
            args.putInt("productCode", fish.getProductCode());
            Navigation.findNavController(view).navigate(R.id.action_fishList_to_fishDetail, args);
        });

        viewModel.getFishList().observe(getViewLifecycleOwner(), list -> {
            adapter.setItems(list);
            binding.tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            binding.rvFish.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.swipeRefresh.setRefreshing(loading);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });

        // Favorite button
        viewModel.getRepository().isMarketFavorite(marketName).observe(getViewLifecycleOwner(), count -> {
            isFavorite = count != null && count > 0;
            binding.btnFavoriteMarket.setImageResource(
                    isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        });

        binding.btnFavoriteMarket.setOnClickListener(v -> {
            viewModel.getRepository().toggleMarketFavorite(marketName, isFavorite);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.loadFishForMarket(marketName));

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        viewModel.loadFishForMarket(marketName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
