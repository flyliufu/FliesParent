package com.lefu8.flies.util;

import android.util.Log;

/**
 * 日志封装类
 *
 * @author liufu on 2017/6/17.
 */

public final class LogUtils {
  private static String TAG = "Flies";
  private static boolean isLogable = false;

  /**
   * 设置日志级别
   *
   * @param level 最低输出日志等级
   */
  public static void setLevel(int level) {
    isLogable = Log.isLoggable(TAG, level);
  }

  /**
   * 设置日志级别
   *
   * @param tag 自定义日志标签
   * @param level 最低输出日志等级
   */
  public static void setLevel(String tag, int level) {
    TAG = tag;
    isLogable = Log.isLoggable(TAG, level);
  }

  public static void v(String msg) {
    if (isLogable) Log.v(TAG, msg);
  }

  public static void v(String msg, Throwable tr) {
    if (isLogable) Log.v(TAG, msg, tr);
  }

  public static void d(String msg) {
    if (isLogable) Log.d(TAG, msg);
  }

  public static void d(String msg, Throwable tr) {
    if (isLogable) Log.d(TAG, msg, tr);
  }

  public static void i(String msg) {
    if (isLogable) Log.i(TAG, msg);
  }

  public static void i(String msg, Throwable tr) {
    if (isLogable) Log.i(TAG, msg, tr);
  }

  public static void w(String msg) {
    if (isLogable) Log.w(TAG, msg);
  }

  public static void w(String msg, Throwable tr) {
    if (isLogable) Log.w(TAG, msg, tr);
  }

  public static void e(String msg) {
    if (isLogable) Log.e(TAG, msg);
  }

  public static void e(String msg, Throwable tr) {
    if (isLogable) Log.e(TAG, msg, tr);
  }

  /**
   * 如发生该类错误不论级别是什么都要输出
   */
  public static void wtf(String msg) {
    Log.wtf(TAG, msg);
  }

  /**
   * 如发生该类错误不论级别是什么都要输出
   */
  public static void wtf(String msg, Throwable tr) {
    Log.wtf(TAG, msg, tr);
  }
}
