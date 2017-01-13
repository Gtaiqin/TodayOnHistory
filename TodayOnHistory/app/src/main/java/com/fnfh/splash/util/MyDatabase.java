package com.fnfh.splash.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fnfh.splash.bean.SQLBean;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(Context context) {
        super(context, "history.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table history(id integer primary key autoincrement,data_id varchar(20),data varchar(20),title varchar(20),url varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    //添加
    public boolean insert(String data_id, String data, String title, String url) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data", data);
        values.put("title", title);
        values.put("url", url);
        values.put("data_id", data_id);
        long minsert = db.insert("history", null, values);
        db.close();
        if (minsert != -1) {
            return true;
        } else {
            return false;
        }
    }

    //删除
    public boolean delete(String data_id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete("history", "data_id=?", new String[]{data_id + ""});
        db.close();
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<SQLBean> select() {
        List<SQLBean> list = new ArrayList<SQLBean>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("history", null, null, null, null, null, null);
        SQLBean bean = null;
        while (cursor.moveToNext()) {
            bean = new SQLBean();
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String data_id = cursor.getString(cursor.getColumnIndex("data_id"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            bean.setData(data);
            bean.setData_id(data_id);
            bean.setTitle(title);
            bean.setUrl(url);
            list.add(bean);
        }
        db.close();
        return list;
    }

    // 读取数据
    public int idread(String id) {
        // 执行sql语句需要sqliteDatabase对象
        // 调用getReadableDatabase方法,来初始化数据库的创建
        SQLiteDatabase database = getReadableDatabase();
        // table:表名, columns：查询的列名,如果null代表查询所有列； selection:查询条件,
        // selectionArgs：条件占位符的参数值,
        // groupBy:按什么字段分组, having:分组的条件, orderBy:按什么字段排序
        Cursor cursor = database.rawQuery("select * from history where data_id=?", new String[]{id});
        int count = 0;
        // 解析Cursor中的数据
        List<SQLBean> list = new ArrayList<SQLBean>();
        if (cursor != null && cursor.getCount() > 0) {// 判断cursor中是否存在数据
            // 循环遍历结果集，获取每一行的内容
            while (cursor.moveToNext()) {// 条件，游标能否定位到下一行
                count = cursor.getInt(cursor.getColumnIndex("data_id"));
            }
            cursor.close();// 关闭结果集
        }
        // 关闭数据库对象
        database.close();
        return count;
    }
}
