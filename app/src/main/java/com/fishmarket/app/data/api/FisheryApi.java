package com.fishmarket.app.data.api;

import com.fishmarket.app.data.model.TransactionData;
import com.fishmarket.app.data.model.MarketRestDay;
import com.fishmarket.app.data.model.HistoricalTransactionResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FisheryApi {

    @GET("Service/OpenData/FromM/AquaticTransData.aspx")
    Call<List<TransactionData>> getTodayTransactions(
            @Query("$top") int top
    );

    @GET("Service/OpenData/FromM/AquaticTransData.aspx")
    Call<List<TransactionData>> getTransactionsByFishCode(
            @Query("$top") int top,
            @Query("TypeNo") int typeNo
    );

    @GET("api/v1/FisheryProductsTransType")
    Call<HistoricalTransactionResponse> getHistoricalTransactions(
            @Query("Start_time") String startTime,
            @Query("End_time") String endTime,
            @Query("MarketName") String marketName,
            @Query("SeafoodProdCode") String productCode
    );

    @GET("Service/OpenData/FromM/MarketRestFish.aspx")
    Call<List<MarketRestDay>> getMarketRestDays(
            @Query("$top") int top
    );
}
