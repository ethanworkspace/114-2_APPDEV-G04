package com.fishmarket.app.data.repository;

import androidx.lifecycle.LiveData;
import com.fishmarket.app.data.api.FisheryApi;
import com.fishmarket.app.data.local.FavoriteDao;
import com.fishmarket.app.data.local.FavoriteEntity;
import com.fishmarket.app.data.local.PurchaseRecordDao;
import com.fishmarket.app.data.local.PurchaseRecordEntity;
import com.fishmarket.app.data.model.MarketRestDay;
import com.fishmarket.app.data.model.TransactionData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FisheryRepository {

    private final FisheryApi api;
    private final FavoriteDao favoriteDao;
    private final PurchaseRecordDao purchaseRecordDao;
    private final ExecutorService executor;

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    public FisheryRepository(FisheryApi api, FavoriteDao favoriteDao, PurchaseRecordDao purchaseRecordDao) {
        this.api = api;
        this.favoriteDao = favoriteDao;
        this.purchaseRecordDao = purchaseRecordDao;
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void getTodayTransactions(DataCallback<List<TransactionData>> callback) {
        api.getTodayTransactions(5000).enqueue(new Callback<List<TransactionData>>() {
            @Override
            public void onResponse(Call<List<TransactionData>> call, Response<List<TransactionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("載入失敗，請稍後再試");
                }
            }
            @Override
            public void onFailure(Call<List<TransactionData>> call, Throwable t) {
                callback.onError("網路連線錯誤: " + t.getMessage());
            }
        });
    }

    public void getTransactionsByFishCode(int fishCode, DataCallback<List<TransactionData>> callback) {
        api.getTransactionsByFishCode(5000, fishCode).enqueue(new Callback<List<TransactionData>>() {
            @Override
            public void onResponse(Call<List<TransactionData>> call, Response<List<TransactionData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("載入失敗");
                }
            }
            @Override
            public void onFailure(Call<List<TransactionData>> call, Throwable t) {
                callback.onError("網路連線錯誤: " + t.getMessage());
            }
        });
    }

    public void getHistoricalTransactions(String startTime, String endTime, String marketName, String productCode, DataCallback<List<TransactionData>> callback) {
        api.getHistoricalTransactions(startTime, endTime, marketName, productCode).enqueue(new Callback<com.fishmarket.app.data.model.HistoricalTransactionResponse>() {
            @Override
            public void onResponse(Call<com.fishmarket.app.data.model.HistoricalTransactionResponse> call, Response<com.fishmarket.app.data.model.HistoricalTransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("載入歷史資料失敗");
                }
            }
            @Override
            public void onFailure(Call<com.fishmarket.app.data.model.HistoricalTransactionResponse> call, Throwable t) {
                callback.onError("網路連線錯誤: " + t.getMessage());
            }
        });
    }

    public void getMarketRestDays(DataCallback<List<MarketRestDay>> callback) {
        api.getMarketRestDays(200).enqueue(new Callback<List<MarketRestDay>>() {
            @Override
            public void onResponse(Call<List<MarketRestDay>> call, Response<List<MarketRestDay>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("載入休市資料失敗");
                }
            }
            @Override
            public void onFailure(Call<List<MarketRestDay>> call, Throwable t) {
                callback.onError("網路連線錯誤: " + t.getMessage());
            }
        });
    }

    // Favorites
    public LiveData<List<FavoriteEntity>> getFavoriteMarkets() {
        return favoriteDao.getFavoriteMarkets();
    }

    public LiveData<List<FavoriteEntity>> getFavoriteFish() {
        return favoriteDao.getFavoriteFish();
    }

    public LiveData<List<FavoriteEntity>> getAllFavorites() {
        return favoriteDao.getAllFavorites();
    }

    public LiveData<Integer> isMarketFavorite(String marketName) {
        return favoriteDao.isMarketFavorite(marketName);
    }

    public LiveData<Integer> isFishFavorite(String marketName, int productCode) {
        return favoriteDao.isFishFavorite(marketName, productCode);
    }

    public void toggleMarketFavorite(String marketName, boolean currentlyFavorite) {
        executor.execute(() -> {
            if (currentlyFavorite) {
                favoriteDao.deleteMarketFavorite(marketName);
            } else {
                FavoriteEntity entity = new FavoriteEntity();
                entity.setType("MARKET");
                entity.setMarketName(marketName);
                entity.setTimestamp(System.currentTimeMillis());
                favoriteDao.insert(entity);
            }
        });
    }

    public void toggleFishFavorite(String marketName, String fishName, int productCode, boolean currentlyFavorite) {
        executor.execute(() -> {
            if (currentlyFavorite) {
                favoriteDao.deleteFishFavorite(marketName, productCode);
            } else {
                FavoriteEntity entity = new FavoriteEntity();
                entity.setType("FISH");
                entity.setMarketName(marketName);
                entity.setFishName(fishName);
                entity.setProductCode(productCode);
                entity.setTimestamp(System.currentTimeMillis());
                favoriteDao.insert(entity);
            }
        });
    }

    // Purchase Records
    public LiveData<List<PurchaseRecordEntity>> getAllPurchaseRecords() {
        return purchaseRecordDao.getAllRecords();
    }

    public void insertPurchaseRecord(PurchaseRecordEntity record) {
        executor.execute(() -> purchaseRecordDao.insert(record));
    }

    public void deletePurchaseRecord(PurchaseRecordEntity record) {
        executor.execute(() -> purchaseRecordDao.delete(record));
    }
}
