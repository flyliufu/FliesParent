package com.lefu8.fliesparent;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.lefu8.flies.util.LogUtils;
import com.lefu8.fliesparent.bean.UserBean;
import com.lefu8.fliesparent.frame.JSONEntity;
import com.lefu8.fliesparent.frame.ObserverImpl;
import com.lefu8.fliesparent.model.UserModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import rx.Observer;
import rx.Subscription;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private UserModel mUserModel = new UserModel();
  private HashMap<String, Subscription> mSubscriptions = new HashMap<>();

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
  private Observer<File> mDownloadFileObserver = new Observer<File>() {
    @Override public void onCompleted() {

    }

    @Override public void onError(Throwable e) {
      mTvName.setText(e.getMessage());
    }

    @Override public void onNext(File file) {
      mTvName.setText(file.getAbsolutePath());
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
    findViewById(R.id.btn_request_file_download).setOnClickListener(this);
    findViewById(R.id.btn_request_file_upload).setOnClickListener(this);
    mTvName = (TextView) findViewById(R.id.tv_user_name);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_request_timeout:
        // 防止重复请求对界面造成影响，设置请求缓存机制
        Subscription timeoutSub = mSubscriptions.get("timeoutSub");
        if (timeoutSub != null && !timeoutSub.isUnsubscribed()) {
          LogUtils.e("unsubscribe true ");
          // 如果上次请求没结束解绑上次请求
          timeoutSub.unsubscribe();
        }
        // 重新绑定请求
        timeoutSub = mUserModel.timeout(mTimeoutObserver);
        // 再次设置缓存
        mSubscriptions.put("timeoutSub", timeoutSub);
        break;
      case R.id.btn_request_list:
        Subscription getUserSub = mSubscriptions.get("getUserSub");
        if (getUserSub != null && !getUserSub.isUnsubscribed()) {
          LogUtils.e("unsubscribe true ");
          getUserSub.unsubscribe();
        }
        getUserSub = mUserModel.getUserList(mGetUserListObserver);
        mSubscriptions.put("getUserSub", getUserSub);
        break;
      case R.id.btn_request_file_download:
        mUserModel.fileDownload(null, mDownloadFileObserver);
        break;
      case R.id.btn_request_file_upload:
        // 设置缓存功能略。。。
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "test.jpg");
        if (file.exists()) {
          Map<String, String> param = new HashMap<>();
          mUserModel.fileUpload(file.getAbsolutePath(), param, mFileUploadObserver);
        }
        break;
      case R.id.btn_request_object:
        // 设置缓存功能略。。。
        mUserModel.getUser(mGetUserObserver);
        break;
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    // 在 Activity 销毁后解绑还未结束的请求。
    for (Subscription sub : mSubscriptions.values()) {
      if (sub != null && !sub.isUnsubscribed()) {
        sub.unsubscribe();
      }
    }
    // 置空缓存变量
    mSubscriptions = null;
  }
}
