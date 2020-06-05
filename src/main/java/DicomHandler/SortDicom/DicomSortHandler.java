package DicomHandler.SortDicom;

import DicomHandler.CleanDicom.DicomCleanHandler;
import DicomHandler.DicomHandler;
import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.Util.StringUtil;
import org.dcm4che3.data.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DicomSortHandler extends DicomHandler {


    public static void main(String[] args){
       /* DicomSortHandler dicomSortSeriesHandler = new DicomSortHandler();

        dicomSortSeriesHandler.setRootPath(new File("C:\\Users\\ShinYongbin\\Desktop\\MOCK_Training\\GX_I7"));
        dicomSortSeriesHandler.readRootPath();
        try {
            dicomSortSeriesHandler.startRecleanDicom();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test");*/

       String test = "1,2,3,4,5";
       System.out.println(test.split(",",2)[1]);
        System.out.println(test.split(",",3)[2]);

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
        for(String studyKey : studyKeyList){
            HashMap<String,List<SliceInfo>> tmSeriesList = fileList.get(studyKey);
            String studyUID =null;
            String studyName =  StringUtil.getRandomString(5);
            Set<String> seriesKeyList =  tmSeriesList.keySet();

            for(String seriesKey : seriesKeyList){
                List<SliceInfo> tmpSliceList = tmSeriesList.get(seriesKey);
                DicomSortSeriesHandler dicomSortSeriesHandler = new DicomSortSeriesHandler();
                dicomSortSeriesHandler.setSliceinfos(tmpSliceList);
                String seriesName = StringUtil.getRandomString(5);
                List<SliceInfo> sortList = dicomSortSeriesHandler.getSortFileList();

                int idx = 0;
                for(SliceInfo slice : sortList){
                    if(studyUID==null)
                        studyUID = slice.studyUID;
                    DicomReader dicomReader = new DicomReader(new File(slice.filePath));
                    DicomCleanHandler dicomCleanHandler = new DicomCleanHandler();

                    dicomCleanHandler.setSortRule(dicomSortSeriesHandler.getSortState());
                    HashMap<Integer, String>  tmpDcmS=  dicomReader.getAttirbutesWithOutSQ();
                    tmpDcmS.put(Tag.StudyInstanceUID,  studyUID);
                    tmpDcmS.put(Tag.InstanceNumber,  Integer.toString(idx++));
                    dicomCleanHandler.buildAttrbutes(tmpDcmS,dicomReader.getPixelData());
                    dicomCleanHandler.saveDicomFile(slice.getOutputOriPath(studyName,seriesName));
                }
            }
        }



    }


    protected HashMap<String, HashMap<String,List<SliceInfo>>> getSortStudys(){
        HashMap<String, HashMap<String,List<SliceInfo>>> outputMap = new HashMap<>();

        for(SliceInfo tmp : this.sliceinfos){
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
            outputMap.put(studyUID,tmpSeries);
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
