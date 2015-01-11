package paul.antton.tedblogrssreader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class TEDRssService extends IntentService {

    private static final String RSS_LINK = "http://blog.ted.com/feed/";
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
        List<ArticleItem> articleItems = null;

        try {
            TEDRssParser parser = new TEDRssParser();
            articleItems = parser.parse(getInputStream(RSS_LINK));
        }
        catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLES, (Serializable) articleItems);
        ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
        receiver.send(0,bundle);
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
}
