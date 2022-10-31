package com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingCardsPreviewAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingCardsTemplateAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GreetingCardTemplatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GreetingCardTemplatesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Integer> mResources;
    private String type;
    private RecyclerView valentineCardsTemplates;
    private GreetingCardsTemplateAdapter greetingCardsTemplateAdapter;
    private ImageView backToActivity;
    private View rootView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GreetingCardTemplatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GreetingCardTemplatesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GreetingCardTemplatesFragment newInstance(String param1, String param2) {
        GreetingCardTemplatesFragment fragment = new GreetingCardTemplatesFragment();
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
        rootView = inflater.inflate(R.layout.fragment_greeting_card_templates, container, false);
        valentineCardsTemplates = (RecyclerView) rootView.findViewById(R.id.card_templates_rv);
        backToActivity = (ImageView) rootView.findViewById(R.id.back_to_main_activity);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setValentineCardPreviewAdapter();
        backToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(), "back pressed", Toast.LENGTH_SHORT).show();
                if ((mainActivity.greetingCardTemplatesFragment != null) && (mainActivity.greetingCardTemplatesFragment.isVisible())) {

                    mainActivity.removeGreetingCardTemplatesFragment(false);
                }
            }
        });
    }

    private void setValentineCardPreviewAdapter()
    {
        type = "valentine card";
        mResources = new ArrayList<>();
        mResources.add(R.drawable.valentine_card_2);
        mResources.add(R.drawable.valentine_card_3);
        mResources.add(R.drawable.valentine_card_4);
//        mResources.add(R.drawable.valentine_card_5);
//        mResources.add(R.drawable.valentine_card_6);
//        mResources.add(R.drawable.valentine_card_7);
//        mResources.add(R.drawable.valentine_card_8);
//        mResources.add(R.drawable.valentine_card_9);
//        mResources.add(R.drawable.valentine_card_10);
//        mResources.add(R.drawable.valentine_card_11);
//        mResources.add(R.drawable.valentine_card_12);
//        mResources.add(R.drawable.valentine_card_13);
//        mResources.add(R.drawable.valentine_card_14);
//        mResources.add(R.drawable.valentine_card_15);
//        mResources.add(R.drawable.valentine_card_16);
//        mResources.add(R.drawable.valentine_card_17);
//        mResources.add(R.drawable.valentine_card_18);
//        mResources.add(R.drawable.valentine_card_19);
//        mResources.add(R.drawable.valentine_card_20);
//        mResources.add(R.drawable.valentine_card_21);
//        mResources.add(R.drawable.valentine_card_22);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        valentineCardsTemplates.setLayoutManager(gridLayoutManager);
        valentineCardsTemplates.setHasFixedSize(true);
        valentineCardsTemplates.setItemViewCacheSize(21);
        valentineCardsTemplates.setDrawingCacheEnabled(true);
        valentineCardsTemplates.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        greetingCardsTemplateAdapter = new GreetingCardsTemplateAdapter(getActivity(), mResources, type);
        valentineCardsTemplates.setAdapter(greetingCardsTemplateAdapter);
    }

}