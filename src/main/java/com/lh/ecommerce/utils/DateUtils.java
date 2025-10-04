package com.lh.ecommerce.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {
  public static Date minutesFromNow(int minutes) {
    long nowMillis = System.currentTimeMillis();
    long expMillis = nowMillis + TimeUnit.MINUTES.toMillis(minutes);
    return new Date(expMillis);
  }

  public static Instant sevenDaysAgo() {
    return daysAgo(7);
  }

  private static Instant daysAgo(int days) {
    return Instant.now().minus(days, ChronoUnit.DAYS);
  }
}
