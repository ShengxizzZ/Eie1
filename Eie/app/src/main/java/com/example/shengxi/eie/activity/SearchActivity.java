package com.example.shengxi.eie.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shengxi.eie.R;
import com.example.shengxi.eie.adapter.CategoryAdapter;
import com.example.shengxi.eie.adapter.SearchAdapter;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.network.manager.RetrofitNetManager;
import com.example.shengxi.eie.utils.NetUtils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *
 * Created by ShengXi on 2018-05-12.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private SearchAdapter searchAdapter;

    @BindView(R.id.ac_search_et)
    EditText mEd_Search;

    @BindView(R.id.ac_search_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_tv)
    TextView tv;

    @Override
    protected int getViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        searchAdapter = new SearchAdapter(this);

    }


    @Override
    protected void setData() {


//        mEd_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND
//                        ||actionId == EditorInfo.IME_ACTION_DONE
//                        ||(event!=null&&KeyEvent.KEYCODE_ENTER==event.getKeyCode()
//                &&event.getAction()== KeyEvent.ACTION_DOWN)){
//
//                    if (NetUtils.netState(getApplicationContext())){
//                        mRecyclerView.setAdapter(searchAdapter);
//                    }
//
//                    return true;
//                }
//                return false;
//            }
//        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtils.netState(SearchActivity.this)) {
                    gotoThread();
                }
            }
        });
    }

    private void gotoThread() {

        Observable<ResponseBody> observable = RetrofitNetManager.getInstance()
                .getForumService(this)
                .getSearchData(new RequestBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse("text/plain,utf-8");
                    }

                    @Override
                    public void writeTo(@NonNull BufferedSink sink) throws IOException {
                        sink.writeString(mEd_Search.getText().toString(), Charset.defaultCharset());
                    }
                });
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, ClassMenuBean>() {
                    @Override
                    public ClassMenuBean call(ResponseBody responseBody) {
                        Gson g = new Gson();
                        return g.fromJson(NetUtils.getResponseBody(responseBody), ClassMenuBean.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ClassMenuBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ClassMenuBean classMenuBean) {
                        CategoryAdapter adapter = new CategoryAdapter(SearchActivity.this, classMenuBean);
                        RecyclerView.LayoutManager lm = new LinearLayoutManager(SearchActivity.this);
                        mRecyclerView.setLayoutManager(lm);
                        mRecyclerView.setAdapter(adapter);

                    }
                });

    }

    @Override
    public void onClick(View v) {

    }
}
