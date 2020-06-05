package DicomHandler.Config;

import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.StandardElementDictionary;
import org.dcm4che3.data.VR;

public class Dictionary {

    public static final ElementDictionary DICTIONARY = StandardElementDictionary.getStandardElementDictionary();

    public static VR getVR(int tag){
        return DICTIONARY.vrOf(tag);
    }
}
