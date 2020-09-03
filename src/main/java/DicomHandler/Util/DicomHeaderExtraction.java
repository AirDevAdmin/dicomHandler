package DicomHandler.Util;

import DicomHandler.Config.Dictionary;
import DicomHandler.ReadDicom.DicomReader;
import org.dcm4che3.data.Tag;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DicomHeaderExtraction {
    public static String FILEPATH = "";
   private static List<Integer> header =   Arrays.asList(0x00100020, 0x00081090, 0x00080070, 0x00180050, 0x00180060, 0x00280030, 0x00189309, 0x00189310, 0x00189306);
    private static HashMap<Integer,String> headerNameMap =  new HashMap<>();
    private static  List<String> headerName = new ArrayList<>();
    private static List<HashMap<String, String>> columsValue = new ArrayList<>();


    public static int[] AnmiTagList = {
           Tag.AcquisitionComments                              ,
           Tag.AcquisitionContextSequence                       ,
           Tag.AcquisitionDate                                  ,
           Tag.AcquisitionDateTime                              ,
           Tag.AcquisitionDeviceProcessingDescription           ,
           Tag.AcquisitionProtocolDescription                   ,
           Tag.AcquisitionTime                                  ,
           Tag.AccessionNumber                                  ,
           Tag.AdmissionID                                      ,
           Tag.AdmittingDate                                    ,
           Tag.AdmittingDiagnosesCodeSequence                   ,
           Tag.AdmittingDiagnosesDescription                    ,
           Tag.AdmittingTime                                    ,
           Tag.DischargeDiagnosisDescription                    ,
           Tag.MedicalAlerts                                    ,
           Tag.MedicalRecordLocator                             ,
           Tag.NamesOfIntendedRecipientsOfResults               ,
           Tag.PreMedication                                    ,
           Tag.ResultsComments                                  ,
           Tag.VisitComments                                    ,
           Tag.PatientName                                      ,
           Tag.PatientID                                        ,
           Tag.PatientAddress                                   ,
           Tag.PatientBirthDate                                 ,
           Tag.PatientBirthName                                 ,
           Tag.PatientBirthTime                                 ,
           Tag.PatientInstitutionResidence                      ,
           Tag.PatientInsurancePlanCodeSequence                 ,
           Tag.PatientMotherBirthName                           ,
           Tag.PatientReligiousPreference                       ,
           Tag.PatientTelephoneNumbers                          ,
           Tag.PatientComments                                  ,
           Tag.PatientState                                     ,
           Tag.PatientTransportArrangements                     ,
           Tag.PatientPrimaryLanguageCodeSequence               ,
           Tag.PatientPrimaryLanguageModifierCodeSequence       ,
           Tag.PersonName                                       ,
           Tag.PersonAddress                                    ,
           Tag.PersonIdentificationCodeSequence                 ,
           Tag.PersonTelephoneNumbers                           ,
           Tag.OtherPatientIDsSequence                          ,
           Tag.OtherPatientIDs                                  ,
           Tag.OtherPatientNames                                ,
           Tag.AdditionalPatientHistory                         ,
           Tag.AuthorObserverSequence                           ,
           Tag.ConfidentialityConstraintOnPatientDataDescription,
           Tag.DistributionAddress                              ,
           Tag.DistributionName                                 ,
           Tag.IdentifyingComments                              ,
           Tag.ImageComments                                    ,
           Tag.Impressions                                      ,
           Tag.InsurancePlanIdentification                      ,
           Tag.InterpretationDiagnosisDescription               ,
           Tag.InterpretationIDIssuer                           ,
           Tag.InterpretationRecorder                           ,
           Tag.InterpretationText                               ,
           Tag.InterpretationTranscriber                        ,
           Tag.IssuerOfAdmissionID                              ,
           Tag.Occupation                                       ,
           Tag.PatientInstitutionResidence                      ,
           Tag.TextComments                                     ,
           Tag.TextString                                       ,
           Tag.VisitComments                                    ,
           Tag.PatientSex                                       ,
           Tag.PatientSize                                      ,
           Tag.PatientAge                                       ,
           Tag.MilitaryRank                                     ,
           Tag.EthnicGroup                                      ,
           Tag.PatientSexNeutered                               ,
           Tag.LastMenstrualDate                                ,
           Tag.PregnancyStatus                                  ,
           Tag.SmokingStatus                                    ,
           Tag.Allergies                                        ,
           Tag.InstitutionAddress                               ,
           Tag.InstitutionName                                  ,
           Tag.InstitutionalDepartmentName                      ,
           Tag.InstitutionCodeSequence                          ,
           Tag.PerformedStationName                             ,
           Tag.ResponsibleOrganization                          ,
           Tag.ScheduledStationAETitle                          ,
           Tag.ScheduledStationName                             ,
           Tag.ScheduledStationGeographicLocationCodeSequence   ,
           Tag.ScheduledStationNameCodeSequence                 ,
           Tag.ScheduledStudyLocation                           ,
           Tag.ScheduledStudyLocationAETitle                    ,
           Tag.StationName                                      ,
           Tag.VerifyingOrganization                            ,
           Tag.PhysiciansOfRecord                               ,
           Tag.PhysiciansOfRecordIdentificationSequence         ,
           Tag.PerformingPhysicianName                          ,
           Tag.PerformingPhysicianIdentificationSequence        ,
           Tag.OperatorsName                                    ,
           Tag.ReferringPhysicianName                           ,
           Tag.ReferringPhysicianAddress                        ,
           Tag.NameOfPhysiciansReadingStudy                     ,
           Tag.RequestingPhysician                              ,
           Tag.RequestingPhysicianIdentificationSequence        ,
           Tag.ActualHumanPerformersSequence                    ,
           Tag.HumanPerformerName                               ,
           Tag.HumanPerformerOrganization                       ,
           Tag.InterpretationApproverSequence                   ,
           Tag.InterpretationAuthor                             ,
           Tag.IntendedRecipientsOfResultsIdentificationSequence,
           Tag.OrderCallbackPhoneNumber                         ,
           Tag.OrderEnteredBy                                   ,
           Tag.OrderEntererLocation                             ,
           Tag.PhysicianApprovingInterpretation                 ,
           Tag.PhysiciansReadingStudyIdentificationSequence     ,
           Tag.PhysiciansOfRecord                               ,
           Tag.PhysiciansOfRecordIdentificationSequence         ,
           Tag.ReferringPhysicianAddress                        ,
           Tag.ReferringPhysicianName                           ,
           Tag.ReferringPhysicianIdentificationSequence         ,
           Tag.ReferringPhysicianTelephoneNumbers               ,
           Tag.RequestingService                                ,
           Tag.ResponsiblePerson                                ,
           Tag.ScheduledHumanPerformersSequence                 ,
           Tag.ScheduledPerformingPhysicianIdentificationSequence,
           Tag.ScheduledPerformingPhysicianName                 ,
           Tag.Manufacturer                                     ,
           Tag.ManufacturerModelName                            ,
           Tag.SoftwareVersions                                 ,
           Tag.OverlayComments                                  ,
           Tag.OverlayData                                      ,
           Tag.OverlayTime                                      ,
           Tag.OverlayData                                      ,
    };



    public static void searchingFoloder(File root, String ketWord){
        System.out.println("BBB : "+root.getAbsolutePath());
        File[] fileList = root.listFiles();
        if(fileList==null)
            return;
        for(File tmpFile :fileList){

            if(tmpFile.isFile())
                extrationDicomHeader(tmpFile);
            else
                searchingFoloder(tmpFile, ketWord);

        }

    }

    private static void extrationDicomHeader(File root) {


        DicomReader dicomReader = new DicomReader( root);
        HashMap<String, String> tmpColumns = new HashMap<>();


        try {
            HashMap<Integer, String> att = dicomReader.getAttirbutesWithOutSQ();
            if(att==null)
                return;;

            for(int tag : AnmiTagList){
                tmpColumns.put(headerNameMap.get(tag),att.get(tag));
            }

            tmpColumns.put("DirName",root.getAbsolutePath());
            columsValue.add(tmpColumns);
//                           columsValue.add(FILEPATH);


        } catch (IOException e) {

            System.out.println("BBB : "+root.getAbsolutePath());
            dicomReader.close();
            e.printStackTrace();
        }




        dicomReader.close();

    }

    public static void main(String[] args) {
        for(int tag : AnmiTagList){
            String tagDes = Dictionary.getDescription(tag);
            headerNameMap.put(tag, tagDes);
            headerName.add(tagDes);
        }
        headerName.add("DirName");
        searchingFoloder(new File("D:\\98_data\\21641655_20140715_DCM"),null);
        ExportExcel.saveXlsx("D:\\98_data\\21641655_20140715_DCM\\DicomHeader.xlsx", headerName, columsValue);
      /*  for(int tag : header){
            String tagDes = Dictionary.getDescription(tag);
            headerNameMap.put(tag, tagDes);
            headerName.add(tagDes);
        }
        headerName.add("DirName");
        searchingFoloder(new File("Z:\\CCC_Study\\2.신촌세브란스"), "PORTAL_AX");

        ExportExcel.saveXlsx("D:\\98_data\\08_ysub\\DicomHeader.xlsx", headerName, columsValue);*/


    /*  for(String tmp : Dictionary.getTagList())
          System.out.println(tmp);*/


        // AC_DicomDictionary.setupList();;
        //patient Id, Study date,
        //        List<Integer> header =   Arrays.asList(0x00100020, 0x00080020, 0x00081090,  0x00080070,0x00180050, 0x00180060,0x00181152 );


//        HashMap<String, String> tmpColumns = new HashMap<>();

   /*     DicomReader dicomReader = new DicomReader(new File("D:\\@Ing\\03_ctims\\녹십자랩셀\\test\\sample_3.dcm"));
        try {
            HashMap<Integer, String> att = dicomReader.getAttirbutesWithOutSQ();
            for(int tag : header){
                tmpColumns.put(headerNameMap.get(tag),att.get(tag));
            }
            columsValue.add(tmpColumns);

            ExportExcel.saveXlsx("D:\\@Ing\\03_ctims\\녹십자랩셀\\test\\DicomHeader.xlsx", headerName, columsValue);

            JOptionPane.showMessageDialog(null, "Complete", "Complete", JOptionPane.INFORMATION_MESSAGE);




        } catch (IOException e) {
            e.printStackTrace();
        }*/


        // AC_DcmStructure dcmStructure =  new AC_DcmStructure();

        //        File root = new File("Z:\\CT_prognosis");
    }


}
