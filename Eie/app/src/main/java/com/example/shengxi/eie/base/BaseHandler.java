package com.example.shengxi.eie.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by ShengXi on 2018-04-18.
 * 我们都知道在使用Handler来进行消息通讯的时候由于Activity持有Handler的强引用容易导致页面无法回收造成内存泄露的危险。
 * 在网上提出了一些解决方案大都是将强引用改为使用WeakReference的弱引用来避免对象无法回收的状况发生
 */


public class BaseHandler <T extends BaseHandler.BaseHandlerCallBack>extends Handler{

    WeakReference <T> wr;
    public BaseHandler(T t){

        wr =  new WeakReference<T>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = wr.get();
        if(t!=null)
            t.callBack(msg);
    }

    public interface BaseHandlerCallBack{

        public void callBack(Message msg);
    }
}
