package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.DoubleFrameAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.DoubleTrendingFrameAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;

public class Trending_Double_listActivity extends AppCompatActivity {
    Dialog myLoadingDialog;
    static final int STRING = 404;
    static final int NATIVE_UnifiedNativeAd = 10519;
    boolean adLoaded, dataLoaded;
    public static final int NUMBER_OF_AD_MOB_ADS = 4;
    //    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();

    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    RecyclerView double_trend_rv;

    ImageView back_arrow;
    ArrayList<Object> doubleTrendingThumbList = new ArrayList<>();
    ArrayList<Object> doubleTrendingFrameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending__double_list);
        CommonMethods.getInstance().frameSelectionAd(Trending_Double_listActivity.this);

        double_trend_rv= findViewById(R.id.double_trend_rv);
        back_arrow=findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        adContainerView = findViewById(R.id.admob_banner_trendingDoubleList);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                  if(RemoteConfigValues.getOurRemote().getRemoveAds() != null){
                    if(RemoteConfigValues.getOurRemote().getRemoveAds().equals("false")){
                        adContainerView.setVisibility(View.VISIBLE);
                        loadBanner();
                    }
                }
            }
        });

        showLoadingDialog();
        preparedoubleTrendingThumbListt();
       /* if(RemoteConfigValues.getOurRemote().getShowNativeAd() != null){
            if(RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")){
                loadAdMobNativeAds();
            }else{
                adLoaded = true;
                preparedoubleTrendingThumbListt();
            }
        }
        else {
            adLoaded = true;
            preparedoubleTrendingThumbListt();
        }*/
    }
    private void loadBanner() {

        adViewBanner1 = new AdView(this);
        adViewBanner1.setAdUnitId(getString(R.string.admob_banner_id));
        adContainerView.removeAllViews();
        adContainerView.addView(adViewBanner1);

        AdSize adSize = getAdSize(this, adContainerView);
        adViewBanner1.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adViewBanner1.loadAd(adRequest);

        adViewBanner1.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainerView.setBackgroundResource(0);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.e("aaaaaaaaaaaaaa", "onAdFailedToLoad: "+loadAdError);
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }
    /*private void loadAdMobNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAds.add(unifiedNativeAd);
                        if (!adLoader.isLoading()) {
                            adLoaded = true;
                            preparedoubleTrendingThumbListt();
                            System.out.println("XXX"+"loaded");
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            adLoaded = true;
                            preparedoubleTrendingThumbListt();
                        }
                        System.out.println("XXX"+"loaded");
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_AD_MOB_ADS);
    }*/

    private void showLoadingDialog() {
        myLoadingDialog = new Dialog(Trending_Double_listActivity.this);
        Window window = myLoadingDialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimationTransition;
        myLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myLoadingDialog.setCancelable(false);
        myLoadingDialog.setContentView(R.layout.fraems_loading);
        myLoadingDialog.setCancelable(false);
        myLoadingDialog.show();
    }

    private void hideLoadingDialog() {
        if (!this.isDestroyed()) {
            if (!isFinishing()) {
                if (myLoadingDialog != null) {
                    if (myLoadingDialog.isShowing()) {
                        myLoadingDialog.dismiss();
                    }
                }

            }
        }
    }
    private void preparedoubleTrendingThumbListt() {
        if(DataHolder.getDataHolder().getDoubleTrendingFramesList() != null &&
                DataHolder.getDataHolder().getDoubleTrendingThumbList() != null){
           /* doubleTrendingFrameList.addAll(DataHolder.getDataHolder().getDoubleTrendingFramesList());
            doubleTrendingFrameList.removeAll(Collections.singleton(null));
            DataHolder.getDataHolder().getDoubleTrendingFramesList().clear();
            DataHolder.getDataHolder().getDoubleTrendingFramesList().addAll(doubleTrendingFrameList);*/

            doubleTrendingFrameList.clear();
            doubleTrendingThumbList.clear();

            doubleTrendingThumbList.addAll(DataHolder.getDataHolder().getDoubleTrendingThumbList());
            doubleTrendingFrameList.addAll(DataHolder.getDataHolder().getDoubleTrendingFramesList());
           // dataLoaded = true;
            hideLoadingDialog();
            if (doubleTrendingThumbList.size() > 0) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(Trending_Double_listActivity.this, 1);
                double_trend_rv.setLayoutManager(gridLayoutManager);
                final DoubleTrendingFrameAdapter doubleFrameAdapter = new DoubleTrendingFrameAdapter(doubleTrendingThumbList, this, 2);
//                gridLayoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        switch( doubleFrameAdapter.getItemViewType(position) ) {
//                            case STRING :
//                                return 1;
//                            case NATIVE_UnifiedNativeAd :
//                                return 1;
//                            default:
//                                return -1;
//                        }
//                    }
//                });
                double_trend_rv.setAdapter(doubleFrameAdapter);

            }
          /*  if (adLoaded && dataLoaded) {


                //add ad to cautionList
                if (doubleTrendingThumbList.size() > 0 && mNativeAds.size() > 0 && doubleTrendingThumbList.size() > 2) {
                    doubleTrendingThumbList.add(1, mNativeAds.get(0));
                    doubleTrendingFrameList.add(1,null);
                }
                if (doubleTrendingThumbList.size() > 5 && mNativeAds.size() > 1) {
                    doubleTrendingThumbList.add(5, mNativeAds.get(1));
                    doubleTrendingFrameList.add(5,null);
                }
                if (doubleTrendingThumbList.size() > 9 && mNativeAds.size() > 2) {
                    doubleTrendingThumbList.add(9, mNativeAds.get(2));
                    doubleTrendingFrameList.add(9,null);
                }
                if (doubleTrendingThumbList.size() > 13 && mNativeAds.size() > 3) {
                    doubleTrendingThumbList.add(13, mNativeAds.get(3));
                    doubleTrendingFrameList.add(13,null);
                }
                if(doubleTrendingThumbList.size() != DataHolder.getDataHolder().getDoubleTrendingFramesList().size()){
                    DataHolder.getDataHolder().getDoubleTrendingFramesList().clear();
                    DataHolder.getDataHolder().getDoubleTrendingFramesList().addAll(doubleTrendingFrameList);
                }
                if (doubleTrendingThumbList.size() > 0) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Trending_Double_listActivity.this, 1);
                    double_trend_rv.setLayoutManager(gridLayoutManager);
                    final DoubleTrendingFrameAdapter doubleFrameAdapter = new DoubleTrendingFrameAdapter(doubleTrendingThumbList, this, 2);
//                gridLayoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        switch( doubleFrameAdapter.getItemViewType(position) ) {
//                            case STRING :
//                                return 1;
//                            case NATIVE_UnifiedNativeAd :
//                                return 1;
//                            default:
//                                return -1;
//                        }
//                    }
//                });
                    double_trend_rv.setAdapter(doubleFrameAdapter);

                }

            }*/
        }

    }

    @Override
    protected void onDestroy() {
        hideLoadingDialog();
        super.onDestroy();
    }
}