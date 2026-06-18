package com.fishmarket.app.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.model.MarketLocation;
import com.fishmarket.app.data.model.MarketRestDay;
import com.fishmarket.app.data.repository.FisheryRepository;
import com.fishmarket.app.util.DateUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewModel extends ViewModel {

    private final FisheryRepository repository;
    private final MutableLiveData<Map<String, Boolean>> marketStatusMap = new MutableLiveData<>(new HashMap<>());

    public MapViewModel(FisheryRepository repository) {
        this.repository = repository;
        loadMarketStatus();
    }

    public LiveData<Map<String, Boolean>> getMarketStatusMap() { return marketStatusMap; }

    private void loadMarketStatus() {
        repository.getMarketRestDays(new FisheryRepository.DataCallback<List<MarketRestDay>>() {
            @Override
            public void onSuccess(List<MarketRestDay> restDays) {
                Map<String, Boolean> map = new HashMap<>();
                for (MarketRestDay rd : restDays) {
                    boolean closed = DateUtils.isClosedToday(rd.getClosedDate(), rd.getYearMonth());
                    if (closed) map.put(rd.getMarketName(), true);
                }
                marketStatusMap.postValue(map);
            }
            @Override
            public void onError(String message) {}
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MapViewModel(repository);
        }
    }
}
