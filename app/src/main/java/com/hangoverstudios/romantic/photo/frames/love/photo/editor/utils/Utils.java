package com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils;

import android.content.Context;
import android.view.SurfaceControl;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.adapters.GreetingCardsTemplateAdapter;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.AddStickerFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.ElementsFragment;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments.GreetingCardTemplatesFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream.toString();
    }


    public static GreetingCardTemplatesFragment getGreetingCardTemplatesFragment(FragmentActivity activity) {
        return (GreetingCardTemplatesFragment) activity.getSupportFragmentManager().findFragmentByTag("myFragmentTag_1");
    }

    public static GreetingCardTemplatesFragment addGreetingCardTemplatesFragment(FragmentActivity activity, int gallery_fragment_container, boolean showInter, View view) {
        FragmentManager fm = activity.getSupportFragmentManager();
        GreetingCardTemplatesFragment greetingCardTemplatesFragment = (GreetingCardTemplatesFragment) fm.findFragmentByTag("myFragmentTag_1");
        if (greetingCardTemplatesFragment == null) {
            greetingCardTemplatesFragment = new GreetingCardTemplatesFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(gallery_fragment_container, greetingCardTemplatesFragment, "myFragmentTag_1");
            ft.commitAllowingStateLoss();
            //galleryFragment.setGalleryListener(createGalleryListener(activity, galleryFragment, showInter, view));
            activity.findViewById(gallery_fragment_container).bringToFront();
            return greetingCardTemplatesFragment;
        }
        activity.getSupportFragmentManager().beginTransaction().show(greetingCardTemplatesFragment).commitAllowingStateLoss();
        return greetingCardTemplatesFragment;
    }

    public static ElementsFragment getElementsFragment(FragmentActivity activity) {
        return (ElementsFragment) activity.getSupportFragmentManager().findFragmentByTag("myFragmentTag_2");
    }

    public static ElementsFragment addElementsFragment(FragmentActivity activity, int gallery_fragment_container, boolean showInter, View view) {
        FragmentManager fm = activity.getSupportFragmentManager();
        ElementsFragment elementsFragment = (ElementsFragment) fm.findFragmentByTag("myFragmentTag_2");
        if (elementsFragment == null) {
            elementsFragment = new ElementsFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(gallery_fragment_container, elementsFragment, "myFragmentTag_2");
            ft.commitAllowingStateLoss();
            //galleryFragment.setGalleryListener(createGalleryListener(activity, galleryFragment, showInter, view));
            activity.findViewById(gallery_fragment_container).bringToFront();
            return elementsFragment;
        }
        activity.getSupportFragmentManager().beginTransaction().show(elementsFragment).commitAllowingStateLoss();
        return elementsFragment;
    }

    public static AddStickerFragment getAddStickerFragment(FragmentActivity activity) {
        return (AddStickerFragment) activity.getSupportFragmentManager().findFragmentByTag("myFragmentTag_3");
    }

    public static AddStickerFragment addAddStickerFragment(FragmentActivity activity, int gallery_fragment_container, boolean showInter, View view) {
        FragmentManager fm = activity.getSupportFragmentManager();
        AddStickerFragment addStickerFragment = (AddStickerFragment) fm.findFragmentByTag("myFragmentTag_3");
        if (addStickerFragment == null) {
            addStickerFragment = new AddStickerFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(gallery_fragment_container, addStickerFragment, "myFragmentTag_3");
            ft.commitAllowingStateLoss();
            //galleryFragment.setGalleryListener(createGalleryListener(activity, galleryFragment, showInter, view));
            activity.findViewById(gallery_fragment_container).bringToFront();
            return addStickerFragment;
        }
        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).show(addStickerFragment).commitAllowingStateLoss();
        return addStickerFragment;
    }
}
