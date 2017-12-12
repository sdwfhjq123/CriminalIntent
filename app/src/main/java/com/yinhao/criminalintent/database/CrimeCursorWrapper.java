package com.yinhao.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.yinhao.criminalintent.VO.Crime;
import com.yinhao.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hp on 2017/12/12.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        //这里是从得到的 CursorWrapper 中取出数据
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));
        String suspectPhoneNum = getString(getColumnIndex(CrimeTable.Cols.PHONE));

        // 然后生成一个 Model 层对象返回，免去了重复写的繁琐
        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        crime.setSuspectPhoneNum(suspectPhoneNum);

        return crime;
    }
}
