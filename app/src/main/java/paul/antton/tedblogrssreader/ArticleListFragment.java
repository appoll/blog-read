package paul.antton.tedblogrssreader;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.ProgressBar;
import data.ArticleContract;


/**
 * Created by Paul's on 10-Jan-15.
 */

public class ArticleListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private int mPosition = ListView.INVALID_POSITION;

    private static final String SELECTED_KEY = "selected_position";

    private ArticleAdapter mArticlesAdapter;

    private static final int ARTICLE_LOADER = 0;

    private static final String [] ARTICLE_COLUMNS =
            {
                    ArticleContract.ArticleEntry.TABLE_NAME + "." + ArticleContract.ArticleEntry._ID,
                    ArticleContract.ArticleEntry.COLUMN_TITLE,
                    ArticleContract.ArticleEntry.COLUMN_AUTHOR,
                    ArticleContract.ArticleEntry.COLUMN_DATETEXT,
                    ArticleContract.ArticleEntry.COLUMN_IMAGE_LINK,
                    ArticleContract.ArticleEntry.COLUMN_CONTENT_LINK,
                    ArticleContract.ArticleEntry.COLUMN_CONTENT
            };

    public static final int COL_ARTICLE_ID = 0;
    public static final int COL_ARTICLE_TITLE = 1;
    public static final int COL_ARTICLE_AUTHOR = 2;
    public static final int COL_ARTICLE_DATE = 3;
    public static final int COL_ARTICLE_IMAGE = 4;
    public static final int COL_ARTICLE_CONTENT_LINK = 5;
    public static final int COL_ARTICLE_CONTENT = 6;

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView mArticleList;
    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ArticleListFragment newInstance(int sectionNumber) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mArticlesAdapter = new ArticleAdapter(getActivity(), null, 0);

            mRootView = inflater.inflate(R.layout.fragment_article_list, container, false);

            mArticleList = (ListView)mRootView.findViewById(R.id.listview_articles);
            mArticleList.setAdapter(mArticlesAdapter);

            mArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPosition = position;
                    Cursor cursor = mArticlesAdapter.getCursor();
                    String content = "blank blank";
                    String article_id = null;
                    if (null != cursor && cursor.moveToPosition(position))
                    {
                         content = cursor.getString(COL_ARTICLE_CONTENT);
                         article_id = cursor.getString(COL_ARTICLE_ID);
                    }

                    ((Callback)getActivity()).onItemSelected(content);
                  //  ((Callback)getActivity()).onItemSelected(article_id);

                }
            });

        if (savedInstanceState !=null && savedInstanceState.containsKey(SELECTED_KEY))
        {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != ListView.INVALID_POSITION)
        {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(ARTICLE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri articlesUri = ArticleContract.ArticleEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                articlesUri,
                ARTICLE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mArticlesAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION)
        {
            mArticleList.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ARTICLE_LOADER,null,this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mArticlesAdapter.swapCursor(null);
    }

    public interface Callback
    {
        public void onItemSelected (String content_link);
    }
}