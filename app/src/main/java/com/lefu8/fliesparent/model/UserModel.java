package com.lefu8.fliesparent.model;

import com.lefu8.flies.api.CommonAPI;
import com.lefu8.fliesparent.bean.UserBean;
import com.lefu8.fliesparent.context.App;
import com.lefu8.fliesparent.frame.AppModel;
import com.lefu8.fliesparent.frame.JSONEntity;
import com.lefu8.fliesparent.frame.ObserverImpl;
import java.util.ArrayList;

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

  public void getUserList(ObserverImpl<JSONEntity<ArrayList<UserBean>>> observer) {

    doGet("json/list.json", null, observer);
  }

  public void getUser(ObserverImpl<JSONEntity<UserBean>> observer) {

    doGet("json/object.json", null, observer);
  }
}
