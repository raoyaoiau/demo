package cn.trust.win.asyncloadnewsapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    public static final String TAG = "mainac";
    public  static  String JSONURL = "http://www.imooc.com/api/teacher?type=4&num=30";
    private ListView mListView;
    private List<NewsData> mNewsDataList = new ArrayList <> (  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        mListView = (ListView)findViewById ( R.id.mlistview );
        new GetNewsDataAsyncTask ().execute ( JSONURL );


    }

    class GetNewsDataAsyncTask extends AsyncTask<String, Void, List<NewsData>>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List <NewsData> doInBackground(String... strings) {
            return getNewsDataFromJson ( getJasonStringFromUrl () );
        }

        @Override
        protected void onPostExecute(List <NewsData> newsData) {
            super.onPostExecute ( newsData );
            mListView.setAdapter ( new MyLoadDataAdapt( newsData, MainActivity.this, mListView) );
        }
    }

    /*
    {
    "id":1,
    "name":"Tony\u8001\u5e08\u804ashell\u2014\u2014\u73af\u5883\u53d8\u91cf\u914d\u7f6e\u6587\u4ef6",
    "picSmall":"http:\/\/img.mukewang.com\/55237dcc0001128c06000338-300-170.jpg",
    "picBig":"http:\/\/img.mukewang.com\/55237dcc0001128c06000338.jpg",
    "description":"\u4e3a\u4f60\u5e26\u6765shell\u4e2d\u7684\u73af\u5883\u53d8\u91cf\u914d\u7f6e\u6587\u4ef6",
    "learner":12312
    }
     */
    public  List<NewsData> getNewsDataFromJson(String strNewsData)
    {
        List<NewsData> mNewsDataListRet = new ArrayList <> (  );
        try {
            JSONObject mJasonObject = new JSONObject ( strNewsData );
            JSONArray mJasonArray = mJasonObject.getJSONArray ( "data" ); // 取出data
            for (int i = 0; i< mJasonArray.length (); i++)
            {
                mJasonObject = mJasonArray.getJSONObject ( i );
                NewsData mNewsData = new NewsData();
                mNewsData.mName = mJasonObject.getString ( "name" );
                mNewsData.mPicSmall = mJasonObject.getString ( "picSmall" );
                mNewsData.mPicBig = mJasonObject.getString ( "picBig" );
                mNewsData.mDescription = mJasonObject.getString ( "description" );
                mNewsData.mLearner = mJasonObject.getInt ("learner");
                mNewsDataListRet.add ( mNewsData );
            }
        } catch (JSONException e) {
            Log.e(TAG,Log.getStackTraceString(e));  //这种方式打印错误很有效果
            e.printStackTrace ( );
        }
        return  mNewsDataListRet;
    }

    public String getJasonStringFromUrl()
    {
        String line = "", result = "";// null 和 ""的区别是什么？
        try {
            URL mUrl = new URL ( JSONURL );
            Log.i ( TAG, "getNewsDataFromUrl: "+mUrl.getHost () );
            InputStream is = mUrl.openStream ();
            if (is != null) {
                InputStreamReader isr = new InputStreamReader ( is, "utf-8" );
                BufferedReader br = new BufferedReader ( isr );
                while ( (line = br.readLine ())!= null)
                    result += line;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }

        return result;
    }
}
