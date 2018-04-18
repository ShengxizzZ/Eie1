package com.example.shengxi.eie.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.utils.DataBaseHelper;

/**
 * Created by ShengXi on 2017/5/12.
 */

public class SettingActivity extends AppCompatActivity{
    private Button btnLogOut;
    private  DataBaseHelper dataBaseHelper;
    private String sname;
    private String sid;
    private String simg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        dataBaseHelper = new DataBaseHelper(this);
        btnLogOut = (Button) findViewById(R.id.setting_btn_logout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSQL();
                finish();
            }
        });
    }

    public void deleteSQL() {
        String sql = "delete from student";
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        db.execSQL(sql);
        db.close();
    }
}
