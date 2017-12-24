package com.example.shengxi.eie.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ShengXi on 2017/4/19.
 */

public class ClassMenuBean implements Serializable{
        /**
         * id : 1
         * tid : 2001
         * teacher : 唐文明
         * img : /AppSwevlet/img/001.jpg
         * url :
         * viewer : 123
         * summary : 收到回复静安寺电话客服收快递费哈就开始
         * title : 就业培训
         */



    public List<Classes> classes;
    public static class Classes{
        public String id;
        public String tid;
        public String teacher;
        public String img;
        public String url;
        public int viewer;
        public String summary;
        public String title;

    }
}
