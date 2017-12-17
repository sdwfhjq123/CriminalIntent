package com.yinhao.criminalintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.yinhao.criminalintent.R;
import com.yinhao.criminalintent.VO.Crime;
import com.yinhao.criminalintent.fragment.CrimeFragment;
import com.yinhao.criminalintent.fragment.CrimeListFragment;

/**
 * Created by hp on 2017/12/5.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;

    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId(), 0);
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId(), 0);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
