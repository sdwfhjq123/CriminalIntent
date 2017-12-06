package com.yinhao.criminalintent.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.yinhao.criminalintent.R;

/**
 * Created by hp on 2017/12/6.
 */

public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.data_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
