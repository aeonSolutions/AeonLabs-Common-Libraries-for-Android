package aeonlabs.common.libraries.Helper;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;

import aeonlabs.common.libraries.activities.FragmentBase;


public class FragmentManagement {
    private HashMap<String, FragmentBase> fragments=new HashMap<String, FragmentBase>();
    private final Activity activity;
    private final int containerViewId;
    public String currentLoadedFragmentTAG="";

    public static final int NOTIFY_ALL_FRRAGMENTS=-1;

    public FragmentManagement(Activity _activity, int _containerViewId) {
        this.fragments=new HashMap<>();
        this.activity=_activity;
        this.containerViewId=_containerViewId;
    }
    public Boolean AddFragment(FragmentBase fragment, Bundle bundle){
        if(bundle != null)
            fragment.setArguments(bundle);

        this.fragments.put(fragment.getTAG(), fragment);
        return true;
    }
    public Boolean RemoveFragment(String tag){
        this.fragments.remove(tag);
        return true;
    }

    public void AddAndStartFragment(FragmentBase fragment, Bundle bundle){
        AddFragment(fragment, bundle);
        setCurrentFragment(fragment.getTAG());
    }

    public void removeCurrentLoadedFragment(){
        this.fragments.remove(currentLoadedFragmentTAG);
    }

    public int getFragmentsCount() {
        return fragments.size();
    }

    public FragmentBase getFragment(String TAG){
        return this.fragments.get(TAG);
    }

    public void setCurrentFragment(String tag){
        currentLoadedFragmentTAG=tag;
        FragmentManager fragmentManager =  ((AppCompatActivity)activity).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(this.containerViewId, fragments.get(tag))
                .addToBackStack(null);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }
}