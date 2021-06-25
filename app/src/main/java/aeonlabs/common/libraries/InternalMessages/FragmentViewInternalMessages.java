package aeonlabs.common.libraries.InternalMessages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aeonlabs.common.libraries.GeoLocation.GeoLocationUtils;
import aeonlabs.common.libraries.Helper.CustomExceptionHandler;
import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.activities.FragmentBase;
import aeonlabs.common.libraries.data.LoadedRecord;
import aeonlabs.common.libraries.R;

public class FragmentViewInternalMessages extends FragmentBase {
    private RecyclerView list;
    private ArrayList<LoadedRecord> recordArrayList;
    private InternalMessagesViewAdapter internalMessagesViewAdapter;
    private ImageView continue_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(getActivity()));
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof CustomExceptionHandler)) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //TODO
        getActivity().setTitle("Messages");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_common_view_internal_messages, container, false);
        list = v.findViewById(R.id.fragment_internal_msg_view_list);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        GeoLocationUtils.checkLocation(getActivity(),null);
        LoadInternalMessages();
    }

    @SuppressLint("StaticFieldLeak")
    private void LoadInternalMessages(){
        if (((ActivityBase) getActivity()).eventMsg.getNumberOfMessages()>0) {
            internalMessagesViewAdapter = new InternalMessagesViewAdapter(getActivity(),getInfoMsg());
            list.setAdapter(internalMessagesViewAdapter);
            list.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        }else {
            Toast.makeText(getActivity(), "No messages to display", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<LoadedRecord> getInfoMsg() {
        ArrayList<LoadedRecord> recordArrayList = new ArrayList<>();
        for (int i = 0; i < ((ActivityBase) getActivity()).eventMsg.getNumberOfMessages(); i++) {
            LoadedRecord record = new LoadedRecord();
            record.setRecord(1, ((ActivityBase) getActivity()).eventMsg.getMessage(i).shortDescription);
            recordArrayList.add(record);
        }
        return recordArrayList;
    }
}
