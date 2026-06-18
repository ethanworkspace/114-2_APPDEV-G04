package com.fishmarket.app.ui.market;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.fishmarket.app.databinding.FragmentMarketListBinding;

public class MarketListFragment extends Fragment {

    private FragmentMarketListBinding binding;
    private MarketListViewModel viewModel;
    private MarketAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarketListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new MarketListViewModel.Factory(app.getRepository()))
                .get(MarketListViewModel.class);

        adapter = new MarketAdapter();
        binding.rvMarkets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMarkets.setAdapter(adapter);

        adapter.setOnMarketClickListener(marketName -> {
            Bundle args = new Bundle();
            args.putString("marketName", marketName);
            Navigation.findNavController(view).navigate(R.id.action_marketList_to_fishList, args);
        });

        viewModel.getMarkets().observe(getViewLifecycleOwner(), markets -> {
            adapter.setItems(markets);
            binding.tvEmpty.setVisibility(markets.isEmpty() ? View.VISIBLE : View.GONE);
            binding.rvMarkets.setVisibility(markets.isEmpty() ? View.GONE : View.VISIBLE);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.swipeRefresh.setRefreshing(loading);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.loadMarkets());

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
