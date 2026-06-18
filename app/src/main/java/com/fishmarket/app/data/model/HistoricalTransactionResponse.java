package com.fishmarket.app.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class HistoricalTransactionResponse {
    @SerializedName("RS")
    private String rs;
    
    @SerializedName("Data")
    private List<TransactionData> data;

    public String getRs() { return rs; }
    public List<TransactionData> getData() { return data; }
}
