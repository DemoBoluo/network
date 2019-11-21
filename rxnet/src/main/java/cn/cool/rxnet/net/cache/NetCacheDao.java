package cn.cool.rxnet.net.cache;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.text.TextUtils;

import cn.cool.rxnet.net.exception.ExceptionHandle;
import cn.cool.rxnet.net.exception.ResponseThrowable;
import cn.cool.rxnet.net.response.HttpResponse;
import cn.cool.rxnet.net.util.GsonUtil;
import cn.cool.rxnet.net.util.Lutils;
import cn.cool.rxnet.net.util.ObjectHelper;

/**
 * Created by TRZ on 2019/6/28.
 * 备注：
 */
public class NetCacheDao<T>{
    private final static String TAG = "MessageItemDao";
    private CacheNetHelper dbHelper;
    private static Application application;
    private String tableName;
    private NetCacheDao() {
        dbHelper = new CacheNetHelper(application);
        tableName = "netdata";
    }
    private static class SingletonNetCacheDao{
         static final NetCacheDao INSATNCE = new NetCacheDao();
    }

    public static NetCacheDao getInstance(Application application){
        NetCacheDao.application = application;
        return SingletonNetCacheDao.INSATNCE;
    }

    public long saveNetData(String tag,T httpResponse){
        if (TextUtils.isEmpty(tag) || httpResponse == null
                || ((HttpResponse)httpResponse).getData() == null){
            return  -1;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("tag",tag);
        values.put("data", GsonUtil.parseBeanToJson(httpResponse));
        values.put("responseTime", SystemClock.currentThreadTimeMillis());
        long replace = database.replace(tableName, null, values);
        if (replace > 0){
            database.setTransactionSuccessful();
        }
        database.endTransaction();
        return  replace;
    }

    public T getSaveNetData(String tag, Class<T> clazz){
        ObjectHelper.requireNonNull(tag,"when save response data,tag can not null");
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selection = "tag=?";
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, null, selection, new String[]{tag}, null, null, null);
            if (cursor.moveToNext()){
                String data = cursor.getString(cursor.getColumnIndex("data"));
                Lutils.i(TAG,"===getSaveNetData==@data===="+data);
                return GsonUtil.parseJsonToBean(data, clazz);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public T getSaveNetData(String tag,ResponseThrowable responseThrowable, Class<T> clazz) throws ClassCastException {
        if (!TextUtils.isEmpty(tag) && clazz != null) {
            if (responseThrowable.code != ExceptionHandle.ERROR.PARSE_ERROR
                    && responseThrowable.code != ExceptionHandle.ERROR.DATA_ERROR) {
                T data = (T) NetCacheDao
                        .getInstance(application)
                        .getSaveNetData(tag, clazz);
                if (data != null) {
                    HttpResponse response = (HttpResponse) data;
                    HttpResponse.Meta meta = new HttpResponse.Meta();
                    meta.code = responseThrowable.code;
                    meta.message = responseThrowable.message;
                    response.setMeta(meta);
                }
                return data;
            }
        }
        return null;
    }

}
