package data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Paul's on 12-Jan-15.
 */
public final class ArticleContract {

    public static final String CONTENT_AUTHORITY = "paul.antton.tedblogrssreader";


    // each URI points to a different table

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLE = "article";



    public static final class ArticleEntry implements BaseColumns
    {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLE).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE ;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLE;

        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_DATETEXT = "date";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_IMAGE_LINK = "image_link";

        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_CONTENT_LINK = "content_link";

        public static Uri buildArticleUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
