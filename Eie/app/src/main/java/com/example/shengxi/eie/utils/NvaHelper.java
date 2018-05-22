package com.example.shengxi.eie.utils;

import android.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.widget.TabHost;

/**
 *
 * Created by ShengXi on 2018-05-07.
 */

public class NvaHelper<T> {

    private Context context;
    private android.support.v4.app.FragmentManager fragmentManager;
    private int container;

    private SparseArray<Tab<T>> tabs = new SparseArray<>();
    private Tab<T> currentTab;
    public NvaHelper(Context context, int containerId,
                     android.support.v4.app.FragmentManager fragmentManager) {

        this.container = containerId;
        this.context = context;
        this.fragmentManager = fragmentManager;

    }

    public NvaHelper<T> add(int menuId,Tab<T> tab){

        tabs.put(menuId,tab);
        return this;
    }

    public boolean performClikMenu(int menuId){
        Tab<T> tab = tabs.get(menuId);
        if (tab!=null){
            toSelect(tab);
            return true;
        }
        return false;
    }

    private void toSelect(Tab<T> tab) {

        Tab<T> oldTab = null;
        if (tab!=null){
            oldTab = currentTab;
//            if (oldTab == tab){
//            }
        }
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    private void doTabChanged(Tab<T> currentTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                ft.detach(oldTab.fragment);
            }
        }
        if (currentTab != null) {
            if (currentTab.fragment == null) {
                Fragment fragment = Fragment.instantiate(context, currentTab.clx.getName());
                currentTab.fragment = fragment;
                ft.add(container, fragment, currentTab.clx.getName());
            } else {
                ft.attach(currentTab.fragment);
            }
        }
        ft.commit();
    }


    /**
     * tab的基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {

        // Fragment对应的Class信息
        public Class<?> clx;
        // 额外的字段，用户自己设定需要使用
        public T extra;

        // 内部缓存的对应的Fragment，
        // Package权限，外部无法使用
        Fragment fragment;

        public Tab(Class<?> cl, T ex) {
            this.clx = cl;
            this.extra = ex;

        }
    }

    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }

}
