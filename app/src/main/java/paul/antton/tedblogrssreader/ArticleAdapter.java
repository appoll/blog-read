package paul.antton.tedblogrssreader;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class ArticleAdapter extends CursorAdapter {


    public ArticleAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_article,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String title = cursor.getString(ArticleListFragment.COL_ARTICLE_TITLE);
        viewHolder.titleView.setText(title);

        String author = cursor.getString(ArticleListFragment.COL_ARTICLE_AUTHOR);
        viewHolder.authorView.setText(author);

        String date = cursor.getString(ArticleListFragment.COL_ARTICLE_DATE);
        viewHolder.dateView.setText(date);

        String imageUrl = cursor.getString(ArticleListFragment.COL_ARTICLE_IMAGE);
        Picasso.with(context).load(imageUrl).into(viewHolder.imageView);

    }

    static class ViewHolder
    {
        public final ImageView imageView;
        public final TextView dateView;
        public final TextView titleView;
        public final TextView authorView;

        ViewHolder(View view) {
            this.imageView = (ImageView)view.findViewById(R.id.list_item_icon);
            this.dateView = (TextView)view.findViewById(R.id.list_item_date_textview);
            this.titleView = (TextView)view.findViewById(R.id.list_item_title_textview);
            this.authorView = (TextView)view.findViewById(R.id.list_item_author_textview);
        }
    }
}
