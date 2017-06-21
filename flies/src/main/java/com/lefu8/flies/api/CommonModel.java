package com.lefu8.flies.api;

import android.support.annotation.NonNull;
import com.lefu8.flies.response.SimpleObserver;
import com.lefu8.flies.util.LogUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 公用网络请求基础类
 *
 * @author liufu on 2017/4/11.
 */
@SuppressWarnings("unchecked") public abstract class CommonModel {

  private final Observer DEFAULT;

  public CommonModel(SimpleObserver simpleObserver) {
    this.DEFAULT = simpleObserver;
  }

  public CommonModel() {
    this.DEFAULT = new Observer() {

      @Override public void onCompleted() {
        LogUtils.d("CommonModel default onCompleted execute.");
      }

      @Override public void onError(Throwable e) {
        LogUtils.d("CommonModel default onError execute :" + e.getMessage());
      }

      @Override public void onNext(Object o) {
        LogUtils.d("CommonModel default onNext execute.");
      }
    };
  }

  public abstract CommonAPI getCommonApi();

  /**
   * GET 请求
   *
   * @param url 请求路径
   * @param params 请求参数
   * @return 结果观察者对象
   */
  protected Observable<String> doGet(String url, Map<String, String> params) {

    params = getPublicParams(params);
    return getCommonApi().doGet(url, getHeaders(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * GET 请求
   *
   * @param url 请求路径
   * @param params 请求参数
   * @param observer 回调方法
   * @return 订阅事件
   */
  protected <T> Subscription doGet(String url, Map<String, String> params,
      SimpleObserver<T> observer) {

    return doGet(url, params).subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * POST 请求
   *
   * @param url 请求路径
   * @param params 请求参数
   * @return 结果观察者对象
   */
  protected Observable<String> doPost(String url, Map<String, String> params) {
    params = getPublicParams(params);

    return getCommonApi().doPost(url, getHeaders(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * POST 请求
   *
   * @param url 请求路径
   * @param params 请求参数
   * @param observer 回调方法
   * @return 订阅事件
   */
  protected <T> Subscription doPost(String url, Map<String, String> params,
      SimpleObserver<T> observer) {

    return doPost(url, params).subscribe(observer == null ? DEFAULT : observer);
  }

  /**
   * 文件上传
   *
   * @param url 上传路径
   * @param type 上传类型
   * @param fileMap 文件列表
   * @param params 请求参数
   * @return 观察者
   */
  protected Observable<String> fileUpload(String url, MediaType type, Map<String, String> fileMap,
      Map<String, String> params) {

    params = getPublicParams(params);
    return getCommonApi().fileUpload(url, getHeaders(),
        filesToMultipartBodyParts(fileMap, params, type))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
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

    return fileUpload(url, FliesMediaType.IMAGE_PNG, fileMap, params).subscribe(
        observer == null ? DEFAULT : observer);
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

    return fileUpload(url, mediaType, fileMap, params).subscribe(
        observer == null ? DEFAULT : observer);
  }

  /**
   * GET 下载文件
   *
   * @param url 下载请求网络路径
   * @param path 下载到本地的路径
   * @param fileName 本地文件名
   * @param params 请求参数
   * @return 文件观察者
   */
  protected Observable<File> doGetFileDownload(@NonNull String url, @NonNull final String path,
      @NonNull final String fileName, Map<String, String> params) {
    params = getPublicParams(params);
    return getCommonApi().doGetFileDownload(url, getHeaders(), params)
        .flatMap(new Func1<Response<ResponseBody>, Observable<File>>() {
          @Override public Observable<File> call(Response<ResponseBody> response) {
            return saveToDiskRx(path, fileName, response);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 下载文件 GET 请求
   *
   * @param url 下载路径
   * @param path 保存在本地的路径不可为空
   * @param fileName 保存文件名，可以为空
   * @param observer 成功回调
   * @param params GET 请求参数
   * @return 订阅事件
   */
  protected Subscription doGetFileDownload(@NonNull String url, @NonNull final String path,
      @NonNull final String fileName, Map<String, String> params, Observer<File> observer) {

    return doGetFileDownload(url, path, fileName, params).subscribe(
        observer == null ? DEFAULT : observer);
  }

  /**
   * GET 下载文件
   *
   * @param url 下载请求网络路径
   * @param path 下载到本地的路径
   * @param fileName 本地文件名
   * @param params 请求参数
   * @return 文件观察者
   */
  protected Observable<File> doPostFileDownload(@NonNull String url, @NonNull final String path,
      @NonNull final String fileName, Map<String, String> params) {
    params = getPublicParams(params);
    return getCommonApi().doPostFileDownload(url, getHeaders(), params)
        .flatMap(new Func1<Response<ResponseBody>, Observable<File>>() {
          @Override public Observable<File> call(Response<ResponseBody> response) {
            return saveToDiskRx(path, fileName, response);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 下载文件 POST 请求
   *
   * @param url 下载路径
   * @param path 保存在本地的路径不可为空
   * @param fileName 保存文件名，可以为空
   * @param observer 成功回调
   * @param params GET 请求参数
   * @return 订阅事件
   */
  protected Subscription doPostFileDownload(@NonNull String url, @NonNull final String path,
      final String fileName, Map<String, String> params, Observer<File> observer) {

    return doPostFileDownload(url, path, fileName, params).subscribe(
        observer == null ? DEFAULT : observer);
  }

  /**
   * 写入文件
   *
   * @param fileDir 路径
   * @param fileName 文件名
   * @param response 返回文件流
   * @return 生成的文件
   */
  private Observable<File> saveToDiskRx(@NonNull final String fileDir, final String fileName,
      final Response<ResponseBody> response) {
    return Observable.create(new Observable.OnSubscribe<File>() {
      @Override public void call(Subscriber<? super File> subscriber) {
        try {
          // 创建下载目录
          File dir = new File(fileDir);

          if (!dir.exists()) {
            boolean mkdir = dir.mkdir();
            if (!mkdir) {
              throw new IOException("The path :" + fileDir + "cant not created.");
            }
          }
          //创建下载目录文件
          File destinationFile = new File(dir, fileName);

          BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
          bufferedSink.writeAll(response.body().source());
          bufferedSink.close();

          subscriber.onNext(destinationFile);
          subscriber.onCompleted();
        } catch (IOException e) {
          LogUtils.e("CommonModel method saveToDiskRx error.", e);
          subscriber.onError(e);
        }
      }
    });
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
