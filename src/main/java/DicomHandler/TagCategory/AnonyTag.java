package DicomHandler.TagCategory;

import org.dcm4che3.data.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AnonyTag {

    public static final int[] MANDATORY_ANONYMIZATION_LIST =
            {
                    Tag.PatientID,
                    Tag.PatientBirthDate,
                    // specal aonoy
                    Tag.PatientName,
                    Tag.PatientSex,
                    Tag.PatientAge,
                    Tag.InstitutionName
            };
    public static final int[] OPTIONAL_ANONYMIZATION_LIST =
            {
                    Tag.OtherPatientIDs,
                    Tag.OtherPatientNames,
            };


    public static List<Integer> getMndtrTagList(){
        List<Integer> out = new ArrayList<>();
        out.addAll(Arrays.stream(MANDATORY_ANONYMIZATION_LIST).boxed().collect(Collectors.toList()));

        return out;
    }

    public static List<Integer> getOptTagList(){
        List<Integer> out = new ArrayList<>();
        out.addAll(Arrays.stream(OPTIONAL_ANONYMIZATION_LIST).boxed().collect(Collectors.toList()));

        return out;
    }

}
