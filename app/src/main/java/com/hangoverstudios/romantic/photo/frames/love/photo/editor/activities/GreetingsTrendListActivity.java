package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingTrendingAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;

public class GreetingsTrendListActivity extends AppCompatActivity {
    Dialog myLoadingDialog;
    public static final int NUMBER_OF_AD_MOB_ADS = 4;
    static final int STRING = 404;
    static final int NATIVE_UnifiedNativeAd = 10519;
    boolean adLoaded, dataLoaded;
    RecyclerView greeting_trend_thumb_rv;
    ImageView back_arrow;
    ArrayList<Object> greetingTrendThumbList = new ArrayList<>();
    ArrayList<Object> greetingTrendFrameList = new ArrayList<>();
    //    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private FrameLayout adContainerView;
    private AdView adViewBanner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings_trend_list);

        CommonMethods.getInstance().frameSelectionAd(GreetingsTrendListActivity.this);
        greeting_trend_thumb_rv = findViewById(R.id.greeting_trend_thumb_rv);
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adContainerView = findViewById(R.id.admob_banner_trendingGreetingList);
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
        prepareSingleFramesList();
       /* if(RemoteConfigValues.getOurRemote().getShowNativeAd() != null){
            if(RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")){
                loadAdMobNativeAds();
            }else{
                adLoaded = true;
                prepareSingleFramesList();
            }
        }
        else {
            adLoaded = true;
            prepareSingleFramesList();
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

    private void showLoadingDialog() {
        myLoadingDialog = new Dialog(GreetingsTrendListActivity.this);
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
                            prepareSingleFramesList();
                            System.out.println("XXX" + "loaded");
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
                            prepareSingleFramesList();
                        }
                        System.out.println("XXX" + "loaded");
                    }
                }).build();

        // Load the Native ads.
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_AD_MOB_ADS);
    }*/

    private void prepareSingleFramesList() {
        if(DataHolder.getDataHolder().getGreetingTrendList() != null &&
        DataHolder.getDataHolder().getGreetingTrendThumbList() != null){
          /*  greetingTrendFrameList.addAll(DataHolder.getDataHolder().getGreetingTrendList());
            greetingTrendFrameList.removeAll(Collections.singleton(null));
            DataHolder.getDataHolder().getGreetingTrendList().clear();
            DataHolder.getDataHolder().getGreetingTrendList().addAll(greetingTrendFrameList);*/

            greetingTrendFrameList.clear();
            greetingTrendThumbList.clear();
            greetingTrendThumbList = DataHolder.getDataHolder().getGreetingTrendThumbList();
            greetingTrendFrameList.addAll(DataHolder.getDataHolder().getGreetingTrendList());
           // dataLoaded = true;
            hideLoadingDialog();
            if (greetingTrendThumbList.size() > 0) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(GreetingsTrendListActivity.this, 2);
                greeting_trend_thumb_rv.setLayoutManager(gridLayoutManager);
                final GreetingTrendingAdapter SingleFrameAdapter = new GreetingTrendingAdapter(greetingTrendThumbList, this, 2);
                gridLayoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch( SingleFrameAdapter.getItemViewType(position) ) {
                            case STRING :
                                return 1;
                            case NATIVE_UnifiedNativeAd :
                                return 2;
                            default:
                                return -1;
                        }
                    }
                });
                greeting_trend_thumb_rv.setAdapter(SingleFrameAdapter);

            }
           /* if (adLoaded && dataLoaded) {


                //add ad to cautionList
                if (greetingTrendThumbList.size() > 0 && mNativeAds.size() > 0 && greetingTrendThumbList.size() > 2) {
                    greetingTrendThumbList.add(2, mNativeAds.get(0));
                    greetingTrendFrameList.add(2,null);
                }
                if (greetingTrendThumbList.size() > 7 && mNativeAds.size() > 1) {
                    greetingTrendThumbList.add(7, mNativeAds.get(1));
                    greetingTrendFrameList.add(7,null);
                }
                if (greetingTrendThumbList.size() > 12 && mNativeAds.size() > 2) {
                    greetingTrendThumbList.add(12, mNativeAds.get(2));
                    greetingTrendFrameList.add(12,null);
                }
                if (greetingTrendThumbList.size() > 17 && mNativeAds.size() > 3) {
                    greetingTrendThumbList.add(17, mNativeAds.get(3));
                    greetingTrendFrameList.add(17,null);
                }
                if(greetingTrendThumbList.size() != DataHolder.getDataHolder().getGreetingTrendList().size()){
                    DataHolder.getDataHolder().getGreetingTrendList().clear();
                    DataHolder.getDataHolder().getGreetingTrendList().addAll(greetingTrendFrameList);
                }
                if (greetingTrendThumbList.size() > 0) {
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(GreetingsTrendListActivity.this, 2);
                    greeting_trend_thumb_rv.setLayoutManager(gridLayoutManager);
                    final GreetingTrendingAdapter SingleFrameAdapter = new GreetingTrendingAdapter(greetingTrendThumbList, this, 2);
                    gridLayoutManager.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch( SingleFrameAdapter.getItemViewType(position) ) {
                                case STRING :
                                    return 1;
                                case NATIVE_UnifiedNativeAd :
                                    return 2;
                                default:
                                    return -1;
                            }
                        }
                    });
                    greeting_trend_thumb_rv.setAdapter(SingleFrameAdapter);

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