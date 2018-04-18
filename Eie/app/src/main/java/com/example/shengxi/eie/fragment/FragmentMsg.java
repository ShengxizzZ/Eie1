package com.example.shengxi.eie.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.ForumInfoActivity;
import com.example.shengxi.eie.activity.ForumPostActivity;
import com.example.shengxi.eie.adapter.BaseRecyclerViewAdapter;
import com.example.shengxi.eie.adapter.ForumAdapter;
import com.example.shengxi.eie.beans.ForumBean;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.MyRecyclerView;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import rx.schedulers.Schedulers;

/**
 * Created by ShengXi on 2017/4/12.
 */

public class FragmentMsg extends Fragment{

    private PullToRefreshListView reList;
    private ImageView addImageView;
    private NetUtils utils = new NetUtils();
    private static ForumBean forumBean;
    private ForumAdapter adapter;
    private static final String TIMES = "10";
    private Observable observable = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                adapter = new ForumAdapter(getActivity(),forumBean);
                reList.setAdapter(adapter);

            }else if (msg.what == 2){
                adapter.notifyDataSetChanged();
            }
            reList.onRefreshComplete();
        }
    };
    @Override
    public void onResume() {
        super.onResume();


    }

    private void gotoThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String data = utils.getRequestForum(TIMES, DataUtils.ForumUrl);
                //Log.e("forumData",data);
                if (data!=null){
                    forumBean = parseGson(data.trim());
                    //callBack.getData(forumBean);
                    handler.sendEmptyMessage(1);

                }
               // Log.e("testData",data);
            }
        }).start();
    }

    private ForumBean parseGson(String trim) {
        Gson gson = new Gson();
        return gson.fromJson(trim,ForumBean.class);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forum,null);
        //initView(view);
//        if (utils.netState(getActivity())){
//            gotoThread();
//        }else {
//            Toast.makeText(getActivity(),"无网络连接。。。",Toast.LENGTH_SHORT).show();
//        }

        observable = Observable.create(new () {
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        return view;
    }

    private void initView(View view) {
        //reList = (PullToRefreshListView) view.findViewById(R.id.forum_pullListView);
        //addImageView = (ImageView) view.findViewById(R.id.forum_add);
        reList.setMode(PullToRefreshBase.Mode.BOTH);
        reList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.w("CallBackTransData",forumBean.forum.get(i-1).articleTitle);
                Intent in = new Intent(getActivity(), ForumInfoActivity.class);
                String[] aryy ={forumBean.forum.get(i-1).articleUpName,
                forumBean.forum.get(i-1).articleUpImg,forumBean.forum.get(i-1).articleMain,
                forumBean.forum.get(i-1).articleTime,forumBean.forum.get(i-1).articleId};
                in.putExtra("data",aryy);
                startActivity(in);

            }
        });

        reList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (utils.netState(getActivity())){
                    gotoThread();
                }else {
                    Toast.makeText(getActivity(),"无网络连接。。。",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if (utils.netState(getActivity())){
                    gotoThread();
                }else {
                    Toast.makeText(getActivity(),"无网络连接。。。",Toast.LENGTH_SHORT).show();
                }
                handler.sendEmptyMessage(2);
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

    public interface CallBack{
         void getData(ForumBean dataBean);

    }
    private CallBack callBack;
    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }


//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        callBack = (CallBack) activity;
//    }
}
