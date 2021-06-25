package aeonlabs.common.libraries.PlugIns.PhotoGallery;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import aeonlabs.common.libraries.GeoLocation.GeoLocationUtils;
import aeonlabs.common.libraries.Libraries.Permissions.PermissionsUtils;
import aeonlabs.common.libraries.activities.FragmentBase;
import aeonlabs.common.libraries.data.SessionData;
import aeonlabs.common.libraries.logger.CrashReports;

import aeonlabs.common.libraries.Helper.CustomExceptionHandler;
import aeonlabs.common.libraries.BuildConfig;
import aeonlabs.common.libraries.R;


public class FragmentGallery extends FragmentBase {

    static final int REQUEST_PERMISSION_KEY = 1;

    LoadAlbum loadAlbumTask;
    GridView galleryGridView;
    ImageView reloadGallery;

    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    String Photopath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        getActivity().setTitle("Gallery title");
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getActivity()));
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
        }


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if (dp < 360) {
            dp = (dp - 17) / 2;
            float px = GalleryMethods.convertDpToPixel(dp, getActivity());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        reloadGallery.setVisibility(View.GONE);
        reloadGallery.setAlpha(0);
        reloadGallery.requestLayout();
        reloadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadGallery.setVisibility(View.GONE);
                reloadGallery.setAlpha(0);
                reloadGallery.requestLayout();
                loadAlbumTask = new LoadAlbum(getActivity().getContentResolver());
                loadAlbumTask.execute();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo_gallery_fragment, container, false);
        galleryGridView = v.findViewById(R.id.galleryGridView);
        reloadGallery = v.findViewById(R.id.reload_gallery);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1008 && resultCode == getActivity().RESULT_OK) {
            File imgFile = new  File(Photopath);
            if(imgFile.exists()) {
                try {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(imgFile);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                    //imgFile.delete();
                    try{
                        Thread.sleep(2000);
                    }catch(Exception e){

                    }
                } catch (Exception e) {
                    CrashReports crashReports = new CrashReports();
                    crashReports.SaveCrash(e, getActivity());
                    //TODO
                    Toast.makeText(getActivity(), "Error loading photo", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        GeoLocationUtils.checkLocation(getActivity(),null);

        reloadGallery.setVisibility(View.GONE);
        reloadGallery.setAlpha(0);
        reloadGallery.requestLayout();
        //TODO
        getActivity().setTitle("gallery title");
        Boolean Loaded=false;

        FragmentGallery test = (FragmentGallery) getActivity().getSupportFragmentManager().findFragmentById(R.id.frgament_photo_gallery_content_frame);
        ContentResolver res = null;
        try {
            res = getActivity().getContentResolver();
        } catch (Exception e) {

        }
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!GalleryMethods.hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        } else if (getActivity() != null && res != null) {
            if (test != null && test.isVisible() && isAdded()) {
                loadAlbumTask = new LoadAlbum(res);
                loadAlbumTask.execute();
                Loaded=true;
            }
        }
        if(!Loaded){
            reloadGallery.setVisibility(View.VISIBLE);
            reloadGallery.setAlpha(0);
            reloadGallery.requestLayout();
        }
    }

    class LoadAlbum extends AsyncTask<String, Void, String> {
        ContentResolver res;

        public LoadAlbum(ContentResolver _res) {
            this.res = _res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path = null;
            String album = null;
            String timestamp = null;
            String countPhoto = null;
            Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;


            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};
            Cursor cursorExternal = res.query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursorInternal = res.query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});
            Long tsLong = System.currentTimeMillis()/1000;
            //TODO
            albumList.add(GalleryMethods.mappingInbox("Take Photo", "",  tsLong.toString(), GalleryMethods.converToTime( tsLong.toString()), null));
            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                countPhoto = GalleryMethods.getCount(getActivity(), album);

                albumList.add(GalleryMethods.mappingInbox(album, path, timestamp, GalleryMethods.converToTime(timestamp), countPhoto));
            }
            cursor.close();
            Collections.sort(albumList, new MapComparator(GalleryMethods.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            AlbumAdapter adapter = new AlbumAdapter(getActivity(), albumList);
            galleryGridView.setAdapter(adapter);
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if(position==0) {
                        if(PermissionsUtils.checkPermissionForReadExtertalStorage(getActivity())){
                            Long tsLong = System.currentTimeMillis()/1000;
                            Photopath = Environment.getExternalStorageDirectory() + "/Camera/qc_"+tsLong.toString()+".jpg";

                            File file= new File (Photopath);
                            file.setWritable(true);
                            if (file.exists()) {
                                file.delete();
                            }
                            else {
                                file.getParentFile().mkdirs();
                            }
                            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri outputUri= FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID, file);
                            i.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }
                            else if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                                ClipData clip= ClipData.newUri(getActivity().getContentResolver(), "QC photo", outputUri);
                                i.setClipData(clip);
                                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            }
                            else {
                                List<ResolveInfo> resInfoList= getActivity().getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);

                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    getActivity().grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                }
                            }
                            try {
                                SessionData.PlugIns.PhotoGallery.setTookPhoto(true);
                                startActivityForResult(i, 1008);
                            }
                            catch (ActivityNotFoundException e) {
                                Toast.makeText(getActivity(), R.string.error_no_camera, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "No permission read storage", Toast.LENGTH_SHORT).show();
                        }
                    }else {

                        Fragment fragment = new FragmentAlbum();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", albumList.get(+position).get(GalleryMethods.KEY_ALBUM));
                        fragment.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frgament_photo_gallery_content_frame, fragment)
                                .addToBackStack(null)
                                .commit();
                        fragmentManager.executePendingTransactions();
                    }
                }
            });
        }
    }

    class AlbumAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;

        public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            AlbumViewHolder holder = null;
            if (convertView == null) {
                holder = new AlbumViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.photo_gallery_album_row, parent, false);

                holder.galleryImage = convertView.findViewById(R.id.galleryImage);
                holder.gallery_count = convertView.findViewById(R.id.gallery_count);
                holder.gallery_title = convertView.findViewById(R.id.gallery_title);

                convertView.setTag(holder);
            } else {
                holder = (AlbumViewHolder) convertView.getTag();
            }
            holder.galleryImage.setId(position);
            holder.gallery_count.setId(position);
            holder.gallery_title.setId(position);

            HashMap<String, String> song = new HashMap<String, String>();
            song = data.get(position);
            try {
                holder.gallery_title.setText(song.get(GalleryMethods.KEY_ALBUM));
                holder.gallery_count.setText(song.get(GalleryMethods.KEY_COUNT));

                if(position==0){
                    holder.galleryImage.setImageResource(R.drawable.camera);
                }else{
                    Glide.with(activity)
                            .load(new File(song.get(GalleryMethods.KEY_PATH))) // Uri of the picture
                            .into(holder.galleryImage);
                }

            } catch (Exception e) {
            }
            return convertView;
        }

        class AlbumViewHolder {
            ImageView galleryImage;
            TextView gallery_count, gallery_title;
        }

    }
}



