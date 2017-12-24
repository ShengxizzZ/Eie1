package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.utils.DataUtils;

/**
 * Created by ShengXi on 2017/5/8.
 */

public class CategoryAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private RequestQueue queue;
    private ClassMenuBean data;

    public CategoryAdapter(Context context, ClassMenuBean data){

        Log.e("data",data.toString());
        queue = Volley.newRequestQueue(context);
        //ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        //ImageLoader.getInstance().init(configuration);
        if (data!=null){
            this.inflater = LayoutInflater.from(context);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Item item;
        if (view == null){
            view = inflater.inflate(R.layout.item_category_list,null);
            item = new Item();
            item.img = (ImageView) view.findViewById(R.id.item_cate_imageview);
            item.title = (TextView) view.findViewById(R.id.item_cate_title);
            item.teacher = (TextView) view.findViewById(R.id.item_cate_teacher);
            item.viewer = (TextView) view.findViewById(R.id.item_cate_count);
            view.setTag(item);

        }else {
            item = (Item) view.getTag();
        }
        item.title.setText(data.classes.get(i).title);
        item.teacher.setText(data.classes.get(i).teacher);
        item.viewer.setText(String.valueOf(data.classes.get(i).viewer));
        ImageRequest request = new ImageRequest(DataUtils.baseUrl+data.classes.get(i).img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                item.img.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
        return  view;
    }

    public class Item{

        public ImageView img;
        public TextView title;
        public TextView teacher;
        public TextView viewer;
    }
}
