package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.SingleFrameAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.getAdSize;

public class SingleFramesListActivity extends AppCompatActivity {

    public static final int NUMBER_OF_AD_MOB_ADS = 4;
    static final int STRING = 404;
    static final int NATIVE_UnifiedNativeAd = 10519;
    boolean adLoaded, dataLoaded;
    RecyclerView card_templates_rv;
    ImageView back_arrow;
    private List<Object> singleFramesLis = new ArrayList<>();
    ArrayList<Object> singleFramesThumbList = new ArrayList<>();
    Dialog myLoadingDialog;
    //    private NativeAdsManager mNativeAdsManager;
    private AdLoader adLoader;
    // List of native ads that have been successfully loaded.
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private FrameLayout adContainerView;
    private AdView adViewBanner1;
    private ArrayList<Integer> mResources;
    private String type;
    SingleFrameAdapter SingleFrameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_frames_list);
        mNativeAds.clear();
        singleFramesLis.clear();
        CommonMethods.getInstance().frameSelectionAd(SingleFramesListActivity.this);
        card_templates_rv = findViewById(R.id.card_templates_rv);
        back_arrow = findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adContainerView = findViewById(R.id.admob_banner_singleList);
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
      /*  if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                loadAdMobNativeAds();
            } else {
                adLoaded = true;
                prepareSingleFramesList();
                System.out.println("XXX - a");
            }
        } else {
            adLoaded = true;
            prepareSingleFramesList();
            System.out.println("XXX - b");
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("XXX - onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("XXX - onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("XXX - onResume");
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
        mNativeAds.clear();
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
                            System.out.println("XXX - c");
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
                            System.out.println("XXX - d");
                            prepareSingleFramesList();

                        }
                        System.out.println("XXX" + "loaded");
                    }
                }).build();

        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_AD_MOB_ADS);
    }*/

    private void showLoadingDialog() {
        myLoadingDialog = new Dialog(SingleFramesListActivity.this);
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

    private void prepareSingleFramesList() {
        if(DataHolder.getDataHolder().getSingleFramesList() != null){
           /* singleFramesLis.clear();
            System.out.println("XXX - 1");
            singleFramesLis.addAll(DataHolder.getDataHolder().getSingleFramesList());
            System.out.println("XXX - size 1: "+ singleFramesLis.size());

            singleFramesLis.removeAll(Collections.singleton(null));
            DataHolder.getDataHolder().getSingleFramesList().clear();
            DataHolder.getDataHolder().getSingleFramesList().addAll(singleFramesLis);*/
            String from = "";
            singleFramesLis.clear();
            singleFramesThumbList.clear();

            if(getIntent().hasExtra("from")){
                if(getIntent().getStringExtra("from").equals("anniversary")){
                    from = "anniversary";
                    singleFramesThumbList.addAll(DataHolder.getDataHolder().getThumbAnniversary());
                    singleFramesLis.addAll(DataHolder.getDataHolder().getAnniversary());
                }else if(getIntent().getStringExtra("from").equals("birthday")){
                    from = "birthday";
                    singleFramesThumbList.addAll(DataHolder.getDataHolder().getThumbBirthday());
                    singleFramesLis.addAll(DataHolder.getDataHolder().getBirthday());
                }else if(getIntent().getStringExtra("from").equals("flower")){
                    from = "flower";
                    singleFramesThumbList.addAll(DataHolder.getDataHolder().getThumbFlowers());
                    singleFramesLis.addAll(DataHolder.getDataHolder().getFlowers());
                }else if(getIntent().getStringExtra("from").equals("quote")){
                    from = "quote";
                    singleFramesThumbList.addAll(DataHolder.getDataHolder().getThumbQuotes());
                    singleFramesLis.addAll(DataHolder.getDataHolder().getQuotes());
                }
            }else{
                from = "";
                singleFramesThumbList.addAll(DataHolder.getDataHolder().getSingleFramesThumbList());
                singleFramesLis.addAll(DataHolder.getDataHolder().getSingleFramesList());
            }


//            for(int i = 0; i < 5; i++)
//            {
//                singleFramesLis.add(i,null);
//            }

           // dataLoaded = true;
            hideLoadingDialog();
            System.out.println("XXX - 2 ");
            if (singleFramesLis.size() > 0) {
                System.out.println("XXX - 7");
                card_templates_rv.setHasFixedSize(true);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(SingleFramesListActivity.this, 2);
                card_templates_rv.setLayoutManager(gridLayoutManager);
                SingleFrameAdapter = new SingleFrameAdapter(singleFramesThumbList, this, 2, from);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        switch (SingleFrameAdapter.getItemViewType(position)) {
                            case STRING:
                                return 1;
                            case NATIVE_UnifiedNativeAd:
                                return 2;
                            default:
                                return -1;
                        }
                    }
                });
                card_templates_rv.setAdapter(SingleFrameAdapter);
            }
           /* if (adLoaded && dataLoaded) {



                if (singleFramesLis.size() > 0 && mNativeAds.size() > 0 && singleFramesThumbList.size() > 2) {
                    singleFramesThumbList.add(2, mNativeAds.get(0));
                    singleFramesLis.add(2, null);
                    System.out.println("XXX - 3");
                }
                if (singleFramesLis.size() > 7 && mNativeAds.size() > 1) {
                    singleFramesThumbList.add(7, mNativeAds.get(1));
                    singleFramesLis.add(7, null);
                    System.out.println("XXX - 4");
                }
                if (singleFramesLis.size() > 12 && mNativeAds.size() > 2) {
                    singleFramesThumbList.add(12, mNativeAds.get(2));
                    singleFramesLis.add(12, null);
                    System.out.println("XXX - 5");
                }
                if (singleFramesLis.size() > 17 && mNativeAds.size() > 3) {
                    singleFramesThumbList.add(17, mNativeAds.get(3));
                    singleFramesLis.add(17, null);
                    System.out.println("XXX - 6");
                }

                if(singleFramesThumbList.size() != DataHolder.getDataHolder().getSingleFramesList().size()){
                    DataHolder.getDataHolder().getSingleFramesList().clear();
                    DataHolder.getDataHolder().getSingleFramesList().addAll(singleFramesLis);
                }

                System.out.println("XXX - size 2 :"+ singleFramesLis.size());
                if (singleFramesLis.size() > 0) {
                    System.out.println("XXX - 7");
                    card_templates_rv.setHasFixedSize(true);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(SingleFramesListActivity.this, 2);
                    card_templates_rv.setLayoutManager(gridLayoutManager);
                    SingleFrameAdapter = new SingleFrameAdapter(singleFramesThumbList, this, 2);
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch (SingleFrameAdapter.getItemViewType(position)) {
                                case STRING:
                                    return 1;
                                case NATIVE_UnifiedNativeAd:
                                    return 2;
                                default:
                                    return -1;
                            }
                        }
                    });
                    card_templates_rv.setAdapter(SingleFrameAdapter);
                }
            }*/
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("XXX - onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("XXX - onStop");
    }

    @Override
    protected void onDestroy() {
        hideLoadingDialog();
        super.onDestroy();
        System.out.println("XXX - onDestroy");
    }
}