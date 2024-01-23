package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.javinindia.nismwa.R;
import com.javinindia.nismwa.utility.TouchImageView;
import com.javinindia.nismwa.utility.Utility;

/**
 * Created by Ashish on 04-08-2017.
 */

public class FullSizeImageFragment extends DialogFragment {
    String img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        img = getArguments().getString("img");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            getArguments().clear();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fullscreen_preview, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        TouchImageView imageViewPreview = view.findViewById(R.id.image_preview);
        final ProgressBar progressBar = view.findViewById(R.id.progress);
        Utility.imageLoadGlideLibrary(getActivity(), progressBar, imageViewPreview, img);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

}
