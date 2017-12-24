package com.example.shengxi.eie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.shengxi.eie.beans.ClassMenuBean;
import com.example.shengxi.eie.beans.students.Users;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ShengXi on 2017/4/19.
 */

public class NetUtils {
    static int LOGIN_FAILED = 0;
    static int LOGIN_SUCCEEDED = 1;

    DataUtils dataUtils = new DataUtils();

    public String getData(String path) {

        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));

                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.w("获取的数据：",sb.toString());
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean netState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            return info.isAvailable();
        } else {
            return false;
        }
    }

    public ClassMenuBean paserGson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ClassMenuBean.class);

    }

    /**
     *
     * @param
     * @param
     * @return
     */
    public String loginByPost(String str,String path){


        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            PrintWriter pw = new PrintWriter(connection.getOutputStream());
            pw.print(str);
            pw.flush();
            pw.close();
            if (connection.getResponseCode()== HttpsURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
                Log.e("获取的输入流",is.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                System.err.println("结果："+sb.toString());
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public String getRequestForum(String str,String path){

        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
//            PrintWriter out = new PrintWriter(connection.getOutputStream());
//            out.print(str);
//            out.flush();
//            out.close();
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.write(str.getBytes("utf-8"));
            dos.flush();
            dos.close();

            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK){

                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.w("获取的数据：",sb.toString());
                return sb.toString();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public String getRequest(String str){


        Log.e("str",str);
        try {
            URL url = new URL(DataUtils.CommentsUpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
//            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

//            PrintWriter pw = new PrintWriter(connection.getOutputStream());
//            String string = new String(str.getBytes("UTF-8"));
//
//            pw.print(string);
//            //Log.w("str.getbyte",str.getBytes().toString());
//            pw.flush();
//            pw.close();
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.write(str.getBytes("utf-8"));
            dos.flush();
            dos.close();
            if (connection.getResponseCode()== HttpsURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
               // Log.e("获取的输入流",is.toString());
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                //System.err.println("结果："+sb.toString());
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public String encode(String text){
        try {
            MessageDigest digest =MessageDigest.getInstance("md5");
            byte [] result=digest.digest(text.getBytes());
            StringBuilder sb=new StringBuilder();
            for(byte b:result){
                int number=b&0xff;
                String hex=Integer.toHexString(number);
                if(hex.length()==1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {

            return "";
        }
    }
}