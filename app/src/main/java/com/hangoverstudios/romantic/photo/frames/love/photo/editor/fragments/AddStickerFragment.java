package com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.StickerPreViewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.views.StickerView;

import java.util.ArrayList;
import java.util.Objects;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity.greetingCardEditActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStickerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String type;
    private ArrayList<Integer> mResources;
    private RecyclerView stickerRecyclerView;
    private StickerPreViewAdapter stickerPreviewAdapter;
    private View rootView;
    public RelativeLayout mContentRootView;
    private CardView saveSticker;
    public StickerView mCurrentView;
    public ArrayList<View> mViews;
    public static AddStickerFragment addStickerFragment;
    private ImageView imageView;
    public int INT_RESOURCE_ID_TO_COPY;
    private Bitmap bitmap;

    public AddStickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStickerFragment newInstance(String param1, String param2) {
        AddStickerFragment fragment = new AddStickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_stricker, container, false);
        stickerRecyclerView = rootView.findViewById(R.id.stickers_preview_recycler);
        saveSticker = rootView.findViewById(R.id.sticker_done);
        mContentRootView = rootView.findViewById(R.id.sticker_content);
        imageView = rootView.findViewById(R.id.sticker_back);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addStickerFragment = this;
        mViews = new ArrayList<>();

        imageView.setImageDrawable(greetingCardEditActivity.getBitmapSelectedCard());

        mContentRootView.setDrawingCacheEnabled(true);
        mContentRootView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mContentRootView.layout(0, 0, mContentRootView.getMeasuredWidth(), mContentRootView.getMeasuredHeight());
        mContentRootView.buildDrawingCache(true);

        setStickersRecyclerView();
        mContentRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "mContentRootView is clicked", Toast.LENGTH_SHORT).show();
                if (mCurrentView != null)
                    mCurrentView.setInEdit(false);
            }
        });
        saveSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSticker();
                greetingCardEditActivity.removeAddStickerFragment(true);
            }
        });
    }

    public void setStickersRecyclerView() {
        type = "stickers";
        mResources = new ArrayList<>();
        for(int i = 1; i <= 38; i++)
        {
            mResources.add(getResources().getIdentifier("sticker_"+i, "drawable", Objects.requireNonNull(getActivity()).getPackageName()));
        }
       /* mResources.add(R.drawable.sticker_1);
        mResources.add(R.drawable.sticker_2);
        mResources.add(R.drawable.sticker_3);
        mResources.add(R.drawable.sticker_4);
        mResources.add(R.drawable.sticker_5);*/
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        stickerRecyclerView.setLayoutManager(layoutManager);
        stickerPreviewAdapter = new StickerPreViewAdapter(getActivity(), mResources, type);
        stickerRecyclerView.setAdapter(stickerPreviewAdapter);
    }

    public void addStickerView(String mResource) {
        final StickerView stickerView = new StickerView(getActivity());
        Bitmap myBitmap = BitmapFactory.decodeFile(mResource);
        stickerView.setBitmap(myBitmap, "path");
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                mContentRootView.removeView(stickerView);
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

            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mContentRootView.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    public void addStickerView(int mResource) {
        final StickerView stickerView = new StickerView(getActivity());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mResource);
        stickerView.setBitmap(bitmap, "int");
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                mContentRootView.removeView(stickerView);
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
        mContentRootView.addView(stickerView, lp);
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

    private void saveSticker() {
        if (mCurrentView != null)
            mCurrentView.setInEdit(false);
        bitmap = Bitmap.createBitmap(mContentRootView.getDrawingCache());
        greetingCardEditActivity.updateSelectedCard(greetingCardEditActivity.getResizedBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight()+50));
        //greetingCardEditActivity.updateSelectedCard(bitmap);
        for (int i = 0; i < mViews.size(); i++) {
            mContentRootView.removeView(mViews.get(i));
        }
        mViews = new ArrayList<>();
        mContentRootView.removeView(mCurrentView);
        // mSuitDrawView.save();
        // saveImage();
    }
/*   private void saveImage() {
        Bitmap b = Bitmap.createBitmap(mContentRootView.getDrawingCache());
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(b, b.getWidth(), b.getHeight(), true);
        categoriesActivity.updateSavedBitmap(resizedBitmap);
        CommonMethods.getInstance().activitiesAD(StickerEditActivity.this);

       *//* File myfolder = new File(Environment.getExternalStorageDirectory().toString() + "/" + getResources().getString(R.string.app_name));
        //getResources().getString(R.string.app_name)
        if (!myfolder.exists())
            if (!myfolder.mkdir()) {
                Toast.makeText(this, myfolder + " can't be created.", Toast.LENGTH_SHORT).show();
            }
        String fileName = getToken(5) + getDateTime() + ".png";
        File file = new File(myfolder, fileName);
        Toast.makeText(SetSuitActivity.this, myfolder.toString(),
                Toast.LENGTH_SHORT).show();
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        onBackPressed();
    }*/
}