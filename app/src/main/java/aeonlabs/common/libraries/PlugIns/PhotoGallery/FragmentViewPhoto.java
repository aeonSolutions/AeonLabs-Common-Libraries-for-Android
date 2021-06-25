package aeonlabs.common.libraries.PlugIns.PhotoGallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;

import aeonlabs.common.libraries.activities.FragmentBase;
import aeonlabs.common.libraries.logger.CrashReports;

import aeonlabs.common.libraries.Helper.CustomExceptionHandler;
import aeonlabs.common.libraries.R;


public class FragmentViewPhoto extends FragmentBase {
    private PhotoView photoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.fragment_about_title));
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getActivity()));
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //TODO
        getActivity().setTitle("View photo");

        Bundle bundle = this.getArguments();
        String photoAddr="";
        if (bundle != null) {
            photoAddr = bundle.getString("photoAddr","");
        }

        if(photoAddr.equals("")){
            photoView.setImageResource(R.drawable.no_camera);
        }else {
            try {
               Glide.with(getActivity())
                       .asBitmap()
                       .placeholder(R.drawable.loading)
                       .error(R.drawable.loading_error_image)
                       .load(photoAddr)
                       .override(1080, 600)
                       .skipMemoryCache(true)  //No memory cache
                       .diskCacheStrategy(DiskCacheStrategy.NONE)   //No disk cache
                       .into(photoView);
            } catch(Exception e){
                CrashReports crashReports = new CrashReports();
                crashReports.SaveCrash(e, getActivity());
                photoView.setImageResource(R.drawable.no_camera);
                //TODO
                Toast.makeText(getActivity(), "photo not found", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.photo_gallery_view_photo, container, false);
        photoView = v.findViewById(R.id.fragment_view_photo_photo);

        return v;
    }
}
