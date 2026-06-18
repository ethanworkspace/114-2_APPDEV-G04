package com.fishmarket.app.ui.records;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.local.PurchaseRecordEntity;
import com.fishmarket.app.data.repository.FisheryRepository;
import java.util.List;

public class PurchaseRecordViewModel extends ViewModel {

    private final FisheryRepository repository;

    public PurchaseRecordViewModel(FisheryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<PurchaseRecordEntity>> getAllRecords() {
        return repository.getAllPurchaseRecords();
    }

    public void insertRecord(String fishName, String marketName, double price) {
        PurchaseRecordEntity record = new PurchaseRecordEntity(fishName, marketName, price, System.currentTimeMillis());
        repository.insertPurchaseRecord(record);
    }

    public void deleteRecord(PurchaseRecordEntity record) {
        repository.deletePurchaseRecord(record);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PurchaseRecordViewModel(repository);
        }
    }
}
