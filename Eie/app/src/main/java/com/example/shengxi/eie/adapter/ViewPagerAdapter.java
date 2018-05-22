package com.example.shengxi.eie.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.squareup.picasso.Picasso;

/**
 *
 * Created by ShengXi on 2018-05-14.
 */

public  class ViewPagerAdapter extends StaticPagerAdapter{

    private ClassMenuBean data;
    public ViewPagerAdapter(ClassMenuBean data){
        this.data= data;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.with(container.getContext())
                .load(DataUtils.baseUrl+data.classes.get(position).img)
                .placeholder(R.mipmap.ic_22)
                .into(imageView);
        CardView cardView = new CardView(container.getContext());
        cardView.setRadius(2);
        cardView.setElevation(3);
        cardView.addView(imageView);
        return cardView;
    }

    @Override
    public int getCount() {
        return data.classes.size();
    }
}
