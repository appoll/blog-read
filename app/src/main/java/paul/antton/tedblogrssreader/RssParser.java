package paul.antton.tedblogrssreader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul's on 12-Jan-15.
 */
 public class RssParser {
        private static final String ns = null;

        public List parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, ns, "rss");
                parser.nextTag();
                return readFeed(parser);
            } finally {
                in.close();
            }
        }

        private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            List entries = new ArrayList();

            parser.require(XmlPullParser.START_TAG, ns, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("item")) {
                    entries.add(readEntry(parser));
                } else {
                    skip(parser);
                }
            }
            return entries;
        }

        private ArticleItem readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String title = null;
            String content_link = null;
            String author = null;
            String image_link = null;
            String date = null;
            String full_content = null;

            boolean image_found = false;

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.i("TAG MY XML", name);
                if (name.equals("title")) {
                    title = readTitle(parser);
                }
                else if (name.equals("link"))
                    {
                        content_link = readContentLink(parser);
                    }

                    else if (name.equals("dc:creator"))
                    {
                        author = readCreator(parser);
                    }
                else if (name.equals("pubDate"))
                {
                    date = readDate(parser);
                }

                else if (name.equals("media:thumbnail"))
                {
                    image_link = readImageUrl(parser);
                }

                else if (name.equals("content:encoded"))
                {
                    full_content = readFullContent(parser);
                }
                 else {
                    skip(parser);
                }
            }

            return new ArticleItem(title, author, date, image_link, content_link, full_content);
        }

        // Processes title tags in the feed.
        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "title");
            return title;
        }

    private String readContentLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String contentLink = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return contentLink;
    }

    private String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "dc:creator");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "dc:creator" );
        return author;
    }

    private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pubDate");
        String author = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pubDate" );
        return author;
    }

    private String readFullContent(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "content:encoded");
        String content = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "content:encoded");
        return content;
    }

    private String readImageUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        String url = "";
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        String tag = parser.getName();

        if (tag.equals("media:thumbnail")) {
            url = parser.getAttributeValue(null, "url");
            parser.nextTag();
            Log.i("URL URL URL URL", url);
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:thumbnail");
        return url;
    }


        // For the tags title and summary, extracts their text values.
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }


        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

    }


