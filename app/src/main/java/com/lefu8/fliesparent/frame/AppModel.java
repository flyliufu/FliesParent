package com.lefu8.fliesparent.frame;

import com.lefu8.flies.api.CommonModel;
import java.util.HashMap;
import java.util.Map;

/**
 * APP 公用 Model 基础类
 *
 * @author liufu on 2017/6/4.
 */

public abstract class AppModel extends CommonModel {

  private Map<String, String> mHeaders = new HashMap<>();

  {
    // TODO: 2017/6/4  这里存放一些不因程序运行改变的键值对
    // mHeaders.put("Content-Type", "text/json");
  }

  @Override protected Map<String, String> getPublicParams(Map<String, String> params) {
    if (params == null) params = new HashMap<>();
    // TODO: 2017/6/4 存放每次请求都需要带上的参数
    return params;
  }

  @Override protected Map<String, String> getHeaders() {
    // TODO: 2017/6/4 方法内部放一些程序运行因素改变的键值对
    return mHeaders;
  }
}
