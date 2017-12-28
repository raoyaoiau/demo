package cn.trust.win.asyncloadnewsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import cn.trust.win.asyncloadnewsapp.libcore.io.DiskLruCache;

/**
 * Created by xiaxu on 2017/12/25.
 */

public class ImageLoad {
    private ImageView mIV;
    private LruCache<String, Bitmap> mCaches;
    private String  mUrl;
    private Set<MyLoadImageAsyncTask> mTasks;
    private ListView mListView;
    private Context mContext;
    public static DiskLruCache mDiskLruCache = null;

    public ImageLoad(ListView mListView, Context mContext) {
        this.mListView = mListView;
        this.mContext = mContext;
                        /*
        本地缓存
         */

        try {
            File cacheDir = Utill.getDiskCacheDir(mContext, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(
                    cacheDir,
                    Utill.getAppVersion(mContext),
                    1,
                    10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }



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
        String url=null;
        Bitmap mBitmap = null;
        ImageView mIV = null;
        for (int i = iStart; i<iEnd; i++)
        {
             url = MyLoadDataAdapt.URLS[i];
            /*
            1, 从内存中读取图片，
             */
             mBitmap = getBitmapFromCache ( url );
             mIV = (ImageView)mListView.findViewWithTag ( url );
            if (mBitmap != null)
            {
                Log.i ( MainActivity.TAG, "11111111111111---------------loadImagesForStopScroll: "+MyLoadDataAdapt.URLS[i].toString ()  );


                mIV.setImageBitmap ( mBitmap );
            }
            else
            {
                /*
                2，从文件中读取
                 */
                String key = Utill.hashKeyForDisk ( url );
                try {
                    DiskLruCache.Snapshot mSnapShot = mDiskLruCache.get ( key );
                    if (mSnapShot!=null)
                    {
                        InputStream is = mSnapShot.getInputStream ( 0 );
                        mBitmap = BitmapFactory.decodeStream ( is );
                        if (mBitmap !=null)
                        {
                            Log.i ( MainActivity.TAG, "22222222222222222##############loadImagesForStopScroll: "+MyLoadDataAdapt.URLS[i].toString ()  );
                            mIV.setImageBitmap ( mBitmap );
                        }

                    }
                    else
                    {
                            /*
                            3,从网络上去下载图片
                             */
                        Log.i ( MainActivity.TAG, "3333333333333***************loadImagesForStopScroll: "+MyLoadDataAdapt.URLS[i].toString ()+"sss:"  );

                        MyLoadImageAsyncTask asyncTask = new MyLoadImageAsyncTask ( url );
                        asyncTask.execute ( url );
                        mTasks.add ( asyncTask );
                    }
                } catch (IOException e) {
                    e.printStackTrace ( );
                }




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
            /*
            缓存图片到内存
             */
            if (mBitmap != null)
                addBitmapToCache ( mUrl, mBitmap );

            /*
            根据url缓存图片到本地
             */
            String key = Utill.hashKeyForDisk(mUrl);
            DiskLruCache.Editor editor = null;
            try {
                editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (Utill.downloadImg(mUrl, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace ( );
            }
            /*

             }}}缓存图片到本地
             */
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
