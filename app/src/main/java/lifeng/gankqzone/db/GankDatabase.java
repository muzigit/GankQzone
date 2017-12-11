package lifeng.gankqzone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lifeng on 2017/11/3.
 *
 * @description 数据库
 */
public class GankDatabase extends SQLiteOpenHelper {
    /*** 数据库名 ***/
    private static final String DB_NAME = "Gank";
    private static final int DB_VERSION = 1;
    /*** 表名 ***/
    public static final String TABLE_NAME = "hot_table";

    /*** 存储的字段 ***/
    public static final String CONTENT_ID = "content_id";
    public static final String PUBLISHEDAT = "publishedAt";
    public static final String DESC = "desc";
    public static final String SOURCE = "source";
    public static final String CONTENT_URL = "content_url";
    public static final String IMAGE_URL = "image_url";
    public static final String AUTHOR = "author";

    public GankDatabase(Context context) {
        /*** 创建数据库 ***/
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL语句:
        //create table if not exists hot (Id integer primary key,content_id text,
        //  publishedAt text,desc text,source text, content_url text ,image_url,author text)

        String sql = "create table if not exists " + TABLE_NAME +
                " (Id integer primary key AUTOINCREMENT," + CONTENT_ID + " text," +
                PUBLISHEDAT + " text," + DESC + " text," + SOURCE + " text, " +
                CONTENT_URL + " text ," + IMAGE_URL + " text," + AUTHOR + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
