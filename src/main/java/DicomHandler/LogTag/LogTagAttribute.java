package DicomHandler.LogTag;



import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.VR;

import java.util.ArrayList;
import java.util.List;

public class LogTagAttribute{


    private Sequence parentSeq  = null;
    private Attributes attributes = new Attributes();
    private List<String> createList = new ArrayList<>();
    private  List<String> exchangeList = new ArrayList<>();


    public static final int CREATE = 1;
    public static final int EXCHANE = 2;

    public LogTagAttribute(Attributes perent){
        this.attributes.setString(LogTag.HeaderName, VR.LO,"Anonymization Log");
        setParent(perent);
    }


    private void setParent(Attributes parent){
        parentSeq = parent.newSequence(0x2021001, 1);

    }


    public void setSortRule(String vaule){
        this.attributes.setString(LogTag.SortRule, VR.LO, "SortRule : "+ vaule);
    }

    public void update(){

        if(parentSeq.size()!=0)
            parentSeq.clear();




        this.attributes.setString(LogTag.createListHeaderName, VR.LO, "Create Tag List");
        this.attributes.setString(LogTag.createList, VR.LO,makeLogValue(this.createList));
        this.attributes.setString(LogTag.exchangeListHeaderName, VR.LO, "Exchange Tag List");
        this.attributes.setString(LogTag.exchangeList, VR.LO,makeLogValue(this.exchangeList));

        parentSeq.add(this.attributes);

    }


    private String makeLogValue( List<String> inputList){
     //   String output = "";
        StringBuilder stringBuilder = new StringBuilder();
        for(String tmp : inputList)
            stringBuilder.append(tmp + " ");
        return stringBuilder.toString();

    }

    public void addLog(int tag, int status, String vaule){

        addLog( tag, status, vaule,"");

    }

    public void addLog(int tag, int status, String vaule, String msg){
        if(status==CREATE) {
            this.createList.add(String.format("TAG : %08x, Created value : "+vaule, tag)+msg);
        }
        else if(status==EXCHANE)
            this.exchangeList.add(String.format("TAG : %08x, Exchanged value : "+vaule, tag)+msg);

    }



}
