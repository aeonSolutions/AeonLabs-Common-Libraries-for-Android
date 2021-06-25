package aeonlabs.common.libraries.PlugIns;

import android.app.Activity;

import aeonlabs.common.libraries.PlugIns.Authentication.AuthenticationSessionData;
import aeonlabs.common.libraries.PlugIns.Location.LocationDetails;
import aeonlabs.common.libraries.PlugIns.Location.LocationDetailsSessionData;
import aeonlabs.common.libraries.PlugIns.PhotoGallery.PhotoGallerySessionData;

public class PlugInsSessionData {
    public PlugInsSessionData(Activity _act){
        this.activity= _act;
        this.PhotoGallery= new PhotoGallerySessionData(this.activity);
    }

    private Activity activity;

    /****************** Photo Gallery ****************/
    static  public PhotoGallerySessionData PhotoGallery= new PhotoGallerySessionData(null);


    /******************* Auhtentication *******************/
    static public AuthenticationSessionData Authentication = new AuthenticationSessionData();

    /******************* Location Details *******************/
    static public LocationDetailsSessionData locationDetails= new LocationDetailsSessionData();

}
