package com.fishmarket.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PurchaseRecordDao {

    @Insert
    void insert(PurchaseRecordEntity record);

    @Delete
    void delete(PurchaseRecordEntity record);

    @Query("SELECT * FROM purchase_records ORDER BY timestamp DESC")
    LiveData<List<PurchaseRecordEntity>> getAllRecords();
}
