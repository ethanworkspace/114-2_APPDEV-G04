package com.fishmarket.app.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.fishmarket.app.FishMarketApp;
import com.fishmarket.app.R;
import com.fishmarket.app.data.local.FavoriteEntity;
import com.fishmarket.app.databinding.FragmentFavoritesBinding;
import com.google.android.material.tabs.TabLayout;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private FavoriteAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new FavoritesViewModel.Factory(app.getRepository()))
                .get(FavoritesViewModel.class);

        adapter = new FavoriteAdapter();
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFavorites.setAdapter(adapter);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("收藏市場"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("收藏魚種"));

        adapter.setOnFavoriteClickListener(item -> {
            Bundle args = new Bundle();
            if ("MARKET".equals(item.getType())) {
                args.putString("marketName", item.getMarketName());
                Navigation.findNavController(view).navigate(R.id.action_favorites_to_fishList, args);
            } else {
                args.putString("marketName", item.getMarketName());
                args.putString("fishName", item.getFishName());
                args.putInt("productCode", item.getProductCode());
                Navigation.findNavController(view).navigate(R.id.action_favorites_to_fishDetail, args);
            }
        });

        adapter.setOnRemoveClickListener(item -> {
            if ("MARKET".equals(item.getType())) {
                viewModel.getRepository().toggleMarketFavorite(item.getMarketName(), true);
            } else {
                viewModel.getRepository().toggleFishFavorite(item.getMarketName(), item.getFishName(), item.getProductCode(), true);
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.setSelectedTab(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewModel.getSelectedTab().observe(getViewLifecycleOwner(), tab -> {
            observeFavorites(tab);
        });

        observeFavorites(0);
    }

    private void observeFavorites(int tab) {
        // Remove previous observers
        viewModel.getFavoriteMarkets().removeObservers(getViewLifecycleOwner());
        viewModel.getFavoriteFish().removeObservers(getViewLifecycleOwner());

        if (tab == 0) {
            viewModel.getFavoriteMarkets().observe(getViewLifecycleOwner(), list -> {
                adapter.setItems(list);
                binding.tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });
        } else {
            viewModel.getFavoriteFish().observe(getViewLifecycleOwner(), list -> {
                adapter.setItems(list);
                binding.tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
