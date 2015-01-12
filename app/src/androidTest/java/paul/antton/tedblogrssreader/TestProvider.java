package paul.antton.tedblogrssreader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import data.ArticleContract;
import data.ArticleDbHelper;

/**
 * Created by Paul's on 12-Jan-15.
 */
public class TestProvider extends AndroidTestCase {

    public final String LOG_TAG = this.getClass().getSimpleName();

    public void test() throws Exception{
        final int expected = 1;
        final int reality = 1;

        assertEquals(expected,reality);

    }

    public void testDeleteDb() throws Throwable {
        // delete database before testing
        mContext.deleteDatabase(ArticleDbHelper.DATABASE_NAME);
    }


    public void testGetType ()
    {
        String type = mContext.getContentResolver().getType(ArticleContract.ArticleEntry.CONTENT_URI);
        Log.d(LOG_TAG, type);
        assertEquals(ArticleContract.ArticleEntry.CONTENT_TYPE, type);

    }

    public void testInsertReadProvider()
    {
        String testTitle = "Has no idea";
        String testAuthor = "Only God";
        String testDate = "some long long time ago";
        String testImage = "link to the thumbnail image";
        String testContentLink = "long story short";
        String testContent = "the loooong loooooong loooooong story";

        ArticleDbHelper dbHelper = new ArticleDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ArticleContract.ArticleEntry.COLUMN_TITLE, testTitle);
        values.put(ArticleContract.ArticleEntry.COLUMN_AUTHOR, testAuthor);
        values.put(ArticleContract.ArticleEntry.COLUMN_DATETEXT, testDate);
        values.put(ArticleContract.ArticleEntry.COLUMN_IMAGE_LINK, testImage);
        values.put(ArticleContract.ArticleEntry.COLUMN_CONTENT_LINK, testContentLink);
        values.put(ArticleContract.ArticleEntry.COLUMN_CONTENT, testContent);

        long locationRowId;

        locationRowId = db.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "New row id: " + locationRowId);
        assertTrue("inserting into the database failed", locationRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(ArticleContract.ArticleEntry.CONTENT_URI, null,
                                                                                                      null,
                                                                                                      null,
                                                                                                      null
        );

        if (cursor.moveToFirst())
        {
            int titleIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE);
            String title = cursor.getString(titleIndex);

            int authorIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_AUTHOR);
            String author = cursor.getString(authorIndex);

            int dateIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_DATETEXT);
            String date = cursor.getString(dateIndex);

            int imageIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_IMAGE_LINK);
            String image_link = cursor.getString(imageIndex);

            int contentLinkIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_CONTENT_LINK);
            String content_link = cursor.getString(contentLinkIndex);

            int contentIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_CONTENT);
            String content = cursor.getString(contentIndex);

            assertEquals(testTitle, title);
            assertEquals(testAuthor,author);
            assertEquals(testDate,date);
            assertEquals(testImage,image_link);
            assertEquals(testContentLink,content_link);
            assertEquals(testContent,content);
        }
        else
        {
            fail("No values returned, i'm crying so hard");
        }


    }
}
