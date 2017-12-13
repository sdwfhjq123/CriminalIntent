package com.yinhao.criminalintent.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import com.yinhao.criminalintent.R;
import com.yinhao.criminalintent.utils.PictureUtils;

/**
 * Created by hp on 2017/12/13.
 */

public class GlancePictureFragment extends DialogFragment {

    private static final String ARG_PATH = "path";
    private ImageView mImage;

    // 由于文件比较大，所以将文件路径传入即可
    public static GlancePictureFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        GlancePictureFragment fragment = new GlancePictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用 getArguments() 方法取出照片文件路径
        String path = getArguments().getString(ARG_PATH);

        // 这个新的 style 其实就做了一件事，那就是使窗口全屏
        // 注意如果继承了 @android:Theme.Dialog 的话，窗口
        // 大小就限定了，所以我没有继承
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
        // 这个 layout 中只有一个 ImageView
        dialog.setContentView(R.layout.dialog_glance_picture);

        mImage = (ImageView) dialog.findViewById(R.id.imageview);
        // 仍然使用 PictureUtils 类的工具来获得缩放的 Bitmap
        mImage.setImageBitmap(
                PictureUtils.getScaledBitmap(path, getActivity()));
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击图片则退出该 dialog
                dialog.dismiss();
            }
        });
        return dialog;
    }
}
