package com.lefu8.flies.api;

import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author liufu on 2017/4/11.
 */

public interface CommonAPI {

  /**
   * 所有的 POST 请求都可以用这方法，传文件的除外
   *
   * @param path 路径
   * @param params 参数
   * @return 返回 JSON
   */
  @POST @FormUrlEncoded Observable<String> doPost(@Url String path,
      @HeaderMap Map<String, String> headerMap, @FieldMap Map<String, String> params);

  /**
   * GET 请求公用方法
   *
   * @param path 路径
   * @param params 参数
   */
  @GET Observable<String> doGet(@Url String path, @HeaderMap Map<String, String> headerMap,
      @QueryMap Map<String, String> params);

  /**
   * 上传文件方法
   *
   * @param path 路径
   * @return 服务器返回JSON
   */
  @POST Observable<String> fileUpload(@Url String path, @HeaderMap Map<String, String> headerMap,
      @Body RequestBody body);
}
