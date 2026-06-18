package com.fishmarket.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites WHERE type = 'MARKET' ORDER BY timestamp DESC")
    LiveData<List<FavoriteEntity>> getFavoriteMarkets();

    @Query("SELECT * FROM favorites WHERE type = 'FISH' ORDER BY timestamp DESC")
    LiveData<List<FavoriteEntity>> getFavoriteFish();

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    LiveData<List<FavoriteEntity>> getAllFavorites();

    @Query("SELECT COUNT(*) FROM favorites WHERE type = 'MARKET' AND marketName = :marketName")
    LiveData<Integer> isMarketFavorite(String marketName);

    @Query("SELECT COUNT(*) FROM favorites WHERE type = 'FISH' AND marketName = :marketName AND productCode = :productCode")
    LiveData<Integer> isFishFavorite(String marketName, int productCode);

    @Insert
    void insert(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE type = 'MARKET' AND marketName = :marketName")
    void deleteMarketFavorite(String marketName);

    @Query("DELETE FROM favorites WHERE type = 'FISH' AND marketName = :marketName AND productCode = :productCode")
    void deleteFishFavorite(String marketName, int productCode);
}
