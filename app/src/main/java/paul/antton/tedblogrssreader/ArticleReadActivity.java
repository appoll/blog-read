package paul.antton.tedblogrssreader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class ArticleReadActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.read_article_container, new ArticleReadFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ArticleReadFragment extends Fragment {

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
}
