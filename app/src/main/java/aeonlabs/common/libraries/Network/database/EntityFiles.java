package aeonlabs.common.libraries.Network.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "files")
public class EntityFiles {

    @PrimaryKey(autoGenerate = true)
    private int CodFile;

    @ColumnInfo(name = "CodQueue")
    private int CodQueue;

    @ColumnInfo(name = "Filename")
    private String Filename;

    @ColumnInfo(name = "Url")
    private String Url;

    @ColumnInfo(name = "Title")
    private String Title;

    @ColumnInfo(name = "AppendCode")
    private Boolean AppendCode;

    public int getCodFile() {
        return CodFile;
    }
    public void setCodFile(int _cod_file) {
        this.CodFile = _cod_file;
    }

    public int getCodQueue() {
        return CodQueue;
    }
    public void setCodQueue(int _cod_queue) {
        this.CodQueue = _cod_queue;
    }

    public String getFilename() {
        return Filename;
    }
    public void setFilename(String _filename) {
        this.Filename = _filename;
    }

    public String getUrl() {
        return Url;
    }
    public void setUrl(String _url) {
        this.Url = _url;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String _title) {
        this.Title = _title;
    }

    public Boolean getAppendCode() { return AppendCode;    }
    public void setAppendCode(Boolean _append_code) {
        this.AppendCode = _append_code;
    }


}
