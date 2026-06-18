package com.fishmarket.app.data.model;

import com.google.gson.annotations.SerializedName;

public class TransactionData {

    @SerializedName(value = "交易日期", alternate = {"TransDate"})
    private String transDate;

    @SerializedName(value = "品種代碼", alternate = {"SeafoodProdCode"})
    private String productCode;

    @SerializedName(value = "魚貨名稱", alternate = {"SeafoodProdName"})
    private String fishName;

    @SerializedName(value = "市場名稱", alternate = {"MarketName"})
    private String marketName;

    @SerializedName(value = "上價", alternate = {"Upper_Price"})
    private double highPrice;

    @SerializedName(value = "中價", alternate = {"Middle_Price"})
    private double midPrice;

    @SerializedName(value = "下價", alternate = {"Lower_Price"})
    private double lowPrice;

    @SerializedName(value = "交易量", alternate = {"Trans_Quantity"})
    private double volume;

    @SerializedName(value = "平均價", alternate = {"Avg_Price"})
    private double avgPrice;

    public String getTransDate() { return transDate; }
    public String getProductCodeStr() { return productCode; }
    public int getProductCode() {
        try {
            if (productCode == null || productCode.equals("-")) return 0;
            return Integer.parseInt(productCode.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    public String getFishName() { return fishName; }
    public String getMarketName() { return marketName; }
    public double getHighPrice() { return highPrice; }
    public double getMidPrice() { return midPrice; }
    public double getLowPrice() { return lowPrice; }
    public double getVolume() { return volume; }
    public double getAvgPrice() { return avgPrice; }

    public void setTransDate(String transDate) { this.transDate = transDate; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public void setProductCode(int productCode) { this.productCode = String.valueOf(productCode); }
    public void setFishName(String fishName) { this.fishName = fishName; }
    public void setMarketName(String marketName) { this.marketName = marketName; }
    public void setHighPrice(double highPrice) { this.highPrice = highPrice; }
    public void setMidPrice(double midPrice) { this.midPrice = midPrice; }
    public void setLowPrice(double lowPrice) { this.lowPrice = lowPrice; }
    public void setVolume(double volume) { this.volume = volume; }
    public void setAvgPrice(double avgPrice) { this.avgPrice = avgPrice; }
}
