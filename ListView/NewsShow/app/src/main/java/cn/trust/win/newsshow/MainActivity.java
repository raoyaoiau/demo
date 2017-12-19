package cn.trust.win.newsshow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class MainActivity extends Activity {
    private static final String TAG = "xiaxu";
    private  ListView mLv;
    private List<Map<String, Object>> data = new ArrayList <> (  );
    private List<NewsData> mNewsList = new ArrayList <> (  );
    private SimpleAdapter mSimpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        ListView mLv = (ListView)findViewById ( R.id.lv );
        initData();

        mLv.setAdapter ( new MyAdapt ( this, mNewsList ) );
        mLv.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Log.i ( TAG, "onItemClick: "+id );
                Toast.makeText ( getApplicationContext (), String.format ( "您点击了第%d行position:%d", id, position ), Toast.LENGTH_SHORT ).show ( );
            }
        } );
/*        mSimpleAdapter = new SimpleAdapter ( this, data, R.layout.list_layout, new String[]{"image", "title", "content"}, new int[]{
                R.id.image, R.id.title, R.id.content
        } );
        mLv.setAdapter ( mSimpleAdapter );*/

    }


    void initData()
    {
        Log.i ( TAG, "initData: " + Integer.toString ( R.mipmap.pins_001 ) );
        Vector<Integer> mImages= new Vector <> (  );
        mImages.add ( R.mipmap.pins_001 );
        mImages.add ( R.mipmap.pins_002 );
        mImages.add ( R.mipmap.pins_003 );
        mImages.add ( R.mipmap.pins_004 );
        mImages.add ( R.mipmap.pins_005 );
        mImages.add ( R.mipmap.pins_006 );
        mImages.add ( R.mipmap.pins_007 );
        mImages.add ( R.mipmap.pins_008 );

        for (int i=0; i<8; i++)
        {
            mNewsList.add (  new NewsData (mImages.get ( i ), "新闻"+i, "内容"+i) );
        }

        for (int i= 0 ; i< 8; i++)
        {
            HashMap<String,Object> map = new HashMap <> (  );
            map.put ( "image", mImages.get ( i ));
            map.put ( "title", "新闻"+i );
            map.put ( "content", "xxxxxxxxxxxxxxxxxxx"+i );
            data.add ( map );
        }
    }

        /**
         * Constructor
         *
         * @param context  The context where the View associated with this SimpleAdapter is running
         * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
         *                 Maps contain the data for each row, and should include all the entries specified in
         *                 "from"
         * @param resource Resource identifier of a view layout that defines the views for this list
         *                 item. The layout file should include at least those named views defined in "to"
         * @param from     A list of column names that will be added to the Map associated with each
         *                 item.
         * @param to       The views that should display column in the "from" parameter. These should all be
         *                 TextViews. The first N views in this list are given the values of the first N columns
         */

}
