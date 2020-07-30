package DicomHandler.SortDicom;

import DicomHandler.CleanDicom.DicomCleanHandler;
import DicomHandler.DicomHandler;
import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.Util.AES256Util;
import DicomHandler.Util.StringUtil;
import org.dcm4che3.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DicomSortHandler extends DicomHandler {


    public static void main(String[] args){
        DicomSortHandler dicomSortSeriesHandler = new DicomSortHandler();

        dicomSortSeriesHandler.setRootPath(new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM"));
        dicomSortSeriesHandler.readRootPath();
        try {
            dicomSortSeriesHandler.startRecleanDicom();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test");
    }

    static Logger logger =  LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected File rootPath ;
    protected String sortFileRootName = "SortDicom";

    public class SliceInfo {
        public String patientID;
        public String studyUID;
        public String seriesUID;
        public String imgOrient;
        public String imgPos;
        public String instNum;
        public String sopUID;
        public String studyDes;
        public String filePath;
        public Object dcmObject;


        public String getStudyUID() {
            return this.studyUID;
        }
        public String getPatientID() {
            return this.studyUID;
        }

        public String getStudyDes() {
            return this.studyDes;
        }
        public String getSeriesUID() {return this.seriesUID;}
        public String getOutputPath(){
            return rootPath.getAbsolutePath()+File.separator+sortFileRootName+File.separator+
                    studyUID+File.separator+seriesUID;
        }
        public String getOutputPath(String studySortName, String seriesSortName){
            return rootPath.getAbsolutePath()+File.separator+sortFileRootName+File.separator+
                    studySortName+File.separator+seriesSortName;
        }

        public String getOutputRandomPath(){
            return rootPath.getAbsolutePath()+File.separator+sortFileRootName+File.separator+
                    StringUtil.getRandomString(5) +File.separator+  StringUtil.getRandomString(5);
        }

        public String getOutputOriPath(String studyName , String seriesName ){
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = filePath.split(pattern);
            String subject = split[split.length-3];
            String timepoint = split[split.length-2];
            return rootPath.getAbsolutePath()+File.separator+sortFileRootName+File.separator+
                    subject +File.separator+ timepoint  +File.separator+studyName+File.separator+seriesName;
        }
        public String getOutputSortPath(){
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = filePath.split(pattern);
            String[] rootsplite = rootPath.getAbsolutePath().split(pattern);
            StringBuilder outputPath = new StringBuilder();
            for(int i=0; i<split.length-1;i++){
                outputPath.append(split[i]+File.separator);
                if(i==rootsplite.length-1)
                    outputPath.append(sortFileRootName+File.separator);
            }
            return outputPath.toString();
        }



    }

    public  List<SliceInfo> sliceinfos = new ArrayList<>();




    public DicomSortHandler(){

    }

    public void setRootPath(File rootPath){
        this.rootPath =rootPath;
    }
    public void readRootPath(){
        if(this.rootPath==null || !this.rootPath.exists()){
            logger.error("root path Error");
            return;
        }
        try {

            fileRead( rootPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fileRead(File file) throws IOException {
        File[] inFileList = file.listFiles();
        for(File inFile : inFileList) {
            if (inFile.isDirectory()) {
                fileRead(inFile);
            } else {
                //  System.out.println(inFile.getAbsoluteFile());
                logger.info("read filepath : " + inFile);
                DicomReader dicomReader = new DicomReader(inFile);
                addSlice(dicomReader.getAttirbutesWithOutSQ(), inFile.getAbsolutePath());
                dicomReader.close();
            }
        }
    }

    public void startRecleanDicom() throws IOException {
        HashMap<String, HashMap<String,List<SliceInfo>>> fileList = getSortStudys();
        Set<String> studyKeyList =  fileList.keySet();
        AES256Util aes256Util = new AES256Util();

        for(String studyKey : studyKeyList){
            HashMap<String,List<SliceInfo>> tmSeriesList = fileList.get(studyKey);
            String patientID =studyKey;
            String studyName =  StringUtil.getRandomString(5);
            Set<String> seriesKeyList =  tmSeriesList.keySet();

            String patientName = null;
            try {
                String encodeString = aes256Util.aesEncode(patientID);
                patientName = encodeString+"_"+aes256Util.aesEncode(encodeString);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }


            for(String seriesKey : seriesKeyList){
                List<SliceInfo> tmpSliceList = tmSeriesList.get(seriesKey);
                DicomSortSeriesHandler dicomSortSeriesHandler = new DicomSortSeriesHandler();
                dicomSortSeriesHandler.setSliceinfos(tmpSliceList);
                //String seriesName = StringUtil.getRandomString(5);
                List<SliceInfo> sortList = dicomSortSeriesHandler.getSortFileList();

                int idx = 0;
                for(SliceInfo slice : sortList){
                    if(patientID==null)
                        patientID = slice.getPatientID();
                    DicomReader dicomReader = new DicomReader(new File(slice.filePath));
                    DicomCleanHandler dicomCleanHandler = new DicomCleanHandler();


                    dicomCleanHandler.setSortRule(dicomSortSeriesHandler.getSortState());
                    HashMap<Integer, String>  tmpDcmS=  dicomReader.getAttirbutesWithOutSQ();

                    String seriesFolderName = tmpDcmS.get(Tag.SeriesNumber)+"_"+
                            tmpDcmS.get(Tag.SeriesDescription)+"_"+tmpDcmS.get(Tag.ProtocolName);
                    tmpDcmS.put(Tag.StudyInstanceUID,  slice.getStudyUID());
                    tmpDcmS.put(Tag.InstanceNumber,  Integer.toString(idx++));
                    dicomCleanHandler.buildAttrbutes(tmpDcmS,dicomReader.getPixelData());
                    dicomCleanHandler.saveDicomFile(slice.getOutputSortPath()+seriesFolderName+File.separator);
                }
            }
        }



    }


    protected HashMap<String, HashMap<String,List<SliceInfo>>> getSortStudys(){
        HashMap<String, HashMap<String,List<SliceInfo>>> outputMap = new HashMap<>();

        for(SliceInfo tmp : this.sliceinfos){
            String patientID = tmp.getPatientID();
            String studyUID = tmp.getStudyUID();
            String seiresUID = tmp.getSeriesUID();
            HashMap<String,List<SliceInfo>> tmpSeries= null;
            List<SliceInfo> sliceInfos = null;
            if (outputMap.containsKey(studyUID)) {
                tmpSeries= outputMap.get(studyUID);
            }else{
                tmpSeries= new HashMap<>();
                //outputMap.put(studyUID,tmpSeries);
            }
            if(tmpSeries.containsKey(seiresUID)){
                sliceInfos = tmpSeries.get(seiresUID);
            }else {
                sliceInfos = new ArrayList<>();
            }
            sliceInfos.add(tmp);
            tmpSeries.put(seiresUID,sliceInfos);
            outputMap.put(patientID,tmpSeries);
        }
       return outputMap;
    }

    public void addSlice(Map<Integer, String> dcmS, String filePath) {
        SliceInfo sliceinfo = new SliceInfo();
        sliceinfo.filePath = filePath;
        sliceinfo.patientID = dcmS.get(Tag.PatientID);
       // sliceinfo.studyUID = dcmS.get(Tag.StudyInstanceUID);

        sliceinfo.studyUID = dcmS.get(Tag.StudyDate)+dcmS.get(Tag.StudyTime);
        sliceinfo.seriesUID = dcmS.get(Tag.SeriesInstanceUID);
        sliceinfo.studyDes = dcmS.get(Tag.StudyDescription);

        sliceinfo.instNum = dcmS.get(Tag.InstanceNumber);
        sliceinfo.imgOrient = dcmS.get(Tag.ImageOrientationPatient);
        sliceinfo.imgPos = dcmS.get(Tag.ImagePositionPatient);
        sliceinfo.sopUID = dcmS.get(Tag.SOPInstanceUID);

        sliceinfos.add(sliceinfo);
    }








}
