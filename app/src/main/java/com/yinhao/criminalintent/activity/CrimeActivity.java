package com.yinhao.criminalintent.activity;

import android.support.v4.app.Fragment;

import com.yinhao.criminalintent.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new CrimeFragment();
    }
}
