package com.fishmarket.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /**
     * 民國日期字串轉為西元 Date
     * e.g. "1150612" -> 2026/06/12
     */
    public static Date parseRocDate(String rocDateStr) {
        try {
            if (rocDateStr == null || rocDateStr.length() < 7) return null;
            int rocYear = Integer.parseInt(rocDateStr.substring(0, 3));
            int month = Integer.parseInt(rocDateStr.substring(3, 5));
            int day = Integer.parseInt(rocDateStr.substring(5, 7));
            int adYear = rocYear + 1911;
            Calendar cal = Calendar.getInstance();
            cal.set(adYear, month - 1, day, 0, 0, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 取得今日民國年月日字串 e.g. "1150612"
     */
    public static String getTodayRocDateStr() {
        Calendar cal = Calendar.getInstance();
        int rocYear = cal.get(Calendar.YEAR) - 1911;
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.US, "%03d%02d%02d", rocYear, month, day);
    }

    /**
     * 取得今日民國年月字串 e.g. "11506"
     */
    public static String getCurrentRocYearMonth() {
        Calendar cal = Calendar.getInstance();
        int rocYear = cal.get(Calendar.YEAR) - 1911;
        int month = cal.get(Calendar.MONTH) + 1;
        return String.format(Locale.US, "%03d%02d", rocYear, month);
    }

    /**
     * 取得 N 天前的日期字串（西元 YYYYMMDD 格式，用於 V1 API）
     */
    public static String getDateDaysAgo(int daysAgo) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -daysAgo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return sdf.format(cal.getTime());
    }

    /**
     * 取得今日西元日期字串 YYYYMMDD
     */
    public static String getTodayDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
        return sdf.format(new Date());
    }

    /**
     * 格式化日期為顯示用字串 MM/dd
     */
    public static String formatDisplayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.US);
        return sdf.format(date);
    }

    /**
     * 判斷今天是否為該市場休市日
     */
    public static boolean isClosedToday(String closedDateStr, String yearMonth) {
        if (closedDateStr == null || yearMonth == null) return false;
        String currentYearMonth = getCurrentRocYearMonth();
        if (!yearMonth.equals(currentYearMonth)) return false;
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String[] dates = closedDateStr.split("\\u3001"); // split by 、
        for (String d : dates) {
            try {
                if (Integer.parseInt(d.trim()) == today) return true;
            } catch (NumberFormatException ignored) {}
        }
        return false;
    }
}
