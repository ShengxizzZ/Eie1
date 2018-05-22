package com.example.shengxi.eie.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.CategoryActivity;
import com.example.shengxi.eie.activity.RepresentActivity;
import com.example.shengxi.eie.adapter.BaseClassAdapter;
import com.example.shengxi.eie.adapter.RepresentAdapter;
import com.example.shengxi.eie.beans.RepresentBean;
import com.example.shengxi.eie.network.manager.RetrofitNetManager;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.MyGridView;
import com.example.shengxi.eie.utils.NetUtils;
import com.example.shengxi.eie.utils.RecyclerViewTouchListener;
import com.google.gson.Gson;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by ShengXi on 2017/4/12.
 */

public class FragmentCom extends Fragment {


    @BindView(R.id.com_class_list)
     MyGridView classGridView;

    @BindView(R.id.fra_com_list)
    RecyclerView teacherGridView;

    private NetUtils utils;
    private RepresentBean representBean;

    private boolean mIsTheTiltleVisible = false;
    private boolean mIsTheTitleContanierCisible = true;
    private String result;
    String[] Title = new String[]{"最近","热门"};


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                Toast.makeText(getActivity(),"获取数据成功",Toast.LENGTH_SHORT).show();


            }else if(msg.what==2){
                Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
            }

        }
    };

    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_com,null);
        unbinder = ButterKnife.bind(this,view);
        initView(view);
        setData();
        gotoThreadGetRepre();
        return view;

    }


    private void gotoThreadGetRepre() {

        Observable<ResponseBody> observable = RetrofitNetManager.getInstance()
                .getForumService(getActivity())
                .getRepresents();
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, RepresentBean>() {
                    @Override
                    public RepresentBean call(ResponseBody responseBody) {
                        return new Gson().fromJson(NetUtils.getResponseBody(responseBody),RepresentBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RepresentBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RepresentBean representBean) {
                        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(),3);
                        RepresentAdapter adapter = new RepresentAdapter(getActivity(),representBean);
                        teacherGridView.setLayoutManager(lm);
                        teacherGridView.setAdapter(adapter);
                    }
                });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                result = utils.getData(DataUtils.RepresentsUrl);
//               // Log.w("Represent:",result);
//                if (result!=null){
//                    representBean = paraseGson(result);
//                    handler.sendEmptyMessage(1);
//                }else{
//                    handler.sendEmptyMessage(2);
//                }
//
//            }
//        }).start();
    }

    private RepresentBean paraseGson(String result) {
        Gson gson = new Gson();
        return gson.fromJson(result,RepresentBean.class);
    }


    private void initView(View view) {


        utils = new NetUtils();

        //classGridView = (MyGridView) view.findViewById(R.id.com_class_list);
        //teacherGridView = (MyGridView) view.findViewById(R.id.com_list);

//        teacherGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent in = new Intent(getActivity(), RepresentActivity.class);
//                String[] aryy ={representBean.represents.get(i).representTitle,
//                        representBean.represents.get(i).representText,representBean.represents.get(i).representImg,
//                        };
//                in.putExtra("data",aryy);
//                startActivity(in);
//            }
//        });
        teacherGridView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),teacherGridView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                int i = viewHolder.getAdapterPosition();
                Intent in = new Intent(getActivity(), RepresentActivity.class);
                String[] aryy ={representBean.represents.get(i).representTitle,
                        representBean.represents.get(i).representText,representBean.represents.get(i).representImg,
                        };
                in.putExtra("data",aryy);
                startActivity(in);
            }

            @Override
            public void onLongClick(RecyclerView.ViewHolder viewHolder) {

            }
        });

        classGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent in = new Intent(getActivity(), CategoryActivity.class);
                String str = getCategory(i);
                in.putExtra("data",str);
                startActivity(in);
            }
        });


        //appbar监听
//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                int maxScroll = appBarLayout.getTotalScrollRange();
//                float percentage = (float) Math.abs(verticalOffset)/(float)maxScroll;
//                handleAlphaOnTitle(percentage);
//                handleToolbarTitleVisibility(percentage);
//            }
//        });
        //initParallaxValues();
    }

//    private void initParallaxValues() {
//        CollapsingToolbarLayout.LayoutParams pet = (CollapsingToolbarLayout.LayoutParams) imageView.getLayoutParams();
//        CollapsingToolbarLayout.LayoutParams backgroud = (CollapsingToolbarLayout.LayoutParams) frameLayout.getLayoutParams();
//        pet.setParallaxMultiplier(0.9f );
//        backgroud.setParallaxMultiplier(0.3f);
//        imageView.setLayoutParams(pet);
//        frameLayout.setLayoutParams(backgroud);
//
//    }

//    private void handleToolbarTitleVisibility(float percentage) {
//
//        mTvToolbarTile.setText("你好");
//        if (percentage>= 0.3f){
//
//            if (!mIsTheTiltleVisible){
//                startAnimation(mToolbar,200,View.VISIBLE);
//                mIsTheTiltleVisible = true;
//            }
//            mTvToolbarTile.setText("你好");
//        }else{
//
//            if (mIsTheTiltleVisible){
//                startAnimation(mToolbar,200,View.INVISIBLE);
//                mIsTheTiltleVisible = false;
//            }
//        }
//    }
//
//    private void handleAlphaOnTitle(float percentage) {
//
//        if (percentage>= 0.9f){
//            if (mIsTheTitleContanierCisible){
//                startAnimation(mLinearLayout,200,View.INVISIBLE);
//                mIsTheTitleContanierCisible = false;
//            }
//
//        }else {
//            if (!mIsTheTitleContanierCisible) {
//                startAnimation(mLinearLayout, 200, View.VISIBLE);
//                mIsTheTitleContanierCisible = true;
//            }
//        }
//    }
//
//    private void startAnimation(View mLinearLayout, int i, int visible) {
//
//        AlphaAnimation alphaAnimation = (visible == View.VISIBLE)?new AlphaAnimation(0f,1f):new AlphaAnimation(1f,0f);
//        alphaAnimation.setDuration(i);
//        alphaAnimation.setFillAfter(true);
//        mLinearLayout.startAnimation(alphaAnimation);
//
//    }

    private String getCategory(int i) {

        switch (i){
            case 0:
                return DataUtils.CATEONE;
            case 1:
                return DataUtils.CATETWO;
            case 2:
                return DataUtils.CATETHREE;
            case 3:
                return DataUtils.CATEFOUR;
            case 4:
                return DataUtils.CATEFIVE;
            case 5:
                return DataUtils.CATESIX;
        }
        return null;
    }


    private void setData() {
        BaseClassAdapter classListAdapter = new BaseClassAdapter(getActivity());
        classGridView.setAdapter(classListAdapter);
    }

    private class BaseFragmentAdapter extends FragmentPagerAdapter{

        private Context context;
        private String[] temp;
        private List<android.support.v4.app.Fragment> mfFragments;

        public BaseFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public BaseFragmentAdapter(FragmentManager fragmentManager, List<android.support.v4.app.Fragment> mFragment, String[] title) {
            super(fragmentManager);
            mfFragments = mFragment;
            temp = title;
        }

        @Override
        public int getCount() {
            return temp.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mfFragments.get(position);
        }

    }


}
