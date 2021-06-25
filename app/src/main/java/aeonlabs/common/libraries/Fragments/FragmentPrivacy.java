package aeonlabs.common.libraries.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.net.URL;

import aeonlabs.common.libraries.Helper.CustomExceptionHandler;

import aeonlabs.common.libraries.UserInterface.UI;
import aeonlabs.common.libraries.activities.FragmentBase;
import aeonlabs.common.libraries.logger.CrashReports;

import aeonlabs.common.libraries.R;
import aeonlabs.common.libraries.data.SessionData;

public class FragmentPrivacy extends FragmentBase {
    WebView web;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG="FragmentPrivacy";
        //TODO
        getActivity().setTitle("Privacy");
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getActivity()));
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
     try{
         URL url = new URL(SessionData.Network.AeonLabsPrivacyPoliciesURL);
         UI.showSimpleProgressDialog(getActivity(), getActivity().getResources().getString(R.string.commServer_connect_title_msg), getActivity().getResources().getString(R.string.commServer_connect_msg),false);
         web.setWebViewClient(new WebViewClient() {
             @Override
             public void onPageStarted(WebView view, String url, Bitmap favicon)
             {
                 // TODO show you progress image
                 super.onPageStarted(view, url, favicon);
                 UI.showSimpleProgressDialog(getActivity(), getActivity().getResources().getString(R.string.commServer_connect_title_msg),getActivity().getResources().getString(R.string.commServer_connect_msg),false);
             }

             @Override
             public void onPageFinished(WebView view, String url)
             {
                 // TODO hide your progress image
                 super.onPageFinished(view, url);
                 UI.removeSimpleProgressDialog(getActivity());
             }
         });
         web.loadUrl(url.toString());
         UI.removeSimpleProgressDialog(getActivity());
     } catch (Exception e) {
         CrashReports crashReports = new CrashReports();
         crashReports.SaveCrash(e, getActivity());

         Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_ivalid_url), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.policies_fragment, container, false);
        web = v.findViewById(R.id.policies_web_render);
        return v;
    }
}
