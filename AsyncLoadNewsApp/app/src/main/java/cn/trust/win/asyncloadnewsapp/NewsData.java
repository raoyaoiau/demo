package cn.trust.win.asyncloadnewsapp;

/**
 * Created by xiaxu on 2017/12/25.
 */

public class NewsData {
    public String mName, mPicSmall, mPicBig, mDescription;
    public int mLearner;

    public NewsData(String mName, String mPicSmall, String mPicBig, String mDescription, int mLearner) {
        this.mName = mName;
        this.mPicSmall = mPicSmall;
        this.mPicBig = mPicBig;
        this.mDescription = mDescription;
        this.mLearner = mLearner;
    }

    public NewsData() {

    }
}
