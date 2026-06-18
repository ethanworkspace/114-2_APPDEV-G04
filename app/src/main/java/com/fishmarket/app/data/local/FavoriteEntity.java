package com.fishmarket.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type; // "MARKET" or "FISH"
    private String marketName;
    private String fishName;
    private int productCode;
    private long timestamp;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getMarketName() { return marketName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }
    public String getFishName() { return fishName; }
    public void setFishName(String fishName) { this.fishName = fishName; }
    public int getProductCode() { return productCode; }
    public void setProductCode(int productCode) { this.productCode = productCode; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
