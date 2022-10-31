package com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.SingleFrameEditActivity;
import com.hangoverstudios.romantic.photo.frames.love.photo.editor.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.GreetingCardEditActivity.greetingCardEditActivity;
import static com.hangoverstudios.romantic.photo.frames.love.photo.editor.activities.MainActivity.mainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ElementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView hiddenDown;
    private View rootView;
    private LinearLayout cameraImage, galleryImage, stickerImage;
    public static final int PICK_IMAGE = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri imageUri;
    private Bitmap bitmap;
    public static Bitmap selectedImageBitmap;
    private Animation bottomDown;
    private RelativeLayout rootRel;

    public ElementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElementsFragment newInstance(String param1, String param2) {
        ElementsFragment fragment = new ElementsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_elements, container, false);
        hiddenDown =  rootView.findViewById(R.id.hidden_down);
        cameraImage =  rootView.findViewById(R.id.camera_btn_linear);
        galleryImage = rootView.findViewById(R.id.gallery_btn_linear);
        stickerImage = rootView.findViewById(R.id.stickers_btn_linear);
        rootRel = rootView.findViewById(R.id.elements_root_frame);
        return rootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hiddenDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greetingCardEditActivity.removeElementsFragment(true);
               /* greetingCardEditActivity.selectedCard.setClickable(true);
               // Toast.makeText(getContext(), "back pressed", Toast.LENGTH_SHORT).show();
                bottomDown = AnimationUtils.loadAnimation(getContext(),
                        R.anim.bottom_down);
                greetingCardEditActivity.hiddenPanel.startAnimation(bottomDown);
                greetingCardEditActivity.hiddenPanel.setVisibility(View.GONE);
               // getActivity().findViewById(R.id.text_sticker_1).setClickable(true);
                getActivity().findViewById(R.id.new_element).setClickable(true);*/
            }
        });
        galleryImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mainActivity.checkStoragePermission()) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE);
                } else {
                    mainActivity.requestStoragePermission();
                }
            }
        });
        stickerImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               // rootRel.setVisibility(View.GONE);
               // rootView.setVisibility(View.GONE);
                greetingCardEditActivity.setStickerFragment();

               // greetingCardEditActivity.stickerRecyclerView.setVisibility(View.VISIBLE);
                //greetingCardEditActivity.setStickersRecyclerView();
            }
        });
        cameraImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mainActivity.checkCameraPermission() && mainActivity.checkStoragePermission()) {
                    launchCamera();
                } else {
                    if (!mainActivity.checkCameraPermission()) {
                        mainActivity.requestCameraPermission();
                    } else if (!mainActivity.checkStoragePermission()) {
                        mainActivity.requestStoragePermission();
                    }
                }
            }
        });
    }

    public void launchCamera() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            File path = new File(Environment.getExternalStorageDirectory()
                    + "/root/Android/data/"
                    + Objects.requireNonNull(getContext()).getPackageName()
                    + "/temp data");
            if (!path.exists()) path.mkdirs();
            File image = new File(path, "valentine_image_capture_element.jpg");
            Uri imageUri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), getActivity().getPackageName() + ".provider", image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                //TODO: action
                imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getContext()).getContentResolver(), imageUri);
                    selectedImageBitmap = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap bitmapFromMedia = null;
                File path = new File(Environment.getExternalStorageDirectory()
                        + "/root/Android/data/"
                        + Objects.requireNonNull(getContext()).getPackageName()
                        + "/temp data");
                if (!path.exists()) path.mkdirs();
                File imageFile = new File(path, "image_capture.jpg");

                Uri stringUri = Uri.fromFile(imageFile);

                try {
                    bitmapFromMedia = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), stringUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmapFromMedia = Bitmap.createBitmap(bitmapFromMedia, 0, 0, bitmapFromMedia.getWidth(), bitmapFromMedia.getHeight(), matrix, true);
                selectedImageBitmap = bitmapFromMedia;
            }

            if (selectedImageBitmap != null) {
                //startActivity(new Intent(, ImageCropActivity.class));
               // greetingCardEditActivity.textSticker1.setClickable(true);
                greetingCardEditActivity.setSelectedPhoto(selectedImageBitmap);
                greetingCardEditActivity.setSelectedImageBitmap(selectedImageBitmap);
                greetingCardEditActivity.removeElementsFragment(false);
            }

        }
    }
    public void onBackPressed()
    {
        getActivity().onBackPressed();
        //Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }
}