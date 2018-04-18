package com.example.shengxi.eie.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.fragment.FragmenUser;
import com.example.shengxi.eie.fragment.FragmentCom;
import com.example.shengxi.eie.fragment.FragmentMsg;
import com.example.shengxi.eie.fragment.FragmentStudy;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabStudy;
    private TextView tabCom;
    private TextView tabMsg;
    private TextView tabUser;

    private FrameLayout contaner;
    private FragmentManager frameManager;
    private FragmentStudy fragmentStudy;

    FragmentCom fragmentCom;
    FragmentMsg fragmentMsg;
    FragmenUser fragmentUser;

    Fragment fragment;

    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        setFirstFragment();
    }

    private void setFirstFragment() {

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        selected();
        tabStudy.setSelected(true);
        if (fragmentStudy == null) {
            fragmentStudy = new FragmentStudy();
            transaction.add(R.id.fragment_container, fragmentStudy);
        } else {
            transaction.show(fragmentStudy);
        }
        transaction.commit();
    }

    private void initView() {

        tabStudy = (TextView) findViewById(R.id.txt_sutudy);
        tabCom = (TextView) findViewById(R.id.txt_com);
        tabMsg = (TextView) findViewById(R.id.txt_msg);
        tabUser = (TextView) findViewById(R.id.txt_user);
        contaner = (FrameLayout) findViewById(R.id.fragment_container);


        tabStudy.setOnClickListener(this);
        tabCom.setOnClickListener(this);
        tabMsg.setOnClickListener(this);
        tabUser.setOnClickListener(this);
    }

    public void selected() {
        tabStudy.setSelected(false);
        tabCom.setSelected(false);
        tabMsg.setSelected(false);
        tabUser.setSelected(false);
    }

    public void hideAllFragment(android.app.FragmentTransaction transaction) {
        if (fragmentStudy != null) {
            transaction.hide(fragmentStudy);
        }
        if (fragmentCom != null) {
            transaction.hide(fragmentCom);
        }
        if (fragmentMsg != null) {
            transaction.hide(fragmentMsg);
        }
        if (fragmentUser != null) {
            transaction.hide(fragmentUser);
        }
    }

    @Override
    public void onClick(View view) {

        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);

        switch (view.getId()) {
            case R.id.txt_sutudy:
                selected();
                tabStudy.setSelected(true);
                if (fragmentStudy == null) {
                    fragmentStudy = new FragmentStudy();
                    transaction.add(R.id.fragment_container, fragmentStudy);
                } else {
                    transaction.show(fragmentStudy);
                }
                break;

            case R.id.txt_com:
                selected();
                tabCom.setSelected(true);
                if (fragmentCom == null) {
                    fragmentCom = new FragmentCom();
                    transaction.add(R.id.fragment_container, fragmentCom);
                } else {
                    transaction.show(fragmentCom);
                }
                break;

            case R.id.txt_msg:
                selected();
                tabMsg.setSelected(true);
                if (fragmentMsg == null) {
                    fragmentMsg = new FragmentMsg();
                    transaction.add(R.id.fragment_container, fragmentMsg);
                } else {
                    transaction.show(fragmentMsg);
                }
                break;

            case R.id.txt_user:
                selected();
                tabUser.setSelected(true);
                if (fragmentUser == null) {
                    fragmentUser = new FragmenUser();
                    transaction.add(R.id.fragment_container, fragmentUser);
                } else {
                    transaction.show(fragmentUser);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 返回键功能
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        exitBy2Click(); // 调用双击退出函数
        return false;
    }

    private static Boolean isExit = false;// 退出标识

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID
            System.exit(0);
        }
    }
}
