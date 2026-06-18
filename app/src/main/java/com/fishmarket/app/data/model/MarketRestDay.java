package com.fishmarket.app.data.model;

import com.google.gson.annotations.SerializedName;

public class MarketRestDay {

    @SerializedName("MarketNo")
    private String marketNo;

    @SerializedName("MarketName")
    private String marketName;

    @SerializedName("MarketType")
    private String marketType;

    @SerializedName("YearMonth")
    private String yearMonth;

    @SerializedName("ClosedDate")
    private String closedDate;

    public String getMarketNo() { return marketNo; }
    public String getMarketName() { return marketName; }
    public String getMarketType() { return marketType; }
    public String getYearMonth() { return yearMonth; }
    public String getClosedDate() { return closedDate; }
}
