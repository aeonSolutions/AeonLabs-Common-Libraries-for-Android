package aeonlabs.common.libraries.Network.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "queue")
public class EntityQueue {

    @PrimaryKey(autoGenerate = true)
    private int CodQueue;

    @ColumnInfo(name = "Title")
    private String Title;

    @ColumnInfo(name = "Description")
    private String Description;

    @ColumnInfo(name = "MsgSuccess")
    private String MsgSuccess;// response for see response from server

    @ColumnInfo(name = "MsgError")
    private String MsgError;  // response for see response from server, debug for see raw data response from server

    @ColumnInfo(name = "MsgType")
    private String MsgType;  // possible: toast or alertbox

    @ColumnInfo(name = "Url")
    private String Url;

    public int getCodQueue() {
        return CodQueue;
    }
    public void setCodQueue(int _cod_queue) {
        this.CodQueue = _cod_queue;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String _title) {
        this.Title = _title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String _description) {
        this.Description = _description;
    }

    public String getMsgSuccess() {
        return MsgSuccess;
    }
    public void setMsgSuccess(String _msg_success) {
        this.MsgSuccess = _msg_success;
    }

    public String getMsgError() {
        return MsgError;
    }
    public void setMsgError(String _msg_error) {
        this.MsgError = _msg_error;
    }

    public String getMsgType() {
        return MsgType;
    }
    public void setMsgType(String _msg_type) {
        this.MsgType = _msg_type;
    }

    public String getUrl() {
        return Url;
    }
    public void setUrl(String _url) {
        this.Url = _url;
    }

}
