package paul.antton.tedblogrssreader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Paul's on 10-Jan-15.
 */
public class ArticleAdapter extends BaseAdapter {
    private List <ArticleItem> mArticleItems;
    private Context mContext;

    public ArticleAdapter (Context context, List<ArticleItem> items)
    {
        mContext = context;
        mArticleItems = items;
    }
    @Override
    public int getCount() {
        return mArticleItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mArticleItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = View.inflate(mContext,R.layout.list_item_article,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.authorView.setText(mArticleItems.get(position).getAuthor());
        holder.titleView.setText(mArticleItems.get(position).getTitle());
        holder.dateView.setText(mArticleItems.get(position).getDate());

        String imageUrl = mArticleItems.get(position).getImage_link();
        Picasso.with(mContext).load(imageUrl).into(holder.imageView);

        return convertView;
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
