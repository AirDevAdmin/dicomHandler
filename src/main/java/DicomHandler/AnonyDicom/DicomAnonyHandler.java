package DicomHandler.AnonyDicom;

import DicomHandler.DicomHandler;
import DicomHandler.LogTag.LogTagAttribute;
import DicomHandler.CleanDicom.DicomCleanHandler;
import DicomHandler.TagCategory.AnonyTag;
import DicomHandler.TagCategory.DicomReconTag;
import org.dcm4che3.data.Tag;

import java.util.List;
import java.util.Map;


public class DicomAnonyHandler extends DicomCleanHandler {


    private static final String ANONY_WORD = "Anonymized";

    private String anonyPatientID = ANONY_WORD;

    public DicomAnonyHandler(String input) {
        this.anonyPatientID = input;
    }

    public static final int[] ANONYMIZATION_LIST =
            {
                    Tag.PatientID,
                    //    Tag.PatientBirthDate,
                    // specal aonoy
                    Tag.PatientName,
                    //   Tag.PatientSex,
                    Tag.PatientAge,
                    Tag.InstitutionName,
                    Tag.OtherPatientIDs,
                    Tag.OtherPatientNames

            };

    public boolean buildAnonymiAtri(Map<Integer, String> arrTagNVaule, byte[] pixelData) {
        buildAttrbutes(arrTagNVaule,pixelData);
        anonymiAttribute();
        logAttrbutes.update();

        return true;
    }

    private boolean anonymiAttribute() {


        List<DicomReconTag> anonyTagList = DicomReconTag.getTagList(ANONYMIZATION_LIST);


        for (DicomReconTag tag : anonyTagList) {
            setAttribute(tag, ANONY_WORD);
        }

        setAttribute(Tag.PatientID, this.anonyPatientID);
        anonymiPatientBrithDate();


        return true;

    }

    private void anonymiPatientBrithDate() {
        String sData = attributes.getString(Tag.PatientBirthDate);
        String sAnomiDate = "";
        if (sData != null && sData.length() > 3) {
            sAnomiDate = sData.substring(0, 4) + "0101";
        } else {
            sAnomiDate = "19000101";
        }
        logAttrbutes.addLog(Tag.PatientBirthDate, LogTagAttribute.EXCHANE, sAnomiDate);
        setAttribute(Tag.PatientBirthDate, this.anonyPatientID);

    }





}
