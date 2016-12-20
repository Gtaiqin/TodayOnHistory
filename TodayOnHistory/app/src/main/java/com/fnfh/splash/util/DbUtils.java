package com.fnfh.splash.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fnfh.splash.bean.SqlBean;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {
    private MyDatabase historyhepler;

    public DbUtils(Context context) {
        historyhepler = new MyDatabase(context);
    }
    //添加
    public boolean insert(String data_id,String data,String title){

        SQLiteDatabase db = historyhepler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data_id",data_id);
        values.put("data",data);
        values.put("title",title);
        long minsert = db.insert("history",null,values);
        if (minsert!=-1){
            return true;
        }else{
            return false;
        }
    }

    //删除
    public boolean delete(String data_id){
        SQLiteDatabase db=historyhepler.getWritableDatabase();
        int delete = db.delete("history","data_id=?",new String[]{data_id+""});
        if(delete>0){
            return true;
        }else{
            return false;
        }
    }
    public List<SqlBean> select(){
        List<SqlBean> list=new ArrayList<>();
        SQLiteDatabase db=historyhepler.getReadableDatabase();
        Cursor cursor=db.query("history",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            SqlBean bean=new SqlBean();
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String data_id = cursor.getString(cursor.getColumnIndex("data_id"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            bean.setData(data_id);
            bean.setData(data);
            bean.setTitle(title);
            list.add(bean);
        }
        return list;
    }

}
