package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

    private NativeAdView adView;

    public NativeAdView getAdView() {
        return adView;
    }

    public UnifiedNativeAdViewHolder(View view) {
        super(view);
        adView = (NativeAdView) view.findViewById(R.id.ad_view);


        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
    }
}