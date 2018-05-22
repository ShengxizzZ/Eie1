package com.example.shengxi.eie.utils;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * Created by ShengXi on 2018-05-18.
 */

public abstract class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener{
    private Context context;
    private RecyclerView re;
    protected   RecyclerViewTouchListener(Context context,RecyclerView recyclerView){
        this.context = context;
        this.re = recyclerView;
    }
    private  GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(context,new ReGestureDetectorCompat(re,this));
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder);
    public abstract void onLongClick(RecyclerView.ViewHolder viewHolder);

    private class ReGestureDetectorCompat implements GestureDetector.OnGestureListener {
        RecyclerViewTouchListener listener;
        RecyclerView recyclerView;
        public ReGestureDetectorCompat(RecyclerView re, RecyclerViewTouchListener listener) {
            this.listener = listener;
            this.recyclerView = re;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = re.findChildViewUnder(e.getX(),e.getY());
            if (child!=null){
                RecyclerView.ViewHolder viewHolder = re.getChildViewHolder(child);
                listener.onItemClick(viewHolder);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = re.findChildViewUnder(e.getX(),e.getY());
            if (child!=null){
                RecyclerView.ViewHolder viewHolder = re.getChildViewHolder(child);
                listener.onItemClick(viewHolder);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
