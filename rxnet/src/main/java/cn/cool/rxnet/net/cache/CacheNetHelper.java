package cn.cool.rxnet.net.cache;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TRZ on 2019/6/28.
 * 备注：
 */
public class CacheNetHelper extends SQLiteOpenHelper {
    private final String CREATE_NETDATA = "create table IF NOT EXISTS netdata(id integer primary key autoincrement," +
            "tag varchar(40)," +
            "data varchar("+Integer.MAX_VALUE+")," +
            "responseTime integer(20) )";

    public CacheNetHelper(Context context){
        this(context, "netcache.db", null, 1);
    }

    public CacheNetHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(CREATE_NETDATA);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
