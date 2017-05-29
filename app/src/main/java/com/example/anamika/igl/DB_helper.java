package com.example.anamika.igl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DB_helper extends SQLiteOpenHelper{
    private static  final String DATABASE_NAME ="User_data";
    private static  final String TABLE_NAME ="Credentials";
    private static  final String COL_ID ="Id";
    private static  final String COL_USER_NAME ="User_name";
    private static  final String COL_PWD ="Password";
    private static  final String EMAIL_ID ="Email_id";
    private static  final int VERSION = 1;
    private static  final String DROP_TABLE = "Drop table if exists TABLE_NAME";
    private static  final String CREATE_TABLE = "Create Credentials(COL_ID primary key autoincrement,COL_USER_NAME text not null,COL_PWD text not null,EMAIL_ID text not null);";


    public DB_helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COL_USER_NAME",user.getUser_name());
        values.put("COL_PWD",user.getPassword());
        values.put("EMAIL_ID",user.getEmail_id());
        values.put("COL_ID",user.getId());
        db.insert("TABLE_NAME",null,values);
        db.close();
    }

    public List<User> getAllUsers() {
        String[] columns = {COL_USER_NAME,COL_PWD,EMAIL_ID,COL_ID};
        String sortedOrder = COL_USER_NAME + "ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,sortedOrder);

        if(cursor.moveToFirst()){
            do {
                User user = new User();
                user.setEmail_id(cursor.getString(cursor.getColumnIndex(EMAIL_ID)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COL_PWD)));
                user.setUser_name(cursor.getString(cursor.getColumnIndex(COL_USER_NAME)));
                user.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                userList.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("COL_USER_NAME",user.getUser_name());
        contentValues.put("COL_PWD",user.getPassword());
        contentValues.put("EMAIL_ID",user.getEmail_id());
        db.update(TABLE_NAME,contentValues,COL_ID + " = ?",new String[]{String.valueOf(user.getId())});

        db.close();
    }
    public  void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COL_ID + " = ?",new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String email,String password){
        String []columns = {COL_ID,COL_PWD};
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_ID + " + ?" + " AND " + COL_PWD +" + ?" ;
        String [] selectionArgs = {email,password};

        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if(cursorCount>0){
            return true;
        }
        return false;
    }
}
