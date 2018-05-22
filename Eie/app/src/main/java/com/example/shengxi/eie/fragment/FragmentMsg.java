package com.example.shengxi.eie.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.ForumInfoActivity;
import com.example.shengxi.eie.activity.ForumPostActivity;
import com.example.shengxi.eie.adapter.ForumAdapter;
import com.example.shengxi.eie.beans.ForumBean;

import com.example.shengxi.eie.network.ApiService;
import com.example.shengxi.eie.network.manager.RetrofitNetManager;

import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 实现获取论坛界面
 * Created by ShengXi on 2017/4/12.
 */

public class FragmentMsg extends Fragment {

    @BindView(R.id.forum_pullListView)
    PullToRefreshListView reList;

    @BindView(R.id.forum_add)
    ImageView addImageView;

    Unbinder butterKnife;
    private static ForumBean mForumBean = null;
    private ForumAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum,null);
        butterKnife = ButterKnife.bind(this,view);
        //reList = (PullToRefreshListView) view.findViewById(R.id.forum_pullListView);
        //addImageView = (ImageView) view.findViewById(R.id.forum_add);
        initView();
        if (NetUtils.netState(getActivity())) {
            gotoThread();
        } else {
            Toast.makeText(getActivity(), "无网络连接。。。", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void initView() {

        reList.setMode(PullToRefreshBase.Mode.BOTH);
        reList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.w("CallBackTransData", mForumBean.forum.get(i - 1).articleTitle);
                Intent in = new Intent(getActivity(), ForumInfoActivity.class);
                String[] aryy = {mForumBean.forum.get(i - 1).articleUpName,
                        mForumBean.forum.get(i - 1).articleUpImg, mForumBean.forum.get(i - 1).articleMain,
                        mForumBean.forum.get(i - 1).articleTime, mForumBean.forum.get(i - 1).articleId};
                in.putExtra("data", aryy);
                startActivity(in);

            }
        });

        reList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (NetUtils.netState(getActivity())) {
                    gotoThread();
                } else {
                    Toast.makeText(getActivity(), "无网络连接。。。", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });


        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), ForumPostActivity.class);
                startActivity(in);
            }
        });
    }
    private void gotoThread() {

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;utf-8"), "10");
        Log.e("thread0", "start new thread");
        ApiService service = RetrofitNetManager.getInstance().getForumService(getActivity());
        service.getForum(requestBody)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, ForumBean>() {
                    @Override
                    public ForumBean call(ResponseBody responseBody) {
                        return getForumBean(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ForumBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ForumBean forumBean) {
                        mForumBean = forumBean;
                        adapter = new ForumAdapter(getActivity(), forumBean);
                        reList.setAdapter(adapter);
                        reList.onRefreshComplete();
                    }
                });
    }

    private ForumBean getForumBean(ResponseBody responseBody) {
        Log.e("thread1", "start new thread");
        Log.d("res", responseBody.toString());
        BufferedReader br = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        String line;
        StringBuilder stringBuffer = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(stringBuffer.toString(), ForumBean.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();

    }
}
