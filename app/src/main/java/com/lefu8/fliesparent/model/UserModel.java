package com.lefu8.fliesparent.model;

import android.os.Environment;
import android.os.SystemClock;
import com.lefu8.flies.api.CommonAPI;
import com.lefu8.fliesparent.bean.UserBean;
import com.lefu8.fliesparent.context.App;
import com.lefu8.fliesparent.frame.AppModel;
import com.lefu8.fliesparent.frame.JSONEntity;
import com.lefu8.fliesparent.frame.ObserverImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import rx.Observer;
import rx.Subscription;

/**
 * @author liufu on 2017/6/4.
 */

public class UserModel extends AppModel {

  /**
   * 设置当前 model 的请求路径对象
   */
  @Override public CommonAPI getCommonApi() {
    return App.getUserAPI();
  }

  public Subscription getUserList(ObserverImpl<JSONEntity<ArrayList<UserBean>>> observer) {

    return doGet("json/list.json", null, observer);
  }

  public Subscription getUser(ObserverImpl<JSONEntity<UserBean>> observer) {

    return doGet("json/object.json", null, observer);
  }

  public Subscription timeout(ObserverImpl<JSONEntity<UserBean>> observer) {

    return doPost("json/timeout.json", null, observer);
  }

  public Subscription fileUpload(String s, Map<String, String> param,
      ObserverImpl<JSONEntity<String>> fileUploadObserver) {
    Map<String, String> fileMap = new HashMap<>();
    fileMap.put("file", s);
    param.put("name", "刘甫");
    return fileUpload("http://172.28.1.41:8080/blog/testUpload", param, fileMap,
        fileUploadObserver);
  }

  public Subscription fileDownload(Map<String, String> params, Observer<File> observer) {
    String absolutePath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .getAbsolutePath();
    return doPostFileDownload("http://172.28.1.41:8080/blog/files/yank", absolutePath + "/liufu",
        "a.txt", params, observer);
  }
}
