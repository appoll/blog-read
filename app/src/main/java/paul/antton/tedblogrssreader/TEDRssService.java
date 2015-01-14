package paul.antton.tedblogrssreader;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import data.ArticleContract;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class TEDRssService extends IntentService {

    private static final String RSS_LINK = "http://blog.ted.com/feed/";
    private static final String RSS_LINK_TEST = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
    public static final String ARTICLES = "articles";
    public static final String RECEIVER = "receiver";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public TEDRssService() {
        super("TEDRssService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

 /*
        List<ArticleItem> articleItems = null;

        try {
            RssParser parser = new RssParser();
            articleItems = parser.parse(getInputStream(RSS_LINK));

            Vector<ContentValues> cVVector = new Vector<ContentValues>(articleItems.size());

            for (int i =0; i<articleItems.size(); i++)
            {
               String title;
               String author;
               String date;
               String image_link;
               String content_link;
               String full_content;

                title = articleItems.get(i).getTitle();
                author = articleItems.get(i).getAuthor();
                date = articleItems.get(i).getDate();
                image_link = articleItems.get(i).getImage_link();
                content_link = articleItems.get(i).getContent_link();
                full_content = articleItems.get(i).getFull_content();



                ContentValues articleValues = new ContentValues();

                articleValues.put(ArticleContract.ArticleEntry.COLUMN_TITLE, title);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_AUTHOR, author);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_DATETEXT, date);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_IMAGE_LINK, image_link);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_CONTENT_LINK, content_link);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_CONTENT, full_content);

                cVVector.add(articleValues);


            }

            if (cVVector.size() >0)
            {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);


                int rowsInserted = this.getContentResolver().bulkInsert(ArticleContract.ArticleEntry.CONTENT_URI, cvArray);

               // Toast.makeText(this, "inserted this many rows: "+ rowsInserted, Toast.LENGTH_LONG).show();
            }


        }
        catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
*/
       /*
        Bundle bundle = new Bundle();

        bundle.putSerializable(ARTICLES, (Serializable) articleItems);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0,bundle);
        */
    }

    public InputStream getInputStream(String link)
    {
        try
        {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public class AlarmReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, TEDRssService.class);
            context.startService(sendIntent);
        }
    }

}
