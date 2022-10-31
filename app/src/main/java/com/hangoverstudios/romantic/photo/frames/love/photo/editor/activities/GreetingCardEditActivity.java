package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingCardsPreviewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.StickerPreViewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.AddStickerFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.ElementsFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.GreetingCardTemplatesFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.CommonMethods;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Utils;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.PhotoDrawView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.StickerView;
import com.google.android.gms.ads.AdView;
import com.isseiaoki.simplecropview.CropImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static android.app.usage.UsageEvents.Event.NONE;
import static android.graphics.Color.RED;
import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.ElementsFragment.selectedImageBitmap;

public class GreetingCardEditActivity extends AppCompatActivity implements View.OnTouchListener {
    private Intent intent;
    private String from;
    CropImageView cropImageView;
    // private LinearLayout rotateLinearBtn, refreshLinearBtn, doneLinearBtn, saveLinearBtn, backLinearBtn;
    private ImageView selectedImage;
    //public RecyclerView stickerRecyclerView;
    public RelativeLayout mContentRootView;
    private Bitmap bitmap;
    PhotoDrawView photoDrawView;
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
    public StickerView mCurrentView;
    public ArrayList<View> mViews;
    //public BubbleTextView mCurrentEditTextView;
    AlertDialog OptionDialog;
    private String categoryType;
    public static GreetingCardEditActivity greetingCardEditActivity;
    //private AdView adViewBanner9;
    private FrameLayout adViewContainer;
    RecyclerView stickersPrevRecycler;
    //StickerPreViewAdapter stickerPreviewAdapter;
    public String RESOURCE_ID_TO_COPY;
    public int INT_RESOURCE_ID_TO_COPY;
    String dResouce[];
    ArrayList<String> imagesFromStorage;
    String subCategory;
    public ImageView newElement, selectedCard;//sticker1, sticker2, sticker3,textSticker1;
    private String TEXT_STICKER_1 = "";
    private View VIEW;
    Context context;
    public FrameLayout hiddenPanel;
    public FrameLayout stickerFragmentFrame;
    double mDensity;
    int viewWidth;
    int viewHeight;
    int bmWidth;
    int bmHeight;
    int actionBarHeight;
    int bottombarHeight;
    double bmRatio;
    double viewRatio;
    private RelativeLayout layoutCanvas;
    private ArrayList<Integer> mResources;
    private String type;
    private View greetingCardView;
    private int POSITION;
    private EditText editText;
    private String filename, filePath;
    private RelativeLayout relImageLayout;
    private ImageView saveImg;
    private AdView adViewBanner2;
    ElementsFragment elementsFragment;
    private Animation bottomDown;
    float dX, dY;
    Boolean isMoving = false;
    private GestureDetector gestureDetector;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int touchMode = NONE;
    private LinearLayout bottomPanel;
    private AddStickerFragment addStickerFragment;
    private LinearLayout backToMain;

    public Bitmap getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(Bitmap selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    private Bitmap selectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting_card_edit);
        greetingCardEditActivity = this;
        POSITION = getIntent().hasExtra("position") ? getIntent().getIntExtra("position", 0) : 0;
        intent = getIntent();
        context = this;
        from = intent.getStringExtra("type");
        categoryType = intent.getStringExtra("category");
        filename = "img_" + String.format("%d.jpg", System.currentTimeMillis());
        adViewContainer = findViewById(R.id.edit_activity_banner_ad);
        adViewContainer.post(new Runnable() {
            @Override
            public void run() {
                CommonMethods.getInstance().loadBannerAd(adViewBanner2, adViewContainer, GreetingCardEditActivity.this);
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        initVars();
        mViews = new ArrayList<>();
        setGreetingCard(POSITION);
        bottomDown = AnimationUtils.loadAnimation(context,
                R.anim.bottom_down);
        //setBottomLayout(categoryType);
        //bitmap = categoriesActivity.getSavedBitmap();
        //   selectedImage.setImageBitmap(bitmap);

        //addStickerView(R.drawable.tem_1_asset_1);
        // addStickerView(R.drawable.tem_1_asset_2);
        //addStickerView(R.drawable.tem_text_1);
        /*if(selectedImageBitmap != null)
        {
            setSelectedImageBitmap(selectedImageBitmap);
        }*/

    }

    private void initializeStickerView() {
        mContentRootView = (RelativeLayout) findViewById(R.id.sticker_view);
        mContentRootView.setDrawingCacheEnabled(true);
        mContentRootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mContentRootView.layout(0, 0, mContentRootView.getMeasuredWidth(), mContentRootView.getMeasuredHeight());
        mContentRootView.buildDrawingCache(true);
    }

    public void setGreetingCard(int position) {
        int localRes = 0;
        final LayoutInflater inflater3 = LayoutInflater.from(this);
        if (position >= 0) {
            switch (position) {
                case 0:
                    localRes = R.layout.valentine_card_1;
                    break;
                case 1:
                    localRes = R.layout.valentine_card_2;
                    break;

            }
        }
        greetingCardView = inflater3.inflate(localRes, null);
        setGreetingCardLayout(position);

    }

    private void resetImageViews() {
        editText = null;
        //sticker1 = null;
        //sticker2 = null;
        //sticker3 = null;
        //textSticker1 = null;
    }

    private void setGreetingCardLayout(int position) {
        resetImageViews();
        if (greetingCardView != null)
            layoutCanvas.removeAllViews();

        layoutCanvas.addView(greetingCardView);
        editText = findViewById(R.id.edit_text);
        selectedCard = findViewById(R.id.selected_image);
        selectedCard.setClickable(false);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }*/
        initializeEditText();
        initializeStickerView();
        clickables();
    }

    public void updateSelectedCard(Bitmap bitmap)
    {
        selectedCard.setImageBitmap(bitmap);
    }

    public Drawable getBitmapSelectedCard()
    {
        return selectedCard.getDrawable();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void openKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                view.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public void setSelectedImageBitmap(Bitmap bitmap) {

        Bitmap mBitmap = getResizedBitmap(bitmap, 900, 900);

        mDensity = getResources().getDisplayMetrics().density;
        actionBarHeight = (int) (110 * mDensity);
        bottombarHeight = (int) (60 * mDensity);
        viewWidth = getResources().getDisplayMetrics().widthPixels;
        viewHeight = getResources().getDisplayMetrics().heightPixels - actionBarHeight - bottombarHeight;
        viewRatio = (double) viewHeight / (double) viewWidth;
        if (mBitmap != null) {
            bmRatio = (double) mBitmap.getHeight() / (double) mBitmap.getWidth();
            if (bmRatio < viewRatio) {
                bmWidth = viewWidth;
                bmHeight = (int) (((double) viewWidth) * ((double) (mBitmap.getHeight()) / (double) (mBitmap.getWidth())));
            } else {
                bmHeight = viewHeight;
                bmWidth = (int) (((double) viewHeight) * ((double) (mBitmap.getWidth()) / (double) (mBitmap.getHeight())));
            }
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        photoDrawView = new PhotoDrawView(this, mBitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, viewWidth, viewHeight);
        photoDrawView.setLayoutParams(new ViewGroup.LayoutParams(viewWidth, viewHeight));
        layoutCanvas.addView(photoDrawView);
        photoDrawView.switchMode(PhotoDrawView.MOVING_MODE);
    }

   /* public void setStickersRecyclerView() {
        type = "stickers";
        mResources = new ArrayList<>();
        mResources.add(R.drawable.sticker_1);
        mResources.add(R.drawable.sticker_2);
        mResources.add(R.drawable.sticker_3);
        mResources.add(R.drawable.sticker_4);
        mResources.add(R.drawable.sticker_5);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        stickerRecyclerView.setLayoutManager(layoutManager);
        stickerPreviewAdapter = new StickerPreViewAdapter(GreetingCardEditActivity.this, mResources, type);
        stickerRecyclerView.setAdapter(stickerPreviewAdapter);
    }*/

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEditText(String tfPath, int txtRes) {
        TEXT_STICKER_1 = Utils.readRawTextFile(context, txtRes);
        // showTextDialog("studio_script", R.color.blue_2, R.raw.valentine_card_5_text_1);
        final Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + tfPath + ".ttf");
        editText.setText(TEXT_STICKER_1);
        editText.setTypeface(tf);
        TEXT_STICKER_1 = editText.getText().toString();
        editText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                selectedCard.setClickable(true);
                if (!hasFocus) {
                    hideKeyboard(v);
                }
                /*else
                    openKeyboard(v);*/

            }
        });
        editText.setOnTouchListener(this);
    }

    private void initializeEditText() {
        if (POSITION >= 0) {
            switch (POSITION) {
                case 0:
                    setEditText("pristina_1", R.raw.valentine_card_1_text_1);
                    break;
                case 1:
                    setEditText("georgia_italic_2", R.raw.valentine_card_2_text_1);
                    break;
                case 2:
                    setEditText("lucida_calligraphy_italic_3", R.raw.valentine_card_3_text_1);
                    break;
                case 3:
                    setEditText("monplesir_script_regular_4", R.raw.valentine_card_4_text_1);
                    break;
                case 4:
                    setEditText("studio_script_5", R.raw.valentine_card_5_text_1);
                    break;
                case 5:
                    setEditText("short_baby_mg_w_6", R.raw.valentine_card_6_text_1);
                    break;
                case 6:
                    setEditText("script_mt_bold_7", R.raw.valentine_card_7_text_1);
                    break;
                case 7:
                    setEditText("mon_amour_one_8", R.raw.valentine_card_8_text_1);
                    break;
                case 8:
                    setEditText("mt_corsva_9", R.raw.valentine_card_9_text_1);
                    break;
                case 9:
                    setEditText("monplesir script_10", R.raw.valentine_card_10_text_1);
                    break;
                case 10:
                    setEditText("lucida_handwriting_Italic_11", R.raw.valentine_card_11_text_1);
                    break;
                case 11:
                    setEditText("lucida_calligraphy_italic_12", R.raw.valentine_card_12_text_1);
                    break;
                case 12:
                    setEditText("alex_brush_13", R.raw.valentine_card_13_text_1);
                    break;
                case 13:
                    setEditText("aiana_violeta_14", R.raw.valentine_card_14_text_1);
                    break;
                case 14:
                    setEditText("believeIt_15", R.raw.valentine_card_15_text_1);
                    break;
                case 15:
                    setEditText("bitcblked_16", R.raw.valentine_card_16_text_1);
                    break;
                case 16:
                    setEditText("mt_corsva_9", R.raw.valentine_card_17_text_1);
                    break;
                case 17:
                    setEditText("becky_tahlia_18", R.raw.valentine_card_18_text_1);
                    break;
                case 18:
                    setEditText("dollie_script_19", R.raw.valentine_card_19_text_1);
                    break;
                case 19:
                    setEditText("berdiri_endiri_20", R.raw.valentine_card_20_text_1);
                    break;
                case 20:
                    setEditText("winter_yesterday_21", R.raw.valentine_card_21_text_1);
                    break;
                case 21:
                    setEditText("lucida_handwriting_Italic_22", R.raw.valentine_card_22_text_1);
                    break;
            }
        }
    }


    public void showTextDialog(String tfPath, final int colorRes, int txtRes) {
        final Dialog dialog = new Dialog(GreetingCardEditActivity.this);
        final Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + tfPath + ".ttf");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_custom_dialog);
        final EditText editText = dialog.findViewById(R.id.edit_text);
        editText.setText(colorRes);
        editText.setText(Utils.readRawTextFile(context, txtRes));
        editText.setTypeface(tf);

        TextView dialogOk = dialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textSticker1.setImageBitmap(textAsBitmap(editText.getText().toString(), 150, colorRes, tf));
                TEXT_STICKER_1 = editText.getText().toString();
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

    private void setImageOnTouch(final ImageView imageView) {
        //imageView.setBackground(getDrawable(R.drawable.img_highlight_2));
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor, Typeface typeface) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(typeface);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    private void clickables() {
        mContentRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }*/
                if (mCurrentView != null)
                    mCurrentView.setInEdit(false);
            }
        });
        newElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectedCard.setClickable(false);
                setBottomUpHiddenLayout();
            }
        });
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSelectedPhoto() == null) {
                    Toast.makeText(context, "Please select photo..", Toast.LENGTH_SHORT).show();
                } else {
                    greetingCardEditActivity.mContentRootView.setVisibility(GONE);
                    editText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight_2));
                    if (editText.isFocusable()) {
                        editText.clearFocus();
                    }
                    save();
                }
            }
        });
       /* if (editText != null) {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isMoving)
                    {
                        selectedCard.setClickable(true);
                        editText.selectAll();
                        Toast.makeText(context, "edit cliked..", Toast.LENGTH_SHORT).show();
                    }
                    //editText.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight));
                }
            });
        }*/
        selectedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSelectedPhoto() == null) {
                    if (editText.isFocusable() || selectedCard.isClickable()) {
                        if (editText.isFocusable()) {
                            editText.clearFocus();
                        }
                        if (selectedCard.isClickable()) {
                            if ((elementsFragment != null) && (elementsFragment.isVisible())) {
                                removeElementsFragment(true);
                            } else {
                                setBottomUpHiddenLayout();
                            }
                            //    selectedCard.setClickable(false);
                        }
                    }
                } else {
                    selectedCard.setClickable(false);
                }
            }
        });
       /* layoutCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCard.setClickable(false);
                setBottomUpHiddenLayout();
            }
        });*/
        /*if (sticker1 != null) {
            sticker1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VIEW = view;
                    deleteImage.setVisibility(VISIBLE);
                    sticker1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight));
                    sticker2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight_2));
                }
            });
        }
        if (sticker2 != null) {
            sticker2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VIEW = view;
                    deleteImage.setVisibility(VISIBLE);
                    sticker1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight_2));
                    sticker2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight));
                }
            });
        }
        if (sticker3 != null) {
            sticker3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VIEW = view;
                    deleteImage.setVisibility(VISIBLE);
                    sticker1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight_2));
                    sticker2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.img_highlight));
                }
            });
        }*/
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    public static Bitmap createBitmap(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void openElementsFragment() {
        elementsFragment = Utils.addElementsFragment(this, R.id.new_hidden_fragment, true, null);
    }
    private void openAddStickerFragment() {

        removeElementsFragment(true);
        addStickerFragment = Utils.addAddStickerFragment(this, R.id.sticker_fragment_container, true, null);
    }

    private void setBottomUpHiddenLayout() {
        //setGreetingCard(POSITION);
        //textSticker1.setClickable(false);
        newElement.setClickable(false);
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.anim.bottom_up);
        hiddenPanel = (FrameLayout) findViewById(R.id.new_hidden_fragment);
        hiddenPanel.setVisibility(VISIBLE);
        hiddenPanel.startAnimation(bottomUp);
        bottomUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomPanel.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        openElementsFragment();
        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.new_hidden_fragment, new ElementsFragment()).commit();*/
        // textSticker1.setVisibility(View.INVISIBLE);
       /* hiddenPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(*//*motionEvent.getAction() == MotionEvent.ACTION_DOWN||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE ||*//*
                        motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN *//*||
                        motionEvent.getAction() == MotionEvent.ACTION_UP*//*)
                {
                    if(hiddenPanel.getVisibility() == VISIBLE)
                    {
                        Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
                        hiddenPanel.startAnimation(bottomDown);
                        hiddenPanel.setVisibility(GONE);
                       // textSticker1.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });*/
    }
    public void setStickerFragment() {
        //setGreetingCard(POSITION);
        //textSticker1.setClickable(false);
        newElement.setClickable(false);
        Animation bottomUp = AnimationUtils.loadAnimation(context,
                R.anim.bottom_up);
        stickerFragmentFrame = (FrameLayout) findViewById(R.id.sticker_fragment_container);
        stickerFragmentFrame.setVisibility(VISIBLE);
        stickerFragmentFrame.startAnimation(bottomUp);
        bottomUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomPanel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        openAddStickerFragment();
        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.new_hidden_fragment, new ElementsFragment()).commit();*/
        // textSticker1.setVisibility(View.INVISIBLE);
       /* hiddenPanel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(*//*motionEvent.getAction() == MotionEvent.ACTION_DOWN||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE ||*//*
                        motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN *//*||
                        motionEvent.getAction() == MotionEvent.ACTION_UP*//*)
                {
                    if(hiddenPanel.getVisibility() == VISIBLE)
                    {
                        Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
                        hiddenPanel.startAnimation(bottomDown);
                        hiddenPanel.setVisibility(GONE);
                       // textSticker1.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });*/
    }

    public void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private void initVars() {
 /*       rotateLinearBtn = findViewById(R.id.rotate_btn_linear);
        refreshLinearBtn = findViewById(R.id.refresh_btn_linear);
        doneLinearBtn = findViewById(R.id.done_btn_linear);
        saveLinearBtn = findViewById(R.id.save_btn_linear);
        backLinearBtn = findViewById(R.id.back_btn_linear);*/

        selectedImage = (ImageView) findViewById(R.id.selected_image);
        //stickersPrevRecycler = findViewById(R.id.stickers_preview_recycler);
        newElement = findViewById(R.id.new_element);
        saveImg = findViewById(R.id.save_img);
        layoutCanvas = findViewById(R.id.rl_content_root);
        //stickerRecyclerView = findViewById(R.id.stickers_preview_recycler);
        backToMain = findViewById(R.id.back_to_main);
        relImageLayout = findViewById(R.id.rl_content_root);
        bottomPanel = findViewById(R.id.card_edit_bottom_panel);
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

    public ArrayList<String> getImagesFromDevice(String directoryPath) {
        final ArrayList<String> tempAudioList = new ArrayList<>();
        File directory1 = new File(directoryPath + "/");
        File[] files = directory1.listFiles();
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

  /*  @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_POINTER_DOWN ||
                event.getAction() == MotionEvent.ACTION_UP)
        {
            if(hiddenPanel.getVisibility() == VISIBLE)
            {
                Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.bottom_down);
                hiddenPanel.startAnimation(bottomDown);
                hiddenPanel.setVisibility(GONE);
              //  textSticker1.setVisibility(VISIBLE);
            }
        }
        return super.onTouchEvent(event);
    }*/

    /*  @Override
      public void onClick(View view) {
          if(view.getId() != R.id.img_1 || view.getId() != R.id.img_2)
          {
              sticker1.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.img_highlight_2));
              sticker2.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.img_highlight_2));
          }
      }*/
    public void save() {
        //ArrayList<String> finalEventList = new ArrayList<>(CommonMethods.getInstance().getBitmapEventQueue());
        //ArrayList<String> trimmedFinalEventList = new ArrayList<>();
      /*for (int i = 0; i < finalEventList.size(); i++) {
          trimmedFinalEventList.add(finalEventList.get(i).substring(finalEventList.get(i).lastIndexOf("/") + 1));
      }
      Log.v("bitmapEventList", "final assets : " + finalEventList.toString());
      Bundle bundle = new Bundle();
      for (int i = 0; i < trimmedFinalEventList.size(); i++) {
          bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, trimmedFinalEventList.get(i));
          bundle.putString("Activity", "CategoriesActivity");
          if (mFirebaseAnalytics != null)
              mFirebaseAnalytics.logEvent("finalSaveBitmapImage", bundle);
      }
      CommonMethods.getInstance().clearBitmapEventQueue();*/
        Bitmap bitmap = saveDrawnBitmap(relImageLayout);
        String path = Environment.getExternalStorageDirectory().toString() + "/" + getResources().getString(R.string.app_name);
        File dir = new File(path);
        dir.mkdirs();
        File dest = new File(dir, filename);
        try {
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshGallery(dest);
        filePath = path + "/" + filename;
        Intent intent = new Intent(GreetingCardEditActivity.this, SaveShareImageActivity2.class);
        intent.putExtra("PATH", filePath);
        startActivity(intent);
        finish();
    }

    public Bitmap saveDrawnBitmap(RelativeLayout relativeLayout) {
        Bitmap saveBitmap = Bitmap.createBitmap(relativeLayout.getWidth(), relativeLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(saveBitmap);
        relativeLayout.draw(cv);
        return saveBitmap;
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    public void removeElementsFragment(boolean setClickable) {
        elementsFragment = Utils.getElementsFragment(this);
        newElement.setClickable(true);
        bottomPanel.setVisibility(VISIBLE);
        selectedCard.setClickable(setClickable);
        hiddenPanel.startAnimation(bottomDown);
        hiddenPanel.setVisibility(GONE);
        bottomDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(elementsFragment).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //localGalleryFragment.onBackPressed();
        // CommonMethods.getInstance().activitiesAD(GreetingCardEditActivity.this);
    }
    public void removeAddStickerFragment(boolean setClickable) {
        addStickerFragment = Utils.getAddStickerFragment(this);
        newElement.setClickable(true);
        bottomPanel.setVisibility(VISIBLE);
        selectedCard.setClickable(setClickable);
        stickerFragmentFrame.startAnimation(bottomDown);
        stickerFragmentFrame.setVisibility(GONE);
        bottomDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSupportFragmentManager().beginTransaction()
                        .remove(addStickerFragment).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //localGalleryFragment.onBackPressed();
        // CommonMethods.getInstance().activitiesAD(GreetingCardEditActivity.this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if ((elementsFragment != null) && (elementsFragment.isVisible())) {

            removeElementsFragment(true);
        }
        else if ((addStickerFragment != null) && (addStickerFragment.isVisible())) {

            removeAddStickerFragment(true);
        }else
            super.onBackPressed();
        // CommonMethods.getInstance().activitiesAD(GreetingCardEditActivity.this);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (gestureDetector.onTouchEvent(motionEvent)) {
            // single tap
            /*editText.requestFocus();
            editText.setActivated(true);
            editText.setPressed(true);
            editText.setFocusable(true);
            editText.setEnabled(true);*/
           // editText.setClickable(true);
           // editText.setFocusableInTouchMode(true);
            //Toast.makeText(context, "edit cliked..", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // your code for move and drag
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                   /* dX = view.getX() - motionEvent.getRawX();
                    dY = view.getY() - motionEvent.getRawY();*/
                    dX = view.getX() - motionEvent.getRawX();
                    dY = view.getY() - motionEvent.getRawY();
                    touchMode = DRAG;
                    // Toast.makeText(context, "edit cliked..", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (touchMode == DRAG) {
                        isMoving = true;
                        view.animate()
                                .x(motionEvent.getRawX() + dX - ((float) view.getWidth() / 100))
                                .y(motionEvent.getRawY() + dY - ((float) view.getHeight() / 100))
                                .setDuration(0)
                                .start();
                    }
                   // invalidate();
                    break;
                default:
                    return false;
            }
        }

        return true;
    }
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            editText.requestFocus();
            editText.setActivated(true);
            editText.setPressed(true);
            editText.setFocusable(true);
            editText.setEnabled(true);
            openKeyboard(editText);
            // editText.setClickable(true);
            editText.setFocusableInTouchMode(true);
            //Toast.makeText(context, "edit cliked..", Toast.LENGTH_SHORT).show();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            editText.selectAll();
            super.onLongPress(e);
        }
    }
}