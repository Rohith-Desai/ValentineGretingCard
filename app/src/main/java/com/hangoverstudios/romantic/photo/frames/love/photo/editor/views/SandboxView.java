package com.hangoverstudios.romantic.photo.frames.love.photo.editor.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleFrameEditActivity;

public class SandboxView extends View implements OnTouchListener {
    private SingleFrameEditActivity activit;
//    private EditScreenTwo activit2;
    private float angle = 0.0f;
    private Bitmap bitmap;
    private int height;
    private boolean isInitialized = false;
    boolean xyChangedFirstTap, xyChangedSecondTap = false;
    boolean xyChanged = true;
    private Vector2D position = new Vector2D();
    private float scale = 0.5f;
    private int selectedCategory;
    private TouchManager touchManager = new TouchManager(2);
    private Matrix transform = new Matrix();
    private int width;
    int xChanged, yChanged;
    private GestureDetectorCompat mDetector;

    GestureDetector gestureDetector;
//
//    public EditScreenTwo getActivit2() {
//        return this.activit2;
//    }
//
//    public void setActivit2(EditScreenTwo activit22) {
//        this.activit2 = activit22;
//    }


    public SingleFrameEditActivity getActivit() {
        return activit;
    }

    public void setActivit(SingleFrameEditActivity activit) {
        this.activit = activit;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(@NonNull Bitmap b) {
        this.bitmap = b;
        invalidate();
    }

    public void updateBitmap(@NonNull Bitmap b) {
        this.bitmap = b;
        invalidate();
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            openCamDialog(e, "double");
//            EditScreen.showDialog(this);
            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }

    public void setBitmap(Bitmap b, int x, int y) {
        xyChanged = false;
        xChanged = x;
        yChanged = y;
        this.position.set((float) (width / 854) * x, (float) (height / 480) * y);
//        this.position.set(x, y);
        this.bitmap = b;
        invalidate();
    }

    public SandboxView(Context context, Bitmap bitmap2, int selectedCategory2) {
        super(context);
        this.selectedCategory = selectedCategory2;
        this.bitmap = bitmap2;
        if (selectedCategory2 == 100) {
            this.scale = 1.0f;
        } else {
            this.scale = 0.5f;
        }

        gestureDetector = new GestureDetector(context, new GestureListener());
        setOnTouchListener(this);
        //        gestureDetector.setOnDoubleTapListener(this);
    }

    private static float getDegreesFromRadians(float angle2) {
        return (float) ((((double) angle2) * 180.0d) / 3.141592653589793d);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInitialized) {
            this.width = canvas.getWidth();
            this.height = canvas.getHeight();
            this.position.set((float) (getWidth() / 2), (float) (getHeight() / 2));
            this.isInitialized = true;
        }
        Paint paint = new Paint();
        this.transform.reset();
        this.transform.postTranslate(((float) (-this.width)) / 2.0f, ((float) (-this.height)) / 2.0f);
        this.transform.postRotate(getDegreesFromRadians(this.angle));
        this.transform.postScale(this.scale, this.scale);
        this.transform.postTranslate(this.position.getX(), this.position.getY());
        /*if (!xyChanged) {
            this.transform.postTranslate(xChanged, yChanged);
            xyChanged = true;
            xyChangedFirstTap = true;
        }else{
            if(xyChangedFirstTap){
                this.transform.postTranslate(xChanged, yChanged);
                xyChangedFirstTap = false;
                xyChangedSecondTap = true;
            }else{
                if(xyChangedSecondTap){
                    this.transform.postTranslate(xChanged, yChanged);
                    xyChangedSecondTap = false;
                }else{
                    this.transform.postTranslate(this.position.getX(), this.position.getY());
                }
            }
        }*/
        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, this.transform, paint);
        }
    }

    public boolean onTouch(View v, final MotionEvent event) {
        openCamDialog(event, "single");
        return gestureDetector.onTouchEvent(event);
//        return true;
    }


    public void openCamDialog(MotionEvent event, String from) {
        this.activit.reoveStickerBorders();
        if (this.bitmap == null) {
            if (event.getAction() == 1) {
                if (this.activit == null) {
//                    this.activit2.showDialog(this);
                } else {
                    this.activit.showDialog(this);
                }
            }
        } else if (from.equals("double")) {
            this.activit.showDialog(this);
        } else {
            try {
                this.touchManager.update(event);
                if (this.touchManager.getPressCount() == 1) {
                    this.position.add(this.touchManager.moveDelta(0));
                } else if (this.touchManager.getPressCount() == 2) {
                    Vector2D current = this.touchManager.getVector(0, 1);
                    Vector2D previous = this.touchManager.getPreviousVector(0, 1);
                    float currentDistance = current.getLength();
                    float previousDistance = previous.getLength();
                    if (previousDistance != 0.0f) {
                        this.scale *= currentDistance / previousDistance;
                    }
                    this.angle -= Vector2D.getSignedAngleBetween(current, previous);
                }
                invalidate();
            } catch (Throwable th) {
            }
        }
    }

    public int getSelectedCategory() {
        return this.selectedCategory;
    }

    public void setSelectedCategory(int selectedCategory2) {
        this.selectedCategory = selectedCategory2;
    }

}