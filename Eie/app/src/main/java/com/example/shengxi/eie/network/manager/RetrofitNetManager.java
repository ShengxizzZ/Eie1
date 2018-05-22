package com.example.shengxi.eie.network.manager;

import android.app.Activity;

import com.example.shengxi.eie.activity.MainActivity;
import com.example.shengxi.eie.network.ApiService;
import com.example.shengxi.eie.utils.DataUtils;
import com.example.shengxi.eie.utils.NetUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 *
 * Created by ShengXi on 2018-05-11.
 */

public class RetrofitNetManager {

    private static RetrofitNetManager apiManager;
    private ApiService api ;
    public static RetrofitNetManager getInstance(){
        if (apiManager == null){
            synchronized (ApiService.class){
                if (apiManager == null){
                    apiManager = new RetrofitNetManager();
                }
            }
        }
        return  apiManager;
    }

    public ApiService getForumService(final Activity activity){

        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        int maxAge = 60*60;
                        int maxStale = 24*60*60;
                        Request request = chain.request();
                        if (NetUtils.netState(activity)){
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_NETWORK)
                                    .build();
                        }else{
                            request = request.newBuilder()
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }
                        Response response = chain.proceed(request);
                        if (NetUtils.netState(activity)){
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control","public,max-age"+maxAge)
                                    .build();
                        }else {
                            response = response.newBuilder()
                                    .removeHeader("Pragma")
                                    .header("Cache-Control","public,only-if-cached,max-stale="+maxStale)
                                    .build();
                        }
                        return response;
                    }
                })
                .retryOnConnectionFailure(true)
                .cache(new Cache(activity.getCacheDir(),10*1024*1024))
                .build();
        if (api == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(DataUtils.BASEURL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(ApiService.class);
        }
        return api;
    }

//    private static class CacheInterceptor implements Interceptor{
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            int maxAge = 60*60;
//            int maxStale = 60*60*24;
//            Request request = chain.request();
//            if (NetUtils.netState())
//            return null;
//        }

}
