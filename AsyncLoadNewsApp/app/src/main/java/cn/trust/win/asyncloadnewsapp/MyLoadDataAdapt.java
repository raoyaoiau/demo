package cn.trust.win.asyncloadnewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by xiaxu on 2017/12/25.
 */

public class MyLoadDataAdapt extends BaseAdapter implements AbsListView.OnScrollListener{
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    private List<NewsData> mNewsDataList;
    private ImageLoad mImageLoad;
    private Context mContext;
    public static String[] URLS;
    private  int iStart, iEnd;

    private  boolean bFirstIn;

    public MyLoadDataAdapt(List <NewsData> mNewsDataList, Context mContext, ListView mListView) {
        this.mNewsDataList = mNewsDataList;
        this.mContext = mContext;

        mImageLoad = new ImageLoad (mListView);
        URLS = new String[ mNewsDataList.size () ];
        for (int i=0; i< mNewsDataList.size (); i++)
        {
            URLS[i] = mNewsDataList.get ( i ).mPicSmall;

        }
        mListView.setOnScrollListener ( this );
        bFirstIn = true;

    }



    @Override
    public int getCount() {
        return mNewsDataList.size ();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mNewsDataList.get ( position );
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (null == convertView)
        {
            //这里inflate的第二个参数为啥要是null,有什么意义。
            convertView = LayoutInflater.from ( mContext ).inflate ( R.layout.item_listview, null);
            mViewHolder = new ViewHolder ();
            mViewHolder.mPicControl = (ImageView)convertView.findViewById ( R.id.mImageView );
            mViewHolder.mLearnerControl = (TextView)convertView.findViewById ( R.id.mLearner );
            mViewHolder.mNameControl = (TextView)convertView.findViewById ( R.id.mName );
            mViewHolder.mDesciptControl = (TextView)convertView.findViewById ( R.id.mDescription );
            convertView.setTag ( mViewHolder );
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag ();
        }

        NewsData mNewsData = mNewsDataList.get(position);

        mViewHolder.mPicControl.setTag ( mNewsData.mPicSmall );
        mImageLoad.showImageByAsync ( mViewHolder.mPicControl, mNewsData.mPicSmall );


      //  mViewHolder.mPicControl.setImageResource (R.mipmap.ic_launcher );
//        Log.e ( MainActivity.TAG, "getView: name>>>>>>:"+mNewsData.mName+mNewsData.mPicSmall);
//        new ImageLoad ( mListView ).loadImages ( mViewHolder.mPicControl,  mNewsData.mPicSmall);
//        mImageLoad.loadImages (mViewHolder.mPicControl, mNewsData.mPicSmall );

//        new ImageLoad ( mListView ).showImageByThread ( mViewHolder.mPicControl, mNewsData.mPicSmall, mNewsData.mName );


        mViewHolder.mLearnerControl.setText ( Integer.toString ( mNewsData.mLearner ));
        mViewHolder.mNameControl.setText (mNewsData.mName);
        mViewHolder.mDesciptControl.setText ( mNewsData.mDescription );

        return convertView;
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if ( scrollState == SCROLL_STATE_IDLE )
        {
            mImageLoad.loadImagesForStopScroll (iStart, iEnd);
        }
        else
        {
            mImageLoad.canAsyncTask();
        }
    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        iStart = firstVisibleItem;
        iEnd = firstVisibleItem+visibleItemCount;
        if (bFirstIn && totalItemCount > 0)
        {
            bFirstIn = false;
            mImageLoad.loadImagesForStopScroll ( iStart, iEnd );
        }

    }

    class ViewHolder{
        private ImageView mPicControl;
        private TextView mLearnerControl;
        private TextView mNameControl;
        private TextView mDesciptControl;
    }
}
