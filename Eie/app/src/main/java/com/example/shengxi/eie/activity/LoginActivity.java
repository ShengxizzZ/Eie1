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
import com.example.shengxi.eie.beans.students.StudentBean;
import com.example.shengxi.eie.beans.students.Users;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengXi on 2017/4/19.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText userId;
    private EditText passWord;
    private TextView registerTv;
    private Button btnLogin;
    private ImageView loginBack;
    static String LOGIN_FAILED = "FAILED";
    static String LOGIN_SUCCEEDED = "SUCCESS";
    private String id;
    private String password;
    private NetUtils netUtils  = new NetUtils();
    private Handler handler;
    private StudentBean studentBean;
    private DataBaseHelper dbHelper;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DataBaseHelper(this);
        initView();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
               // dialog.dismiss();
                if (msg.what == 111) {  // 处理发送线程传回的消息

                   setDataToSql();
                    finish();
                }else if (msg.what == 222){

                    Toast.makeText(LoginActivity.this, "账号和密码不匹配", Toast.LENGTH_SHORT).show();
                }
            }};

    }

    private void setDataToSql() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues v = new ContentValues();
        v.put("id",studentBean.student.get(0).sId);
        v.put("name",studentBean.student.get(0).sName);
        v.put("img",studentBean.student.get(0).sImg);
        db.insert("student",null,v);
        db.close();
    }


    private void initView() {
        registerTv = (TextView) findViewById(R.id.tv_regist);
        userId = (EditText) findViewById(R.id.et_username);
        loginBack = (ImageView) findViewById(R.id.login_back);
        passWord = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (netUtils.netState(LoginActivity.this)) { //检查网络
                    if (userId.getText().toString().equals(""))
                        Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
                    else {
                        id = userId.getText().toString();
                        password = passWord.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = "";
                                if(!id.equals("")) {
                                    // 发送数据，获取对象
                                    result = netUtils.loginByPost(getUserInfo(), DataUtils.LoginUrl);
                                    if(!"".equals(result)){
                                        studentBean = parseGson(result);
                                        //Log.e("登陆成功：",studentBean.student.get(0).sId);
                                        Log.i("tag", "登陆成功");
                                        handler.sendEmptyMessage(111);
                                    }else{
                                        Log.i("tag", "登陆失败");
                                        handler.sendEmptyMessage(222);
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
}
