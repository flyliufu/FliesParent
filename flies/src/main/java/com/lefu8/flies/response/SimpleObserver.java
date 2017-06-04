package com.lefu8.flies.response;

import com.google.gson.Gson;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import rx.Observer;

/**
 * @author liufu on 2017/6/4.
 */

public abstract class SimpleObserver<T> implements Observer<String> {

  @Override public void onCompleted() {

  }

  @Override public void onError(Throwable e) {

  }

  @Override public void onNext(String json) {

  }

  protected abstract void onParse(T t);
}
