package aeonlabs.common.libraries.Network.DataRequest;

import aeonlabs.common.libraries.Network.database.EntityQueue;

public class DataQueue {
    private EntityQueue queue= new EntityQueue();

    public DataQueue(){

    }

    public EntityQueue getQueue(){
        return queue;
    }

    public void setMessages(String msgType, String msgSuccess, String msgError){
        queue.setMsgType(msgType);
        queue.setMsgSuccess(msgSuccess);
        queue.setMsgError(msgError);
    }

    public void setMsgType(String msgType){
        queue.setMsgType(msgType);
    }
    public void setMsgSuccess(String msgSuccess){
        queue.setMsgSuccess(msgSuccess);
    }
    public void setMsgError(String msgError){
        queue.setMsgError(msgError);
    }
    public void setUrl(String url){
        queue.setUrl(url);
    }
    public void setTitle(String title){
        queue.setTitle(title);
    }
    public void setDescription(String description){
        queue.setDescription(description);
    }
}
