package com.lefu8.fliesparent;

import android.content.Context;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lefu8.flies.response.SimpleObserver;
import com.lefu8.flies.util.LogUtils;
import com.lefu8.fliesparent.bean.UserBean;
import com.lefu8.fliesparent.frame.JSONEntity;
import com.lefu8.fliesparent.frame.ObserverImpl;
import com.lefu8.fliesparent.model.UserModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private UserModel mUserModel = new UserModel();

  private ObserverImpl<JSONEntity<String>> mFileUploadObserver =
      new ObserverImpl<JSONEntity<String>>() {
        @Override public void onError(Throwable e) {
          mTvName.setText(e.getMessage());
        }

        @Override protected void onParse(JSONEntity<String> entity) {
          mTvName.setText(entity.getMsg());
        }
      };

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
  private ObserverImpl<JSONEntity<UserBean>> mTimeoutObserver =
      new ObserverImpl<JSONEntity<UserBean>>() {
        @Override protected void onParse(JSONEntity<UserBean> entity) {
          LogUtils.i("Response success");
          if (!"0000".equals(entity.getCode())) {
            LogUtils.w("Result is failed");
            Toast.makeText(getBaseContext(), entity.getMsg(), Toast.LENGTH_LONG).show();
          }
        }
      };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initView();
  }

  private void initView() {
    findViewById(R.id.btn_request_object).setOnClickListener(this);
    findViewById(R.id.btn_request_timeout).setOnClickListener(this);
    findViewById(R.id.btn_request_list).setOnClickListener(this);
    findViewById(R.id.btn_request_file_upload).setOnClickListener(this);
    mTvName = (TextView) findViewById(R.id.tv_user_name);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_request_timeout:
        mUserModel.timeout(mTimeoutObserver);
        break;
      case R.id.btn_request_list:
        mUserModel.getUserList(mGetUserListObserver);
        break;
      case R.id.btn_request_file_upload:
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "test.jpg");
        if (file.exists()) {
          Map<String, String> param = new HashMap<>();
          param.put("name", "android user");
          mUserModel.fileUpload(file.getAbsolutePath(), param, mFileUploadObserver);
        }
        break;
      case R.id.btn_request_object:
        mUserModel.getUser(mGetUserObserver);
        break;
    }
  }
}
