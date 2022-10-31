package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

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
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;*/
  import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
/*import com.hangoverstudios.romantic.photo.frames.love.photo.editor.image.ImageUtility;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Constants;*/
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SaveShareImageActivity extends AppCompatActivity {

    private Bundle bundle;
    private String imagePath;
    private ImageView shareImageview;
    // AdView mAdView;
    // InterstitialAd mInterstitialAd;
    private ImageView back, rate, share, goToHome;
    private FrameLayout adViewContainer;
    private AdView adViewBanner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_share_image_activity);
        CommonMethods.getInstance().activitiesAD(SaveShareImageActivity.this);
        adViewContainer = findViewById(R.id.save_share_banner_ad);
        adViewContainer.post(new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().loadBannerAd(adViewBanner3, adViewContainer, SaveShareImageActivity.this);
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null) {
            imagePath = bundle.getString("PATH");
        }

        //  imagePath = getIntent().hasExtra("PATH")?getIntent().getStringExtra("PATH"):null;

        back = findViewById(R.id.share_back);
        goToHome = findViewById(R.id.go_to_home);
//        rate = findViewById(R.id.rate_app);
        share = findViewById(R.id.share_app);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaveShareImageActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

       /* rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate();
            }
        });*/

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("text/plain");
//                    i.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().loadLabel(getPackageManager()).toString());
//                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommand_message) + "  https://play.google.com/store/apps/details?id=" + getPackageName().toLowerCase() + " \n");
//                    startActivity(Intent.createChooser(i, "Choose one"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                shareImages(imagePath);

            }
        });

        shareImageview = (ImageView) findViewById(R.id.share_imageView);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        shareImageview.setImageBitmap(bitmap);
        // new BitmapWorkerTask().execute();
        // Set a toolbar to replace the action bar.
    }

    public void shareImages(String path) {
        ArrayList<Uri> files = new ArrayList<Uri>();
        Uri uri = FileProvider.getUriForFile(SaveShareImageActivity.this, getApplicationContext().getPackageName() + ".provider", new File((String) path));
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
        /*if (ImageUtility.getAmazonMarket(SaveShareImageActivity.this)) {
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
                Toast.makeText(SaveShareImageActivity.this, getString(R.string.error_img_not_found), Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    public void myClickHandler(View view) {
        int id = view.getId();
        if (id == R.id.instagramShare) {
            try {
//                Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity.this,
//                        getApplicationContext().getPackageName(), new File(imagePath));
//                Intent intent = new Intent();
//                intent.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().loadLabel(getPackageManager()).toString());
//                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommand_message) + "  https://play.google.com/store/apps/details?id=" + getPackageName().toLowerCase() + " \n");
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_STREAM, uriImage);
//                intent.setPackage("com.instagram.android");
//                startActivity(intent);

                ArrayList<Uri> files = new ArrayList<Uri>();
                Uri uri = FileProvider.getUriForFile(SaveShareImageActivity.this, getApplicationContext().getPackageName() + ".provider", new File((String) imagePath));
                files.add(uri);
                Intent shareIntent;
                shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                startActivity(shareIntent);

            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_instagram_app), Toast.LENGTH_SHORT).show();
            }


        }
        if (id == R.id.share_imageView) {
            Toast.makeText(this, getString(R.string.saved_image_message), Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.whatsup_share) {
            try {
                Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity.this,
                        getApplicationContext().getPackageName(), new File(imagePath));

                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().loadLabel(getPackageManager()).toString());
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommand_message) + "  https://play.google.com/store/apps/details?id=" + getPackageName().toLowerCase() + " \n");
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uriImage);
                intent.setPackage("com.whatsapp");
                startActivity(intent);


//                ArrayList<Uri> files = new ArrayList<Uri>();
//                Uri uri = FileProvider.getUriForFile(SaveShareImageActivity.this, getApplicationContext().getPackageName() + ".provider", new File((String) imagePath));
//                files.add(uri);
//                Intent shareIntent;
//                shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
//                shareIntent.setType("image/*");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.setPackage("com.whatsapp");
//                startActivity(shareIntent);


                //You can read the image from external drove too
//                Uri uri = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/mona");
//
//
//
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_STREAM,uri);
//                intent.setType("image/*");
//                intent.setPackage("com.whatsapp");
//                startActivity(intent);


//                Uri imageUri = Uri.parse(imagePath);
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.setPackage("com.whatsapp");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/*");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(shareIntent);
//                try {
//                    startActivity(shareIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    ToastHelper.MakeShortText("Kindly install whatsapp first");
//                }

            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_whatsapp_app), Toast.LENGTH_SHORT).show();
            }
        }
        if (id == R.id.facebook_share) {
            // initShareIntent("face");

            try {
                ArrayList<Uri> files = new ArrayList<Uri>();
                Uri uri = FileProvider.getUriForFile(SaveShareImageActivity.this, getApplicationContext().getPackageName() + ".provider", new File((String) imagePath));
                files.add(uri);
                Intent shareIntent;
                shareIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.facebook.orca");
                startActivity(shareIntent);
            } catch (Exception i) {
                Toast.makeText(this, getString(R.string.no_facebook_app), Toast.LENGTH_SHORT).show();
            }

        }
        if (id == R.id.more) {
//            Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity.this,
//                    getApplicationContext().getPackageName(), new File(imagePath));
//
//            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().loadLabel(getPackageManager()).toString());
//            sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommand_message) + "  https://play.google.com/store/apps/details?id=" + getPackageName().toLowerCase() + " \n");
//            sharingIntent.setType("image/*");
//            sharingIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
//            startActivity(sharingIntent);
            shareImages(imagePath);
        }
    }

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().loadLabel(getPackageManager()).toString());
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommand_message) + "  https://play.google.com/store/apps/details?id=" + getPackageName().toLowerCase() + " \n");
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
                share, 0);
        if (!resInfo.isEmpty()) {
            // FilePath = getImagePath();

            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type)
                        || info.activityInfo.name.toLowerCase().contains(type)) {

                    Uri uriImage = FileProvider.getUriForFile(SaveShareImageActivity.this,
                            getApplicationContext().getPackageName(), new File(imagePath));

                    share.putExtra(Intent.EXTRA_SUBJECT, "Created With #Photo Collage Editor App");
                    share.putExtra(Intent.EXTRA_TEXT, "Created With #Photo Collage Editor App");
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
