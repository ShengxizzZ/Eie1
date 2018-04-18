package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.activity.VideoTeachActivity;
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;

import io.vov.vitamio.ThumbnailUtils;
import io.vov.vitamio.provider.MediaStore;

import static android.R.attr.bitmap;

/**
 * Created by ShengXi on 2017/4/20.
 */

public class ClassMenuAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private RequestQueue queue;
    private ClassMenuBean data;
    private Context context;
    public ClassMenuAdapter(Context context,ClassMenuBean data){

        queue = Volley.newRequestQueue(context);
        //ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        //ImageLoader.getInstance().init(configuration);
        if (data!=null){
            this.inflater = LayoutInflater.from(context);
            this.context = context;
            this.data = data;
        }
    }
    @Override
    public int getCount() {
        if (data!=null){
            return data.classes.size();
        }else {
            return 0;
        }
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Item item;
        if (view == null){
            view = inflater.inflate(R.layout.item_classmenu,null);
            item = new Item();
            item.img = (ImageView) view.findViewById(R.id.item_live_cover);
            item.title = (TextView) view.findViewById(R.id.item_live_title);
            item.teacher = (TextView) view.findViewById(R.id.item_live_user);
            item.viewer = (TextView) view.findViewById(R.id.item_live_count);
            item.index = (TextView) view.findViewById(R.id.gv_index);
            view.setTag(item);

        }else {
            item = (Item) view.getTag();
        }

        item.index.setText(data.classes.get(i).id);
        item.teacher.setText(data.classes.get(i).teacher);
        item.viewer.setText(String.valueOf(data.classes.get(i).viewer));

        new Thread(new Runnable() {
            @Override
            public void run() {
                //设置缩略图,Vitamio提供的工具类。
                final Bitmap videoThumbnail = ThumbnailUtils.createVideoThumbnail(
                        context, DataUtils.baseUrl+data.classes.get(i).url
                        , MediaStore.Video.Thumbnails.MINI_KIND);
                if (videoThumbnail != null) {
                    item.img.post(new Runnable() {
                        @Override
                        public void run() {
                            item.img.setDrawingCacheQuality(ImageView.DRAWING_CACHE_QUALITY_LOW);
                            item.img.setImageBitmap(videoThumbnail);
                        }
                    });
                }
            }
        }).start();
//        Log.e("imageUrl:",DataUtils.baseUrl+data.classes.get(i).img);
//        ImageRequest request = new ImageRequest(DataUtils.baseUrl+data.classes.get(i).img, new Response.Listener<Bitmap>() {
//            @Override
//            public void onResponse(Bitmap bitmap) {
//                item.img.setImageBitmap(bitmap);
//            }
//        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        queue.add(request);
        //ImageLoader.getInstance().displayImage(DataUtils.baseUrl+data.classes.get(i).img,item.img);
        item.title.setText(data.classes.get(i).title);
        return  view;
    }

    public class Item{

        public ImageView img;
        public TextView title;
        public TextView teacher;
        public TextView viewer;
        public TextView index;
    }
}
