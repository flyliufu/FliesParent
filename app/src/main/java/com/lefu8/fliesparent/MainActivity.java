package com.lefu8.fliesparent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.lefu8.flies.response.SimpleObserver;
import com.lefu8.fliesparent.bean.UserBean;
import com.lefu8.fliesparent.frame.JSONEntity;
import com.lefu8.fliesparent.frame.ObserverImpl;
import com.lefu8.fliesparent.model.UserModel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private UserModel mUserModel = new UserModel();
  private ObserverImpl<JSONEntity<UserBean>> mGetUserObserver =
      new ObserverImpl<JSONEntity<UserBean>>() {
        @Override public void onError(Throwable e) {
          mTvName.setText(e.getMessage());
        }

        @Override protected void onParse(JSONEntity<UserBean> entity) {
          mTvName.setText(entity.getObject().getName());
        }
      };
  private TextView mTvName;
  private ObserverImpl<JSONEntity<ArrayList<UserBean>>> mGetUserListObserver =
      new ObserverImpl<JSONEntity<ArrayList<UserBean>>>() {
        @Override public void onError(Throwable e) {
          mTvName.setText(e.getMessage());
        }

        @Override protected void onParse(JSONEntity<ArrayList<UserBean>> entity) {
          mTvName.setText("一共有：" + entity.getObject().size() + "位用户。");
        }
      };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();
  }

  private void initView() {
    findViewById(R.id.btn_request_object).setOnClickListener(this);
    findViewById(R.id.btn_request_list).setOnClickListener(this);
    mTvName = (TextView) findViewById(R.id.tv_user_name);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_request_list:
        mUserModel.getUserList(mGetUserListObserver);
        break;
      case R.id.btn_request_object:
        mUserModel.getUser(mGetUserObserver);
        break;
    }
  }
}
