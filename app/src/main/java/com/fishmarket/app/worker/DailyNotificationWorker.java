package com.fishmarket.app.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.fishmarket.app.data.api.FisheryApi;
import com.fishmarket.app.data.api.RetrofitClient;
import com.fishmarket.app.data.model.TransactionData;
import com.fishmarket.app.util.NotificationHelper;
import java.util.List;
import retrofit2.Response;

public class DailyNotificationWorker extends Worker {

    public DailyNotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull @Override
    public Result doWork() {
        try {
            FisheryApi api = RetrofitClient.getInstance();
            Response<List<TransactionData>> response = api.getTodayTransactions(5000).execute();

            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                List<TransactionData> data = response.body();

                // Find the fish with highest volume (today's featured fish)
                TransactionData featured = data.get(0);
                for (TransactionData td : data) {
                    if (td.getVolume() > featured.getVolume()) {
                        featured = td;
                    }
                }

                String title = "\uD83D\uDC1F 今日漁市焦點";
                String body = String.format("%s — %s 平均價 $%.1f 元/kg，交易量 %.1f kg",
                        featured.getMarketName(), featured.getFishName(),
                        featured.getAvgPrice(), featured.getVolume());

                NotificationHelper.showNotification(getApplicationContext(), title, body,
                        featured.getMarketName(), featured.getFishName(), featured.getProductCode());

                return Result.success();
            }
            return Result.retry();
        } catch (Exception e) {
            return Result.retry();
        }
    }
}
