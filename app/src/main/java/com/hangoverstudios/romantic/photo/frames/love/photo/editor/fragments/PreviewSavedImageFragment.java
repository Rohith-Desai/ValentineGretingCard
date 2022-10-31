package com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SaveShareImageActivity2;


public class PreviewSavedImageFragment extends Fragment {

    View view;
    ShapeableImageView previewImage;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.preview_image_layout, container, false);
        previewImage =  view.findViewById(R.id.preview_image);
        previewImage.setImageBitmap(((SaveShareImageActivity2)getActivity()).getSavedBitmap());
        previewImage.setShapeAppearanceModel(new ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED,50)
                .build());
        //previewImage.setStrokeWidth(5);
       // previewImage.setStrokeColor(ColorStateList.valueOf(getActivity().getColor(R.color.white)));
        return view;
    }
}
