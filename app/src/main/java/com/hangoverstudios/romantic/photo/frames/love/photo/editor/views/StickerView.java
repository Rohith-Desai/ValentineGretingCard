package com.hangoverstudios.romantic.photo.frames.love.photo.editor.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.view.MotionEventCompat;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity.greetingCardEditActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.AddStickerFragment.addStickerFragment;

public class StickerView extends androidx.appcompat.widget.AppCompatImageView {
    public Bitmap originalBitmap, effectBitmap;
    public boolean styleBitmpa, opacityBitmap;

    private Bitmap deleteBitmap;
    private Bitmap flipVBitmap;
    private Bitmap topBitmap;
    private Bitmap resizeBitmap;
    private Bitmap mBitmap;
    private Rect dst_delete;
    private Rect dst_resize;
    private Rect dst_flipV;
    private Rect dst_top;
    private int deleteBitmapWidth;
    private int deleteBitmapHeight;
    private int resizeBitmapWidth;
    private int resizeBitmapHeight;
    private int flipVBitmapWidth;
    private int flipVBitmapHeight;
    private int topBitmapWidth;
    private int topBitmapHeight;
    private Paint localPaint;
    private int mScreenwidth, mScreenHeight;
    private static final float BITMAP_SCALE = 0.5f;
    private PointF mid = new PointF();
    private OperationListener operationListener;
    private float lastRotateDegree;

    private boolean isPointerDown = false;
    private final float pointerLimitDis = 20f;
    private final float pointerZoomCoeff = 0.09f;
    private float lastLength;
    private boolean isInResize = false;
    private Matrix matrix = new Matrix();
    private boolean isInSide;
    private float lastX, lastY;
    private boolean isInEdit = true;
    private float MIN_SCALE = 0.05f;
    private float MAX_SCALE = 2.0f;
    private double halfDiagonalLength;
    private float oringinWidth = 0;
    private float oldDis;
    private DisplayMetrics dm;
    private boolean isHorizonMirror = false;
    private final int LB = 10;
    private Context CONTEXT;
    String TYPE = "";

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CONTEXT = context;
        init();
    }

    public StickerView(Context context) {
        super(context);
        CONTEXT = context;
        init();
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CONTEXT = context;
        init();
    }

    private void init() {

        dst_delete = new Rect();
        dst_resize = new Rect();
        dst_flipV = new Rect();
        dst_top = new Rect();
        localPaint = new Paint();
        localPaint.setColor(getResources().getColor(R.color.blue));
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(2.0f);
        dm = getResources().getDisplayMetrics();
        mScreenwidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isHorizonMirror) {
            if (mBitmap != null) {
                float[] arrayOfFloat = new float[9];
                matrix.getValues(arrayOfFloat);
                float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] + LB;
                float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
                float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] - LB;
                float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
                float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] + LB;
                float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] + LB;
                float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] - LB;
                float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] + LB;
                canvas.save();
                canvas.drawBitmap(mBitmap, matrix, null);
                dst_delete.left = (int) (f3 - deleteBitmapWidth / 4);
                dst_delete.right = (int) (f3 + deleteBitmapWidth / 4);
                dst_delete.top = (int) (f4 - deleteBitmapHeight / 4);
                dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 4);
                dst_resize.left = (int) (f7 - resizeBitmapWidth / 4);
                dst_resize.right = (int) (f7 + resizeBitmapWidth / 4);
                dst_resize.top = (int) (f8 - resizeBitmapHeight / 4);
                dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 4);
                dst_top.left = (int) (f1 - flipVBitmapWidth / 4);
                dst_top.right = (int) (f1 + flipVBitmapWidth / 4);
                dst_top.top = (int) (f2 - flipVBitmapHeight / 4);
                dst_top.bottom = (int) (f2 + flipVBitmapHeight / 4);
                dst_flipV.left = (int) (f5 - topBitmapWidth / 4);
                dst_flipV.right = (int) (f5 + topBitmapWidth / 4);
                dst_flipV.top = (int) (f6 - topBitmapHeight / 4);
                dst_flipV.bottom = (int) (f6 + topBitmapHeight / 4);
                if (isInEdit) {
                    canvas.drawLine(f1, f2, f3, f4, localPaint);
                    canvas.drawLine(f3, f4, f7, f8, localPaint);
                    canvas.drawLine(f5, f6, f7, f8, localPaint);
                    canvas.drawLine(f5, f6, f1, f2, localPaint);
                    canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                    canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                    canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
                    canvas.drawBitmap(topBitmap, null, dst_top, null);
                }
                canvas.restore();
            }
        } else {
            if (mBitmap != null) {
                float[] arrayOfFloat = new float[9];
                matrix.getValues(arrayOfFloat);
                float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] - LB;
                float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
                float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] + LB;
                float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
                float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] - LB;
                float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] + LB;
                float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] + LB;
                float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] + LB;
                canvas.save();
                canvas.drawBitmap(mBitmap, matrix, null);
                dst_delete.left = (int) (f3 - deleteBitmapWidth / 4);
                dst_delete.right = (int) (f3 + deleteBitmapWidth / 4);
                dst_delete.top = (int) (f4 - deleteBitmapHeight / 4);
                dst_delete.bottom = (int) (f4 + deleteBitmapHeight / 4);
                dst_resize.left = (int) (f7 - resizeBitmapWidth / 4);
                dst_resize.right = (int) (f7 + resizeBitmapWidth / 4);
                dst_resize.top = (int) (f8 - resizeBitmapHeight / 4);
                dst_resize.bottom = (int) (f8 + resizeBitmapHeight / 4);
                dst_top.left = (int) (f1 - flipVBitmapWidth / 4);
                dst_top.right = (int) (f1 + flipVBitmapWidth / 4);
                dst_top.top = (int) (f2 - flipVBitmapHeight / 4);
                dst_top.bottom = (int) (f2 + flipVBitmapHeight / 4);
                dst_flipV.left = (int) (f5 - topBitmapWidth / 4);
                dst_flipV.right = (int) (f5 + topBitmapWidth / 4);
                dst_flipV.top = (int) (f6 - topBitmapHeight / 4);
                dst_flipV.bottom = (int) (f6 + topBitmapHeight / 4);
                if (isInEdit) {
                    canvas.drawLine(f1, f2, f3, f4, localPaint);
                    canvas.drawLine(f3, f4, f7, f8, localPaint);
                    canvas.drawLine(f5, f6, f7, f8, localPaint);
                    canvas.drawLine(f5, f6, f1, f2, localPaint);
                    canvas.drawBitmap(deleteBitmap, null, dst_delete, null);
                    canvas.drawBitmap(resizeBitmap, null, dst_resize, null);
                    canvas.drawBitmap(flipVBitmap, null, dst_flipV, null);
                    canvas.drawBitmap(topBitmap, null, dst_top, null);
                }
                canvas.restore();
            }
        }
    }

    @Override
    public void setImageResource(int resId) {
        Log.v("StickerView", "res : " + resId);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        originalBitmap = BitmapFactory.decodeResource(getResources(), resId);
        setBitmap(bitmap, "null");
    }

    public void setBitmap(Bitmap bitmap, String type) {
        TYPE = type;
        matrix.reset();
        mBitmap = bitmap;
        //mBitmap = getResizedBitmap(bitmap,50,50);
        setDiagonalLength();
        initBitmaps();

        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        oringinWidth = w;
        float initScale = (MIN_SCALE + MAX_SCALE) / 4;
        matrix.postScale(initScale, initScale, w / 4.0f, h / 4.0f);
        matrix.postTranslate(mScreenwidth / 4.0f - w / 4.0f, (mScreenwidth) / 4.0f - h / 4.0f);
        invalidate();
    }
    public void setBitmap(Bitmap bitmap) {
        matrix.reset();
        mBitmap = bitmap;
        setDiagonalLength();
        initBitmaps();
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        oringinWidth = w;
        float initScale = (MIN_SCALE + MAX_SCALE) / 6;
        matrix.postScale(initScale, initScale, w / 2, h / 2);
        matrix.postTranslate(mScreenwidth / 2 - w / 2, (mScreenwidth) / 2 - h / 2);
        invalidate();
    }
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

    public void setBitmap(String from, Bitmap bitmap) {
        if(from.equals("blend")){
            mBitmap = bitmap;
            invalidate();
        }
    }
    public Bitmap getBitmap(){
        return mBitmap;
    }


    private void setDiagonalLength() {
        halfDiagonalLength = Math.hypot(mBitmap.getWidth(), mBitmap.getHeight()) / 2;
    }

    private void initBitmaps() {
        if (mBitmap.getWidth() >= mBitmap.getHeight()) {
            float minWidth = mScreenwidth / 12.0f;
            if (mBitmap.getWidth() < minWidth) {
                MIN_SCALE = 0.05f;
            } else {
                MIN_SCALE = 0.05f * minWidth / mBitmap.getWidth();
            }

            if (mBitmap.getWidth() > mScreenwidth) {
                MAX_SCALE = 2.0f;
            } else {
                MAX_SCALE = 2.0f * mScreenwidth / mBitmap.getWidth();
            }
        } else {
            float minHeight = mScreenwidth / 12.0f;
            if (mBitmap.getHeight() < minHeight) {
                MIN_SCALE = 0.05f;
            } else {
                MIN_SCALE = 0.05f * minHeight / mBitmap.getHeight();
            }
            if (mBitmap.getHeight() > mScreenwidth) {
                MAX_SCALE = 2.0f;
            } else {
                MAX_SCALE = 2.0f * mScreenwidth / mBitmap.getHeight();
            }
        }

        topBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_top_enable);
        deleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_delete);
        flipVBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_flip);
        resizeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_resize);

        deleteBitmapWidth = (int) (deleteBitmap.getWidth() * BITMAP_SCALE);
        deleteBitmapHeight = (int) (deleteBitmap.getHeight() * BITMAP_SCALE);

        resizeBitmapWidth = (int) (resizeBitmap.getWidth() * BITMAP_SCALE);
        resizeBitmapHeight = (int) (resizeBitmap.getHeight() * BITMAP_SCALE);

        flipVBitmapWidth = (int) (flipVBitmap.getWidth() * BITMAP_SCALE);
        flipVBitmapHeight = (int) (flipVBitmap.getHeight() * BITMAP_SCALE);

        topBitmapWidth = (int) (topBitmap.getWidth() * BITMAP_SCALE);
        topBitmapHeight = (int) (topBitmap.getHeight() * BITMAP_SCALE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInButton(event, dst_delete)) {
                    if (operationListener != null) {
                        operationListener.onDeleteClick();
                        /*if (CommonMethods.getInstance().getBitmapEventQueue().size() > 0)
                            CommonMethods.getInstance().getBitmapEventQueue().remove();*/
                    }
                } else if (isInResize(event)) {
                    isInResize = true;
                    lastRotateDegree = rotationToStartPoint(event);
                    midPointToStartPoint(event);
                    lastLength = diagonalLength(event);
                } else if (isInButton(event, dst_flipV)) {
                    PointF localPointF = new PointF();
                    midDiagonalPoint(localPointF);
                    matrix.postScale(-1.0F, 1.0F, localPointF.x, localPointF.y);
                    isHorizonMirror = !isHorizonMirror;
                    invalidate();
                } else if (isInButton(event, dst_top)) {
//                    if (TYPE.equals("int")) {
//                        addStickerFragment.addStickerView(addStickerFragment.INT_RESOURCE_ID_TO_COPY);
//                    } else {
//                       // addStickerFragment.addStickerView(addStickerFragment.RESOURCE_ID_TO_COPY);
//                    }
                    bringToFront();
                    if (operationListener != null) {
                        operationListener.onTop(this);
                    }
                } else if (isInBitmap(event)) {
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                } else {
                    handled = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (spacing(event) > pointerLimitDis) {
                    oldDis = spacing(event);
                    isPointerDown = true;
                    midPointToStartPoint(event);
                } else {
                    isPointerDown = false;
                }
                isInSide = true;
                isInResize = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPointerDown) {
                    float scale;
                    float disNew = spacing(event);
                    if (disNew == 0 || disNew < pointerLimitDis) {
                        scale = 1;
                    } else {
                        scale = disNew / oldDis;
                        scale = (scale - 1) * pointerZoomCoeff + 1;
                    }
                    float scaleTemp = (scale * Math.abs(dst_flipV.left - dst_resize.left)) / oringinWidth;
                    if (((scaleTemp <= MIN_SCALE)) && scale < 1 ||
                            (scaleTemp >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);
                    invalidate();
                } else if (isInResize) {

                    matrix.postRotate((rotationToStartPoint(event) - lastRotateDegree) * 2, mid.x, mid.y);
                    lastRotateDegree = rotationToStartPoint(event);

                    float scale = diagonalLength(event) / lastLength;

                    if (((diagonalLength(event) / halfDiagonalLength <= MIN_SCALE)) && scale < 1 ||
                            (diagonalLength(event) / halfDiagonalLength >= MAX_SCALE) && scale > 1) {
                        scale = 1;
                        if (!isInResize(event)) {
                            isInResize = false;
                        }
                    } else {
                        lastLength = diagonalLength(event);
                    }
                    matrix.postScale(scale, scale, mid.x, mid.y);

                    invalidate();
                } else if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    matrix.postTranslate(x - lastX, y - lastY);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isInResize = false;
                isInSide = false;
                isPointerDown = false;
                break;

        }
        if (handled && operationListener != null) {
            operationListener.onEdit(this);
        }
        return handled;
    }

    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];

        if (isHorizonMirror) {
            arrayOfFloat2[0] = f1 + LB;
            arrayOfFloat2[1] = f3 - LB;
            arrayOfFloat2[2] = f7 - LB;
            arrayOfFloat2[3] = f5 + LB;
            arrayOfFloat3[0] = f2 - LB;
            arrayOfFloat3[1] = f4 - LB;
            arrayOfFloat3[2] = f8 + LB;
            arrayOfFloat3[3] = f6 + LB;

        } else {
            arrayOfFloat2[0] = f1 - LB;
            arrayOfFloat2[1] = f3 + LB;
            arrayOfFloat2[2] = f7 + LB;
            arrayOfFloat2[3] = f5 - LB;
            arrayOfFloat3[0] = f2 - LB;
            arrayOfFloat3[1] = f4 - LB;
            arrayOfFloat3[2] = f8 + LB;
            arrayOfFloat3[3] = f6 + LB;
        }
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        double s = a1 * a2;
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;
    }

    private boolean isInButton(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private boolean isInResize(MotionEvent event) {
        int left = -20 + this.dst_resize.left;
        int top = -20 + this.dst_resize.top;
        int right = 20 + this.dst_resize.right;
        int bottom = 20 + this.dst_resize.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }

    private void midPointToStartPoint(MotionEvent event) {

        if (isHorizonMirror) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2] + LB;
            float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5] - LB;
            float f3 = f1 + event.getX(0);
            float f4 = f2 + event.getY(0);
            mid.set(f3 / 4, f4 / 4);
        } else if (mBitmap.getConfig() == ARGB_8888) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = f1 + event.getX(0);
            float f4 = f2 + event.getY(0);
            mid.set(f3 / 2, f4 / 2);
        } else {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2] - LB;
            float f2 = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5] - LB;
            float f3 = f1 + event.getX(0);
            float f4 = f2 + event.getY(0);
            mid.set(f3 / 2, f4 / 2);
        }
    }

    private void midDiagonalPoint(PointF paramPointF) {
        if (isHorizonMirror) {
            float[] arrayOfFloat = new float[9];
            this.matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] + LB;
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] - LB;
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] - LB;
            float f5 = f1 + f3;
            float f6 = f2 + f4;
            paramPointF.set(f5 / 2.0F, f6 / 2.0F);
        } else if (mBitmap.getConfig() == ARGB_8888) {
            float[] arrayOfFloat = new float[9];
            this.matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f5 = f1 + f3;
            float f6 = f2 + f4;
            paramPointF.set(f5 / 2.0F, f6 / 2.0F);
        } else {
            float[] arrayOfFloat = new float[9];
            this.matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2] - LB;
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5] - LB;
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2] + LB;
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5] - LB;
            float f5 = f1 + f3;
            float f6 = f2 + f4;
            paramPointF.set(f5 / 2.0F, f6 / 2.0F);
        }
    }

    private float rotationToStartPoint(MotionEvent event) {
        float[] arrayOfFloat = new float[9];
        matrix.getValues(arrayOfFloat);
        float x = 0.0f * arrayOfFloat[0] + 0.0f * arrayOfFloat[1] + arrayOfFloat[2];
        float y = 0.0f * arrayOfFloat[3] + 0.0f * arrayOfFloat[4] + arrayOfFloat[5];
        double arc = Math.atan2(event.getY(0) - y, event.getX(0) - x);
        return (float) Math.toDegrees(arc);
    }

    private float diagonalLength(MotionEvent event) {
        float diagonalLength = (float) Math.hypot(event.getX(0) - mid.x, event.getY(0) - mid.y);
        return diagonalLength;
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        } else {
            return 0;
        }
    }

    public interface OperationListener {
        void onDeleteClick();

        void onEdit(StickerView stickerView);
        void onTop(StickerView stickerView);
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }
}
