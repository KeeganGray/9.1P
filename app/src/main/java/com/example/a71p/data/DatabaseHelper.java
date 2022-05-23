package com.example.a71p.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.a71p.model.LostArticle;
import com.example.a71p.util.Util;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.ID + " INTEGER PRIMARY KEY , " + Util.CONDITION + " TEXT, " + Util.NAME + " TEXT, " + Util.PHONE + " TEXT, " + Util.DESC + " TEXT, " + Util.DATE + " TEXT, " + Util.LOC + " TEXT, " + Util.LAT + " TEXT, " + Util.LON + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(sqLiteDatabase);
    }

    public long insertNew(LostArticle newLostArticle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.CONDITION, newLostArticle.Condition);
        contentValues.put(Util.NAME, newLostArticle.Name);
        contentValues.put(Util.PHONE, newLostArticle.Phone);
        contentValues.put(Util.DESC, newLostArticle.Description);
        contentValues.put(Util.DATE, newLostArticle.Date);
        contentValues.put(Util.LOC, newLostArticle.Location);
        contentValues.put(Util.LAT, newLostArticle.Latitude);
        contentValues.put(Util.LON, newLostArticle.Longitude);
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return newRowId;
    }
}
