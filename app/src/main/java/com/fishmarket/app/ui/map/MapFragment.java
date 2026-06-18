package com.fishmarket.app.ui.map;

import android.content.Intent;
import android.net.Uri;
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
import com.fishmarket.app.data.model.MarketLocation;
import com.fishmarket.app.databinding.FragmentMapBinding;
import java.util.List;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MapViewModel viewModel;
    private MapMarketAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new MapViewModel.Factory(app.getRepository()))
                .get(MapViewModel.class);

        adapter = new MapMarketAdapter();
        binding.rvMarkets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMarkets.setAdapter(adapter);

        List<MarketLocation> markets = MarketLocation.getAllMarkets();

        viewModel.getMarketStatusMap().observe(getViewLifecycleOwner(), statusMap -> {
            adapter.setData(markets, statusMap);
        });

        adapter.setOnMarketActionListener(new MapMarketAdapter.OnMarketActionListener() {
            @Override
            public void onNavigateClick(MarketLocation market) {
                // Open external map app
                Uri gmmIntentUri = Uri.parse("geo:" + market.getLatitude() + "," + market.getLongitude() 
                        + "?q=" + Uri.encode(market.getMarketName() + "漁市場"));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    // Fallback if google maps app is not installed
                    startActivity(new Intent(Intent.ACTION_VIEW, gmmIntentUri));
                }
            }

            @Override
            public void onViewPricesClick(MarketLocation market) {
                Bundle args = new Bundle();
                args.putString("marketName", market.getMarketName());
                Navigation.findNavController(view).navigate(R.id.action_map_to_fishList, args);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
