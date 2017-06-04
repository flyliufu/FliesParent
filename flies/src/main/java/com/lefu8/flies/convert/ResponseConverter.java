package com.lefu8.flies.convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author liufu on 2016/12/13.
 */

final class ResponseConverter implements Converter<ResponseBody, String> {

  @Override public String convert(ResponseBody value) throws IOException {
    InputStreamReader reader = (InputStreamReader) value.charStream();
    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();

    String line;
    try {

      br = new BufferedReader(reader);
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }
}
