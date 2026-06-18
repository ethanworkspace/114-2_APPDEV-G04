package com.fishmarket.app.ui.market;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.fishmarket.app.data.model.MarketLocation;
import com.fishmarket.app.data.model.MarketRestDay;
import com.fishmarket.app.data.repository.FisheryRepository;
import com.fishmarket.app.util.DateUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketListViewModel extends ViewModel {

    private final FisheryRepository repository;
    private final MutableLiveData<List<MarketInfo>> markets = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private List<MarketInfo> allMarkets = new ArrayList<>();

    public MarketListViewModel(FisheryRepository repository) {
        this.repository = repository;
        loadMarkets();
    }

    public LiveData<List<MarketInfo>> getMarkets() { return markets; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void loadMarkets() {
        loading.setValue(true);
        List<MarketLocation> locations = MarketLocation.getAllMarkets();
        repository.getMarketRestDays(new FisheryRepository.DataCallback<List<MarketRestDay>>() {
            @Override
            public void onSuccess(List<MarketRestDay> restDays) {
                Map<String, MarketRestDay> restMap = new HashMap<>();
                String currentYM = DateUtils.getCurrentRocYearMonth();
                for (MarketRestDay rd : restDays) {
                    if (rd.getYearMonth().equals(currentYM)) {
                        restMap.put(rd.getMarketName(), rd);
                    }
                }
                allMarkets.clear();
                for (MarketLocation loc : locations) {
                    MarketRestDay rd = restMap.get(loc.getMarketName());
                    boolean closed = false;
                    String closedDates = "";
                    if (rd != null) {
                        closed = DateUtils.isClosedToday(rd.getClosedDate(), rd.getYearMonth());
                        closedDates = rd.getClosedDate();
                    }
                    allMarkets.add(new MarketInfo(loc.getMarketName(), loc.getMarketNo(), closed, closedDates, loc.getAddress()));
                }
                markets.postValue(allMarkets);
                loading.postValue(false);
            }
            @Override
            public void onError(String message) {
                // Still show markets without rest day info
                allMarkets.clear();
                for (MarketLocation loc : locations) {
                    allMarkets.add(new MarketInfo(loc.getMarketName(), loc.getMarketNo(), false, "", loc.getAddress()));
                }
                markets.postValue(allMarkets);
                loading.postValue(false);
            }
        });
    }

    public void filter(String query) {
        if (query == null || query.isEmpty()) {
            markets.setValue(allMarkets);
            return;
        }
        List<MarketInfo> filtered = new ArrayList<>();
        for (MarketInfo m : allMarkets) {
            if (m.getName().contains(query) || m.getAddress().contains(query)) {
                filtered.add(m);
            }
        }
        markets.setValue(filtered);
    }

    public static class MarketInfo {
        private final String name;
        private final String marketNo;
        private final boolean closedToday;
        private final String closedDates;
        private final String address;

        public MarketInfo(String name, String marketNo, boolean closedToday, String closedDates, String address) {
            this.name = name;
            this.marketNo = marketNo;
            this.closedToday = closedToday;
            this.closedDates = closedDates;
            this.address = address;
        }
        public String getName() { return name; }
        public String getMarketNo() { return marketNo; }
        public boolean isClosedToday() { return closedToday; }
        public String getClosedDates() { return closedDates; }
        public String getAddress() { return address; }
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final FisheryRepository repository;
        public Factory(FisheryRepository repository) { this.repository = repository; }
        @NonNull @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MarketListViewModel(repository);
        }
    }
}
