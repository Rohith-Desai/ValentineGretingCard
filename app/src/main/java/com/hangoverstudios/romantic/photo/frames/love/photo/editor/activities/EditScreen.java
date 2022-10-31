package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.FilterItemAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.FilterItemDoubleAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingStickerPreViewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.ThumbnailsAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.BubbleTextView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.PhotoFilter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.SandboxViewDouble;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.StickerView;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailCallback;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.BuildConfig;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SplashActivity.mFirebaseAnalytics;
//import static Home.fromFeatureMain;
//import static SplashActivity.localCordinates;
//import static SplashActivity.mFirebaseAnalytics;

public class EditScreen extends AppCompatActivity implements ThumbnailCallback {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ArrayList<Integer> mResources;
    private RecyclerView stickerRecyclerView;
    private GreetingStickerPreViewAdapter stickerPreviewAdapter;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 201;
    private static final int CAMERA_REQUEST = 1888;
    public static EditScreen eds;
    /* access modifiers changed from: private */
    public static int RESULT_LOAD_IMAGE = 1;
    public static Uri imageUri;
    static ArrayList<Object> singleFramesShortList = new ArrayList<>();
    static ArrayList<Object> occasionShortList = new ArrayList<>();

    static ArrayList<Object> stickersShortList = new ArrayList<>();
    static ArrayList<Object> bordersShortList = new ArrayList<>();
    static ArrayList<Object> bordersShortList1 = new ArrayList<>();
    static ArrayList<Object> filtersShortList = new ArrayList<>();
    static ArrayList<Object> doubleFramesShortList = new ArrayList<>();
    static ArrayList<Object> tripleFramesShortList = new ArrayList<>();
    static ArrayList<Object> greetingFramesShortList = new ArrayList<>();
    /* access modifiers changed from: private */
    public File imageFile;
    Locale myLocale;
    //    boolean styleBitmpa, opacityBitmap;
//    Bitmap originalBitmap, effectBitmap;
    public Bitmap filterBitMap;
    public Bitmap filterBitMap1;
    public Bitmap filterBitMap2;
    LinearLayout l1, l2;
    String posName;
    int pos;
    String savedImage;
    LinearLayout frame;
    FrameLayout frameLay, border_linear;
    RecyclerView edit_recycle;
    Typeface type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9, type_10, type_11, type_12,
            type_13, type_14, type_15, type_16, type_17, type_18, type_19, type_20, type_21, type_22, selectedType;
    SandboxViewDouble imag2;
    SandboxViewDouble imag1;
    public SandboxViewDouble global;
    int MIN = 10;
    int MAX = 160;
    int STEP = 5;
    int shadowValue = 0;
    String app_lang, phone_lang;
    private int frameType = 0;
    private TextView frames, stickers, filters, text, album, blend, save, share;
    private String[] types, types_hi, types_te;
    private int currentColor = Color.parseColor("#00ff00");
    //    Views
    private StickerView mCurrentView;
    private ArrayList<View> mViews;
    private BubbleTextView mCurrentEditTextView;
    private AlertDialog dialog;
    private boolean isOutSide = false;
    androidx.appcompat.app.AlertDialog alertDialog;
    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
    private static boolean canRewarded = false;
    private static boolean rewardVideoComplete = false;
    TextView water_mark;
    String occNamed = "";
    String from = "";
    private Activity activity;

    public static PointF getTransparentCenter(Bitmap bitmap, Point viewSize) {
        List<Point> transparentPoints = new ArrayList<>();
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int pixel = bitmap.getPixel(i, j);
                if ((pixel & 0xff000000) == 0) {
                    //the point color is transparent
                    transparentPoints.add(new Point(i, j));
                }
            }
        }
        int totalX = 0;
        int totalY = 0;
        for (Point transparentPoint : transparentPoints) {
            totalX += transparentPoint.x;
            totalY += transparentPoint.y;
        }
        float centerX = (float) totalX / transparentPoints.size();
        float centerY = (float) totalY / transparentPoints.size();
        float x = viewSize.x * centerX / bitmap.getWidth();
        float y = viewSize.y * centerY / bitmap.getHeight();
        return new PointF(x, y);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_frame);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity = this;

        app_lang = getSharedPreferences("PHOTOFR", MODE_PRIVATE).getString("Lang", "en");
        phone_lang = Locale.getDefault().getLanguage();
        if (app_lang.equals(phone_lang)) {
            onInit();
            app_lang = phone_lang;
        } else {
            switch (app_lang) {
                case "hi":
                    setLocale("hi");
                    break;
                case "te":
                    setLocale("te");
                    break;
                case "se":
                    setLocale("se");
                    break;
                case "fr":
                    setLocale("fr");
                    break;
                case "de":
                    setLocale("de");
                    break;
                case "ga":
                    setLocale("ga");
                    break;
                case "pt":
                    setLocale("pt");
                    break;
                default:
                    setLocale("en");
                    break;
            }
        }


        eds = this;
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialog = alertDialogBuilder.create();

    }

    void onInit() {
        mViews = new ArrayList<>();

        types = new String[]{"earwig factory", "action jackson", "barbatrick", "if", "BrushScriptStd",
                "FancyPantsNF", "Fiddums Family", "Fortunaschwein_complete", "FUNDR__", "HoboStd",
                "hotpizza", "NuevaStd-Bold", "NuevaStd-BoldCond", "NuevaStd-BoldCondItalic",
                "NuevaStd-Cond", "NuevaStd-CondItalic", "NuevaStd-Italic"};
        types_hi = new String[]{
                "font1", "font2", "himali", "ruchi", "samarkan", "vimale"
        };
        types_te = new String[]{"Gidugu", "Suravaram"};

        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
        stickerRecyclerView = findViewById(R.id.stickers_recycle);

//        blend = findViewById(R.id.blend);
        filters = findViewById(R.id.filters);
        water_mark = findViewById(R.id.water_mark);

//        blend.setText(R.string.blend);
        filters.setText(R.string.filters);
        border_linear = findViewById(R.id.border_linear);
        l1 = (LinearLayout) findViewById(R.id.imageLay1);
        l1.setVisibility(View.VISIBLE);
        frame = findViewById(R.id.bg_frame);
        String imagePath = "";
        if (getIntent().hasExtra("PATH")) {
            imagePath = getIntent().getStringExtra("PATH");
        }
        if (getIntent().hasExtra("FROM")) {
            from = getIntent().getStringExtra("FROM");
        }
        setBackgroundOfSingleFrame(imagePath);
        frameLay = (FrameLayout) findViewById(R.id.FrameLayout1);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bglay);
        l2 = (LinearLayout) findViewById(R.id.imageLay2);
        pos = 1;
        if (getIntent().hasExtra("POSITION")) {
            pos = getIntent().getIntExtra("POSITION", 1);
            posName = getIntent().getStringExtra("NAME");
        }
//        int pos = getIntent().getExtras().getInt(DBhelper.EMP_ID);
        imag1 = new SandboxViewDouble(this, null, 0, 1);
        imag1.setActivit(this);
        imag2 = new SandboxViewDouble(this, null, 0, 2);
        imag2.setActivit(this);
        l1.addView(imag1);
        l2.addView(imag2);

        (findViewById(R.id.whatsapp)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                frameLay.setDrawingCacheEnabled(true);
                Bitmap save = frameLay.getDrawingCache();
                File sdCardFile = Environment.getExternalStorageDirectory();
                if (sdCardFile.canWrite()) {
                    File viewerFile = new File(sdCardFile, getString(R.string.app_name));
                    viewerFile.mkdirs();
                    EditScreen.this.imageFile = new File(viewerFile, "" + getString(R.string.app_name) + ".png");
                    if (EditScreen.this.imageFile.exists()) {
                        EditScreen.this.imageFile.delete();
                    }
                    try {
                        EditScreen.this.imageFile.createNewFile();
                        FileOutputStream fileStream = new FileOutputStream(EditScreen.this.imageFile);
                        save.compress(CompressFormat.PNG, 100, fileStream);
                        fileStream.close();
                        Intent i = new Intent("android.intent.action.SEND");
                        i.setType("image/*");
                        String sAux = "" + getString(R.string.app_name) + ": \n";
                        sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                        i.putExtra(Intent.EXTRA_TEXT, sAux);


                        Uri uri = FileProvider.getUriForFile(EditScreen.this, BuildConfig.APPLICATION_ID + ".provider", EditScreen.this.imageFile);

                        i.putExtra("android.intent.extra.STREAM", uri);
//                        i.putExtra("android.intent.extra.STREAM", Uri.fromFile(EditScreen.this.imageFile));


                        EditScreen.this.startActivity(Intent.createChooser(i, EditScreen.this.getString(R.string.share).toString())); //Sareap
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                frameLay.setDrawingCacheEnabled(false);
            }
        });

        (findViewById(R.id.save)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                l1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                l2.setBackgroundColor(Color.parseColor("#FFFFFF"));
                withoutMark();
//                if (CloudDatabase.getFirebase().getEnableWatermarkReward() != null) {
//                    if (!CloudDatabase.getFirebase().getEnableWatermarkReward().equals("")) {
//                        if (CloudDatabase.getFirebase().getEnableWatermarkReward().equals("true")) {
//                            showWaterMarkDialog();
//                        } else {
//                            withoutMark();
//                        }
//                    }
//                }


            }
        });

        frameLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reoveStickerBorders();
            }
        });

        album = findViewById(R.id.album);
        album.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(EditScreen.this, AlbumActivity.class));
//                getAllCreations();
            }
        });

        edit_recycle = findViewById(R.id.edit_recycle);
        frames = findViewById(R.id.frames);
        stickers = findViewById(R.id.stickers);
        text = findViewById(R.id.text_style);
        frames.setText(R.string.frames);
        stickers.setText(R.string.stickers);
        text.setText(R.string.text);

        save = findViewById(R.id.save);
        save.setText(R.string.save);

        share = findViewById(R.id.whatsapp);
        share.setText(R.string.share);

        album = findViewById(R.id.album);
        album.setText(R.string.album);

        preparData();


        frames.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reoveStickerBorders();
//                openFrames();
                if (from.equals("Double_Trend")) {
                    Intent i = new Intent(EditScreen.this, DoubleTrendSubListActivity.class);
                    startActivityForResult(i, 333);
                } else if (from.equals("Double")) {
                    Intent i = new Intent(EditScreen.this, DoubleSubListActivity.class);
                    startActivityForResult(i, 333);
                }


            }
        });

        stickers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                reoveStickerBorders();
//                openStickers();
                edit_recycle.setVisibility(View.GONE);
                setStickersRecyclerView();

            }
        });

        filters.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reoveStickerBorders();
                stickerRecyclerView.setVisibility(View.GONE);
//                openFilter();
                initHorizontalList();

            }
        });

        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                reoveStickerBorders();
                openAlertDialog();

//               startActivity(new Intent(EditScreen.this, TextSettingsActivity.class));
            }
        });
    }

    private void setBackgroundOfSingleFrame(String imagePath) {
      /*  if (imagePath.contains(getApplicationContext().getPackageName())) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            BitmapDrawable background = new BitmapDrawable(myBitmap);
            frame.setBackgroundDrawable(background);
        }*/
        if (imagePath.contains("https")) {
            String[] paths = imagePath.split("/");
            String getpath = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ?
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Romantic Photo Editor" + "/Frames"
                    : Environment.getExternalStorageDirectory().toString() + "/" + "Romantic Photo Editor" + "/Frames";

            Bitmap myBitmap = BitmapFactory.decodeFile(getpath +"/"+ paths[paths.length - 1]);
            BitmapDrawable background = new BitmapDrawable(myBitmap);
            frame.setBackgroundDrawable(background);


        }
        else {

//        imagePath = "single1";
            Resources resources = EditScreen.this.getResources();
            int resourceId = resources.getIdentifier(imagePath, "drawable",
                    EditScreen.this.getPackageName());
            if (Objects.requireNonNull(getResources()).getIdentifier(imagePath, "drawable", getPackageName()) > 0) {
                if (resourceId > 0) {
                    Drawable drawable = resources.getDrawable(resourceId);
                    frame.setBackground(drawable);
//                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.single_1);
                }
            }
        }

    }

//
//    private void startRewardVideoAd() {
//        final Dialog dialog = new Dialog(EditScreen.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.save_mark);
//
//        TextView markImg = dialog.findViewById(R.id.mark);
//        TextView noMarkImg = dialog.findViewById(R.id.nomark);
//        noMarkImg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startRewardVideoAd();
//                dialog.dismiss();
//            }
//        });
//        markImg.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                withMark();
//            }
//        });
//
//        dialog.show();
//    }

    private void withMark() {
        water_mark.setVisibility(View.VISIBLE);
        reoveStickerBorders();
        frameLay.setDrawingCacheEnabled(true);
        if (EditScreen.this.saveImageToSDCard(frameLay.getDrawingCache())) {
//            startActivity(new Intent(EditScreen.this, ShareFrame.class)
//                    .putExtra("SAVED_IMG", savedImage));
            finish();
            Toast.makeText(EditScreen.this, "Saved successfully in Album", LENGTH_SHORT).show();
        }
        frameLay.setDrawingCacheEnabled(false);
    }

//
//    private void startRewardVideoAd() {
//        alertDialog.setMessage("please wait..until Ad is load...");
//        alertDialog.setCancelable(false);
//        alertDialog.setIcon(R.drawable.icon);
//        alertDialog.show();
//        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(EditScreen.this);
////        mRewardedVideoAd.loadAd(("ca-app-pub-3940256099942544/5224354917"), new AdRequest.Builder().build());
//        mRewardedVideoAd.loadAd(getString(R.string.admob_reward_id), new AdRequest.Builder().build());
//        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
//            @Override
//            public void onRewarded(RewardItem reward) {
//                canRewarded = true;
//                rewardVideoComplete = true;
//            }
//
//            @Override
//            public void onRewardedVideoAdLeftApplication() {
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//                if (rewardVideoComplete && canRewarded) {
//                    withoutMark();
//                } else {
//                    rewardVideoComplete = false;
//                    canRewarded = false;
//                }
//            }
//
//            @Override
//            public void onRewardedVideoAdFailedToLoad(int errorCode) {
//                withoutMark();
//            }
//
//            @Override
//            public void onRewardedVideoAdLoaded() {
//                alertDialog.dismiss();
//                mRewardedVideoAd.show();
//            }
//
//            @Override
//            public void onRewardedVideoAdOpened() {
//            }
//
//            @Override
//            public void onRewardedVideoStarted() {
//            }
//
//            @Override
//            public void onRewardedVideoCompleted() {
//                rewardVideoComplete = true;
//                Log.e("TEST", "onRewardedVideoCompleted first");
//            }
//        });
//    }

    private void withoutMark() {
//        water_mark.setVisibility(View.INVISIBLE);
//        Bundle bundle2 = new Bundle();
//        bundle2.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "SavedImages");
//        if (mFirebaseAnalytics != null) {
//            mFirebaseAnalytics.logEvent("Nature_Frames", bundle2);
//        }
        reoveStickerBorders();
        frameLay.setDrawingCacheEnabled(true);
        saveImageToSDCard(frameLay.getDrawingCache());
        frameLay.setDrawingCacheEnabled(true);
//        if (EditScreen.this.saveImageToSDCard(frameLay.getDrawingCache())) {
////            startActivity(new Intent(EditScreen.this, ShareFrame.class)
////                    .putExtra("SAVED_IMG", savedImage));
//            finish();
//            Toast.makeText(EditScreen.this, "Saved successfully in Album", LENGTH_SHORT).show();
//        }
//        frameLay.setDrawingCacheEnabled(false);
    }

    Bitmap CropBitmapTransparencyS(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if (alpha > 0)   // pixel is not 100% transparent
                {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }
        if ((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    private void changeOPacity(int progresValue) {
        if (mCurrentView != null) {
            Bitmap newBitmap = null;
            mCurrentView.opacityBitmap = true;
            if (mCurrentView.styleBitmpa) {
                newBitmap = Bitmap.createBitmap(mCurrentView.effectBitmap.getWidth(), mCurrentView.effectBitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);
                Paint alphaPaint = new Paint();
                alphaPaint.setAlpha(progresValue);
                canvas.drawBitmap(mCurrentView.effectBitmap, 0, 0, alphaPaint);
//                mCurrentView.effectBitmap = newBitmap;

            } else {
                if (mCurrentView.originalBitmap != null) {
                    newBitmap = Bitmap.createBitmap(mCurrentView.originalBitmap.getWidth(), mCurrentView.originalBitmap.getHeight(), Config.ARGB_8888);
                    Canvas canvas = new Canvas(newBitmap);
                    Paint alphaPaint = new Paint();
                    alphaPaint.setAlpha(progresValue);
                    canvas.drawBitmap(mCurrentView.originalBitmap, 0, 0, alphaPaint);
                    mCurrentView.effectBitmap = mCurrentView.originalBitmap;
                }
            }

//            bitmap.a(progresValue * 25);
            /*final Paint paint = new Paint();
            Canvas canvas = new Canvas(bitmap);

            paint.setAlpha(100);*/
//            canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
//            canvas.drawBitmap(bitmap, 0, 0, paint);

            /*Bitmap transBmp = Bitmap.createBitmap(mCurrentView.getBitmap().getWidth(),
                    mCurrentView.getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(transBmp);
            final Paint paint = new Paint();
            paint.setAlpha((int) 0.5f);
            canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);*/
//            canvas.drawBitmap(mCurrentView.getBitmap(), 0, 0, paint);

            if (newBitmap != null) {
                mCurrentView.setBitmap(newBitmap, "blend");
            }

        }
    }

    private void editImgAler() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyCustomTheme);
        dialog.setMessage(R.string.change_msg);
        dialog.setTitle(R.string.require_attention);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        showDialog(imag2);

                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

   /* private void changeImgWithDouble() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_doubletap);
        TextView t1 = dialog.findViewById(R.id.text_dialog);
        TextView t2 = dialog.findViewById(R.id.head121);
        t2.setText(R.string.change_image);
        t1.setText(R.string.change_img_msg);


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setText(R.string.ok);
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        *//*android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        dialog.setMessage("If you want to change your image, double click on your image!");
        dialog.setTitle("Change Image!");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();*//*
        SharedPreferences pref = getApplicationContext().getSharedPreferences("N_Photo_Frames", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDoublePop", true);
        editor.apply();
    }*/

    private void preparData() {
        PhotoFilter photoFilter = new PhotoFilter();
        stickersShortList.clear();
        bordersShortList.clear();
        bordersShortList1.clear();
        filtersShortList.clear();
        singleFramesShortList.clear();
        occasionShortList.clear();
        doubleFramesShortList.clear();
        tripleFramesShortList.clear();
        greetingFramesShortList.clear();
        bordersShortList.add("border_empty");
        bordersShortList1.add("border_empty");
        filtersShortList.add("filter_rj_0");
        for (int j = 1; j < 36; j++) {
            if (j <= 15) {
                filtersShortList.add("filter_rj_" + j);
            }
            stickersShortList.add("n_" + j);
            if (j <= 20) {
                bordersShortList.add("border_icon_" + j);
                bordersShortList1.add("border_" + j);
            }

            if (j <= 10) {
                singleFramesShortList.add("single_" + j);
                tripleFramesShortList.add("triple_" + j);
//                greetingFramesShortList.add("greeting_frame_" + j);
            }
            if (j <= 20) {

                doubleFramesShortList.add("double_" + j);
            }
            if (j <= 2) {
                greetingFramesShortList.add("aapthi_blend_" + j);
            }
        }
        checkDownloadedFiles();

        createFonts();

    }

    private void checkDownloadedFiles() {
        File mediaStorageDir = null;
        mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files/");

        if (mediaStorageDir.exists()) {
            File[] files = mediaStorageDir.listFiles();
            if (files != null) {
                Log.d("Files", "Size: " + files.length);
                for (File file : files) {
                    Log.d("Files", "FileName:" + file.getName());
                    if (file.getName().contains("aapthi_frame")) {
                        singleFramesShortList.add(file.getName());
                    } else if (file.getName().contains("aapthi_blend_")) {
                        greetingFramesShortList.add(file.getName());
                    } else if (file.getName().contains(occNamed)) {
                        occasionShortList.add(file.getName());
                    }
                }
            }
        }
    }


    public void setStickersRecyclerView() {
        String type = "stickers";
        mResources = new ArrayList<>();
        for (int i = 1; i <= 38; i++) {
            mResources.add(getResources().getIdentifier("sticker_" + i, "drawable", Objects.requireNonNull(this).getPackageName()));
        }
       /* mResources.add(R.drawable.sticker_1);
        mResources.add(R.drawable.sticker_2);
        mResources.add(R.drawable.sticker_3);
        mResources.add(R.drawable.sticker_4);
        mResources.add(R.drawable.sticker_5);*/
        stickerRecyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stickerRecyclerView.setLayoutManager(layoutManager);
        stickerPreviewAdapter = new GreetingStickerPreViewAdapter(this, mResources, type, "Double");
        stickerRecyclerView.setAdapter(stickerPreviewAdapter);
    }

    private void openBorders() {
        prepareList(bordersShortList, 201);
    }

    private void openFilter() {
//        l2.setDrawingCacheEnabled(true);
//        filterBitMap = l2.getDrawingCache();
        prepareList(filtersShortList, 301);
    }


    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        edit_recycle.setLayoutManager(layoutManager);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        if (eds.filterBitMap != null) {
            final Context context = this.getApplication();
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    Bitmap thumbImage = Bitmap.createScaledBitmap(eds.filterBitMap, 100, 100, false);
                    ThumbnailsManager.clearThumbs();
                    List<Filter> filters = FilterPack.getFilterPack(getApplicationContext());

                    for (Filter filter : filters) {
                        ThumbnailItem thumbnailItem = new ThumbnailItem();
                        thumbnailItem.image = thumbImage;
                        thumbnailItem.filter = filter;
                        ThumbnailsManager.addThumb(thumbnailItem);
                    }

                    List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                    ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                    edit_recycle.setAdapter(adapter);
                    edit_recycle.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
            };
            handler.post(r);
        }
    }


    private void getAllCreations() {

        String path = Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_name);
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
        }
        System.out.println(files.length);
    }

    private void openAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyCustomTheme);
        LayoutInflater inflater = (this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor1, null);
        final EditText enteredText = (EditText) dialogView.findViewById(R.id.text_view);
        final TextView textView = (TextView) dialogView.findViewById(R.id.text);
        final ImageButton cancelBtn = (ImageButton) dialogView.findViewById(R.id.cancel_text_btn);
        final ImageButton colorPickerBtn = (ImageButton) dialogView.findViewById(R.id.color_picker_btn);
        final SeekBar shadowBg = (SeekBar) dialogView.findViewById(R.id.shadow_bg);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
        if (app_lang.equals("hi")) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types_hi);
        } else if (app_lang.equals("te")) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types_te);
        } else {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        }
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        textView.setTypeface(type_1);
                        selectedType = type_1;
                        break;
                    case 1:
                        textView.setTypeface(type_2);
                        selectedType = type_2;
                        break;
                    case 2:
                        textView.setTypeface(type_3);
                        selectedType = type_3;
                        break;
                    case 3:
                        textView.setTypeface(type_4);
                        selectedType = type_4;
                        break;
                    case 4:
                        textView.setTypeface(type_5);
                        selectedType = type_5;
                        break;
                    case 5:
                        textView.setTypeface(type_6);
                        selectedType = type_6;
                        break;
                    case 6:
                        textView.setTypeface(type_7);
                        selectedType = type_7;
                        break;
                    case 7:
                        textView.setTypeface(type_8);
                        selectedType = type_8;
                        break;
                    case 8:
                        textView.setTypeface(type_9);
                        selectedType = type_9;
                        break;
                    case 9:
                        textView.setTypeface(type_10);
                        selectedType = type_10;
                        break;
                    case 10:
                        textView.setTypeface(type_11);
                        selectedType = type_11;
                        break;
                    case 11:
                        textView.setTypeface(type_12);
                        selectedType = type_12;
                        break;
                    case 12:
                        textView.setTypeface(type_13);
                        selectedType = type_13;
                        break;
                    case 13:
                        textView.setTypeface(type_14);
                        selectedType = type_14;
                        break;
//                    case 14:
//                        textView.setTypeface(type_15);
//                        selectedType = type_15;
//                        break;
//                    case 15:
//                        textView.setTypeface(type_16);
//                        selectedType = type_16;
//                        break;
//                    case 16:
//                        textView.setTypeface(type_17);
//                        selectedType = type_17;
//                        break;
//                    case 17:
//                        textView.setTypeface(type_18);
//                        selectedType = type_18;
//                        break;
//                    case 18:
//                        textView.setTypeface(type_19);
//                        selectedType = type_19;
//                        break;
//                    case 19:
//                        textView.setTypeface(type_20);
//                        selectedType = type_20;
//                        break;
//                    case 20:
//                        textView.setTypeface(type_21);
//                        selectedType = type_21;
//                        break;
//                    case 21:
//                        textView.setTypeface(type_22);
//                        selectedType = type_22;
//                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        shadowBg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                textView.setShadowLayer(1, progresValue / 5, progresValue / 5, Color.BLACK);
                shadowValue = progresValue / 5;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });
//        spinner.setTe
        enteredText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cancelBtn.setVisibility(View.VISIBLE);
                } else {
                    cancelBtn.setVisibility(View.INVISIBLE);
                }
                textView.setText(enteredText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//

        currentColor = ContextCompat.getColor(this, R.color.colorAccent);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredText.setText("");
            }
        });
        colorPickerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(EditScreen.this, currentColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        currentColor = color;
                        textView.setTextColor(currentColor);
//                        colorLayout.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Toast.makeText(getApplicationContext(), "Action canceled!", LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
        builder.setView(dialogView);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = enteredText.getText().toString();
                if (!text.isEmpty()) {
//                    addBubble(text, currentColor, selectedType);
                    addStickerView(text, currentColor, selectedType);
                } else {
                    enteredText.setError("This should not be empty");
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create();
        builder.show();
    }

    private void prepareList(ArrayList list, int code) {
        edit_recycle.setHasFixedSize(true);

        LinearLayoutManager linearVertical = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        FilterItemDoubleAdapter framesAdapter = new FilterItemDoubleAdapter(EditScreen.this, list, code);

//            edit_recycle.setLayoutManager(linearVertical);
        edit_recycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        edit_recycle.setItemAnimator(new DefaultItemAnimator());
        edit_recycle.setAdapter(framesAdapter);
        edit_recycle.setVisibility(View.VISIBLE);

    }

    private void createFonts() {
        if (app_lang.equals("hi")) {
            Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
            type_1 = Typeface.createFromAsset(getAssets(), "fonts/font1.ttf");
            type_2 = Typeface.createFromAsset(getAssets(), "fonts/font2.ttf");
            type_3 = Typeface.createFromAsset(getAssets(), "fonts/himali.ttf");
            type_4 = Typeface.createFromAsset(getAssets(), "fonts/ruchi.ttf");
            type_5 = Typeface.createFromAsset(getAssets(), "fonts/samarkan.ttf");
            type_6 = Typeface.createFromAsset(getAssets(), "fonts/vimale.ttf");

        } else if (app_lang.equals("te")) {

            type_1 = Typeface.createFromAsset(getAssets(), "fonts/suravaram.ttf");
            type_2 = Typeface.createFromAsset(getAssets(), "fonts/gidugu.ttf");
//            type_3 = Typeface.createFromAsset(getAssets(), "fonts/gurajada.ttf");
//            type_4 = Typeface.createFromAsset(getAssets(), "fonts/lakkireddy.ttf");
//            type_5 = Typeface.createFromAsset(getAssets(), "fonts/mallanna.ttf");
//            type_6 = Typeface.createFromAsset(getAssets(), "fonts/ntr.ttf");
//            type_7 = Typeface.createFromAsset(getAssets(), "fonts/ramabhadra.ttf");
//            type_8 = Typeface.createFromAsset(getAssets(), "fonts/raviprakash.ttf");
//            type_9 =
//            type_10 = Typeface.createFromAsset(getAssets(), "fonts/suravaram.ttf");
//            type_11 = Typeface.createFromAsset(getAssets(), "fonts/tenaliramakrishna.ttf");
//            type_12 = Typeface.createFromAsset(getAssets(), "fonts/timmana.ttf");

        } else {

            selectedType = Typeface.createFromAsset(getAssets(), "fonts/new_five.ttf");
//            type_1 = Typeface.createFromAsset(getAssets(), "fonts/new_five.ttf");
//            type_2 = Typeface.createFromAsset(getAssets(), "fonts/new_one.ttf");
//            type_3 = Typeface.createFromAsset(getAssets(), "fonts/new_six.ttf");
//            type_4 = Typeface.createFromAsset(getAssets(), "fonts/new_two.ttf");
            type_1 = Typeface.createFromAsset(getAssets(), "fonts/BrushScriptStd.otf");
            type_2 = Typeface.createFromAsset(getAssets(), "fonts/FancyPantsNF.otf");
            type_3 = Typeface.createFromAsset(getAssets(), "fonts/Fiddums Family.ttf");
            type_4 = Typeface.createFromAsset(getAssets(), "fonts/Fortunaschwein_complete.ttf");
            type_5 = Typeface.createFromAsset(getAssets(), "fonts/FUNDR__.TTF");
            type_6 = Typeface.createFromAsset(getAssets(), "fonts/HoboStd.otf");
            type_7 = Typeface.createFromAsset(getAssets(), "fonts/hotpizza.ttf");
//            type_12 = Typeface.createFromAsset(getAssets(), "fonts/micross.ttf");
            type_8 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-Bold.otf");
            type_9 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-BoldCond.otf");
            type_10 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-BoldCondItalic.otf");
            type_11 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-Cond.otf");
            type_12 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-CondItalic.otf");
            type_13 = Typeface.createFromAsset(getAssets(), "fonts/NuevaStd-Italic.otf");

        }


    }

    private void addStickerView(String text, int currentColor, Typeface selectedType) {

        final StickerView stickerView = new StickerView(this);
        StringBuilder sb = new StringBuilder(text);
        int i = 0;
        while ((i = sb.indexOf(" ", i + 20)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        text = sb.toString();
        Bitmap bitmap = textAsBitmap(text, 34, currentColor, selectedType);
//        Bitmap bitmap = drawMultilineTextToBitmap(ImageEditActivity.this, R.mipmap.transparent, text, currentColor, selectedType);
        stickerView.setBitmap(bitmap);
//        stickerView.setImageResource(imageName);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                frame.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        frame.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor, Typeface selectedType) {
        Paint paint = new TextPaint();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm));
        paint.setTypeface(selectedType);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setAntiAlias(true);
        paint.setShadowLayer(1, shadowValue, shadowValue, Color.BLACK);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 140.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public Bitmap textAsBitmap1(String text, float textSize, int textColor, Typeface selectedType) {
        Paint paint = new TextPaint();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm));
        paint.setTypeface(selectedType);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(image);

//        for (String line: text.split("\n"))
//        {
//            canvas.drawText(line, 0, baseline, paint);
//            baseline += -paint.ascent() + paint.descent();
//        }
//
//        String[] lines = text.split("\n");
//        float lineSpace = textSize * 0.2f;
//
//        float TReheight = (lines.length * (baseline + lineSpace) + baseline);
//        float top = (image.getHeight() - height) / 2;
//        top += baseline;
//        for (String textsa : lines) {
//            if (TextUtils.isEmpty(textsa)) {
//                continue;
//            }
//            canvas.drawText(text, image.getWidth(), top, paint);
//            top += baseline + lineSpace;
//        }
//        canvas.drawBitmap(image,0,0,null);
//        canvas.drawBitmap(image, matrix, null);

        String[] lines = text.split("\n");
        float lineSpace = textSize * 0.7f;

        image = Bitmap.createBitmap(width, height * lines.length, Config.ARGB_8888);
        canvas.setBitmap(image);
        for (int i = 0; i < lines.length; ++i) {
            canvas.drawText(lines[i], 0, baseline + (textSize + lineSpace) * i, paint);
//            image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
//        canvas.drawBitmap(image, 0, 0, null);
//        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void reoveStickerBorders() {
        stickerRecyclerView.setVisibility(View.GONE);
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        if (mCurrentView != null)
            mCurrentView.setInEdit(false);

        edit_recycle.setVisibility(View.GONE);


    }

    public boolean checkStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);

    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions((Activity) this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSION_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    // main logic
                } else {
                    showCamPermisionDialog();
                }
                break;
        }
    }

    private void showCamPermisionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditScreen.this, R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setCancelable(false);
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
                finish();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void showPermisionDialog1() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MyCustomTheme);
        dialog.setMessage(getString(R.string.permission_request_msg));
        dialog.setTitle(getString(R.string.permission_request));
        dialog.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
//                        if (!checkCameraPermission()) {
//                            requestCameraPermission();
//
//                        }

                    }
                });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public boolean saveImageToSDCard(Bitmap b) {
        if (b != null) {
            File mydir = null;
            if (getApplicationContext() != null) {
                mydir = new File(getApplicationContext().getExternalFilesDir(getResources().getString(R.string.app_name)).getAbsolutePath());
            }
            if (mydir != null) {
                if (!mydir.exists()) {
                    mydir.mkdirs();
                    Toast.makeText(getApplicationContext(), "Directory Created", Toast.LENGTH_LONG).show();
                }
            }


            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getExternalFilesDir(String.valueOf(getResources().getString(R.string.app_name))).getAbsolutePath());
            //sb.append("/");
            //sb.append(getResources().getString(R.string.MainFolderName));
            sb.append("/");
            sb.append(getResources().getString(R.string.app_name));
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            StringBuilder sb2 = new StringBuilder();
            //sb2.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
            sb2.append(getApplicationContext().getExternalFilesDir(getResources().getString(R.string.app_name)).getAbsolutePath());
            // sb2.append("/");
            //sb2.append(getResources().getString(R.string.MainFolderName));
            sb2.append("/");
            sb2.append(getResources().getString(R.string.app_name));
            sb2.append("/");
            sb2.append(System.currentTimeMillis());
            sb2.append(".jpg");
            File dest = new File(sb2.toString());
            try {
                FileOutputStream out = new FileOutputStream(dest);
                b.compress(Bitmap.CompressFormat.PNG, 100, out);

                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            savedImage = sb2.toString();
            if (mainActivity != null) {
                mainActivity.updateList(false);
            }
            Intent intent = new Intent(EditScreen.this, SaveShareImageActivity2.class);
            intent.putExtra("PATH", savedImage);
            startActivity(intent);
            return true;
        }
        return false;
    }


    private void askRating() {
//        if (!AppHelper.isRatingDone(this)) {
//            startActivity(new Intent(EditScreen.this, Rating.class));
//        }
    }

    public void showDialog1(SandboxViewDouble s) {

        if (s.getMyId() == 1) {
            filterBitMap = filterBitMap1;
        } else if (s.getMyId() == 2) {
            filterBitMap = filterBitMap2;
        }
        this.global = s;

    }

    public void showDialog(SandboxViewDouble s) {

        if (dialog == null || !dialog.isShowing()) {
            this.global = s;
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367057, new String[]{"Take from Camera", "Select from Gallery"});
            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditScreen.this, android.R.layout.simple_list_item_1, new String[]{getString(R.string.take_camera), getString(R.string.select_gallery),});
            AlertDialog.Builder builder = new AlertDialog.Builder(EditScreen.this, R.style.MyCustomTheme);
            builder.setTitle(getString(R.string.select_option));
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    File photo;
                    if (which == 0) {
                        /*Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");

                        ContentValues localContentValues = new ContentValues();
                        localContentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, getString(R.string.new_picture));
                        localContentValues.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_DESCRIPTION, getString(R.string.from_camera));
                        imageUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, localContentValues);
                        cameraIntent.putExtra("output", imageUri);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }*/
                        if (checkCameraPermission() && checkStoragePermission()) {
                            launchCamera();
                        }
                    }
                    if (which == 1) {
                        Intent pickIntent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
                        if (pickIntent.resolveActivity(getPackageManager()) != null) {
                            try {
                                startActivityForResult(pickIntent, RESULT_LOAD_IMAGE);
                            } catch (ActivityNotFoundException e) {
                                Log.e("TAG", "Not Found.");
                            }
                        }

                    }
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }

    public void launchCamera() {
        if (ContextCompat.checkSelfPermission(EditScreen.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
           /* File path = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getPackageName()
                    + "/temp data");
            if (!path.exists()) path.mkdirs();
            File image = new File(path, "valentine_image_capture_edit.jpg");
            Uri imageUri = FileProvider.getUriForFile(EditScreen.this, getApplicationContext().getPackageName() + ".provider", image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
            File photo = null;
            try {
                photo = createImageFiles();
                Uri photoURI = FileProvider.getUriForFile(EditScreen.this, getApplicationContext().getPackageName() + ".provider", photo);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                saveUriPath(Uri.fromFile(photo).getPath());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAMERA_REQUEST);
                }

            } catch (IOException e) {

            }
        }
    }
    private static final String PHOTO_PREFERENCE_NAME = "editescreen_temp_data";
    private static final String PHOTO_KEY = "EDITESCREEN_IMAGE_URI_PATH";
    public void saveUriPath(String selectedImageUriPath) {
        SharedPreferences savePhotoData = getSharedPreferences(PHOTO_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = savePhotoData.edit();
        prefEditor.putString(PHOTO_KEY, selectedImageUriPath);
        prefEditor.apply();
    }
    public String getUriPath() {
        SharedPreferences getSelectedImageUriPath = getSharedPreferences(PHOTO_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return getSelectedImageUriPath.getString(PHOTO_KEY, null);
    }
    private File createImageFiles() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }


    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && data != null) {
            Uri selectedImage = data.getData();
            //String[] filePathColumn = {"_data"};
            if (selectedImage != null) {
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                if (cursor != null) {
//                    cursor.moveToFirst();
////                    String string = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//                    cursor.close();
                imageUri = selectedImage;
                Bitmap b = null;
                try {
                    b = convertBitmap(imageUri, true);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                if (b != null && global != null) {
                    if (frameType == 2) {
                        //stickker view
                        addPicStickerView(b);
                        mCurrentView.originalBitmap = b;
                    } else {
                        // image in frame
//                Canvas canvas = new Canvas();f
//                canvas.drawBitmap(b, 382, 185, null);
                        int xCoord = 0, yCoord = 0;
                        String serverImage = posName;
//                            if (serverImage.contains("aapthi_frame") && CloudDatabase.getFirebase().getFrameUrl() != null) {
//                                if (serverImage.contains(CloudDatabase.getFirebase().getFrameUrl())) {
//                                    serverImage = serverImage.replace(CloudDatabase.getFirebase().getFrameUrl(), "");
//                                }
//                                serverImage = serverImage.replace(".png","");
//                                String requiredString =serverImage;
//                                System.out.println(requiredString);
////                                String requiredString = serverImage.substring(serverImage.indexOf("e") + 1, serverImage.indexOf("."));
//                                int a = Integer.parseInt(requiredString);
//                                String serverCoordinates = CloudDatabase.getFirebase().getServerCoordinates();
//                                if (serverCoordinates != null && !serverCoordinates.equals("")) {
//                                    String[] a1 = serverCoordinates.split(",");
//                                    String[] a2 = new String[a1.length];
//                                    for(int i = a1.length-1 , j = 0; i>= 0 ; i--,j++){
//                                        a2[j] = a1[i];
//                                    }
//                                    System.out.println(a2.length);
//                                    String coordString = a2[a-1];
//
//                                    coordString = coordString.split("_")[1];
//                                    System.out.println(coordString);
////                                    String coordString = serverCoordinates.substring(serverCordinates.indexOf(requiredString + "_") + 3, serverCordinates.indexOf(",", serverCordinates.indexOf(requiredString + "_") + 1));
//
//                                    xCoord = Integer.parseInt(coordString.split(";")[0]);
//                                    yCoord = Integer.parseInt(coordString.split(";")[1]);
//
//                                    if (xCoord != 0) {
//                                        if (eds.global != null) {
//                                            eds.global.setBitmap(eds.global.getBitmap(), xCoord, yCoord);
//                                        }
//                                    }
//                                }
//
//
//                            } else if(serverImage.contains("single_" )){
//                                 String img =  serverImage.replace("single_","");
//                                   img =  img.replace("single_","");
//                                   img =  img.replace(".webp","");
//                                int a = Integer.parseInt(img);
//                                String coord = localCordinates[a-1];
//                                xCoord = Integer.parseInt(coord.split(";")[0]);
//                                yCoord = Integer.parseInt(coord.split(";")[1]);
//
//                            }

                        global.setBitmap(b);
                        if (global.getMyId() == 1) {
                            filterBitMap1 = b;
                        } else if (global.getMyId() == 2) {
                            filterBitMap2 = b;
                        }

                    }
                }
                //  }

            }

        }
        if (requestCode == CAMERA_REQUEST && resultCode == -1) {
//            global.setBitmap(convertBitmap(imageUri, true));
            Bitmap b = null;


           /* File path = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/temp data");
            if (!path.exists()) path.mkdirs();
            File imageFile = new File(path, "valentine_image_capture_edit.jpg");

            Uri stringUri = Uri.fromFile(imageFile);

            try {
                b = Media.getBitmap(EditScreen.this.getContentResolver(), stringUri);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            b = BitmapFactory.decodeFile(getUriPath());

            if (b != null && global != null) {
                if (frameType == 2) {
                    //stickker view
                    addPicStickerView(b);
                    mCurrentView.originalBitmap = b;
                } else {
                    // image in frame
                    int xCoord = 0, yCoord = 0;
                    String serverImage = posName;
//                    if (serverImage.contains("aapthi_frame")&& CloudDatabase.getFirebase().getFrameUrl() != null) {
//                        if (serverImage.contains(CloudDatabase.getFirebase().getFrameUrl())) {
//                            serverImage = serverImage.replace(CloudDatabase.getFirebase().getFrameUrl(), "");
//                        }
////                        String requiredString = serverImage.substring(serverImage.indexOf("e") + 1, serverImage.indexOf("."));
//                        serverImage = serverImage.replace(".png","");
//                        String requiredString =serverImage;
//                        int a = Integer.parseInt(requiredString);
//                        String serverCoordinates = CloudDatabase.getFirebase().getServerCoordinates();
//                        if (serverCoordinates != null && !serverCoordinates.equals("")) {
//                            String[] a1 = serverCoordinates.split(",");
//                            String[] a2 = new String[a1.length];
//                            for(int i = a1.length-1 , j = 0; i>= 0 ; i--,j++){
//                                a2[j] = a1[i];
//                            }
//                            System.out.println(a2.length);
//                            String coordString = a2[a-1];
//
//                            coordString = coordString.split("_")[1];
//                            System.out.println(coordString);
////                                    String coordString = serverCoordinates.substring(serverCordinates.indexOf(requiredString + "_") + 3, serverCordinates.indexOf(",", serverCordinates.indexOf(requiredString + "_") + 1));
//
//                            xCoord = Integer.parseInt(coordString.split(";")[0]);
//                            yCoord = Integer.parseInt(coordString.split(";")[1]);
//
//                            if (xCoord != 0) {
//                                if (eds.global != null) {
//                                    eds.global.setBitmap(eds.global.getBitmap(), xCoord, yCoord);
//                                }
//                            }
//                        }
//
//
//                    }  else if(serverImage.contains("single_")){
//                        String img =  serverImage.replace("single_","");
//                        img =  img.replace("single_","");
//                        img =  img.replace(".webp","");
//                        int a = Integer.parseInt(img);
//                        String coord = localCordinates[a-1];
//                        xCoord = Integer.parseInt(coord.split(";")[0]);
//                        yCoord = Integer.parseInt(coord.split(";")[1]);
//
//                    }

                    global.setBitmap(b);
                    if (global.getMyId() == 1) {
                        filterBitMap1 = b;
                    } else if (global.getMyId() == 2) {
                        filterBitMap2 = b;
                    }
                }
            }

        }
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("N_Photo_Frames", MODE_PRIVATE);
//        boolean isDoublePopShown = pref.getBoolean("isDoublePop", false);
//        if (!isDoublePopShown) {
//            changeImgWithDouble();
//        }


        if (requestCode == 333) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String greeting_path = data.getStringExtra("GREETING_PATH");
                //setBackgroundOfSingleFrame(greeting_path);
                if (greeting_path.contains("Romantic Photo Editor")){
                    Bitmap myBitmap = BitmapFactory.decodeFile(greeting_path);
                    BitmapDrawable background = new BitmapDrawable(myBitmap);
                    frame.setBackgroundDrawable(background);

                }
                else {
                    setBackgroundOfSingleFrame(greeting_path);

                }
            }
        }
    }

    public Bitmap convertBitmap(Uri uri, boolean fromCamera) {
        Bitmap bitmap = null;
        Options bfOptions = new Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inInputShareable = true;
        File file = null;
        if (this.isOutSide) {
            try {
                file = new File(new URI(uri.toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            this.isOutSide = false;
        } else {
            if (uri != null) {
                file = new File(getRealPathFromURI(uri));
            }
        }
        FileInputStream fs = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fs = fileInputStream;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        if (fs != null) {
            try {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        }
        if (fs != null) {
            try {
                fs.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file.getCanonicalPath());
        } catch (IOException e7) {
            e7.printStackTrace();
        }
        int rotate = 0;
        if (exif != null) {
            int a = Objects.requireNonNull(exif).getAttributeInt("Orientation", 1);
            switch (a) {
                case 3:
                    rotate = 180;
                    break;
                case 6:
                    rotate = 90;
                    break;
                case 8:
                    rotate = 270;
                    break;
            }
        }
        Bitmap bitmap2 = getResizedBitmap(bitmap, getWindowManager().getDefaultDisplay().getWidth() / 1, getWindowManager().getDefaultDisplay().getHeight() / 1);
        if (rotate == 0) {
            return bitmap2;
        }
        int w = bitmap2.getWidth();
        int h = bitmap2.getHeight();
        Matrix mtx = new Matrix();
        mtx.preRotate((float) rotate);
        return Bitmap.createBitmap(bitmap2, 0, 0, w, h, mtx, false).copy(Config.ARGB_8888, true);
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));


    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (newWidth > newHeight) {
            int scaleWidth = newWidth;
            return Bitmap.createScaledBitmap(bm, scaleWidth, (height * scaleWidth) / width, true);
        }
        int scaleHeight = newHeight;
        return Bitmap.createScaledBitmap(bm, (width * scaleHeight) / height, scaleHeight, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        AppHelper.displayFBAD(EditScreen.this);
    }

    public void addStickerView(int mResource) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageResource(mResource);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                frameLay.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                if (position >= 0 && position < mViews.size()) {
                    StickerView stickerTemp = (StickerView) mViews.remove(position);
                    mViews.add(mViews.size(), stickerTemp);
                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        frameLay.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public void addPicStickerView(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                frameLay.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                if (position >= 0 && position < mViews.size()) {
                    StickerView stickerTemp = (StickerView) mViews.remove(position);
                    mViews.add(mViews.size(), stickerTemp);
                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        frameLay.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        sourceBitmap.setHasAlpha(true);
        int startWidth = sourceBitmap.getWidth();//  int minX
        int startHeight = sourceBitmap.getHeight();//  int minY
        int endWidth = -1;//  int maxX
        int endHeight = -1;//  int maxY
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = ((sourceBitmap.getPixel(x, y) & 0xff000000) >> 24);
                //
                if (alpha == 0)   // pixel is not 100% transparent
                {
                    // Log.d("Alpha",alpha+" ");
                    if (x < startWidth)
                        startWidth = x;
                    if (x > endWidth)
                        endWidth = x;
                    if (y < startHeight)
                        startHeight = y;
                    if (y > endHeight)
                        endHeight = y;
                }
            }
        }
        if ((endWidth < startWidth) || (endHeight < startHeight))
            return null; // Bitmap is entirely transparent
        Log.w("Startwidh = ", startWidth + " ");
        Log.w("StartHeight = ", startHeight + " ");
        Log.w("Endwidh = ", endWidth + " ");
        Log.w("End Height = ", endHeight + " ");

        // angle=getAngle(startWidth,startHeight,endWidth,endHeight);

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, startWidth, startHeight, (endWidth - startWidth) + 1, (endHeight - startHeight) + 1);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private void myMethod(int n) {
        if (mCurrentView != null) {
            if (mCurrentView.opacityBitmap) {
                if (mCurrentView.effectBitmap != null) {
                    Bitmap bitmap = mCurrentView.effectBitmap;
                    Shader shaderA = new LinearGradient(mCurrentView.effectBitmap.getWidth() - n, 0, mCurrentView.effectBitmap.getWidth(), 0, 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
                    Shader shaderB = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    //Shader shaderC = new LinearGradient(0, 0, 100, bitmap.getHeight(), 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderC = new LinearGradient(0, 0, 0, n, 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderD = new LinearGradient(0, 0, n, 0, 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderE = new LinearGradient(0, bitmap.getHeight() - n, 0, bitmap.getHeight(), 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
                    Shader shader1 = new ComposeShader(shaderA, shaderB, PorterDuff.Mode.SRC_IN);
                    Shader shader2 = new ComposeShader(shaderC, shader1, PorterDuff.Mode.SRC_IN);
                    Shader shader3 = new ComposeShader(shaderD, shader2, PorterDuff.Mode.SRC_IN);
                    Paint paint = new Paint();
                    paint.setShader(new ComposeShader(shaderE, shader3, PorterDuff.Mode.SRC_IN));
                    //lets create a new empty bitmap
                    Bitmap newBitmap = Bitmap.createBitmap(mCurrentView.effectBitmap.getWidth(), mCurrentView.effectBitmap.getHeight(), Config.ARGB_8888);
//        // create a canvas where we can draw on
                    Canvas canvas = new Canvas(newBitmap);

                    canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
//                mCurrentView.effectBitmap = newBitmap;
                    mCurrentView.setBitmap(newBitmap, "blend");
                    mCurrentView.styleBitmpa = true;
                }
            } else {
                if (mCurrentView.originalBitmap != null) {
                    Bitmap bitmap = mCurrentView.originalBitmap;
                    Paint paint = new Paint();
                    Shader shaderA = new LinearGradient(mCurrentView.getBitmap().getWidth() - n, 0, mCurrentView.getBitmap().getWidth(), 0, 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
                    Shader shaderB = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    //Shader shaderC = new LinearGradient(0, 0, 100, bitmap.getHeight(), 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderC = new LinearGradient(0, 0, 0, n, 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderD = new LinearGradient(0, 0, n, 0, 0x00ffffff, 0xffffffff, Shader.TileMode.CLAMP);
                    Shader shaderE = new LinearGradient(0, bitmap.getHeight() - n, 0, bitmap.getHeight(), 0xffffffff, 0x00ffffff, Shader.TileMode.CLAMP);
                    Shader shader1 = new ComposeShader(shaderA, shaderB, PorterDuff.Mode.SRC_IN);
                    Shader shader2 = new ComposeShader(shaderC, shader1, PorterDuff.Mode.SRC_IN);
                    Shader shader3 = new ComposeShader(shaderD, shader2, PorterDuff.Mode.SRC_IN);
                    paint.setShader(new ComposeShader(shaderE, shader3, PorterDuff.Mode.SRC_IN));
                    // lets create a new empty bitmap
                    Bitmap newBitmap = Bitmap.createBitmap(mCurrentView.getBitmap().getWidth(), mCurrentView.getBitmap().getHeight(), Config.ARGB_8888);
//        // create a canvas where we can draw on
                    Canvas canvas = new Canvas(newBitmap);
                    canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
                    mCurrentView.effectBitmap = newBitmap;
                    mCurrentView.setBitmap(newBitmap, "blend");
                    mCurrentView.styleBitmpa = true;
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        app_lang = getSharedPreferences("PHOTOFR", MODE_PRIVATE).getString("Lang", "en");
        phone_lang = Locale.getDefault().getLanguage();
        if (app_lang.equals(phone_lang)) {
//            onInit();
        } else {
            switch (app_lang) {
                case "hi":
                    setLocale("hi");
                    break;
                case "te":
                    setLocale("te");
                    break;
                case "se":
                    setLocale("se");
                    break;
                case "fr":
                    setLocale("fr");
                    break;
                case "de":
                    setLocale("de");
                    break;
                case "ga":
                    setLocale("ga");
                    break;
                case "pt":
                    setLocale("pt");
                    break;
                default:
                    setLocale("en");
                    break;
            }
        }


    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }
//        res.updateConfiguration(conf, dm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(conf);
        } else {
            res.updateConfiguration(conf, dm);
        }
//        Intent refresh = new Intent(this, StartingActivity.class);
//        startActivity(refresh);

        onInit();
    }


    @Override
    public void onThumbnailClick(Filter filter) {
        int height = filterBitMap.getHeight();
        int width = filterBitMap.getWidth();

        Bitmap filterBmp = filter.processFilter(Bitmap.createScaledBitmap(filterBitMap, width, height - 100, false));
        if (global != null) {
            global.setBitmap(filterBmp);
        }
//        mCurrentView.originalBitmap = filterBmp;

    }

}
