package com.example.shengxi.eie.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.LoginActivity;
import com.example.shengxi.eie.activity.SettingActivity;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.FastBlurUtil;

/**
 * Created by ShengXi on 2017/4/12.
 */

public class FragmenUser extends Fragment {

    private CircleImageView userView;
    private ImageView userBg;
    private Button btnLogout;
    private TextView userName;
    DataBaseHelper dbHelper;
    RequestQueue queue;
    String sid;
    String sname;
    String simg;
    Bitmap blurBitmap2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                userBg.setScaleType(ImageView.ScaleType.FIT_XY);
                userBg.setImageBitmap(blurBitmap2);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (isEmpty()){
            getSqlData();
            setDataToUI();
        }else{
            userName.setText("");
            userView.setImageResource(R.mipmap.ico_user_default);
            userBg.setImageResource(R.color.gray_20);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = inflater.inflate(R.layout.fragment_user, null);
        }
        initView(view);
        if (isEmpty()){
            getSqlData();
            setDataToUI();
        }

        return  view;
}

    private void setDataToUI() {
        userName.setText(sname);
        ImageRequest request = new ImageRequest(DataUtils.baseUrl + simg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                userView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                blurBitmap2 = FastBlurUtil.GetUrlBitmap(DataUtils.baseUrl + simg, 10);
                handler.sendEmptyMessage(1);

            }
        }).start();

    }

    private void getSqlData() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("select *from student",null);
        while (cur.moveToNext()){
            sname = cur.getString(cur.getColumnIndex("name"));
            simg = cur.getString(cur.getColumnIndex("img"));
        }
        db.close();


    }
    private boolean isEmpty() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor curser = db.rawQuery("select *from student ",null);
        int i = 0;
        while (curser.moveToNext()) {
            i++;
        }
        curser.close();
        db.close();
        if (i == 0) {
            return false;
        } else {
            return true;
        }
    }


    private void initView(View view) {
        userView = (CircleImageView) view.findViewById(R.id.user_avatar_view);
        userBg = (ImageView) view.findViewById(R.id.user_bg);
        userName = (TextView) view.findViewById(R.id.user_name);
        btnLogout = (Button) view.findViewById(R.id.btn_logout);
        dbHelper = new DataBaseHelper(getActivity());
        queue = Volley.newRequestQueue(getActivity());


        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询本地数据库判断是否登陆
                if (!isEmpty()){
                    Intent in = new Intent(getActivity(), LoginActivity.class);
                    FragmenUser.this.startActivity(in);
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent in = new Intent(getActivity(), SettingActivity.class);
//                startActivity(in);
               deleteSQL();

               onResume();

            }
        });

    }

    public void deleteSQL() {
        String sql = "delete from student";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL(sql);
        db.close();
    }
}
