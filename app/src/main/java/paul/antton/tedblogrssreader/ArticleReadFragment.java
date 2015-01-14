package paul.antton.tedblogrssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Paul's on 14-Jan-15.
 */
public class ArticleReadFragment extends Fragment {

    public ArticleReadFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_article_read, container, false);
        WebView mWebView;
        mWebView = (WebView) rootView.findViewById(R.id.article_read_webview);

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDefaultFontSize(40);



        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT))
        {
            String content = intent.getStringExtra(Intent.EXTRA_TEXT);
            mWebView.loadData(content, "text/html; charset=UTF-8", null);

        }

        Bundle arguments = getArguments();

        if (arguments !=null && arguments.containsKey("continut"))
        {
            mWebView.loadData(arguments.getString("continut"), "text/html; charset=UTF-8",null);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}