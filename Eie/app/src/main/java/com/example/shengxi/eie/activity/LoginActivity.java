package com.example.shengxi.eie.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.beans.students.StudentBean;
import com.example.shengxi.eie.beans.students.Users;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by ShengXi on 2017/4/19.
 */

public class LoginActivity extends BaseActivity{

    @BindView(R.id.et_username)
    EditText userId;

    @BindView(R.id.et_password)
    EditText passWord;

    @BindView(R.id.tv_regist)
    TextView registerTv;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.login_back)
    ImageView loginBack;

    static String LOGIN_FAILED = "FAILED";
    static String LOGIN_SUCCEEDED = "SUCCESS";
    private final static int SUCCESS = 111;
    private final static int FAILURE = 222;
    private String id;
    private String password;
    private NetUtils netUtils  = new NetUtils();
    private StudentBean studentBean;
    private DataBaseHelper dbHelper;

    Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {
        @Override
        public void callBack(Message msg) {
            // dialog.dismiss();
            if (msg.what == SUCCESS) {  // 处理发送线程传回的消息
                setDataToSql();
                finish();
            }else if (msg.what == FAILURE){
                Toast.makeText(LoginActivity.this, "账号和密码不匹配", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected int getViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        dbHelper = new DataBaseHelper(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void setData() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtils.netState(LoginActivity.this)) { //检查网络
                    if (userId.getText().toString().equals(""))
                        Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    else {
                        id = userId.getText().toString();
                        password = passWord.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result;
                                if(!id.equals("")) {
                                    // 发送数据，获取对象
                                    result = netUtils.loginByPost(getUserInfo(), DataUtils.LoginUrl);
                                    if(!"".equals(result)){
                                        studentBean = parseGson(result);
                                        //Log.e("登陆成功：",studentBean.student.get(0).sId);
                                        Log.i("tag", "登陆成功");
                                        handler.sendEmptyMessage(SUCCESS);
                                    }else{
                                        Log.i("tag", "登陆失败");
                                        handler.sendEmptyMessage(FAILURE);
                                    }
                                }
                            }
                        }).start();
                        //new LoginPostThread(userId.getText().toString(), passWord.getText().toString()).start();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "网络未连接", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(in);
            }
        });

        loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getUserInfo() {

        Gson gson = new Gson();
        List<Users> data = new ArrayList<>();
        Users users = new Users();
        users.setSid(id);
        users.setPassWord(netUtils.encode(password));
        data.add(users);
        return "{\"students\":" + gson.toJson(data) + " }";
    }

    private StudentBean parseGson(String result) {

        Gson gson = new Gson();
        return gson.fromJson(result,StudentBean.class);
    }

    private void setDataToSql() {

        Observable<Boolean> observable =Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                ContentValues v = new ContentValues();
                v.put("id",studentBean.student.get(0).sId);
                v.put("name",studentBean.student.get(0).sName);
                v.put("img",studentBean.student.get(0).sImg);
                Long i =db.insert("student",null,v);
                db.close();
                subscriber.onNext(i==0);
            }
        });
        Subscriber<Boolean> studentBeanSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean studentBean) {
                Log.d("setdata to sqlite is?",studentBean.toString());

            }
        };
        observable.subscribe(studentBeanSubscriber);


    }

}
