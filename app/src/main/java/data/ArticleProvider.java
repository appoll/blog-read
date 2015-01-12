package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Paul's on 12-Jan-15.
 */
public class ArticleProvider extends ContentProvider{



    private static final int ARTICLE = 100;
    private static final int ARTICLE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ArticleDbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new ArticleDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri))
        {
            case ARTICLE:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(ArticleContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            }

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        if (match == ARTICLE)
            return ArticleContract.ArticleEntry.CONTENT_TYPE;
        else throw new UnsupportedOperationException("Unknown uri: " + uri);

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
            // only match base uris here -> cursors notify for descendants
        switch (match)
        {
            case ARTICLE:
            {
                long id = db.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, values);
                if (id>0)
                    returnUri = ArticleContract.ArticleEntry.buildArticleUri(id);
                else
                    throw new SQLException("Failed to insert row into "+ uri);
                break;
            }
            default: throw new UnsupportedOperationException("Unknown uri: " +uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ArticleContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority,ArticleContract.PATH_ARTICLE,ARTICLE);

        return matcher;
    }
}
