package com.lefu8.flies.api;

import android.support.annotation.NonNull;
import com.lefu8.flies.response.SimpleObserver;
import com.lefu8.flies.util.LogUtils;
import java.io.File;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 公用网络请求基础类
 *
 * @author liufu on 2017/4/11.
 */
@SuppressWarnings("unchecked") public abstract class CommonModel {

  private final SimpleObserver DEFAULT;

  public CommonModel(SimpleObserver simpleObserver) {
    this.DEFAULT = simpleObserver;
  }

  public CommonModel() {
    this.DEFAULT = new SimpleObserver() {
      @Override protected void onParse(Object o) {
        // this method will never be execute.
      }

      @Override public void onNext(Object o) {
        LogUtils.d("CommonModel default onNext execute.");
      }
    };
  }

  public abstract CommonAPI getCommonApi();

  /**
   * GET 请求
   */
  protected <T> Subscription doGet(String url, Map<String, String> params,
      SimpleObserver<T> observer) {
    params = getPublicParams(params);

    Observable<String> observable = getCommonApi().doGet(url, getHeaders(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    return observable.subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * POST 请求
   */
  protected <T> Subscription doPost(String url, Map<String, String> params,
      SimpleObserver<T> observer) {
    params = getPublicParams(params);

    Observable<String> observable = getCommonApi().doPost(url, getHeaders(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());

    return observable.subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * 文件上传，上传文件类型默认为 png
   *
   * @param url 路径
   * @param params 参数
   * @param fileMap 文件列表
   */

  protected <T> Subscription fileUpload(String url, Map<String, String> params,
      Map<String, String> fileMap, SimpleObserver<T> observer) {
    params = getPublicParams(params);

    Observable<String> observable = getCommonApi().fileUpload(url, getHeaders(),
        filesToMultipartBodyParts(fileMap, params, FliesMediaType.IMAGE_PNG))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    return observable.subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * 文件上传
   *
   * @param url 路径
   * @param params 参数
   * @param fileMap 文件列表
   * @param mediaType 上传文件类型
   */

  protected <T> Subscription fileUpload(String url, Map<String, String> params,
      Map<String, String> fileMap, MediaType mediaType, SimpleObserver<T> observer) {
    params = getPublicParams(params);

    Observable<String> observable = getCommonApi().fileUpload(url, getHeaders(),
        filesToMultipartBodyParts(fileMap, params, mediaType))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    return observable.subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * 将文本类型，文件类型参数 转换成 MultipartBody
   *
   * @param fileMap 文件路径
   * @param params 请求参数
   */
  @NonNull private MultipartBody filesToMultipartBodyParts(Map<String, String> fileMap,
      Map<String, String> params, MediaType mediaType) {

    MultipartBody.Builder builder = new MultipartBody.Builder();
    if (params != null && !params.isEmpty()) {
      // 放置一般数据类型
      for (String key : params.keySet()) {
        builder.addFormDataPart(key, params.get(key));
      }
    }
    if (fileMap != null && !fileMap.isEmpty()) {
      // 放置文件类型
      for (String key : fileMap.keySet()) {
        File file = new File(fileMap.get(key));
        if (!file.exists()) {
          throw new RuntimeException(String.format("File not found : %s.", fileMap.get(key)));
        }
        RequestBody requestBody = RequestBody.create(mediaType, file);
        builder.addFormDataPart(key, file.getName(), requestBody);
      }
    }
    return builder.setType(MultipartBody.FORM).build();
  }

  /**
   * 设置公共请求参数
   */
  protected abstract Map<String, String> getPublicParams(Map<String, String> params);

  /**
   * 设置公共请求Header部分
   */
  protected abstract Map<String, String> getHeaders();
}
