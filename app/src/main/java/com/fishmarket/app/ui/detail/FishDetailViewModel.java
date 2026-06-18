package com.fishmarket.app.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.model.TransactionData;
import com.fishmarket.app.data.repository.FisheryRepository;
import java.util.List;

public class FishDetailViewModel extends ViewModel {

    private final FisheryRepository repository;
    private final MutableLiveData<TransactionData> todayData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private String marketName;
    private int productCode;

    public FishDetailViewModel(FisheryRepository repository) {
        this.repository = repository;
    }

    public LiveData<TransactionData> getTodayData() { return todayData; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }
    public FisheryRepository getRepository() { return repository; }

    public void loadFishDetail(String marketName, String fishName, int productCode) {
        this.marketName = marketName;
        this.productCode = productCode;
        loading.setValue(true);
        // Load today's data
        repository.getTodayTransactions(new FisheryRepository.DataCallback<List<TransactionData>>() {
            @Override
            public void onSuccess(List<TransactionData> data) {
                for (TransactionData td : data) {
                    if (td.getMarketName().equals(marketName) && td.getProductCode() == productCode) {
                        todayData.postValue(td);
                        break;
                    }
                }
                loading.postValue(false);
            }
            @Override
            public void onError(String message) {
                error.postValue(message);
                loading.postValue(false);
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new FishDetailViewModel(repository);
        }
    }
}
