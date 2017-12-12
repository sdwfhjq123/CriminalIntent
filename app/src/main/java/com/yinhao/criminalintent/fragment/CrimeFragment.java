package com.yinhao.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.yinhao.criminalintent.R;
import com.yinhao.criminalintent.VO.Crime;
import com.yinhao.criminalintent.VO.CrimeLab;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yinhao on 2017/12/3.
 */

public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_CRIME_POSITION = "crime_position";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mChooseTimeButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private CheckBox mSolvedCheckBox;

    private int mPosition;

    public static CrimeFragment newInstance(UUID crimeId, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putInt(ARG_CRIME_POSITION, position);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当activity接收到回调时，通知fragmentManager调用此方法
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mPosition = getArguments().getInt(ARG_CRIME_POSITION);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //This one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                //DatePickerFragment目标fragment为CrimeFragment
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mChooseTimeButton = (Button) v.findViewById(R.id.crime_choose_time);
        mChooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fm, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));
//                startActivity(i);
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());
                builder.setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser();
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
            Toast.makeText(getActivity(), "have not contact app", Toast.LENGTH_SHORT).show();
        }

        mCallButton = (Button) v.findViewById(R.id.crime_call);
        handleCallSuspectButton(mCrime.getSuspectPhoneNum());
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + mCrime.getSuspectPhoneNum());
                Intent i = new Intent(Intent.ACTION_DIAL, number);
                startActivity(i);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_crime:
                CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
                crimeLab.removeCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_DATE:
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(date);
                updateDate();
                break;
            case REQUEST_TIME:
                Date date1 = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mCrime.setDate(date1);
                updateDate();
                break;
            case REQUEST_CONTACT:
                if (data != null) {
                    Uri contactUri = data.getData();
                    //Specify which fields you want your query to return 列举你想返回的字段
                    //values for
                    String[] queryFields = new String[]{
                            ContactsContract.Contacts.DISPLAY_NAME
                    };
                    String[] queryContactId = new String[]{
                            ContactsContract.Contacts._ID
                    };
                    //Perform you query -the contactUri is like a "where" 执行
                    //clause here 条款
                    Cursor cursor = getActivity().getContentResolver()
                            .query(contactUri, queryFields, null, null, null);
                    try {
                        //Double-check that you actually got results
                        if (cursor.getCount() == 0) {
                            return;
                        }
                        //Pull out the first column of the first row if data that is your suspect's name
                        cursor.moveToFirst();
                        String suspect = cursor.getString(0);
                        mCrime.setSuspect(suspect);
                    } finally {
                        cursor.close();
                    }

                    Cursor c = getActivity().getContentResolver()
                            .query(contactUri, queryContactId, null, null, null);
                    try {
                        if (c.getCount() == 0) {
                            return;
                        }
                        c.moveToFirst();
                        String contactId = c.getString(0);
                        String phoneNum = getPhoneNumById(contactId);
                        mCrime.setSuspectPhoneNum(phoneNum);
                        handleCallSuspectButton(phoneNum);
                    } finally {
                        c.close();
                    }
                }

                break;
        }
    }

    private String getPhoneNumById(String contactId) {
        // 首先找到需要查找的表的 URI
        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // 然后还是通过 ContentResolver 对数据进行请求
        // 跟数据库的操作十分类似
        Cursor c = getActivity().getContentResolver().query(
                phoneUri,
                // 要请求的数据是 NUMBER，即电话号码
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                // 相当于 Where 语句，注意这里不是 _ID，应该是 CONTACT_ID
                // 对于 Phone 表来说，CONTACT_ID 应该是其外键，_ID 是主键
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                new String[]{contactId},
                null
        );
        String phoneNumber = null;
        try {
            if (c.getCount() == 0) {
                return phoneNumber;
            }
            // 只取第一个数据
            c.moveToFirst();
            phoneNumber = c.getString(0);
        } finally {
            c.close();
        }
        return phoneNumber;
    }

    private void handleCallSuspectButton(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            mCallButton.setText(getString(R.string.crime_call_no_phone_number));
            mCallButton.setEnabled(false);
        } else {
            mCallButton.setText(getString(R.string.crime_call_suspect, phoneNumber));
            mCallButton.setEnabled(true);
        }
    }

    private void updateDate() {
        mDateButton.setText(getDateFormat(mCrime.getDate()));
    }

    public String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
