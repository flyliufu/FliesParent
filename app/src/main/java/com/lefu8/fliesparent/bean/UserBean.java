package com.lefu8.fliesparent.bean;

import java.io.Serializable;

/**
 * @author liufu on 2017/6/4.
 */

public class UserBean implements Serializable {
  private String name;
  private String sex;
  private String age;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }
}
