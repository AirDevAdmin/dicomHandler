package DicomHandler.Util;

import DicomHandler.Config.Dictionary;
import DicomHandler.ReadDicom.DicomReader;

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


    public static void searchingFoloder(File root, String ketWord){
        System.out.println("BBB : "+root.getAbsolutePath());
        File[] fileList = root.listFiles();
        if(fileList==null)
            return;
        for(File tmpFile :fileList){

            if(tmpFile.isFile())
                continue;
            else if(tmpFile.getName().toUpperCase().equalsIgnoreCase(ketWord.toUpperCase()))
                extrationDicomHeader(tmpFile);
            else
                searchingFoloder(tmpFile, ketWord);

        }

    }

    private static void extrationDicomHeader(File root) {

        File dcm = null;
        if(root.listFiles()==null)
            return;
        dcm = root.listFiles()[0];




        DicomReader dicomReader = new DicomReader( dcm);
        HashMap<String, String> tmpColumns = new HashMap<>();


        try {
            HashMap<Integer, String> att = dicomReader.getAttirbutesWithOutSQ();
            for(int tag : header){
                tmpColumns.put(headerNameMap.get(tag),att.get(tag));
            }

            tmpColumns.put("DirName", dcm.getAbsolutePath().substring(0, dcm.getAbsolutePath().lastIndexOf("\\")));
            columsValue.add(tmpColumns);
//                           columsValue.add(FILEPATH);


        } catch (IOException e) {

            System.out.println("BBB : "+dcm.getAbsolutePath());
            dicomReader.close();
            e.printStackTrace();
        }




        dicomReader.close();

    }

    public static void main(String[] args) {
      /*  for(int tag : header){
            String tagDes = Dictionary.getDescription(tag);
            headerNameMap.put(tag, tagDes);
            headerName.add(tagDes);
        }
        headerName.add("DirName");
        searchingFoloder(new File("Z:\\CCC_Study\\2.신촌세브란스"), "PORTAL_AX");

        ExportExcel.saveXlsx("D:\\98_data\\08_ysub\\DicomHeader.xlsx", headerName, columsValue);*/


      for(String tmp : Dictionary.getTagList())
          System.out.println(tmp);


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
