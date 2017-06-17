package com.lefu8.fliesparent.frame;

import com.google.gson.Gson;
import com.lefu8.flies.response.SimpleObserver;
import com.lefu8.flies.util.LogUtils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author liufu on 2017/6/4.
 */
public abstract class ObserverImpl<T extends JSONEntity<?>> extends SimpleObserver<T> {

  private Gson gson;

  public ObserverImpl() {
    gson = new Gson();
  }

  public ObserverImpl(Gson gson) {
    this.gson = gson;
  }

  @Override public void onNext(String json) {
    LogUtils.d(json);
    try {
      Type type =
          ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
      T unknown = gson.fromJson(json, type);
      // TODO: 2017/6/4 这里可以根据对象判断一此公共的错误
      if (unknown == null) {
        LogUtils.w("Response is null");
        // 请求返回值为空
        onError(new Exception("Response is null"));
      } else {
        onParse(unknown);
      }
    } catch (Exception e) {
      onError(e);
      LogUtils.e("Parse json string error.", e);
    }
  }
}
