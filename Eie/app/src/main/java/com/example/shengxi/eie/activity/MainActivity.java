package com.example.shengxi.eie.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.fragment.FragmenUser;
import com.example.shengxi.eie.fragment.FragmentCom;
import com.example.shengxi.eie.fragment.FragmentMsg;
import com.example.shengxi.eie.fragment.FragmentSplah;
import com.example.shengxi.eie.fragment.FragmentStudy;
import com.example.shengxi.eie.utils.BottomNavigationViewHelper;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.Vitamio;

public class MainActivity extends BaseActivity {

    private BottomNavigationView mBottomNavigationView;

    private FragmentManager frameManager;
    private FragmentStudy fragmentStudy;
    FragmentCom fragmentCom;
    FragmentMsg fragmentMsg;
    FragmenUser fragmentUser;

    android.app.Fragment fragments[];
    private FragmentSplah mFragmentSplah;
    private FragmentTransaction ft;
    private int lastShowFragment = 0;

    private Handler handler = new Handler();

    @Override
    protected int getViewId() {
        return R.layout.activity_viewtub;
    }

    @Override
    protected void initView() {
        Vitamio.initialize(this);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setSplashFragement();
        ViewStub viewStub = (ViewStub) findViewById(R.id.main_viewStub);
        viewStub.inflate();
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        initFragment();

    }

    @Override
    protected void setData() {
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        int[][] states = new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}};
        int[] colors = new int[]{getResources().getColor(R.color.gray_20), getResources().getColor(R.color.black_80)};
        ColorStateList csl = new ColorStateList(states, colors);
        mBottomNavigationView.setItemIconTintList(csl);
        mBottomNavigationView.setItemTextColor(csl);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_study:
                        if (lastShowFragment != 0) {
                            switchFragment(lastShowFragment, 0);
                            lastShowFragment = 0;
                        }
                        return true;
                    case R.id.item_com:
                        if (lastShowFragment != 1) {
                            switchFragment(lastShowFragment, 1);
                            lastShowFragment = 1;
                        }
                        return true;
                    case R.id.item_msg:
                        if (lastShowFragment != 2) {
                            switchFragment(lastShowFragment, 2);
                            lastShowFragment = 2;
                        }
                        return true;
                    case R.id.item_user:
                        if (lastShowFragment != 3) {
                            switchFragment(lastShowFragment, 3);
                            lastShowFragment = 3;
                        }
                        return true;
                }
                return false;
            }
        });

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                // 开启延迟加载,也可以不用延迟可以立马执行（我这里延迟是为了实现fragment里面的动画效果的耗时）
                handler.postDelayed(new DelayRunnable(MainActivity.this, mFragmentSplah), 4000);
            }
        });
    }

    private void switchFragment(int lastShowFragment, int i) {
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragments[lastShowFragment]);
        if (!fragments[i].isAdded()) {
            ft.add(R.id.fragment_container, fragments[i]);
        }
        ft.show(fragments[i]).commitAllowingStateLoss();
    }

    private void initFragment() {
        fragmentStudy = new FragmentStudy();
        fragmentCom = new FragmentCom();
        fragmentMsg = new FragmentMsg();
        fragmentUser = new FragmenUser();
        fragments = new android.app.Fragment[]{fragmentStudy, fragmentCom, fragmentMsg, fragmentUser};
        lastShowFragment = 0;
        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragmentStudy)
                .show(fragmentStudy)
                .commit();

    }

    /**
     * 设置fragment式splashActivity
     */
    private void setSplashFragement() {

        mFragmentSplah = new FragmentSplah();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, mFragmentSplah);
        ft.commit();
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

    static class DelayRunnable implements Runnable {

        private WeakReference<Context> contextWeakReference;
        private WeakReference<FragmentSplah> splashFragmentWeakReference;

        public DelayRunnable(Context context, FragmentSplah f) {
            contextWeakReference = new WeakReference<Context>(context);
            splashFragmentWeakReference = new WeakReference<FragmentSplah>(f);
        }

        @Override
        public void run() {
            //移除Fragment
            if (contextWeakReference != null) {
                FragmentSplah splashFragment = splashFragmentWeakReference.get();
                if (splashFragment == null) {
                    return;
                }
                FragmentActivity activity = (FragmentActivity) contextWeakReference.get();
                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.remove(splashFragment);
                transaction.commit();
            }

        }
    }
}
