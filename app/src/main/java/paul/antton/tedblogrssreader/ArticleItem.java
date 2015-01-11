package paul.antton.tedblogrssreader;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class ArticleItem {


    public ArticleItem(String title, String author, String date, String image_link, String content_link) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.image_link = image_link;
        this.content_link = content_link;
    }

    private final String title;
    private final String author;
    private final String date;
    private final String image_link;
    private final String content_link;



    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getImage_link() {
        return image_link;
    }
}
