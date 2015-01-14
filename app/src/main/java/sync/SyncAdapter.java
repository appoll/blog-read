package sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import data.ArticleContract;
import paul.antton.tedblogrssreader.ArticleItem;
import paul.antton.tedblogrssreader.R;
import paul.antton.tedblogrssreader.RssParser;

/**
 * Created by Paul's on 13-Jan-15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter{

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    private static final String RSS_LINK = "http://blog.ted.com/feed/";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<ArticleItem> articleItems = null;

        try {
            RssParser parser = new RssParser();
            articleItems = parser.parse(getInputStream(RSS_LINK));

            Vector<ContentValues> cVVector = new Vector<ContentValues>(articleItems.size());

            for (int i =0; i<articleItems.size(); i++)
            {
                String title;
                String author;
                String date;
                String image_link;
                String content_link;
                String full_content;

                title = articleItems.get(i).getTitle();
                author = articleItems.get(i).getAuthor();
                date = articleItems.get(i).getDate();
                image_link = articleItems.get(i).getImage_link();
                content_link = articleItems.get(i).getContent_link();
                full_content = articleItems.get(i).getFull_content();



                ContentValues articleValues = new ContentValues();

                articleValues.put(ArticleContract.ArticleEntry.COLUMN_TITLE, title);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_AUTHOR, author);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_DATETEXT, date);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_IMAGE_LINK, image_link);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_CONTENT_LINK, content_link);
                articleValues.put(ArticleContract.ArticleEntry.COLUMN_CONTENT, full_content);

                cVVector.add(articleValues);


            }

            if (cVVector.size() >0)
            {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                int rowsInserted = getContext().getContentResolver().bulkInsert(ArticleContract.ArticleEntry.CONTENT_URI, cvArray);

            }
        }
        catch (XmlPullParserException e) {
            Log.w(e.getMessage(), e);
        } catch (IOException e) {
            Log.w(e.getMessage(), e);
        }
    }


    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
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

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager

        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount,context);
        }

        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */

        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }


    public static void initializeSyncAdapter(Context context)
    {
        Toast.makeText(context, "initialize sync adapter ", Toast.LENGTH_LONG).show();
        getSyncAccount(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

}
