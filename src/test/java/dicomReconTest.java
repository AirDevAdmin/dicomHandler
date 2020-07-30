import DicomHandler.CleanDicom.DicomCleanHandler;
import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.Util.DataConverter;
import DicomHandler.Util.StringUtil;
import org.dcm4che3.data.Tag;
import org.dcm4che3.io.DicomOutputStream;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class dicomReconTest {

    String[] tmpArray = new String[4];
    @Test
    public void testReconDicom(){
        File[] dmcList = new File("D:\\98_data\\singo\\R1101_1_SCN_200108").listFiles();


        tmpArray[0] = UUID.randomUUID().toString();
        tmpArray[1] = UUID.randomUUID().toString();
        tmpArray[2] = UUID.randomUUID().toString();



           for(File tmp : dmcList)
            rewirteData(tmp);
   /*  File tmp =
                new File("D:\\98_data\\singo\\R1101_1_SCN_200108\\105");
        rewirteData(tmp);*/



    }

    public void rewirteData(File tmpFile){
      //  File tmpFile = new File("D:\\98_data\\03_AiCRO_Dev\\200723_익명화안됨\\R1101_1_SCN_200108\\105");
        DicomReader dicomReader = new DicomReader(tmpFile);

        int test = -6545;

        HashMap<Integer, String> test2222 = new HashMap<>();

        byte[]  tmpbyteArr = new byte[256];
        for(int i = -127 ;i<128;i++){
            tmpbyteArr[i+127] = (byte)i;
        }

        try {
            HashMap<Integer, String> atribut =  dicomReader.getAttirbutesWithOutSQ();
            byte[] tmppixelDAta = dicomReader.getPixelData();
            double[] doubles = DataConverter.DCMPixelData2Singnal(tmppixelDAta,atribut);
            byte[] reconPixel = new byte[tmppixelDAta.length];
            double dRescaleIntercept = 0.0;
            double dRescaleSlope = 1.0;
            String tmp = atribut.get(Tag.RescaleIntercept);
            if(tmp!=null)
                dRescaleIntercept = Double.parseDouble(atribut.get(Tag.RescaleIntercept));
            tmp = atribut.get(Tag.RescaleSlope);
            if(tmp!=null)
                dRescaleSlope = Double.parseDouble(atribut.get(Tag.RescaleSlope));

int idx = 0;


            for(int i=0 ; i<doubles.length; i++){
                int d = (int) ((doubles[i]-dRescaleIntercept)/dRescaleSlope)/2;

                   /* System.out.print(d);
                    System.out.println(  "   "+Integer.toUnsignedString(d));*/
                byte[] bytes=new byte[4];
                bytes[0]=(byte)((d&0xFF000000)>>24);
                bytes[1]=(byte)((d&0x00FF0000)>>16);
                bytes[2]=(byte)((d&0x0000FF00)>>8);
                bytes[3]=(byte) (d&0x000000FF);
                //    System.out.println(bytes[2]);




                 /*   if(b0-256<0) {
                        System.out.println("!!!!!!!!!!!!!!  " + d);
                        b0 = b0-256;
                    }else{
                        System.out.println("@@@@@@@@@@@@@@@"+ d));
                    }*/


                //  System.out.println(bytes[3]+"  "+ bytes[2]);

                reconPixel[i*2] = (byte)  (bytes[3]);

                  /*  if(bytes[2] < 8)
                        reconPixel[i*2+1] = 0;  //(byte)  ((bytes[2]));
                    else*/
                 reconPixel[i*2+1] = (byte)  ((bytes[2]));




            }

            System.out.println(idx);
            DicomCleanHandler dicomCleanHandler = new DicomCleanHandler();


            /*  dicomCleanHandler.setSortRule(dicomSortSeriesHandler.getSortState());*/
            HashMap<Integer, String>  tmpDcmS= atribut;



            tmpDcmS.put(Tag.BitsStored,  "12");
            tmpDcmS.put(Tag.PatientID,  "TESTGE_DATA");
            tmpDcmS.put(Tag.PatientName,  "TESTGE_DATA");
            tmpDcmS.put(Tag.HighBit,  "11");
            tmpDcmS.put(Tag.PixelRepresentation,"0");
            tmpDcmS.put(Tag.StudyInstanceUID,   tmpArray[0]);
            tmpDcmS.put(Tag.MediaStorageSOPInstanceUID,  tmpArray[1]);
            tmpDcmS.put(Tag.SOPInstanceUID,  UUID.randomUUID().toString());
            tmpDcmS.put(Tag.SeriesInstanceUID,  tmpArray[2]);
            String tmpTime =        tmpDcmS.get(Tag.AcquisitionTime);
            tmpTime +=  "."+ StringUtil.getRandomNumber(6);
            tmpDcmS.put(Tag.AcquisitionTime,  tmpTime);

            dicomCleanHandler.buildAttrbutes(tmpDcmS,reconPixel);
            dicomCleanHandler.saveDicomFile("D:\\98_data\\singo\\test");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int unsigned32(int n) {
        return n & 0xFFFFFFFF;
    }


}
