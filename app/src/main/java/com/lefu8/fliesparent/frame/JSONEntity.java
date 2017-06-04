package com.lefu8.fliesparent.frame;

import java.io.Serializable;

/**
 * @author liufu on 2017/6/4.
 */

public class JSONEntity<T> implements Serializable {
  private String msg;
  private String code;
  private T object;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }
}
