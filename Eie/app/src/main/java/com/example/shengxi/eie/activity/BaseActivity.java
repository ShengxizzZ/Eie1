package com.example.shengxi.eie.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 *
 * Created by ShengXi on 2018-05-07.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initView();
        setData();
    }


    protected abstract int getViewId();
    protected abstract void initView();
    protected abstract void setData();
}
