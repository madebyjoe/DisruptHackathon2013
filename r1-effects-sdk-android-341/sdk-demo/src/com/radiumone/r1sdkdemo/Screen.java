package com.radiumone.r1sdkdemo;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class Screen {
  private static Integer WIDTH = null;
  private static Integer HEIGHT = null;
  private static Activity ACTIVITY;

  public static void init(final Activity activity) {
    ACTIVITY = activity;
  }

  public static boolean isNull() {
    return WIDTH == null || HEIGHT == null;
  }

  public static void setHeight(final int height) {
    if (HEIGHT == null) {
      HEIGHT = height;
    }
  }

  public static void setWidth(final int width) {
    if (WIDTH == null) {
      WIDTH = width;
    }
  }

  public static int HEIGHT() {
    return HEIGHT;
  }

  public static int WIDTH() {
    return WIDTH;
  }

  public static int HEIGHT_PERC(final double perc) {
    return (int) (HEIGHT * perc);
  }

  public static int WIDTH_PERC(final double perc) {
    return (int) (WIDTH * perc);
  }

  public static double WIDTH_INCHES() {
    final DisplayMetrics dm = new DisplayMetrics();
    ACTIVITY.getWindowManager().getDefaultDisplay().getMetrics(dm);
    return dm.widthPixels/dm.xdpi;
  }

  public static double HEIGHT_INCHES() {
    final DisplayMetrics dm = new DisplayMetrics();
    ACTIVITY.getWindowManager().getDefaultDisplay().getMetrics(dm);
    return dm.heightPixels/dm.ydpi;
  }

  public static double DURATION_FACTOR() {
    return Screen.HEIGHT_INCHES()/4.5;
  }

  public static double SCALED_DENSITY() {
    return ACTIVITY.getResources().getDisplayMetrics().scaledDensity;
  }

  public static double DENSITY() {
    return ACTIVITY.getResources().getDisplayMetrics().density;
  }

  public static double DP_TO_PIX(final double dp) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dp, ACTIVITY.getResources().getDisplayMetrics());
  }

}
