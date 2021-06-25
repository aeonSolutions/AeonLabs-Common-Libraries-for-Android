package aeonlabs.common.libraries.data;

public class LoadedRecord {

    private int MAX_NUMBER_OF_RECORDS=8;

    private String[] record;
    private boolean isSelected = false;

    public LoadedRecord(){
        record= new String[MAX_NUMBER_OF_RECORDS];
    }

    // record[0] is reserved for data primary key ID
    public String getRecord(int pos){
        return record[pos];
    }
    public void setRecord(int pos, String _record){
        if(pos>= record.length){
            String[] tmp = new String[pos];
            System.arraycopy(record, 0, tmp, 0, record.length);
            this.record=new String[pos];
            System.arraycopy(tmp, 0, this.record, 0, tmp.length);
        }
        this.record[pos] = _record;
    }

    public boolean isSelected() {
        return isSelected;
    }
}