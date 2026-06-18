package com.fishmarket.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "purchase_records")
public class PurchaseRecordEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fishName;
    private String marketName;
    private double price;
    private long timestamp;

    public PurchaseRecordEntity(String fishName, String marketName, double price, long timestamp) {
        this.fishName = fishName;
        this.marketName = marketName;
        this.price = price;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFishName() { return fishName; }
    public String getMarketName() { return marketName; }
    public double getPrice() { return price; }
    public long getTimestamp() { return timestamp; }
}
