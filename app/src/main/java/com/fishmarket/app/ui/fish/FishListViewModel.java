package com.fishmarket.app.ui.fish;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.model.TransactionData;
import com.fishmarket.app.data.repository.FisheryRepository;
import java.util.ArrayList;
import java.util.List;

public class FishListViewModel extends ViewModel {

    private final FisheryRepository repository;
    private final MutableLiveData<List<TransactionData>> fishList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private List<TransactionData> allFish = new ArrayList<>();
    private String marketName;

    public FishListViewModel(FisheryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<TransactionData>> getFishList() { return fishList; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
    public FisheryRepository getRepository() { return repository; }

    public void loadFishForMarket(String marketName) {
        this.marketName = marketName;
        loading.setValue(true);
        repository.getTodayTransactions(new FisheryRepository.DataCallback<List<TransactionData>>() {
            @Override
            public void onSuccess(List<TransactionData> data) {
                allFish.clear();
                for (TransactionData td : data) {
                    if (td.getMarketName().equals(marketName)) {
                        allFish.add(td);
                    }
                }
                fishList.postValue(allFish);
                loading.postValue(false);
            }
            @Override
            public void onError(String message) {
                error.postValue(message);
                loading.postValue(false);
            }
        });
    }

    public void filter(String query) {
        if (query == null || query.isEmpty()) {
            fishList.setValue(allFish);
            return;
        }
        List<TransactionData> filtered = new ArrayList<>();
        for (TransactionData td : allFish) {
            if (td.getFishName().contains(query)) {
                filtered.add(td);
            }
        }
        fishList.setValue(filtered);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FishListViewModel(repository);
        }
    }
}
