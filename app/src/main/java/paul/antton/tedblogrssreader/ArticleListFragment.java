package paul.antton.tedblogrssreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Paul's on 10-Jan-15.
 */

public class ArticleListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ProgressBar mProgressBar;
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

        if (mRootView == null)
        {
            mRootView = inflater.inflate(R.layout.fragment_article_list, container, false);
            mProgressBar = (ProgressBar)mRootView.findViewById(R.id.progressBar);
            mArticleList = (ListView)mRootView.findViewById(R.id.listview_articles);
            mArticleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArticleAdapter adapter = (ArticleAdapter) parent.getAdapter();
                    ArticleItem item = (ArticleItem) adapter.getItem(position);
                    String content_link = item.getContent_link();
                    ((Callback)getActivity()).onItemSelected(content_link);
                }
            });
            startService();
        }
        else
        {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            parent.removeView(mRootView);
        }

/*        String[] data = {"bla", "aia", "muahmuah", "numai pot", "vreau acasa", "nani nani puiu mamii", "deaici"};
        List<String> articles = new ArrayList<String>(Arrays.asList(data));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_article, R.id.list_item_title_textview,articles);
        mProgressBar.setVisibility(View.GONE);
        mArticleList.setVisibility(View.VISIBLE);
        mArticleList.setAdapter(adapter);
*/


        return mRootView;
    }

    private void startService()
    {
        Intent intent = new Intent (getActivity(), TEDRssService.class);
        intent.putExtra(TEDRssService.RECEIVER, resultReceiver);
        getActivity().startService(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }



    private final ResultReceiver resultReceiver = new ResultReceiver(new Handler())
    {
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            List <ArticleItem> articles = (List<ArticleItem>) resultData.getSerializable(TEDRssService.ARTICLES);
            if (articles != null)
            {
              //  Toast.makeText(getActivity(),"nu-s goale in pastele matii" + articles.size(),Toast.LENGTH_SHORT ).show();
                ArticleAdapter adapter = new ArticleAdapter(getActivity(), articles);
                mArticleList.setAdapter(adapter);
            }
            else
            {
                Toast.makeText(getActivity(), "error downloading", Toast.LENGTH_LONG).show();
            }

            mProgressBar.setVisibility(View.GONE);
            mArticleList.setVisibility(View.VISIBLE);
        }
    };





    public interface Callback
    {
        public void onItemSelected (String content_link);
    }
}