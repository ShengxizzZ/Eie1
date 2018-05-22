package com.example.shengxi.eie.network;

import com.example.shengxi.eie.beans.ForumBean;
import com.example.shengxi.eie.beans.ForumInfoBean;
import com.squareup.okhttp.ResponseBody;

import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 *
 * Created by ShengXi on 2018-05-11.
 */

public interface ApiService {

    @POST("ForumServlet")
    Observable<okhttp3.ResponseBody> getForum(@Body RequestBody requestBody);

    /**
     * classmenu
     * @return api
     */
    @GET("ClassMenuServlet")
    Observable<okhttp3.ResponseBody> getClassMenu();

    @POST("CategoryServlet")
    Observable<okhttp3.ResponseBody> getHotClassMenu(@Body RequestBody requestBody);

    /**
     * search data
     * @param requestBody request data
     * @return get response
     */
    @POST("SearchServlet")
    Observable<okhttp3.ResponseBody> getSearchData(@Body RequestBody requestBody);

    @GET("RepresentServlet")
    Observable<okhttp3.ResponseBody> getRepresents();

}
