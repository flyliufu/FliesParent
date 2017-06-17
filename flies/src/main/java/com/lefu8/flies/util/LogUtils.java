package com.lefu8.flies.util;

import android.util.Log;

/**
 * 日志封装类
 *
 * @author liufu on 2017/6/17.
 */

public final class LogUtils {
  private static String TAG = "Flies";
  private static int sLogLevel = 0;

  /**
   * 设置日志级别
   *
   * @param level 最低输出日志等级
   */
  public static void setLevel(int level) {
    sLogLevel = level;
  }

  /**
   * 设置日志级别
   *
   * @param tag 自定义日志标签
   * @param level 最低输出日志等级
   */
  public static void setLevel(String tag, int level) {
    TAG = tag;
    sLogLevel = level;
  }

  public static void v(String msg) {
    if (sLogLevel <= Log.VERBOSE) Log.v(TAG, msg);
  }

  public static void v(String msg, Throwable tr) {
    if (sLogLevel <= Log.VERBOSE) Log.v(TAG, msg, tr);
  }

  public static void d(String msg) {
    if (sLogLevel <= Log.DEBUG) Log.d(TAG, msg);
  }

  public static void d(String msg, Throwable tr) {
    if (sLogLevel <= Log.DEBUG) Log.d(TAG, msg, tr);
  }

  public static void i(String msg) {
    if (sLogLevel <= Log.INFO) Log.i(TAG, msg);
  }

  public static void i(String msg, Throwable tr) {
    if (sLogLevel <= Log.INFO) Log.i(TAG, msg, tr);
  }

  public static void w(String msg) {
    if (sLogLevel <= Log.WARN) Log.w(TAG, msg);
  }

  public static void w(String msg, Throwable tr) {
    if (sLogLevel <= Log.WARN) Log.w(TAG, msg, tr);
  }

  public static void e(String msg) {
    if (sLogLevel <= Log.ERROR) Log.e(TAG, msg);
  }

  public static void e(String msg, Throwable tr) {
    if (sLogLevel <= Log.ERROR) Log.e(TAG, msg, tr);
  }

  public static void wtf(String msg) {
    if (sLogLevel <= Log.ASSERT) Log.wtf(TAG, msg);
  }

  public static void wtf(String msg, Throwable tr) {
    if (sLogLevel <= Log.ASSERT) Log.wtf(TAG, msg, tr);
  }
}
