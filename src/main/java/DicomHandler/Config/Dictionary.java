package DicomHandler.Config;

import DicomHandler.Util.StringUtil;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.StandardElementDictionary;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {

    public static final ElementDictionary DICTIONARY = StandardElementDictionary.getStandardElementDictionary();

    public static VR getVR(int tag){
        return DICTIONARY.vrOf(tag);


    }
    public static String getDescription(int tag) {
        return DICTIONARY.keywordOf(tag);
    }

    public static List<String> getTagList() {
        List<String> output = new ArrayList<>();
        Tag TagList = new Tag();
        Field[] tagFileds = TagList.getClass().getFields();
        for(Field tmpField : tagFileds)
            output.add(StringUtil.insertSpaceFrontBigText(tmpField.getName()));
        return output;



    }



}
