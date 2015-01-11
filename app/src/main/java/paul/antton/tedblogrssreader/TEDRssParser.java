package paul.antton.tedblogrssreader;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class TEDRssParser {

    private final String namespace = null;


    public List <ArticleItem> parse (InputStream inputStream) throws XmlPullParserException, IOException {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return  readFeed (parser);
        }
        finally {
            inputStream.close();
        }
    }

    private List<ArticleItem> readFeed (XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "rss");

        List<ArticleItem> items = new ArrayList<ArticleItem>();

        while (parser.next() != XmlPullParser.END_DOCUMENT)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("item"))
            {
                items.add(readItem(parser));
            }
        }
        return items;
    }

    private ArticleItem readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, "item");

        String title = null;
        String content_link = null;
        String author = null;
        String image_link = null;
        String date = null;


        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("title"))
            {
                title = readTitle(parser);
            }
            else if (name.equals("link"))
            {
                content_link = readContentLink(parser);
            }

           else if (name.equals("creator"))
            {
                author = readCreator(parser);
            }

        }

        // image_link = "http://tedconfblog.files.wordpress.com/2015/01/koprivinca.jpg?w=150";
        image_link = "http://1.gravatar.com/blavatar/909a50edb567d0e7b04dd0bcb5f58306?s=96&d=http%3A%2F%2Fs2.wp.com%2Fi%2Fbuttonw-com.png";
        date = "WHY NO DATE";
        return new ArticleItem(title, author, date, image_link, content_link);
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, "title");
        return title;
    }

    private String readContentLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespace, "link");
        String contentLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, namespace, "link");
        return contentLink;
    }

    private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "dc", "creator");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, "dc", "creator" );
        return "PULA N CUR";
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String ret = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            ret = parser.getText();
            parser.nextTag();
        }
        return ret;
    }
}
