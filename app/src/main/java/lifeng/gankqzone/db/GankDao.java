package lifeng.gankqzone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import lifeng.gankqzone.bean.OnedayRes;

/**
 * Created by lifeng on 2017/11/3.
 *
 * @description 对数据库进行增删改查
 */
public class GankDao {

    private Context mContext;
    private GankDatabase mGankDatabase;
    private ArrayList<OnedayRes.Gank> mDbBeen;
    //插入到哪一列中
    String sqlParam;
    //要插入的值
    String value;

    public GankDao(Context context) {
        this.mContext = context;
        if (mGankDatabase == null) {
            mGankDatabase = new GankDatabase(context);
        }
    }

    public void addData(OnedayRes.Gank gank) {
        sqlParam = null;
        value = null;
        //insert into hot(content_id,desc) values("11","22")
        paramJoint(gank);

        SQLiteDatabase writableDatabase = mGankDatabase.getWritableDatabase();
//        writableDatabase.beginTransaction();
//        String sql = "insert into" + GankDatabase.TABLE_NAME + " (" + sqlParam + ") values(" + value + ")";
//        Log.i("SQL", "addData sql:" + sql);
//        writableDatabase.execSQL(sql);
//        writableDatabase.setTransactionSuccessful();

        ContentValues cv = new ContentValues();
        cv.put(GankDatabase.CONTENT_ID, gank.get_id());
        cv.put(GankDatabase.PUBLISHEDAT, gank.getPublishedAt());
        cv.put(GankDatabase.DESC, gank.getDesc());
        cv.put(GankDatabase.SOURCE, gank.getSource());
        cv.put(GankDatabase.CONTENT_URL, gank.getUrl());
        if (gank.getImages() != null && gank.getImages().size() > 0) {
            cv.put(GankDatabase.IMAGE_URL, gank.getImages().get(0));
        }
        cv.put(GankDatabase.AUTHOR, gank.getWho());
        long row = writableDatabase.insert(GankDatabase.TABLE_NAME, null, cv);
        Log.i("SQL", "addData row:" + row);
        writableDatabase.close();
    }

    /**
     * 查询某个字段的前十条数据
     */
    public ArrayList<OnedayRes.Gank> queryData() {
        //select * from hot where content_id  limit 0,10;
        mDbBeen = new ArrayList<>();
        ArrayList<String> images = null;
        SQLiteDatabase readableDatabase = mGankDatabase.getReadableDatabase();

//        String sql = "select * from hot where " + GankDatabase.CONTENT_ID + " limit 0,10";
        String sql = "select * from " + GankDatabase.TABLE_NAME + " limit 0,10";
//        String sql = "select * from user";
        Cursor cursor = readableDatabase.rawQuery(sql, null);
        cursor.moveToFirst();//转移到结果的第一行
        while (!cursor.isAfterLast()) {
            images = new ArrayList<>();
            OnedayRes.Gank gank = new OnedayRes.Gank();
            String contentId = cursor.getString(cursor.getColumnIndex(GankDatabase.CONTENT_ID));
            String publishedAt = cursor.getString(cursor.getColumnIndex(GankDatabase.PUBLISHEDAT));
            String desc = cursor.getString(cursor.getColumnIndex(GankDatabase.DESC));
            String source = cursor.getString(cursor.getColumnIndex(GankDatabase.SOURCE));
            String contentUrl = cursor.getString(cursor.getColumnIndex(GankDatabase.CONTENT_URL));
            String imageUrl = cursor.getString(cursor.getColumnIndex(GankDatabase.IMAGE_URL));
            String author = cursor.getString(cursor.getColumnIndex(GankDatabase.AUTHOR));

            gank.set_id(contentId);
            gank.setPublishedAt(publishedAt);
            gank.setDesc(desc);
            gank.setSource(source);
            gank.setUrl(contentUrl);
            images.add(imageUrl);
            gank.setImages(images);
            gank.setWho(author);
            Log.i("TTT", "queryData DbBean:" + gank.toString());
            mDbBeen.add(gank);
            cursor.moveToNext();
        }
        cursor.close();
        readableDatabase.close();
        return mDbBeen;
    }

    private void paramJoint(OnedayRes.Gank gank) {
        if (gank.get_id() != null) {
            sqlParam = GankDatabase.CONTENT_ID + ",";
            value = gank.get_id();
        }
        if (gank.getPublishedAt() != null) {
            sqlParam += GankDatabase.PUBLISHEDAT + ",";
            value += gank.getPublishedAt();
        }
        if (gank.getDesc() != null) {
            sqlParam += GankDatabase.DESC + ",";
            value += gank.getDesc();
        }
        if (gank.getSource() != null) {
            sqlParam += GankDatabase.SOURCE + ",";
            value += gank.getSource();
        }
        if (gank.getUrl() != null) {
            sqlParam += GankDatabase.CONTENT_URL + ",";
            value += gank.getUrl();
        }

        if (gank.getImages() != null && gank.getImages().size() > 0) {
            sqlParam += GankDatabase.IMAGE_URL + ",";
            value += gank.getImages().get(0) + ",";
        }

        sqlParam += GankDatabase.AUTHOR;
        if (gank.getWho() == null) {
            value += "";
        } else {
            value += gank.getWho();
        }
    }
}
