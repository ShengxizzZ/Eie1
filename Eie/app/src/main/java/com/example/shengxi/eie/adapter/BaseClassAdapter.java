package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shengxi.eie.R;

/**
 * Created by ShengXi on 2017/5/7.
 */

public class BaseClassAdapter extends BaseAdapter{

    String classese[] = {"大学生","城镇失业人员","留学国人员","科研人员","农民工","退役军人"};

    private LayoutInflater inflater;

    public BaseClassAdapter(Context context){
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return classese.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder item;

        if (view == null){

            view = inflater.inflate(R.layout.item_class_list,null);
            item = new ViewHolder();
            item.icon = (ImageView) view.findViewById(R.id.grid_image);
            item.textView  = (TextView) view.findViewById(R.id.grid_info);
            view.setTag(item);


        }else{
            item = (ViewHolder) view.getTag();
        }
        item.textView.setText(classese[i]);
        item.icon.setBackgroundResource(getIcon(i));
        return view;
    }

    private int getIcon(int i) {

        switch (i){
            case 0:
                return R.mipmap.ic_com_unman;
            case 1:
                return R.mipmap.ic_com_citizen;
            case 2:
                return R.mipmap.ic_com_abord;
            case 3:
                return R.mipmap.ic_com_sr;
            case 4:
                return R.mipmap.ic_com_pw;
            case 5:
                return R.mipmap.ic_com_so;
        }
        return 0;
    }

    public class ViewHolder{

        public ImageView icon;
        public TextView textView;
    }


}
