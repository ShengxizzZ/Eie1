package com.example.shengxi.eie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import com.example.shengxi.eie.beans.CommentsBean;
import com.example.shengxi.eie.beans.ForumInfoBean;
import com.example.shengxi.eie.fragment.FragmentMsg;
import com.example.shengxi.eie.utils.CircleImageView;
import com.example.shengxi.eie.utils.DataUtils;

/**
 * Created by ShengXi on 2017/4/24.
 */

public class ForumInfoAdapter extends BaseAdapter {


    private LayoutInflater inflater;
    private CommentsBean dataBean;
    private RequestQueue queue;

    public ForumInfoAdapter(Context context, CommentsBean forumInfoBean) {

        queue = Volley.newRequestQueue(context);
        if (forumInfoBean != null) {
            this.inflater = LayoutInflater.from(context);
            this.dataBean = forumInfoBean;
        }
    }

    @Override
    public int getCount() {
        return dataBean.comments.size();
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


        //if (i==1){
        final Holder item;
        if (view == null) {
            view = inflater.inflate(R.layout.item_foruminfo, null);
            //view = inflater.inflate(R.layout.item_foruminfo_head,viewGroup);
            item = new Holder();
            item.imageView = (CircleImageView) view.findViewById(R.id.foruminfo_circleimageview);
            item.name = (TextView) view.findViewById(R.id.foruminfo_up_name);
            item.date = (TextView) view.findViewById(R.id.foruminfo_uptime);
            item.main = (TextView) view.findViewById(R.id.foruminfo_mian);
            view.setTag(item);
        } else {
            item = (Holder) view.getTag();
        }
        Log.e("url",dataBean.comments.get(i).commentsImg);
        ImageRequest request = new ImageRequest(DataUtils.baseUrl + dataBean.comments.get(dataBean.comments.size()-i-1).commentsImg, new Response.Listener<Bitmap>() {
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
        item.name.setText(dataBean.comments.get(dataBean.comments.size()-i-1).commentsName);
        item.date.setText(dataBean.comments.get(dataBean.comments.size()-i-1).commentsDate);
        item.main.setText(dataBean.comments.get(dataBean.comments.size()-i-1).mainText);
        return view;
    }

    public class Holder {

        public CircleImageView imageView;
        public TextView name;
        public TextView date;
        public TextView main;

    }
}
