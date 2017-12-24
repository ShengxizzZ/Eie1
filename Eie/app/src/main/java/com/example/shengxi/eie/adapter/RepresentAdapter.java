package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.shengxi.eie.R;
import com.example.shengxi.eie.beans.RepresentBean;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataUtils;

/**
 * Created by ShengXi on 2017/5/8.
 */

public class RepresentAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private RepresentBean data;
    private RequestQueue queue;

    public RepresentAdapter(Context context, RepresentBean data) {

        queue = Volley.newRequestQueue(context);
        if (data!=null){

            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

    }

    @Override
    public int getCount() {
        return data.represents.size();
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
            view = inflater.inflate(R.layout.item_represent_list,null);
            item = new Item();
            item.imageView = (CircleImageView) view.findViewById(R.id.repre_imageview);
            item.name = (TextView) view.findViewById(R.id.repre_name);
            view.setTag(item);

        }else{
            item = (Item) view.getTag();
        }
        ImageRequest request = new ImageRequest(DataUtils.baseUrl + data.represents.get(i).representImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                item.imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);

        item.name.setText(data.represents.get(i).representName);

        return view;
    }

    public class Item{

        public CircleImageView imageView;
        public TextView name;
    }

}
