package com.lefu8.fliesparent.context;

import android.app.Application;
import android.util.Log;
import com.lefu8.flies.api.CommonAPI;
import com.lefu8.flies.convert.ConvertFactory;
import com.lefu8.flies.util.LogUtils;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author liufu on 2017/6/4.
 */

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    LogUtils.setLevel(Log.DEBUG);
  }

  private static Retrofit.Builder retBuilder = new Retrofit.Builder().client(buildOKHTTP())
      .addConverterFactory(new ConvertFactory())
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

  private static CommonAPI mUserAPI;

  public static CommonAPI getUserAPI() {
    if (mUserAPI == null) {
      mUserAPI = retBuilder.baseUrl("http://191.101.10.186/").build().create(CommonAPI.class);
    }
    return mUserAPI;
  }

  public static OkHttpClient buildOKHTTP() {

    X509TrustManager xtm = new X509TrustManager() {
      @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {
      }

      @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {
      }

      @Override public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] x509Certificates = new X509Certificate[0];
        return x509Certificates;
      }
    };

    SSLContext sslContext = null;
    try {
      sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, new TrustManager[] { xtm }, new SecureRandom());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder builder =
        new OkHttpClient.Builder().connectTimeout(30000L, TimeUnit.MILLISECONDS)
            .readTimeout(30000L, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .sslSocketFactory(sslContext.getSocketFactory())
            .hostnameVerifier(new HostnameVerifier() {
              @Override public boolean verify(String hostname, SSLSession session) {
                return true;
              }
            });

    return builder.build();
  }
}
