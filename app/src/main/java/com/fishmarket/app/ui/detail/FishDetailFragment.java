package com.fishmarket.app.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.fishmarket.app.FishMarketApp;
import com.fishmarket.app.R;
import com.fishmarket.app.databinding.FragmentFishDetailBinding;
import java.util.Locale;

public class FishDetailFragment extends Fragment {

    private FragmentFishDetailBinding binding;
    private FishDetailViewModel viewModel;
    private String marketName, fishName;
    private int productCode;
    private boolean isFavorite = false;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFishDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        marketName = getArguments() != null ? getArguments().getString("marketName", "") : "";
        fishName = getArguments() != null ? getArguments().getString("fishName", "") : "";
        productCode = getArguments() != null ? getArguments().getInt("productCode", 0) : 0;

        FishMarketApp app = (FishMarketApp) requireActivity().getApplication();
        viewModel = new ViewModelProvider(this, new FishDetailViewModel.Factory(app.getRepository()))
                .get(FishDetailViewModel.class);

        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.tvFishName.setText(fishName);
        binding.tvMarketName.setText(marketName + "漁市場");

        viewModel.getTodayData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                binding.tvAvgPrice.setText(String.format(Locale.US, "$%.1f", data.getAvgPrice()));
                binding.tvHighPrice.setText(String.format(Locale.US, "$%.1f", data.getHighPrice()));
                binding.tvMidPrice.setText(String.format(Locale.US, "$%.1f", data.getMidPrice()));
                binding.tvLowPrice.setText(String.format(Locale.US, "$%.1f", data.getLowPrice()));
                binding.tvVolume.setText(String.format(Locale.US, "%.1f kg", data.getVolume()));
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(requireContext(), err, Toast.LENGTH_SHORT).show();
        });

        // Favorite button
        viewModel.getRepository().isFishFavorite(marketName, productCode).observe(getViewLifecycleOwner(), count -> {
            isFavorite = count != null && count > 0;
            binding.btnFavoriteFish.setImageResource(
                    isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        });

        binding.btnFavoriteFish.setOnClickListener(v -> {
            viewModel.getRepository().toggleFishFavorite(marketName, fishName, productCode, isFavorite);
        });

        binding.btnViewRecipes.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("query", fishName);
            Navigation.findNavController(view).navigate(R.id.action_fishDetail_to_recipes, args);
        });

        viewModel.loadFishDetail(marketName, fishName, productCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
