package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingCardsPreviewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingsAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.ImageAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.GreetingCardTemplatesFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.DataHolder;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.billing.Security.verifyPurchase;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.commonMethods;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.populateUnifiedNativeAdView;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    ArrayList<Object> greetingPotList = new ArrayList<>();
    RecyclerView greeting_recycle,creation_recycle;
    TextView view_all_greetings,view_all_creations;
    private RecyclerView valentineCardsPreview;
    private GreetingCardsPreviewAdapter greetingCardsPreviewAdapter;
    private ArrayList<Integer> mResources;
    private String type;
    private TextView viewAll;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 201;
    private static final int PERMISSION__STORAGE_REQUEST_CODE = 200;
    LinearLayout selectPhoto, savedImages,rating_layout,creation_linear;
    Dialog mainDialog;
    boolean camera = false;
    boolean savedGallery = false;
    public static MainActivity mainActivity;
    private RelativeLayout myCreations;
    boolean showAd = true;
    private FrameLayout adViewContainer, cardTemplatesContainerPanel;
    private AdView adViewBanner1;
    private NativeAd nativeAd;
    private RelativeLayout alternativeToNativeAd;
    RelativeLayout scrollViewNative;
    public CardView more;
    public GreetingCardTemplatesFragment greetingCardTemplatesFragment;

    CardView remove_ads;
    private BillingClient billingClient;
    String PRODUCT_ID = "removeads";
    public static final String PREF_FILE = "PHOTO_COLLAGE";
    public static final String PURCHASE_KEY = "removeads";
    ImageView starOne, starTwo , starThree, starFour, starFive;
    ArrayList<String> imagesList;
    ImageAdapter imageAdapter;
    CardView creation_card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        remove_ads = findViewById(R.id.remove_ads);

        if (RemoteConfigValues.getOurRemote().getEnableIAPflag() != null) {
            if (RemoteConfigValues.getOurRemote().getEnableIAPflag().equals("true")) {
                remove_ads.setVisibility(View.VISIBLE);
            } else if (RemoteConfigValues.getOurRemote().getEnableIAPflag().equals("false")) {
                remove_ads.setVisibility(View.GONE);
            }
        }

        if (getPurchaseValueFromPref()) {
            commonMethods.disableAds();
            remove_ads.setVisibility(View.GONE);
        }

        remove_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPurchaseDialog();
            }
        });
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(INAPP);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else {
                        savePurchaseValueToPref(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });

        if (getIntent().hasExtra("showAd")) {
            showAd = getIntent().getBooleanExtra("showAd", true);
        }
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (showAd) {
                    CommonMethods.getInstance().onLaunchAD(MainActivity.this);
                }
            }
        }, 2000);*/
        if (showAd) {
            //Toast.makeText(MainActivity.this,"show",Toast.LENGTH_SHORT).show();
            CommonMethods.getInstance().onLaunchAD(MainActivity.this);
        }

        scrollViewNative = findViewById(R.id.main_native_ads_scroll);
        alternativeToNativeAd = findViewById(R.id.alternative_to_native_ad_main);
        if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                if (RemoteConfigValues.getOurRemote().getAdRotationPolicyNative().equals("true")) {
                    if (RemoteConfigValues.getOurRemote().getShowNativeAdOnMainScreen().equals("true")) {
                        alternativeToNativeAd.setVisibility(View.GONE);
                        scrollViewNative.setVisibility(View.VISIBLE);
                        admobAd();
                    }
                } else {
                    if (RemoteConfigValues.getOurRemote().getNativeOnlyGoogle().equals("true")) {
                        if (RemoteConfigValues.getOurRemote().getShowNativeAdOnMainScreen().equals("true")) {
                            alternativeToNativeAd.setVisibility(View.GONE);
                            scrollViewNative.setVisibility(View.VISIBLE);
                            admobAd();
                        }
                    }
                }
            }
        } else {
            alternativeToNativeAd.setVisibility(View.VISIBLE);
            scrollViewNative.setVisibility(View.GONE);
        }

        adViewContainer = findViewById(R.id.main_activity_banner_ad);
        adViewContainer.post(new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().loadBannerAd(adViewBanner1, adViewContainer, MainActivity.this);
            }
        });

        cardTemplatesContainerPanel = findViewById(R.id.card_templates_fragment);
        valentineCardsPreview = findViewById(R.id.valentine_card_preview_rv);
        setValentineCardPreviewAdapter();
        myCreations = findViewById(R.id.saved_images);
        myCreations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

        viewAll = findViewById(R.id.view_all);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addGreetingCardTemplatesFragment();
                    /*getSupportFragmentManager().beginTransaction()
                            .add(R.id.card_templates_fragment, new GreetingCardTemplatesFragment()).commit();*/

                Intent  i = new Intent(MainActivity.this,SingleFramesListActivity.class);
                startActivity(i);
            }
        });
       // more = findViewById(R.id.view_more_right);
       /* more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGreetingCardTemplatesFragment();

            }
        });*/
        // dialog = new Dialog(this);
        /*if(CommonMethods.getInstance().getShowPermission())
        {*/

        // }
        if (!checkStoragePermission() || !checkCameraPermission()) {
            showPermissionDialog();
        }


        view_all_greetings = findViewById(R.id.view_all_greetings);
        view_all_greetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GreetingsListActivity.class));
            }
        });
        greeting_recycle = findViewById(R.id.greeting_recycle);
        view_all_creations=findViewById(R.id.view_all_creations);
        creation_recycle=findViewById(R.id.creation_recycle);
        creation_card=findViewById(R.id.creation_card);
        creation_linear=findViewById(R.id.creation_linear);
        view_all_creations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });
        if (isDirectoryNotEmpty(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"+"/data/"+getPackageName()+"/files/" + getResources().getString(R.string.app_name)+"/"+getResources().getString(R.string.app_name))) {
            imagesList = getImagesFromDevice();
            if (imagesList != null && imagesList.size() > 0) {
                creation_card.setVisibility(View.GONE);
                creation_linear.setVisibility(VISIBLE);
            } else {
                creation_card.setVisibility(View.VISIBLE);
                creation_linear.setVisibility(GONE);
            }
            setImageAdapter(imagesList, false);
        } else {
            creation_card.setVisibility(View.VISIBLE);
            creation_linear.setVisibility(GONE);
        }
        prepareGreetingsList();


    }

    private void prepareGreetingsList() {
        greetingPotList.clear();
        greetingPotList  = DataHolder.getDataHolder().getGreetingsList();

        if(greetingPotList!=null){
            if(greetingPotList.size() > 0){
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                greeting_recycle.setLayoutManager(layoutManager);
                GreetingsAdapter greetingsAdapter = new GreetingsAdapter(greetingPotList, MainActivity.this, 1);
                greeting_recycle.setAdapter(greetingsAdapter);
            }
        }

        starOne = findViewById(R.id.starOne);
        starTwo = findViewById(R.id.starTwo);
        starThree = findViewById(R.id.starThree);
        starFour = findViewById(R.id.starFour);
        starFive = findViewById(R.id.starFive);
        rating_layout = findViewById(R.id.rating_layout);

        /*if (!CommonMethods.getInstance().ratingIsDone(MainActivity.this)) {
            rating_layout.setVisibility(View.VISIBLE);
        }else{
            rating_layout.setVisibility(View.GONE);
        }*/
        starFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingPlaystore();
            }
        });
        starFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingPlaystore();
            }
        });
        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });


    }

    public void showPermissionDialog() {
        mainDialog = new Dialog(MainActivity.this);
        mainDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mainDialog.setCancelable(false);
        mainDialog.setContentView(R.layout.permission_dialog);
        TextView msgTxt = mainDialog.findViewById(R.id.dialog_msg);
        TextView msgSubTxt = mainDialog.findViewById(R.id.dialog_msg_sub);
        msgTxt.setText("Storage Permission");
        msgSubTxt.setText("Need storage and camera\npermission to access your photos\nfrom gallery and take picture");
        TextView dialogOk = mainDialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = true;
                if (!checkCameraPermission()) {
                    requestCameraPermission();
                } else if (!checkStoragePermission()) {
                    requestStoragePermission();
                }
            }
        });
        //TextView dialogCancel = dialog.findViewById(R.id.dialog_cancel);
        /*dialogCancel.setVisibility(View.VISIBLE);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        mainDialog.show();
    }

    private void setValentineCardPreviewAdapter() {
        type = "valentine card";
        mResources = new ArrayList<>();

        mResources.add(R.drawable.valentine_card_1);

        mResources.add(R.drawable.valentine_card_2);
        mResources.add(R.drawable.valentine_card_3);
        mResources.add(R.drawable.valentine_card_4);
//        mResources.add(R.drawable.valentine_card_5);
//        mResources.add(R.drawable.valentine_card_6);
//        mResources.add(R.drawable.valentine_card_7);
//        mResources.add(R.drawable.valentine_card_8);
//        mResources.add(R.drawable.valentine_card_9);
//        mResources.add(R.drawable.valentine_card_10);
//        mResources.add(R.drawable.valentine_card_11);
//        mResources.add(R.drawable.valentine_card_12);
//        mResources.add(R.drawable.valentine_card_13);
//        mResources.add(R.drawable.valentine_card_14);
//        mResources.add(R.drawable.valentine_card_15);
//        mResources.add(R.drawable.valentine_card_16);
//        mResources.add(R.drawable.valentine_card_17);
//        mResources.add(R.drawable.valentine_card_18);
//        mResources.add(R.drawable.valentine_card_19);
//        mResources.add(R.drawable.valentine_card_20);
//        mResources.add(R.drawable.valentine_card_21);
//        mResources.add(R.drawable.valentine_card_22);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        valentineCardsPreview.setLayoutManager(layoutManager);
        greetingCardsPreviewAdapter = new GreetingCardsPreviewAdapter(MainActivity.this, mResources, type);
        valentineCardsPreview.setAdapter(greetingCardsPreviewAdapter);
        valentineCardsPreview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollHorizontally(1)) {
                   // more.setVisibility(VISIBLE);
                    setMoreAnimation(R.anim.left_to_right);
                } else {
                    setMoreAnimation(R.anim.right_to_left);
                    //more.setVisibility(GONE);
                }
            }
        });
    }

    private void setMoreAnimation(int res) {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this,
                res);
        animation.setRepeatMode(0);
       // more.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
              /*  if(more.getVisibility() == VISIBLE)
                    more.setVisibility(GONE);
                else
                    more.setVisibility(VISIBLE);*/

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void openGreetingCardTemplateFragment() {

        greetingCardTemplatesFragment = Utils.addGreetingCardTemplatesFragment(MainActivity.this, R.id.card_templates_fragment, true, null);
    }

    public void addGreetingCardTemplatesFragment() {
        //newElement.setClickable(false);
        Animation rightToLeft = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);
        //hiddenPanel = (FrameLayout) findViewById(R.id.new_hidden_fragment);
        cardTemplatesContainerPanel = (FrameLayout) findViewById(R.id.card_templates_fragment);
        cardTemplatesContainerPanel.setVisibility(VISIBLE);
        cardTemplatesContainerPanel.startAnimation(rightToLeft);
        rightToLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //cardTemplatesContainerPanel.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        openGreetingCardTemplateFragment();
    }

    public void removeGreetingCardTemplatesFragment(boolean setClickable) {
        Animation leftToRight = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        greetingCardTemplatesFragment = Utils.getGreetingCardTemplatesFragment(this);
        // newElement.setClickable(true);
        //bottomPanel.setVisibility(VISIBLE);
        //selectedCard.setClickable(setClickable);
        cardTemplatesContainerPanel.startAnimation(leftToRight);
        cardTemplatesContainerPanel.setVisibility(GONE);
        leftToRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(greetingCardTemplatesFragment).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //localGalleryFragment.onBackPressed();
        // CommonMethods.getInstance().activitiesAD(GreetingCardEditActivity.this);
    }

    public boolean checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION__STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    updateList(false);

                    if (readAccepted && writeAccepted) {
                        if (camera) {
                            mainDialog.dismiss();
                        } /*else {
                            if (savedGallery) {
                                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                            } else {
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryIntent.setType("image/*");
                                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
                            }

                        }*/
                    } else {
                        showStoragePermisionDialog();
                    }

                }
                break;
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        if (mainDialog.isShowing())
                            mainDialog.dismiss();
                    }
                } else {
                    showCamPermisionDialog();
                }
                break;
        }
    }

    private void showCamPermisionDialog() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this,R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setTitle(getString(R.string.permission_request));
        dialog.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        requestCameraPermission();

                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public boolean checkStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void requestStoragePermission() {

        ActivityCompat.requestPermissions((Activity) this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION__STORAGE_REQUEST_CODE);
    }

    private void showStoragePermisionDialog() {
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this,R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setTitle(getString(R.string.permission_request));
        dialog.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void admobAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_id));
        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isDestroyed();
                        }
                        if (isDestroyed || isFinishing() || isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (MainActivity.this.nativeAd != null) {
                            MainActivity.this.nativeAd.destroy();
                        }
                        MainActivity.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = findViewById(R.id.native_ad_main);
                        NativeAdView adView =
                                (NativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(true).build();

        com.google.android.gms.ads.nativead.NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if ((greetingCardTemplatesFragment != null) && (greetingCardTemplatesFragment.isVisible())) {

            removeGreetingCardTemplatesFragment(false);
        } else {
            startActivity(new Intent(MainActivity.this, ExitActivity.class));
            finish();
        }
        // CommonMethods.getInstance().activitiesAD(GreetingCardEditActivity.this);


    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
//if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(INAPP);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases);
            }
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getApplicationContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
            //if item is purchased
            if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(getApplicationContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    if (!getPurchaseValueFromPref()) {
                        savePurchaseValueToPref(true);
//                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        commonMethods.disableAds();
                        remove_ads.setVisibility(View.GONE);
                        this.recreate();
                    }
                }
            }
            //if purchase is pending
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getApplicationContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false);

                Toast.makeText(getApplicationContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            // To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArW30RHIYqfOharZg8QEPtr0Lt2mVrf3fX0+ohShed3B/uNXZFFdI3Rt3Xf0NXojTRcv9MMiVovGmIiz7M1f22wYgmi9gX4je0TOJOZn3VOuvdpsXmxowNFGpsS+UNv53QwIHPMkoK2A37jz+lE6odIbRfLgyl0U/5cRSMGisd+KI4ElQFJjzCgw3O5a0u+zsknFlQH6GbyV3mBl0ZEbhX6Ab1OKPsWWV3wN010HdGSjq4vGj2KQf6l47NGprXsPsive4iAGSXRvjpTUbYnOwEHO2BuPncscDxK6gDtLwwDPCzFcjO9XQp8cEGEA08Rr/mLw07zdINaXvP8bVeIiILwIDAQAB";
            return verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;

        }

    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                savePurchaseValueToPref(true);
                Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                MainActivity.this.recreate();
            }
        }
    };

    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    private void showPurchaseDialog() {
        //check if service is already connected
        if (billingClient.isReady()) {
            initiatePurchase();
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow(MainActivity.this, flowParams);
                            } else {
                                //try to add item/product id "purchase" inside managed product in google play console
                                Toast.makeText(getApplicationContext(), "Purchase Item not Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void ratingPlaystore() {
        //CommonMethods.getInstance().ratingDone(MainActivity.this);
        Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
        startActivity(ratingIntent);
    }

    private void openFeedback() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.MyCustomTheme);
        alertDialog.setTitle("App Feedback");
        alertDialog.setMessage("Please give us your feedback we will improve ");
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                /*Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","hangoverstudios.mobile@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject:Men Suit Editor Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));*/
             //   CommonMethods.getInstance().ratingDone(MainActivity.this);

                Intent intent = new Intent(Intent.ACTION_VIEW);

                Uri data = Uri.parse("mailto:hangoverstudios.mobile@gmail.com?subject= Romantic Photo Editor Feedback ");
                intent.setData(data);
                PackageManager packageManager = getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent);
                }
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public boolean isDirectoryNotEmpty(String directoryPath) {
        Log.e("TEST_1", "directoryPath" + directoryPath);
        try {
            File file = new File(directoryPath);
            if (file.exists() && file.isDirectory()) {
                if (file.exists() && file.list().length > 0) {
                    System.out.println("Directory is not empty!");
                    return true;
                } else {
                    System.out.println("Directory is empty!");
                    return false;
                }
            } else {
                System.out.println("This is not a directory");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<String> getImagesFromDevice() {
        final ArrayList<String> tempAudioList = new ArrayList<>();
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"+"/data/"+getPackageName()+"/files/" + getResources().getString(R.string.app_name)+"/"+getResources().getString(R.string.app_name);
       // String p=Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.hangoverstudios.video.flip.rotate/files/Video Rotate/Video Rotate";
        File directory1 = new File(directoryPath);
        File[] files = directory1.listFiles();
        Arrays.sort( files, new Comparator(){
            public int compare(Object o1, Object o2) {
                if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                    return -1;
                } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
        if (isDirectoryNotEmpty(directoryPath)) {
            for (int i = 0; i < files.length; i++) {
                tempAudioList.add(files[i].getAbsolutePath());
                Log.e("Files", "FileName:" + files[i].getAbsolutePath());
            }
            return tempAudioList;
        } else {
            return null;
        }
    }
    private void setImageAdapter(final ArrayList<String> imageFilesList, boolean selectAll) {
        imageAdapter = new ImageAdapter(MainActivity.this, imageFilesList, 1);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        creation_recycle.setLayoutManager(layoutManager);
        // imageRecycler.setDrawingCacheEnabled(true);
        // imageRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        creation_recycle.setAdapter(imageAdapter);
    }
    public void updateList(boolean selectAll) {
        if (isDirectoryNotEmpty(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android"+"/data/"+getPackageName()+"/files/" + getResources().getString(R.string.app_name)+"/"+getResources().getString(R.string.app_name))) {

            imagesList = getImagesFromDevice();
            if (getImagesFromDevice() != null) {
                creation_recycle.setVisibility(View.VISIBLE);
                creation_card.setVisibility(View.GONE);
                creation_linear.setVisibility(VISIBLE);
                setImageAdapter(imagesList, selectAll);
            } else {
                creation_recycle.setVisibility(View.GONE);
                creation_card.setVisibility(View.VISIBLE);
                creation_linear.setVisibility(GONE);
            }

        }
        else {

            creation_card.setVisibility(View.VISIBLE);
            creation_linear.setVisibility(GONE);
            creation_recycle.setVisibility(View.GONE);

        }

    }

}
