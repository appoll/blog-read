package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import data.ArticleContract.ArticleEntry;

/**
 * Created by Paul's on 12-Jan-15.
 */
public class ArticleDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "article.db";


    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ArticleEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ArticleEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "+
                ArticleEntry.COLUMN_DATETEXT + " TEXT NOT NULL, " +
                ArticleEntry.COLUMN_IMAGE_LINK + " TEXT NOT NULL, " +
                ArticleEntry.COLUMN_CONTENT_LINK + " TEXT NOT NULL, " +
                ArticleEntry.COLUMN_CONTENT + " TEXT NOT NULL " +
                ")";

        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        onCreate(db);
    }
}
