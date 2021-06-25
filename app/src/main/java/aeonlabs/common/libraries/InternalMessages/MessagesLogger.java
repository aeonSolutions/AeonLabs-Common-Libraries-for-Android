package aeonlabs.common.libraries.InternalMessages;

import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import aeonlabs.common.libraries.Fonts.TextViewTimesNewRoman;
import aeonlabs.common.libraries.activities.ActivityBase;
import aeonlabs.common.libraries.R;


public class MessagesLogger {
    private ActivityBase activity;
    private TextViewTimesNewRoman UIcounter;
    private ImageView UImsgBtn;
    private int ignoreMessagesTimeInterval=120;

    public static int MESSAGE_TYPE_INFO=1;
    public static int MESSAGE_TYPE_WARNING=2;
    public static int MESSAGE_TYPE_CRITICAL=3;

    public static int MESSAGE_DISPLAY_AS_TOAST=40;
    public static int MESSAGE_DISPLAY_AS_ALERT=41;
    public static int MESSAGE_DISPLAY_NONE=42;

    public static int MESSAGE_STATUS_READED=80;
    public static int MESSAGE_STATUS_VIEWED=81;
    public static int MESSAGE_STATUS_NOT_VIEWED=82;

    public ArrayList<MessagesStack> messagesStack;

    public class MessagesStack{
        public int type;
        public String shortDescription;
        public String longDescription;
        public String file;
        public String codeLineNumber;
        public Time time;
        public int status;}

    public MessagesLogger(ActivityBase _activity){
        this.activity=_activity;
        messagesStack=  new ArrayList<>();
    }

    public MessagesStack getMessage(int pos){
        return messagesStack.get(pos);
    }

    public int getNumberOfMessages(){
            return messagesStack.size();
    }

    public void deleteMessage(int pos){
        messagesStack.remove(pos);
    }
    public void setElements(TextViewTimesNewRoman counter, ImageView btn){
        this.UIcounter=counter;
        this.UImsgBtn=btn;

        this.UImsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load connect fragment
                ((ActivityBase) activity).fragmentManagement.removeCurrentLoadedFragment();
                ((ActivityBase) activity).fragmentManagement.AddAndStartFragment(new FragmentViewInternalMessages(), null);
            }
        });
    }

    public void loadUI(){
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                if(UIcounter != null && UImsgBtn!=null){
                    UIcounter.setText(Integer.toString(getNumberOfMessages()));
                    if(getNumberOfMessages()>0) {
                        UImsgBtn.setImageResource(getWarningLevel());
                        UIcounter.setVisibility(View.VISIBLE);
                        UImsgBtn.setVisibility(View.VISIBLE);
                    }else{
                        UIcounter.setVisibility(View.INVISIBLE);
                        UImsgBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }});
    }

    private int getWarningLevel(){
        int icon = R.drawable.error_messages_yellow;
        for (int i=0; i<messagesStack.size();i++){
            if(messagesStack.get(i).type==MESSAGE_TYPE_CRITICAL) {
                icon = R.drawable.error_messages;
                break;
            }
        }
    return icon;
    }

    private Boolean checkForExistingMsgTimeInterval(String shortDescription){
        Time now= new Time();
        now.setToNow();
        for (int i=0; i<messagesStack.size();i++){
            if(messagesStack.get(i).shortDescription.equals(shortDescription) && ((ignoreMessagesTimeInterval*1000)>(now.toMillis(false)-messagesStack.get(i).time.toMillis(false)))){
                return true;
            }
        }
        return false;
    }
    public void raiseNewMessage(int display, int type, String shortDescription, String longDescription){
        if(checkForExistingMsgTimeInterval(shortDescription))
            return;

        MessagesStack messagesStackItem= new MessagesStack();
        messagesStackItem.shortDescription=shortDescription;
        messagesStackItem.longDescription=longDescription;
        messagesStackItem.type=type;
        Time now = new Time();
        now.setToNow();
        messagesStackItem.time= now;

        if(display== MESSAGE_DISPLAY_NONE){
            messagesStackItem.status=MESSAGE_STATUS_NOT_VIEWED;
        }else{
            messagesStackItem.status=MESSAGE_STATUS_VIEWED;
        }

        messagesStack.add(messagesStackItem);

        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                if(display== MESSAGE_DISPLAY_AS_TOAST)
                    Toast.makeText(activity,shortDescription, Toast.LENGTH_SHORT).show();
            }});
        loadUI();
    }
}
