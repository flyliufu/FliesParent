package com.lefu8.flies.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author liufu on 2016/12/13.
 */

public class ConvertFactory extends Converter.Factory {
  private Gson gson;

  public ConvertFactory(Gson gson) {
    this.gson = gson;
  }

  public ConvertFactory() {
    gson = new Gson();
  }

  @Override
  public Converter<ResponseBody, String> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    return new ResponseConverter();
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
    return new RequestConverter<>(gson, adapter);
  }
}
