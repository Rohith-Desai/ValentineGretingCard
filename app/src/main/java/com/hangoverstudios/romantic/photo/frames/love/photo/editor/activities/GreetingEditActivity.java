package com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingStickerPreViewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.BubbleTextView;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.StickerView;

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
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;

public class GreetingEditActivity extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 201;
    private static final int CAMERA_REQUEST = 1888;
    public static int RESULT_LOAD_IMAGE = 1;
    public static Uri imageUri;
    public static GreetingEditActivity eObj;
    public File imageFile;
    LinearLayout blend_layout;
    ImageView greeting_bg;
    FrameLayout frame;
    FrameLayout frameLay;
    int shadowValue = 0;
    Typeface type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9, type_10, type_11, type_12,
            type_13, type_14, type_15, type_16, type_17, type_18, type_19, type_20, type_21, type_22, selectedType;
    SeekBar outerTrans, opacitybar;
    String savedImage;
    ImageView back_arrow;
    private boolean isOutSide = false;
    private GreetingStickerPreViewAdapter stickerPreviewAdapter;
    private RecyclerView stickerRecyclerView;
    private ArrayList<Integer> mResources;
    private String[] types;
    private BubbleTextView mCurrentEditTextView;
    private TextView frames, stickers, borders, filters, text, add_pic, album, blend, save, share;
    private int currentColor = Color.parseColor("#00ff00");
    private StickerView mCurrentView;
    private AlertDialog dialog;
    private ArrayList<View> mViews;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eObj = this;
        setContentView(R.layout.activity_greeting_edit);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        frameLay = findViewById(R.id.FrameLayout1);
        mViews = new ArrayList<>();
        stickerRecyclerView = findViewById(R.id.stickers_recycle);

        frames = findViewById(R.id.frames);
        blend_layout = findViewById(R.id.blend_layout);
        save = findViewById(R.id.save);
        text = findViewById(R.id.text_style);
        opacitybar = findViewById(R.id.opacitybar);
        outerTrans = findViewById(R.id.outerTrans);
        stickers = findViewById(R.id.stickers);
        add_pic = findViewById(R.id.add_pic);
        blend = findViewById(R.id.blend);
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        types = new String[]{"earwig factory", "action jackson", "barbatrick", "if", "BrushScriptStd",
                "FancyPantsNF", "Fiddums Family", "Fortunaschwein_complete", "FUNDR__", "HoboStd",
                "hotpizza", "NuevaStd-Bold", "NuevaStd-BoldCond", "NuevaStd-BoldCondItalic",
                "NuevaStd-Cond", "NuevaStd-CondItalic", "NuevaStd-Italic"};


        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertDialog();
            }
        });

        add_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                blend_layout.setVisibility(View.GONE);

                showDialog();


            }
        });

        stickers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStickersRecyclerView();
            }
        });

        blend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                reoveStickerBorders();
                if (blend_layout.getVisibility() == View.VISIBLE) {
                    blend_layout.setVisibility(View.GONE);
                } else {
                    blend_layout.setVisibility(View.VISIBLE);
                }
            }
        });


        String imagePath = "";

        if (getIntent().hasExtra("PATH")) {
            imagePath = getIntent().getStringExtra("PATH");
        }
        if (getIntent().hasExtra("FROM")){
            from=getIntent().getStringExtra("FROM");
        }

        frame = findViewById(R.id.bg_frame);
        setBackgroundOfGreeting(imagePath);

//                .placeholder(R.drawable.loading_img)
//                .into(greeting_bg);


        opacitybar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int opacity = 100; // from 0 to 255

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                changeOPacity(progresValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        outerTrans.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                myMethod(250);
                myMethod(i);
                System.out.println(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println(seekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println(seekBar);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withoutMark();
            }
        });


        frames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from!=null && from.equals("Trending_Greeting")) {
                    Intent i = new Intent(GreetingEditActivity.this, GreetingTrendSubListActivity.class);
                    startActivityForResult(i, 123);
                }
                else {
                    Intent intent = new Intent(GreetingEditActivity.this, SubGreetingActivity.class);
                    startActivityForResult(intent, 123);

                }

            }
        });
        frameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                reoveStickerBorders();
            }
        });

        createFonts();
    }

    public void setBackgroundOfGreeting(String imagePath) {
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
            Resources resources = GreetingEditActivity.this.getResources();
            int resourceId = resources.getIdentifier(imagePath, "drawable",
                    GreetingEditActivity.this.getPackageName());
            if (Objects.requireNonNull(getResources()).getIdentifier(imagePath, "drawable", getPackageName()) > 0) {
                if (resourceId > 0) {
                    Drawable drawable = resources.getDrawable(resourceId);
                    frame.setBackground(drawable);
//                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.single_1);
                }
            }
        }

    }


    private void withoutMark() {

        reoveStickerBorders();
        frameLay.setDrawingCacheEnabled(true);
        // Toast.makeText(GreetingEditActivity.this,"save", LENGTH_SHORT).show();
        saveImageToSDCard(frameLay.getDrawingCache());
//        if (GreetingEditActivity.this.saveImageToSDCard(frameLay.getDrawingCache())) {
//            startActivity(new Intent(GreetingEditActivity.this, SaveShareImageActivity2.class)
//                    .putExtra("SAVED_IMG", savedImage));
//            finish();
//            Toast.makeText(GreetingEditActivity.this, "Saved successfully in Album", LENGTH_SHORT).show();
//        }
        frameLay.setDrawingCacheEnabled(false);
    }

    /* public boolean saveImageToSDCard(Bitmap b) {
         if (b != null) {
                String  filename = "img_" + String.format("%d.jpg", System.currentTimeMillis());
                 String path = Environment.getExternalStorageDirectory().toString() +"/Android"+"/data/"+getPackageName()+ "/" + getResources().getString(R.string.app_name);
                 File dir = new File(path);
                 dir.mkdirs();
                 File dest = new File(dir, filename);
                 try {
                     FileOutputStream out = new FileOutputStream(dest);
                     b.compress(Bitmap.CompressFormat.PNG, 100, out);

                     out.flush();
                     out.close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 savedImage = path + "/" + filename;
                 mainActivity.updateList(false);
                 Intent intent = new Intent(GreetingEditActivity.this, SaveShareImageActivity2.class);
                 intent.putExtra("PATH", savedImage);
                 startActivity(intent);

                 return true;

         }

         return false;
     }*/
    public boolean saveImageToSDCard(Bitmap b) {
        if (b != null) {

            File mydir = new File(getApplicationContext().getExternalFilesDir(getResources().getString(R.string.app_name)).getAbsolutePath());
            if (!mydir.exists()) {
                mydir.mkdirs();
                Toast.makeText(getApplicationContext(), "Directory Created", Toast.LENGTH_LONG).show();
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
            Intent intent = new Intent(GreetingEditActivity.this, SaveShareImageActivity2.class);
            intent.putExtra("PATH", savedImage);
            startActivity(intent);

            return true;

        }


        return false;
    }

   /* public boolean saveImageToSDCard(Bitmap b) {
        FileOutputStream fos;
        if (b!=null){

            try {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    String  filename = "img_" + String.format("%d.jpg", System.currentTimeMillis());
                    String path = Environment.getExternalStorageDirectory().toString() + "/" + getResources().getString(R.string.app_name);
                    File dir = new File(path);
                    if (!dir.exists()){

                        dir.mkdirs();

                    }


                    ContentResolver resolver=getContentResolver();
                    ContentValues contentValues=new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,filename);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,path);
                    Uri imageuri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

                    fos=(FileOutputStream)resolver.openOutputStream(Objects.requireNonNull(imageuri));
                    b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    Objects.requireNonNull(fos);
                    savedImage = path + "/" + filename;
                    mainActivity.updateList(false);
                    Intent intent = new Intent(GreetingEditActivity.this, SaveShareImageActivity2.class);
                    intent.putExtra("PATH", savedImage);
                    startActivity(intent);
                    Toast.makeText(GreetingEditActivity.this,"save", LENGTH_SHORT).show();

                    return true;


                }

            }
            catch (Exception e){

                Toast.makeText(GreetingEditActivity.this,e.getMessage()+"notsave", LENGTH_SHORT).show();

            }

        }


        return false;
    }*/


    public void reoveStickerBorders() {
        if (mCurrentEditTextView != null) {
            mCurrentEditTextView.setInEdit(false);
        }
        if (mCurrentView != null)
            mCurrentView.setInEdit(false);

        blend_layout.setVisibility(View.GONE);
        stickerRecyclerView.setVisibility(View.GONE);

//        edit_recycle.setVisibility(View.GONE);
//        l2.setVisibility(View.INVISIBLE);


    }

    private void changeOPacity(int progresValue) {
        if (mCurrentView != null) {
            Bitmap newBitmap = null;
            mCurrentView.opacityBitmap = true;
            if (mCurrentView.styleBitmpa) {
                newBitmap = Bitmap.createBitmap(mCurrentView.effectBitmap.getWidth(), mCurrentView.effectBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);
                Paint alphaPaint = new Paint();
                alphaPaint.setAlpha(progresValue);
                canvas.drawBitmap(mCurrentView.effectBitmap, 0, 0, alphaPaint);
//                mCurrentView.effectBitmap = newBitmap;

            } else {
                if (mCurrentView.originalBitmap != null) {
                    newBitmap = Bitmap.createBitmap(mCurrentView.originalBitmap.getWidth(), mCurrentView.originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
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
                mCurrentView.setBitmap("blend", newBitmap);
            }

        }
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
                    Bitmap newBitmap = Bitmap.createBitmap(mCurrentView.effectBitmap.getWidth(), mCurrentView.effectBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        // create a canvas where we can draw on
                    Canvas canvas = new Canvas(newBitmap);

                    canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
//                mCurrentView.effectBitmap = newBitmap;
                    mCurrentView.setBitmap("blend", newBitmap);
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
                    Bitmap newBitmap = Bitmap.createBitmap(mCurrentView.getBitmap().getWidth(), mCurrentView.getBitmap().getHeight(), Bitmap.Config.ARGB_8888);
//        // create a canvas where we can draw on
                    Canvas canvas = new Canvas(newBitmap);
                    canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
                    mCurrentView.effectBitmap = newBitmap;
                    mCurrentView.setBitmap("blend", newBitmap);
                    mCurrentView.styleBitmpa = true;
                }
            }
        }
    }


    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
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
        stickerPreviewAdapter = new GreetingStickerPreViewAdapter(this, mResources, type, "greeting");
        stickerRecyclerView.setAdapter(stickerPreviewAdapter);
    }

    public void addStickerView(int mResource) {
        final StickerView stickerView = new StickerView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mResource);
        stickerView.setBitmap(bitmap, "int");
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                frameLay.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                /*if (mCurrentEditTextView != null) {
                    mCurrentEditTextView.setInEdit(false);
                }*/
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
        frameLay.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }


    private void openAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyCustomTheme);
        LayoutInflater inflater = (this).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
        final EditText enteredText = (EditText) dialogView.findViewById(R.id.text_view);
        final TextView textView = (TextView) dialogView.findViewById(R.id.text);
        final ImageButton cancelBtn = (ImageButton) dialogView.findViewById(R.id.cancel_text_btn);
        final ImageButton colorPickerBtn = (ImageButton) dialogView.findViewById(R.id.color_picker_btn);
        final SeekBar shadowBg = (SeekBar) dialogView.findViewById(R.id.shadow_bg);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter;
//        if (app_lang.equals("hi")) {
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types_hi);
//        } else if (app_lang.equals("te")) {
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types_te);
//        } else {
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
//        }
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
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
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredText.setText("");
            }
        });
        colorPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(GreetingEditActivity.this, currentColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
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
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        canvas.drawText(text, 0, baseline, paint);
        return image;
    }


    public void showDialog() {

        if (dialog == null || !dialog.isShowing()) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367057, new String[]{"Take from Camera", "Select from Gallery"});
            ArrayAdapter<String> adapter = new ArrayAdapter<>(GreetingEditActivity.this, android.R.layout.simple_list_item_1, new String[]{getString(R.string.take_camera), getString(R.string.select_gallery),});
            AlertDialog.Builder builder = new AlertDialog.Builder(GreetingEditActivity.this,R.style.MyCustomTheme);
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
                        Intent pickIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        if (ContextCompat.checkSelfPermission(GreetingEditActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
           /* File path = new File(Environment.getExternalStorageDirectory()
                    + "/root/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/temp data");
            if (!path.exists()) path.mkdirs();
            File image = new File(path, "valentine_image_capture_greeting.jpg");

//            Uri imageUri = FileProvider.getUriForFile(GreetingEditActivity.this.getApplicationContext(), getApplicationContext().getPackageName() + ".provider", image);
            Uri imageUri = FileProvider.getUriForFile(GreetingEditActivity.this, getApplicationContext().getPackageName() + ".provider", image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
            File photo = null;
            try {
                photo = createImageFiles();
                Uri photoURI = FileProvider.getUriForFile(GreetingEditActivity.this, getApplicationContext().getPackageName() + ".provider", photo);
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
    private static final String PHOTO_PREFERENCE_NAME = "greetingedite_temp_data";
    private static final String PHOTO_KEY = "GREETINGEDITE_IMAGE_URI_PATH";
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
                        if (imageUri != null) {
                            b = convertBitmap(imageUri, true);
                           // b = getResizedBitmap(b, 250);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    if (b != null) {

                        //stickker view
                        blend_layout.setVisibility(View.VISIBLE);
                        addPicStickerView(b);
                        mCurrentView.originalBitmap = b;
                    }
             //   }

            }

        }
        if (requestCode == CAMERA_REQUEST && resultCode == -1) {
//            global.setBitmap(convertBitmap(imageUri, true));
            Bitmap b = null;


           /* File path = new File(Environment.getExternalStorageDirectory()
                    + "/root/Android/data/"
                    + getApplicationContext().getPackageName()
                    + "/temp data");
            if (!path.exists()) path.mkdirs();
            File imageFile = new File(path, "valentine_image_capture_greeting.jpg");

            Uri stringUri = Uri.fromFile(imageFile);

            try {
                b = MediaStore.Images.Media.getBitmap(GreetingEditActivity.this.getContentResolver(), stringUri);
                b = getResizedBitmap(b, 250);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }*/
            b = BitmapFactory.decodeFile(getUriPath());

            if (b != null) {

                //stickker view
                blend_layout.setVisibility(View.VISIBLE);
                addPicStickerView(b);
                mCurrentView.originalBitmap = b;

            }

        }
//
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("N_Photo_Frames", MODE_PRIVATE);
//        boolean isDoublePopShown = pref.getBoolean("isDoublePop", false);
//        if (!isDoublePopShown) {
//            changeImgWithDouble();
//        }


        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String greeting_path = data.getStringExtra("GREETING_PATH");
                //setBackgroundOfGreeting(greeting_path);
                if (greeting_path.contains("Romantic Photo Editor")){
                    Bitmap myBitmap = BitmapFactory.decodeFile(greeting_path);
                    BitmapDrawable background = new BitmapDrawable(myBitmap);
                    frame.setBackgroundDrawable(background);

                }
                else {
                    setBackgroundOfGreeting(greeting_path);

                }

            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

    public Bitmap convertBitmap(Uri uri, boolean fromCamera) {
        Bitmap bitmap = null;
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
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
        return Bitmap.createBitmap(bitmap2, 0, 0, w, h, mtx, false).copy(Bitmap.Config.ARGB_8888, true);
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
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(GreetingEditActivity.this,R.style.MyCustomTheme);
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
        android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void createFonts() {

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