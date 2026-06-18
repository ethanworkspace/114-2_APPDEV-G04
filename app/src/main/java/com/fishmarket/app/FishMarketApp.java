package com.fishmarket.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import com.fishmarket.app.data.api.FisheryApi;
import com.fishmarket.app.data.api.RetrofitClient;
import com.fishmarket.app.data.local.AppDatabase;
import com.fishmarket.app.data.repository.FisheryRepository;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.fishmarket.app.worker.DailyNotificationWorker;
import java.util.concurrent.TimeUnit;

public class FishMarketApp extends Application {

    public static final String CHANNEL_ID = "fish_market_daily";
    private FisheryRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        scheduleDailyNotification();
    }

    public FisheryRepository getRepository() {
        if (repository == null) {
            FisheryApi api = RetrofitClient.getInstance();
            AppDatabase db = AppDatabase.getInstance(this);
            repository = new FisheryRepository(api, db.favoriteDao(), db.purchaseRecordDao());
        }
        return repository;
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "今日漁市焦點",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("每日推播今日焦點魚類及行情");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    private void scheduleDailyNotification() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                DailyNotificationWorker.class, 24, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "daily_fish_notification",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }
}
