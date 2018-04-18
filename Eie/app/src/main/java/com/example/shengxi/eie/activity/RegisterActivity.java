package com.example.shengxi.eie.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.beans.students.Users;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShengXi on 2017/5/12.
 */

public class RegisterActivity extends AppCompatActivity{

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILS = "FAILS";
    private TextView registerId;
    private TextView registerName;
    private TextView registerPassWord;
    private TextView registerComfirePassword;
    private Button btnRegister;
    private ImageView registerBack;
    private NetUtils utils;
    private String id;
    private String name;
    private String password;
    private String compassword;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1){
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                finish();
            }else if(msg.what == 2){
                Toast.makeText(RegisterActivity.this,"用户已经存在！",Toast.LENGTH_SHORT).show();
                registerComfirePassword.setText("");
                registerPassWord.setText("");
                registerId.setText("");
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

    }

    private void initView() {
        utils = new NetUtils();
        btnRegister = (Button) findViewById(R.id.register_ok);
        registerId = (TextView) findViewById(R.id.register_userid);
        registerName = (TextView) findViewById(R.id.register_username);
        registerPassWord = (TextView) findViewById(R.id.register_password);
        registerComfirePassword = (TextView) findViewById(R.id.register_com_password);
        registerBack = (ImageView) findViewById(R.id.register_back);
        registerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = registerId.getText().toString();
                name = registerName.getText().toString();
                password = registerPassWord.getText().toString();
                compassword = registerComfirePassword.getText().toString();

                if (("".equals(id)||"".equals(name)||"".equals(password)||"".equals(compassword))){

                    Toast.makeText(RegisterActivity.this,"有一行为空！",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("register","不为空");
                    if (password.equals(compassword)){
                        if (utils.netState(RegisterActivity.this)){
                            gotoThread();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"密码不相同！",Toast.LENGTH_SHORT).show();
                        registerPassWord.setText("");
                        registerComfirePassword.setText("");

                    }

                }
            }
        });
    }

    private void gotoThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String result = utils.loginByPost(getDataString(), DataUtils.RegisterUrl);
                if (!"".equals(result)){
                    if(SUCCESS.equals(result)){
                        handler.sendEmptyMessage(1);
                    }
                }else{
                    handler.sendEmptyMessage(2);
                }

            }
        }).start();

    }

    private String getDataString() {

        Gson gson = new Gson();
        List<Users> users = new ArrayList<>();
        Users user = new Users();
        user.setSid(id);
        user.setName(name);
        user.setPassWord(utils.encode(compassword));
        users.add(user);
        return "{\"register\":" + gson.toJson(users) + " }";
    }
}
