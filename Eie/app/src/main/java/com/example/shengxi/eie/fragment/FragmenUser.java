package com.example.shengxi.eie.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.LoginActivity;
import com.example.shengxi.eie.base.BaseHandler;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataBaseHelper;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.FastBlurUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by ShengXi on 2017/4/12.
 */

public class FragmenUser extends Fragment {

    @BindView(R.id.user_avatar_view)
    CircleImageView userView;

    @BindView(R.id.user_bg)
    ImageView userBg;

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @BindView(R.id.user_name)
    TextView userName;

    Unbinder unbinder;
    DataBaseHelper dbHelper;
    String sname;
    String simg;
    Bitmap blurBitmap2;
    Handler handler = new BaseHandler<>(new BaseHandler.BaseHandlerCallBack() {
        @Override
        public void callBack(Message msg) {
            if (msg.what == 1) {

            }
        }
    });

    @Override
    public void onResume() {
        super.onResume();
        if (isEmpty()) {
            getSqlData();
            setDataToUI();
        } else {
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
        unbinder = ButterKnife.bind(this, view);
        initView();
        if (isEmpty()) {
            getSqlData();
            setDataToUI();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void setDataToUI() {
        userName.setText(sname);
        Picasso.with(getActivity())
                .load(DataUtils.baseUrl + simg)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(userView);

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(DataUtils.baseUrl+simg);
            }
        }).map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String url) {
                return FastBlurUtil.GetUrlBitmap(url, 10);//地址和透明效果
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        userBg.setScaleType(ImageView.ScaleType.FIT_XY);
                        userBg.setImageBitmap(blurBitmap2);
                    }
                });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                blurBitmap2 =
//                handler.sendEmptyMessage(1);
//
//            }
//        }).start();
    }

    private void getSqlData() {

        Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                String data[] = new String[2];
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cur = db.rawQuery("select *from student", null);
                while (cur.moveToNext()) {
                    data[0] = cur.getString(cur.getColumnIndex("name"));
                    data[1] = cur.getString(cur.getColumnIndex("img"));
                }
                cur.close();
                db.close();
                subscriber.onNext(data);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String[]>() {
                    @Override
                    public void onCompleted() {
                        Log.e("completed", "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String[] strings) {
                        sname = strings[0];
                        simg = strings[1];
                    }
                });

    }

    private boolean isEmpty() {
        final int[] i = new int[1];
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor curser = db.rawQuery("select *from student ", null);
                int i = 0;
                while (curser.moveToNext()) {
                    i++;
                }
                curser.close();
                db.close();
                subscriber.onNext(i);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer aBoolean) {
                        i[0] = aBoolean;
                    }
                });
        return (i[0] == 0);
    }

    private void initView() {
        dbHelper = new DataBaseHelper(getActivity());
        userView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询本地数据库判断是否登陆
                if (!isEmpty()) {
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
