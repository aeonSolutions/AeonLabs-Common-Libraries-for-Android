package aeonlabs.common.libraries.Network.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fields")
public class EntityFields {

    @PrimaryKey(autoGenerate = true)
    private int CodField;

    @ColumnInfo(name = "CodQueue")
    private int CodQueue;

    @ColumnInfo(name = "Name")
    private String Name;

    @ColumnInfo(name = "Value")
    private String Value;

    @ColumnInfo(name = "RequestVar")
    private String RequestVar;

    @ColumnInfo(name = "Type")
    private String Type;

    public int getCodField() {
        return CodField;
    }
    public void setCodField(int _cod_field) {
        this.CodField = _cod_field;
    }

    public int getCodQueue() {
        return CodQueue;
    }
    public void setCodQueue(int _cod_queue) {
        this.CodQueue = _cod_queue;
    }

    public String getName() {
        return Name;
    }
    public void setName(String _name) {
        this.Name = _name;
    }

    public String getValue() {
        return Value;
    }
    public void setValue(String _value) {
        this.Value = _value;
    }

    public String getRequestVar() {
        return RequestVar;
    }
    public void setRequestVar(String _request_var) {
        this.RequestVar = _request_var;
    }

    public String getType() { return Type;    }
    public void setType(String _type) {
        this.Type = _type;
    }
}
