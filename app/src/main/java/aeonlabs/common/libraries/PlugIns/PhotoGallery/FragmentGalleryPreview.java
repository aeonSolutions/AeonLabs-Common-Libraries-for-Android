package aeonlabs.common.libraries.PlugIns.PhotoGallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;

import aeonlabs.common.libraries.activities.FragmentBase;

import aeonlabs.common.libraries.Helper.CustomExceptionHandler;
import aeonlabs.common.libraries.R;

public class FragmentGalleryPreview extends FragmentBase {
    ImageView GalleryPreviewImg;
    String path="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getActivity()));
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
        }


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            path = bundle.getString("path", "");
        }
        Glide.with(getActivity())
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.photo_gallery_preview_fragment, container, false);
        GalleryPreviewImg = v.findViewById(R.id.GalleryPreviewImg);

        return v;
    }
}
