package com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.PhotoFilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleFrameEditActivity.sfeObj;

public class FilterItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int STRING = 3;
    private final Context mContext;
    private final List<Object> mRecyclerViewItems;
    int FrameType = 0;
    int code;

//    FilterItemAdapter imgFilter = new FilterItemAdapter();

    public FilterItemAdapter(Context mContext, List<Object> mRecyclerViewItems, int code) {
        this.mContext = mContext;
        this.mRecyclerViewItems = mRecyclerViewItems;
        this.code = code;
    }

    public static File savebitmap(Bitmap bmp, String fileName) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + fileName + ".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mRecyclerViewItems.get(position);
        if (recyclerViewItem instanceof String) {
            return STRING;
        }

        return STRING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {


            case STRING:
            default:

                View menuItemLayoutView2 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.single_filter_item, parent, false);


                return new StringViewHolder(menuItemLayoutView2);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {

            case STRING:

            default:

                final StringViewHolder stringViewHolder = (StringViewHolder) holder;
                if (code == 301) {
                    
                    final String img1 = mRecyclerViewItems.get(position).toString();
                    Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);
                    Glide.with(mContext)
                            .load(getImage(mRecyclerViewItems.get(position).toString()))
//                            .placeholder(R.drawable.loadimg)
                            .into(stringViewHolder.img);
                    stringViewHolder.img.startAnimation(a);
               
                }

                stringViewHolder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       
                        if (code == 301) {

                            PhotoFilter photoFilter = new PhotoFilter();
                            if (position == 0) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(sfeObj.filterBitMap);
                                }
                            } else if (position == 1) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter1(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 2) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter2(mContext, sfeObj.filterBitMap));
                                }

                            } else if (position == 3) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter3(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 4) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter4(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 5) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter5(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 6) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter6(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 7) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter7(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 8) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter8(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 9) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter9(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 10) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter10(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 11) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter11(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 12) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter12(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 13) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter13(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 14) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter14(mContext, sfeObj.filterBitMap));
                                }
                            } else if (position == 15) {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter15(mContext, sfeObj.filterBitMap));
                                }
                            } else {
                                if (sfeObj != null && sfeObj.global != null && sfeObj.filterBitMap != null && mContext != null) {
                                    sfeObj.global.setBitmap(photoFilter.filter16(mContext, sfeObj.filterBitMap));
                                }
                            }
//                            Drawable drawable = mContext.getResources().getDrawable(mContext.getResources()
//                                    .getIdentifier(EditScreen.singleFramesShortList.get(position).toString(), "drawable", mContext.getPackageName()));

//                            imgFilter.applyBoostEffect(sfeObj.filterBitMap, 2, 30);
//                            sfeObj.global.setBitmap( imgFilter.applyBoostEffect(sfeObj.filterBitMap, 3, 67));
                        }
//                        sfeObj.edit_recycle.setVisibility(View.GONE);
                    }
                });

                break;


        }

    }

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }

    public class StringViewHolder extends RecyclerView.ViewHolder {
        private TextView myString;
        private ImageView img;

        StringViewHolder(View view) {
            super(view);
//            myString = view.findViewById(R.id.string_text);
            img = view.findViewById(R.id.frame_img);

        }
    }


}
