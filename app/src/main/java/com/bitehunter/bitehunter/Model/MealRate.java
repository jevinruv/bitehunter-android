package com.bitehunter.bitehunter.Model;

/**
 * Created by Pasan .M. Semage on 2018-01-23.
 */

public class MealRate {

    private int mCusID;
    private String mCusName;
    private String mDate;
    private int mRate;
    private String mReview;

    public MealRate(int mCusID, String mCusName, String mDate, int mRate, String mReview) {
        this.mCusID = mCusID;
        this.mCusName = mCusName;
        this.mDate = mDate;
        this.mRate = mRate;
        this.mReview = mReview;
    }

    public int getmCusID() {
        return mCusID;
    }

    public String getmCusName() {
        return mCusName;
    }

    public String getmDate() {
        return mDate;
    }

    public int getmRate() {
        return mRate;
    }

    public String getmReview() {
        return mReview;
    }
}
