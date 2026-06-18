package com.fishmarket.app.ui.favorites;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.local.FavoriteEntity;
import com.fishmarket.app.data.repository.FisheryRepository;
import java.util.List;

public class FavoritesViewModel extends ViewModel {

    private final FisheryRepository repository;
    private final MutableLiveData<Integer> selectedTab = new MutableLiveData<>(0);

    public FavoritesViewModel(FisheryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<FavoriteEntity>> getFavoriteMarkets() {
        return repository.getFavoriteMarkets();
    }

    public LiveData<List<FavoriteEntity>> getFavoriteFish() {
        return repository.getFavoriteFish();
    }

    public LiveData<List<FavoriteEntity>> getAllFavorites() {
        return repository.getAllFavorites();
    }

    public FisheryRepository getRepository() { return repository; }

    public LiveData<Integer> getSelectedTab() { return selectedTab; }
    public void setSelectedTab(int tab) { selectedTab.setValue(tab); }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FavoritesViewModel(repository);
        }
    }
}
