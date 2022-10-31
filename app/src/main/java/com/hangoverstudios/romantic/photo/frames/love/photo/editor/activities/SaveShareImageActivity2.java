package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;*/
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.imageview.ShapeableImageView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.PreviewSavedImageFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.ChangeBackgroundData;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
/*import com.hangoverstudios.romantic.photo.frames.love.photo.editor.image.ImageUtility;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Constants;*/
import com.google.android.gms.ads.AdView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods.populateUnifiedNativeAdView;

public class SaveShareImageActivity2 extends AppCompatActivity {

    private Bundle bundle;
    private String imagePath;
    private ImageView back;
    private FrameLayout adViewContainer;
    private AdView adViewBanner3;
    private ShapeableImageView imageViewPreview;
    private AppCompatImageView imgBack, imgHome, previewImg;
    private FrameLayout previewSavedImageFragmentContainer;
    private PreviewSavedImageFragment previewSavedImageFragment;
    private Bitmap savedBitmap;
    private ImageView hiddenForegroundImage;
    private View hiddenBackground;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_share_image_activity);
        CommonMethods.getInstance().activitiesAD(SaveShareImageActivity2.this);
        /*adViewContainer = findViewById(R.id.save_share_banner_ad);
        adViewContainer.post(new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().loadBannerAd(adViewBanner3, adViewContainer, SaveShareImageActivity2.this);
            }
        });*/
        bundle = getIntent().getExtras();
        if (bundle != null) {
            imagePath = bundle.getString("PATH");
        }

        previewImg = findViewById(R.id.imageViewPreviewIcon);
        imageViewPreview = findViewById(R.id.imageViewPreview);

        imgBack = findViewById(R.id.imageViewBack);
        imgHome = findViewById(R.id.imageViewHome);
        back = findViewById(R.id.share_back);
        hiddenForegroundImage = findViewById(R.id.hidden_foreground_img);
        previewSavedImageFragmentContainer = findViewById(R.id.containerPreview);
        previewSavedImageFragment = new PreviewSavedImageFragment();
        hiddenBackground = findViewById(R.id.hidden_background_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveShareImageActivity2.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("showAd", false);
                startActivity(intent);
                finish();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        previewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(previewSavedImageFragment);
            }
        });
        previewSavedImageFragmentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewSavedImageFragment.isVisible()) {
                    //fragmentTransaction.remove(selectionFragment);
                    getFragmentManager().beginTransaction().remove((Fragment) previewSavedImageFragment).commitAllowingStateLoss();
                    previewSavedImageFragmentContainer.setVisibility(View.GONE);
                }
            }
        });
        if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                admobAd();
            }
        }

        ChangeBackgroundData.getChangeBackgroundData().setSfBackgroundImageView(hiddenBackground);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        shareImageview.setImageBitmap(bitmap);
        setSavedImageBitmap(bitmap);
    }

    private void loadFragment(Fragment fragment) {
        previewSavedImageFragmentContainer.setVisibility(View.VISIBLE);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(previewSavedImageFragmentContainer.getId(), fragment);
        fragmentTransaction.commit(); // save the changes
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
                        if (SaveShareImageActivity2.this.nativeAd != null) {
                            SaveShareImageActivity2.this.nativeAd.destroy();
                        }
                        SaveShareImageActivity2.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = findViewById(R.id.native_ad_save_share);
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


    public void shareImages(String path) {
        ArrayList<Uri> files = new ArrayList<Uri>();
        Uri uri = FileProvider.getUriForFile(SaveShareImageActivity2.this, getApplicationContext().getPackageName() + ".provider", new File((String) path));
        files.add(uri);
        Intent shareIntent;
        shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }


    void loadFullScreenAds() throws Exception {

       /* mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constants.ADS_ADMOB_FULLSCREEN_ID);
        AdRequest localAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(localAdRequest);*/
    }


    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        /*if (mAdView != null) {
            mAdView.pause();
        }*/
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        /*if (mAdView != null) {
            mAdView.resume();
        }*/
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        /*if (mAdView != null) {
            mAdView.destroy();
        }*/

        super.onDestroy();
    }

    public boolean isAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    void rate() {
        Intent intentRateMe = new Intent(Intent.ACTION_VIEW);
        /*if (ImageUtility.getAmazonMarket(SaveShareImageActivity2.this)) {
            intentRateMe.setData(Uri.parse("amzn://apps/android?p=" + getPackageName().toLowerCase()));
        } else {
            intentRateMe.setData(Uri.parse("market://details?id=" + getPackageName().toLowerCase()));
        }*/
        startActivity(intentRateMe);
    }

/*    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        DisplayMetrics metrics;
        BitmapLoader bitmapLoader;

        public BitmapWorkerTask() {
            metrics = getResources().getDisplayMetrics();
            bitmapLoader = new BitmapLoader();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Void... arg0) {
            try {
                return bitmapLoader.load(getApplicationContext(), new int[]{metrics.widthPixels, metrics.heightPixels}, imagePath);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                shareImageview.setImageBitmap(bitmap);
            } else {
                Toast.makeText(SaveShareImageActivity2.this, getString(R.string.error_img_not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    public void myClickHandler(View view) {
        int id = view.getId();
        if (id == R.id.instagramShare) {
            try {
                Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity2.this,
                        getApplicationContext().getPackageName() + ".provider", new File(imagePath));
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uriImage);
                intent.setPackage("com.instagram.android");
                startActivity(intent);
            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_instagram_app), Toast.LENGTH_SHORT).show();
            }


        }
        if (id == R.id.share_imageView) {
            Toast.makeText(this, getString(R.string.saved_image_message), Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.whatsup_share) {
            try {
                Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity2.this,
                        getApplicationContext().getPackageName() + ".provider", new File(imagePath));

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uriImage);
                intent.setPackage("com.whatsapp");
                startActivity(intent);
            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_whatsapp_app), Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.facebook_share) {
            // initShareIntent("face");

            try {
                Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity2.this,
                        getApplicationContext().getPackageName() + ".provider", new File(imagePath));

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uriImage);
                intent.setPackage("com.facebook.katana");
                startActivity(intent);
            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_facebook_app), Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.more) {
            Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity2.this,
                    getApplicationContext().getPackageName() + ".provider", new File(imagePath));

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            sharingIntent.setType("image/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
            startActivity(sharingIntent);
        }
    }

    private void setSavedImageBitmap(final Bitmap bitmap) {
        savedBitmap = bitmap;
        imageViewPreview.setImageBitmap(bitmap);
        ChangeBackgroundData.getChangeBackgroundData().setSaveFinishedBitmap(bitmap);
        hiddenForegroundImage.setImageBitmap(bitmap);
    }

    public Bitmap getSavedBitmap() {
        return savedBitmap;
    }

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
                share, 0);
        if (!resInfo.isEmpty()) {
            // FilePath = getImagePath();

            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type)
                        || info.activityInfo.name.toLowerCase().contains(type)) {

                    Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity2.this,
                            getApplicationContext().getPackageName() + ".provider", new File(imagePath));

                    share.putExtra(Intent.EXTRA_SUBJECT, "Created With #Romantic Photo Editor");
                    share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                    share.putExtra(Intent.EXTRA_STREAM, uriImage);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(this, getString(R.string.no_facebook_app), Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(Intent.createChooser(share, "Select"));
        }
    }

}
