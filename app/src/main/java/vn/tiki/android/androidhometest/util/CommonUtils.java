package vn.tiki.android.androidhometest.util;

import java.util.concurrent.TimeUnit;

public class CommonUtils {
    public static String getDuration(long milliseconds) {
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;
        if (days != 0)
            return String.format("%d ngày", days);
        else
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
