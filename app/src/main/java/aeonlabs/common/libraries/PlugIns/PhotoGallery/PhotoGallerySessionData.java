package aeonlabs.common.libraries.PlugIns.PhotoGallery;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PhotoGallerySessionData {
    public PhotoGallerySessionData(Activity _act){
        this.activity=_act;
    }

    private Activity activity;

    static public Boolean tookPhoto=false;
    static public Set<String> gallerySelection= new HashSet<>();

    public void setTookPhoto(Boolean _state){
        tookPhoto=_state;
        SharedPreferences settingsfile= this.activity.getSharedPreferences("session",MODE_PRIVATE);
        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putBoolean("tookPhoto", _state);
        myeditor.apply();
        myeditor.commit();
    }

    public Boolean getTookPhoto(){
        SharedPreferences settings= this.activity.getSharedPreferences("session", MODE_PRIVATE);
        tookPhoto= settings.getBoolean("tookPhoto", false);
        return tookPhoto;
    }

    public void setPhotos2Upload(String _state){
        SharedPreferences settings= this.activity.getSharedPreferences("session",MODE_PRIVATE);
        Set<String > set = settings.getStringSet("photos2upload",new HashSet<>());
        set.add(_state);
        SharedPreferences.Editor myeditor = settings.edit();
        myeditor.putStringSet("photos2upload",set);
        myeditor.apply();
        myeditor.commit();
    }

    public  Set<String > getPhotos2Upload(){
        SharedPreferences settings= this.activity.getSharedPreferences("session", MODE_PRIVATE);
        Set<String >set = settings.getStringSet("photos2upload",new HashSet<>());
        return set;
    }

    public  void clearPhotos2Upload(String str){
        SharedPreferences settings= this.activity.getSharedPreferences("session",MODE_PRIVATE);
        SharedPreferences.Editor myeditor = settings.edit();
        if(str.equals("")) {
            myeditor.putStringSet("photos2upload", new HashSet<>());
        }else {
            Set<String> set = settings.getStringSet("photos2upload", new HashSet<>());
            set.remove(str);
            myeditor.putStringSet("photos2upload", set);
        }
        myeditor.apply();
        myeditor.commit();
    }
}
