package com.kufangdidi.www.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kufangdidi.www.activity.baike.DatabaseHelper;
import com.kufangdidi.www.modal.UserModal;
import com.kufangdidi.www.utils.LogUtils;

import java.io.File;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/** 数据库管理
 * Created by hqyl on 2017/5/19.
 */

public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    public DBManager(Context context)
    {
        LogUtils.d("DBManager --> Constructor");
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        try{
            File file = context.getDir("com.kufangdidi.www",0);
            LogUtils.d("DBManager file"+file);
            helper = new DatabaseHelper(context);
            db = helper.getWritableDatabase();
        } catch (Exception e) {
            LogUtils.d("getWritableDatabase error"+e.getMessage());
        }
    }

    /**
     * add persons
     *
     * @param user
     */
    public void add(UserModal user)
    {
        LogUtils.d("DBManager --> add");
        try
        {

            ContentValues values = new ContentValues();
            values.put("_id", 1);
            values.put("userPhone", user.getUserPhone());
            values.put("userPassword", user.getUserPassword());
            values.put("jiguang_username", user.getJiguang_username());
            values.put("jiguang_password", user.getJiguang_password());

            long id = db.insert(DatabaseHelper.TABLE_USER,
                    "_id", values);

            LogUtils.d("添加用户："+id);

        }catch(Exception e){
             e.printStackTrace();
        }
    }


    /**
     * query all persons, return list
     *
     * @return List<Person>
     */

    public UserModal queryUser()
    {
        UserModal person = new UserModal();
        LogUtils.d("DBManager --> query");
        try {
            Cursor c = db.rawQuery("select * from " + DatabaseHelper.TABLE_USER + " where _id=?",
                    new String[]{"1"});
            while (c.moveToNext()) {
                person.setUserPhone(c.getString(c.getColumnIndex("userPhone")));
                person.setUserPassword(c.getString(c.getColumnIndex("userPassword")));
                person.setJiguang_username(c.getString(c.getColumnIndex("jiguang_username")));
                person.setJiguang_password(c.getString(c.getColumnIndex("jiguang_password")));


            }
            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return person;
    }

    //清空某一个表
    public void deleteTableMenu(){
        db.execSQL("delete from menu");
    }


     public void delete()
    {
        try{
            db.execSQL("delete from user");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    public void deleteLogin()
    {
        try{
            db.execSQL("delete from login");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void deleteVersion()
    {
        try{
            db.execSQL("delete from "+DatabaseHelper.TABLE_VERSION);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }


    //判断表是否存在
    public boolean IsTableExist(String table) {
        LogUtils.d("IsTableExist 检查"+table+"表是否存在");
        boolean isTableExist=true;
        try{
            SQLiteDatabase db=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, null, null);
            Cursor c=db.rawQuery("SELECT count(*) FROM "+DatabaseHelper.DATABASE_NAME+" WHERE type='table' AND name='"+table+"'", null);
            LogUtils.d("IsTableExist 检查"+c.getInt(0));
            if (c.getInt(0)==0) {
                isTableExist=false;
                LogUtils.d("IsTableExist 检查"+isTableExist);
            }
            c.close();
            db.close();
            LogUtils.d("IsTableExist 检查"+isTableExist);
        }catch(Exception e){
            e.printStackTrace();
        }
        return isTableExist;
    }

    public void close(){
        try{
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
