package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.BuildConfig;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.RemoteConfigValues;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.ImageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;

public class GalleryActivity extends AppCompatActivity {
    ImageAdapter imageAdapter;
    public RecyclerView imageRecycler;
    ArrayList<String> imagesList;
    ImageView selectDelete, selectSelectAll, selectShare;
    LinearLayout selectBack;
    public TextView selectCount, noImagesToShow;
    public static GalleryActivity galleryActivity;
    public LinearLayout selectionOptions, toolOptions, backToHome;
    private UnifiedNativeAd nativeAd;
    private AdView adViewBanner10;
    private FrameLayout adViewContainer;
    private RelativeLayout alternativeToNativeAd;
    RelativeLayout scrollViewNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        noImagesToShow = findViewById(R.id.no_images_txt);
       // scrollViewNative = findViewById(R.id.native_ads_scroll);
       // alternativeToNativeAd = findViewById(R.id.alternative_to_native_ad_gall);
        galleryActivity = this;
       /* if (RemoteConfigValues.getOurRemote().getShowNativeAd() != null) {
            if (RemoteConfigValues.getOurRemote().getShowNativeAd().equals("true")) {
                if (RemoteConfigValues.getOurRemote().getAdRotationPolicyNative().equals("true")) {
                    alternativeToNativeAd.setVisibility(View.GONE);
                    scrollViewNative.setVisibility(View.VISIBLE);
                    admobAd();
                } else {
                    if (RemoteConfigValues.getOurRemote().getNativeOnlyGoogle().equals("true")) {
                        alternativeToNativeAd.setVisibility(View.GONE);
                        scrollViewNative.setVisibility(View.VISIBLE);
                        admobAd();
                    }
                }
            }
        } else {
            alternativeToNativeAd.setVisibility(View.VISIBLE);
            scrollViewNative.setVisibility(View.GONE);
        }*/
        adViewContainer = findViewById(R.id.gallery_banner_ad);
        adViewContainer.post(new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().loadBannerAd(adViewBanner10, adViewContainer, GalleryActivity.this);
            }
        });

        imageRecycler = findViewById(R.id.image_recycler);
        if (isDirectoryNotEmpty(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android" + "/data/" + getPackageName() + "/files/" + getResources().getString(R.string.app_name) + "/" + getResources().getString(R.string.app_name))) {
            imagesList = getImagesFromDevice();
            if (imagesList != null && imagesList.size() > 0) {
                noImagesToShow.setVisibility(View.GONE);
            } else {
                noImagesToShow.setVisibility(View.VISIBLE);
            }
            setImageAdapter(imagesList, false);
        } else {
            noImagesToShow.setVisibility(View.VISIBLE);
        }

        selectionOptions = findViewById(R.id.select_options);
        selectCount = findViewById(R.id.select_count);
        selectDelete = findViewById(R.id.select_delete);

        selectShare = findViewById(R.id.select_share);
        selectBack = findViewById(R.id.select_back_linear);
        selectSelectAll = findViewById(R.id.select_selectall);

        toolOptions = findViewById(R.id.tool_options);
        selectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        selectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList(false);
                selectCount.setText("selected : 0");
                selectionOptions.setVisibility(View.GONE);
                toolOptions.setVisibility(View.VISIBLE);
            }
        });
        selectShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < imageAdapter.selectedItems.size(); i++) {
                    shareImages();
                }
            }
        });
        selectSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageAdapter.selectAll) {
                    updateList(false);
                } else {
                    updateList(true);
                }
            }
        });

        backToHome = findViewById(R.id.back_to_home_linear);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setImageAdapter(final ArrayList<String> imageFilesList, boolean selectAll) {
        imageAdapter = new ImageAdapter(GalleryActivity.this, imageFilesList, selectAll);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        imageRecycler.setLayoutManager(gridLayoutManager);
        imageRecycler.setHasFixedSize(true);
        imageRecycler.setItemViewCacheSize(20);
        // imageRecycler.setDrawingCacheEnabled(true);
        // imageRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        imageRecycler.setAdapter(imageAdapter);
    }

    public ArrayList<String> getImagesFromDevice() {
        final ArrayList<String> tempAudioList = new ArrayList<>();
        String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android" + "/data/" + getPackageName() + "/files/" + getResources().getString(R.string.app_name) + "/" + getResources().getString(R.string.app_name);
        File directory1 = new File(directoryPath);
        File[] files = directory1.listFiles();
        Arrays.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
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

    public void shareImages() {
        ArrayList<Uri> files = new ArrayList<Uri>();
        for (int i = 0; i < imageAdapter.selectedItems.size(); i++) {

            Uri uri = FileProvider.getUriForFile(GalleryActivity.this, getApplicationContext().getPackageName()+ ".provider", new File(imagesList.get(imageAdapter.selectedItems.get(i))));
            files.add(uri);
        }
        Intent shareIntent;
        shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(shareIntent, "send"));
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

    public void showDeleteDialog() {
        final Dialog dialog = new Dialog(GalleryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_dialog);
        TextView msgTxt = dialog.findViewById(R.id.dialog_msg);
        TextView msgSubTxt = dialog.findViewById(R.id.dialog_msg_sub);
        msgTxt.setText("Conform Delete.");
        msgSubTxt.setText("Are you sure want to delete selected items permanently.");
        TextView dialogOk = dialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllSelectedItems();
                if (mainActivity != null)
                    mainActivity.updateList(false);
                dialog.dismiss();
            }
        });
        TextView dialogCancel = dialog.findViewById(R.id.dialog_cancel);
        dialogCancel.setVisibility(View.VISIBLE);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void deleteAllSelectedItems() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + getResources().getString(R.string.app_name));
        if (imageAdapter.selectedItems.size() != 0) {
            for (int i = 0; i < imageAdapter.selectedItems.size(); i++) {
                Log.e("DELETE", "delete images paths : " + imagesList.get(imageAdapter.selectedItems.get(i)));
                removeImage(imagesList.get(imageAdapter.selectedItems.get(i)));
            }
            updateList(false);
            selectionOptions.setVisibility(View.GONE);
            toolOptions.setVisibility(View.VISIBLE);
        } else {
            updateList(false);
            selectionOptions.setVisibility(View.GONE);
            toolOptions.setVisibility(View.VISIBLE);
        }
        refreshGallery(file);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    public void updateList(boolean selectAll) {
        imagesList = getImagesFromDevice();
        if (getImagesFromDevice() != null) {
            imageRecycler.setVisibility(View.VISIBLE);
            noImagesToShow.setVisibility(View.GONE);
            setImageAdapter(imagesList, selectAll);
        } else {
            imageRecycler.setVisibility(View.GONE);
            selectionOptions.setVisibility(View.GONE);
            toolOptions.setVisibility(View.VISIBLE);
            noImagesToShow.setVisibility(View.VISIBLE);
        }
    }

    public void removeImage(String path) {
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :");
            } else {
                System.out.println("file not Deleted :");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        CommonMethods.getInstance().activitiesAD(GalleryActivity.this);
    }

   /* private void admobAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.admob_native_id));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout =
                        findViewById(R.id.native_ad_gallery);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();
        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }*/

   /* public static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }
        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }
        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getVideoController();
    }*/

}
