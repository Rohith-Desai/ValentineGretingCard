package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

public class ChangeBackgroundData {

    public static final ChangeBackgroundData changeBackgroundData = new ChangeBackgroundData();
    private View backgroundImageView;
    private View sfBackgroundImageView;
    private View displayingBackground;
    private TextView doneTxt;
    private ImageView doneImg;
    private TextView freeTxt;
    private ImageView freeImg;
    private RecyclerView imageRecyclerView;
    private Bitmap saveFinishedBitmap;
    private boolean isBackgroundApplied = false;

    public boolean isBackgroundApplied() {
        return isBackgroundApplied;
    }

    public void setBackgroundApplied(boolean backgroundApplied) {
        isBackgroundApplied = backgroundApplied;
    }

    public Bitmap getSaveFinishedBitmap() {
        return saveFinishedBitmap;
    }

    public void setSaveFinishedBitmap(Bitmap saveFinishedBitmap) {
        this.saveFinishedBitmap = saveFinishedBitmap;
    }

    public static ChangeBackgroundData getChangeBackgroundData() {
        return changeBackgroundData;
    }

    public void setBackgroundImageView(View backgroundImageView) {
        this.backgroundImageView = backgroundImageView;
    }

    public void setColorFilter(Context context,ImageView imageView, TextView textView) {
        imageView.setColorFilter(context.getResources().getColor(R.color.blue_2));
        textView.setTextColor(context.getResources().getColor(R.color.blue_2));
    }

    public void removeColorFilter(Context context, ImageView imageView, TextView textView) {
        imageView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        imageView.setColorFilter(context.getResources().getColor(R.color.transparent));
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
    }

    public TextView getDoneTxt() {
        return doneTxt;
    }

    public void setDoneTxt(TextView doneTxt) {
        this.doneTxt = doneTxt;
    }

    public ImageView getDoneImg() {
        return doneImg;
    }

    public void setDoneImg(ImageView doneImg) {
        this.doneImg = doneImg;
    }

    public TextView getFreeTxt() {
        return freeTxt;
    }

    public void setFreeTxt(TextView freeTxt) {
        this.freeTxt = freeTxt;
    }

    public ImageView getFreeImg() {
        return freeImg;
    }

    public void setFreeImg(ImageView freeImg) {
        this.freeImg = freeImg;
    }

    public RecyclerView getImageRecyclerView() {
        return imageRecyclerView;
    }

    public void setImageRecyclerView(RecyclerView imageRecyclerView) {
        this.imageRecyclerView = imageRecyclerView;
    }
    public View getSfBackgroundImageView() {
        return sfBackgroundImageView;
    }

    public void setSfBackgroundImageView(View sfBackgroundImageView) {
        this.sfBackgroundImageView = sfBackgroundImageView;
    }
    public View getDisplayingBackground() {
        return displayingBackground;
    }

    public void setDisplayingBackground(View displayingBackground) {
        this.displayingBackground = displayingBackground;
    }
}
