 package aeonlabs.common.libraries.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBase extends Fragment implements FragmentBaseObserver{
    private ProgressDialog progressDialog;
    private final boolean PROGRESS_CANCELABLE = false;
    public static String TAG ="";

    public String getTAG(){
        return TAG;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
  }

    public void displayProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        progressDialog.setMessage("Progress...");
        progressDialog.setCancelable(PROGRESS_CANCELABLE);
        progressDialog.setCanceledOnTouchOutside(PROGRESS_CANCELABLE);

        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void notifyObserversFragment(String... params) {

    }
}
