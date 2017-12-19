package cn.trust.win.newsshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by xiaxu on 2017/12/19.
 */


class NewsData
{
    private int image_id;
    private String mTitle;
    private String mContent;

    public NewsData(int image_id, String mTitle, String mContent) {
        this.image_id = image_id;
        this.mTitle = mTitle;
        this.mContent = mContent;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
}

public class MyAdapt extends BaseAdapter {
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    private Context mContext;
    private List<NewsData> mNewsList;

    public MyAdapt(Context mContext, List <NewsData> mNewsList) {
        this.mContext = mContext;
        this.mNewsList = mNewsList;
    }

    @Override
    public int getCount() {
        return mNewsList.size ();
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
        return mNewsList.get ( position );
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
        NewsData mNewsData = mNewsList.get(position);
        ViewHold mViewHold;
        View mView;
        if (convertView == null)
        {
            //这里LayoutInflater 什么鬼？
            mView = LayoutInflater.from ( mContext ).inflate ( R.layout.list_layout, null);
            mViewHold = new ViewHold ();
            mViewHold.mImage = (ImageView)mView.findViewById ( R.id.image );
            mViewHold.mTitle = (TextView) mView.findViewById ( R.id.title );
            mViewHold.mContent = (TextView) mView.findViewById ( R.id.content );
            mView.setTag ( mViewHold );
        }
        else
        {
            mView = convertView;
            mViewHold = (ViewHold) mView.getTag ();
        }
        mViewHold.mImage.setImageResource ( mNewsData.getImage_id ());
        mViewHold.mTitle.setText ( mNewsData.getmTitle ());
        mViewHold.mContent.setText ( mNewsData.getmContent ());

        return mView;
    }

    class ViewHold{
        private  ImageView mImage;
        private TextView  mTitle;
        private TextView mContent;
    }
}
