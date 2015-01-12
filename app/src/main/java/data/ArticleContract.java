package data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Paul's on 12-Jan-15.
 */
public final class ArticleContract {

    public static final String CONTENT_AUTHORITY = "paul.antton.tedblogrssreader";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLE = "article";



    public static final class ArticleEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_DATETEXT = "date";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_IMAGE_LINK = "image_link";

        public static final String COLUMN_CONTENT = "content";

        public static final String COLUMN_CONTENT_LINK = "content_link";

    }

}
