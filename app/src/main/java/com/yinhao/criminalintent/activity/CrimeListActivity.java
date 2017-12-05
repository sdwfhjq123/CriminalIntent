package com.yinhao.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.yinhao.criminalintent.fragment.CrimeListFragment;

/**
 * Created by hp on 2017/12/5.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }
}
