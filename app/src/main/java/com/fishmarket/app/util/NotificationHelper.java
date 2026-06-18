package com.fishmarket.app.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.fishmarket.app.FishMarketApp;
import com.fishmarket.app.R;
import com.fishmarket.app.ui.MainActivity;

public class NotificationHelper {

    public static void showNotification(Context context, String title, String body,
                                        String marketName, String fishName, int productCode) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("marketName", marketName);
        intent.putExtra("fishName", fishName);
        intent.putExtra("productCode", productCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, FishMarketApp.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fish_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1001, builder.build());
    }
}
