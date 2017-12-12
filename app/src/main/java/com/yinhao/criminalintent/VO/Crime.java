package com.yinhao.criminalintent.VO;

import java.util.Date;
import java.util.UUID;

/**
 * Created by yinhao on 2017/12/3.
 */

public class Crime {
    //只读
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private String mSuspectPhoneNum;

    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID uuid) {
        mId = uuid;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getSuspectPhoneNum() {
        return mSuspectPhoneNum;
    }

    public void setSuspectPhoneNum(String suspectPhoneNum) {
        mSuspectPhoneNum = suspectPhoneNum;
    }
}
