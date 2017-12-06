package com.yinhao.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yinhao.criminalintent.R;
import com.yinhao.criminalintent.VO.Crime;
import com.yinhao.criminalintent.VO.CrimeLab;
import com.yinhao.criminalintent.fragment.CrimeFragment;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String TAG = "CrimePagerActivity";
    private static final String EXTRA_CRIME_ID = "com.yinhao.criminalintent.activity.crime_id";

    private ViewPager mViewPager;
    private Button mCrimeJumpToFirstButton;
    private Button mCrimeJumpToLastButton;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    mCrimeJumpToFirstButton.setClickable(false);
                }
                if (position == mCrimes.size() - 1) {
                    mCrimeJumpToLastButton.setClickable(false);
                }
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        //跳转到第一个
        mCrimeJumpToFirstButton = (Button) findViewById(R.id.crime_jump_to_first);
        mCrimeJumpToFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrimeJumpToFirstButton.setClickable(true);
                mViewPager.setCurrentItem(0);
            }
        });

        //跳转到最后一个
        mCrimeJumpToLastButton = (Button) findViewById(R.id.crime_jump_to_last);
        mCrimeJumpToLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrimeJumpToLastButton.setClickable(true);
                mViewPager.setCurrentItem(mCrimes.size() - 1);
            }
        });
    }
}
