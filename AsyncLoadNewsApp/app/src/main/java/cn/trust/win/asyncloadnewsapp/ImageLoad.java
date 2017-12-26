package cn.trust.win.asyncloadnewsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.ArraySet;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiaxu on 2017/12/25.
 */

public class ImageLoad {
    private ImageView mIV;
    private LruCache<String, Bitmap> mCaches;
    private String  mUrl;
    private ListView mListView;
    private Handler mHander = new Handler (  ){

        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage ( msg );
            if ( mIV.getTag ().equals ( mUrl ))
                mIV.setImageBitmap ( (Bitmap) msg.obj);
        }
    };
    private Set<MyLoadImageAsyncTask> mTasks;

    public ImageLoad(ListView mListView) {
        this.mListView = mListView;
        mTasks  = new HashSet <> (  );

        int cacheSize = (int) (Runtime.getRuntime ().maxMemory ()/4);
        mCaches = new LruCache <String, Bitmap> (cacheSize  ){

            /**
             * Returns the size of the entry for {@code key} and {@code value} in
             * user-defined units.  The default implementation returns 1 so that size
             * is the number of entries and max size is the maximum number of entries.
             * <p>
             * <p>An entry's size must not change while it is in the cache.
             *
             * @param key
             * @param value
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount ();
            }
        };
    }

    public void showImageByAsync(ImageView mIv, String mUrl)
    {
        assert (mUrl!=null);
        Bitmap bitmap = getBitmapFromCache ( mUrl );
        if (bitmap == null)
        {
            mIv.setImageResource ( R.mipmap.ic_launcher );
        }
        else
        {
            mIv.setImageBitmap ( bitmap );
        }

    }


    /*
    利用handle去加载图片
     */
    public void showImageByThread(ImageView mIV, final String mUrl, String mName)
    {
        this.mIV = mIV;
        this.mUrl = mUrl;
        Log.i ( MainActivity.TAG, "showImageByThread: " + mName);
        new Thread ( new Runnable ( ) {
            @Override
            public void run() {
                Bitmap mBitmap = getBitMapFromUrl ( mUrl );
                Message mMsg = Message.obtain ();
                mMsg.obj = mBitmap;
                mHander.sendMessage ( mMsg );


            }
        } ).start ();

    }

    /*
     通过url异步加载图片
     */
    public void loadImages(ImageView mIV, String mUrl)
    {

//        new MyLoadImageAsyncTask ( mIV, mUrl ).execute ( mUrl );
//        Bitmap mBitmap = getBitmapFromCache ( mUrl );
//        if ( mBitmap== null)
//        {


//            MyLoadImageAsyncTask mLoadAsync = new MyLoadImageAsyncTask (mIV, mUrl);
//            mLoadAsync.execute ( mUrl );
//            mTasks.add ( mLoadAsync );
//            new MyLoadImageAsyncTask ( mIV, mUrl ).execute ( mUrl );
//        }
//        else
//        {
////            ImageView mIV = (ImageView)mListView.findViewWithTag ( mUrl );
//            if (mIV.getTag ().equals ( mUrl ) && mBitmap != null)
//                mIV.setImageBitmap ( mBitmap );
//        }

    }

    public Bitmap getBitmapFromCache(String url){ return  mCaches.get ( url );}
    public void  addBitmapToCache(String url, Bitmap mBitmap)
    {
        if (getBitmapFromCache ( url ) == null)
        {
            mCaches.put ( url, mBitmap );
        }
    }


    public Bitmap getBitMapFromUrl(String strUrl)
    {
        Bitmap mBitmap = null;
        try {
            HttpURLConnection mCom = (HttpURLConnection) new URL ( strUrl ).openConnection ();
            InputStream is = new BufferedInputStream ( mCom.getInputStream () );//包装类
            mBitmap = BitmapFactory.decodeStream ( is );
            mCom.disconnect ();
//            Thread.sleep ( 1000 );//模拟网络不好
        } catch (IOException e) {
            e.printStackTrace ( );
        }

        return  mBitmap;
    }

    public void loadImagesForStopScroll(int iStart, int iEnd) {
        for (int i = iStart; i<iEnd; i++)
        {
            String url = MyLoadDataAdapt.URLS[i];
            Bitmap mBitmap = getBitmapFromCache ( url );
            if (mBitmap == null)
            {
                Log.i ( MainActivity.TAG, "***************loadImagesForStopScroll: "+MyLoadDataAdapt.URLS[i].toString ()  );

                MyLoadImageAsyncTask asyncTask = new MyLoadImageAsyncTask ( url );
                asyncTask.execute ( url );
                mTasks.add ( asyncTask );
            }
            else
            {
                Log.i ( MainActivity.TAG, "---------------loadImagesForStopScroll: "+MyLoadDataAdapt.URLS[i].toString ()  );

                ImageView mIV = (ImageView)mListView.findViewWithTag ( url );
                mIV.setImageBitmap ( mBitmap );
            }
        }
        
    }

    public void canAsyncTask() {

        if (mTasks!=null&& mTasks.size ()!=0)
        {
            for (MyLoadImageAsyncTask f: mTasks
                    ) {
                f.cancel ( true );

            }
        }


    }

    class MyLoadImageAsyncTask extends AsyncTask<String, Void, Bitmap>
    {
        private String mUrl;

        public MyLoadImageAsyncTask( String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String mUrl = strings[0];
            Bitmap mBitmap = getBitMapFromUrl ( mUrl );
            if (mBitmap != null)
                addBitmapToCache ( mUrl, mBitmap );

            return mBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute ( bitmap );
            ImageView mIV = (ImageView)mListView.findViewWithTag ( mUrl );
            if (mIV !=null && bitmap != null)
                mIV.setImageBitmap ( bitmap );
            mTasks.remove ( this );
        }
    }

}
