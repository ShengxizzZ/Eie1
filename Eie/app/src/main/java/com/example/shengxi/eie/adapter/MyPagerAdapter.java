package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shengxi.eie.activity.VideoTeachActivity;
import com.example.shengxi.eie.beans.ClassMenuBean;

import java.util.List;

/**
 * Created by ShengXi on 2017/4/20.
 */

public class MyPagerAdapter extends PagerAdapter{

    List<ImageView>list;
    Context context;
    ClassMenuBean data;
    public MyPagerAdapter(List<ImageView> mImageViewList, ClassMenuBean data, Context context) {
        this.list = mImageViewList;
        this.context = context;
        this.data = data;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView=list.get(position);
        container.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String []arr = {data.classes.get(position-2).teacher,data.classes.get(position-2).title,data.classes.get(position-2).summary,data.classes.get(position-2).url,data.classes.get(position-2).id};
                Intent in = new Intent(context, VideoTeachActivity.class);
                in.putExtra("data",arr);
                context.startActivity(in);
                Log.e("监听","点击了"+position);
            }
        });
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
