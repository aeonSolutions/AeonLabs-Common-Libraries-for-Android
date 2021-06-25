package aeonlabs.common.libraries.UserInterface;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.ActionMenuView;
import android.widget.ProgressBar;

import static android.content.Context.MODE_PRIVATE;

public class UIsessionData {
    public UIsessionData(Activity _act){
        this.activity= _act;
    }
    private Activity activity;

    static public ProgressBar progressBar;

    static public Boolean LoadNewFragment=true;

    static public boolean userIsInteracting=false;

    static public int CurrentNavItem=-1;
    public void setCurrentNavItem(int _state){
        CurrentNavItem=_state;
        SharedPreferences settingsfile= this.activity.getSharedPreferences("session",MODE_PRIVATE);
        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putInt("CurrentNavItem", _state);
        myeditor.apply();
        myeditor.commit();
    }

    public int getCurrentNavItem(){
        SharedPreferences settings= this.activity.getSharedPreferences("session", MODE_PRIVATE);
        CurrentNavItem= settings.getInt("CurrentNavItem", -1);
        return CurrentNavItem;
    }


}
