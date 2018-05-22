package com.example.shengxi.eie.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.SplashActivity;
import com.example.shengxi.eie.utils.DataUtils;
import com.squareup.picasso.Picasso;

/**
 *
 * Created by ShengXi on 2018-05-05.
 */

public class FragmentSplah extends Fragment {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_splash, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.splash_imageview);

        Picasso.with(getActivity())
                .load(DataUtils.baseUrl + "/AppSwevlet/img/g01.jpg")
                .into(imageView);

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
