import DicomHandler.ReadDicom.DicomReader;
import com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReader;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class dicomReaderTest {

    @Test
    public  void renameFile(){
        String rootPath = "D:\\98_data\\tmp";
        File[] file = new File(rootPath).listFiles();
        for(File tmpProject : file){
            for(File tmpFile : tmpProject.listFiles()){
                String[] fileName = tmpFile.getName().split("_");
                for(File dcmfile : tmpFile.listFiles()) {
                    String newName =tmpProject.getAbsoluteFile() + File.separator +
                            fileName[1] + File.separator + fileName[2]+File.separator+dcmfile.getName();
                    System.out.println(newName);

                    dcmfile.renameTo(new File(newName));
                }
            }
        }


    }

    public void writeToFile(String filename, byte[] pData)

    {

        if(pData == null){

            return;

        }



        int lByteArraySize = pData.length;

        System.out.println(filename);



        try{

            File lOutFile = new File(filename);

            FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);

            lFileOutputStream.write(pData);

            lFileOutputStream.close();

        }catch(Throwable e){

            e.printStackTrace(System.out);

        }

    }

    private static int byteArrayToInt(byte[] bytes) {
        final int size = Integer.SIZE / 8;
        ByteBuffer buff = ByteBuffer.allocate(size);
        final byte[] newBytes = new byte[size];
        for (int i = 0; i < size; i++) {
            if (i + bytes.length < size) {
                newBytes[i] = (byte) 0x00;
            } else {
                newBytes[i] = bytes[i + bytes.length - size];
            }
        }
        buff = ByteBuffer.wrap(newBytes);
        buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
        return buff.getInt();
    }

    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }

    public  byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte)(value >> 24);
        byteArray[1] = (byte)(value >> 16);
        byteArray[2] = (byte)(value >> 8);
        byteArray[3] = (byte)(value);
        return byteArray;
    }

    String[] folderName = {"chest","hand","mammo","spine"};

    @Test
    public void Tmp() {

        String input = "D:\\98_data\\03_AiCRO_Dev\\PET-CT\\etc";
        String outputTraining = "D:\\98_data\\03_AiCRO_Dev\\PET-CT\\etc\\output\\Testing2";
        String outputTest = "D:\\98_data\\03_AiCRO_Dev\\PET-CT\\etc\\output\\validation";

        int traningDataIdx = 0;
        int TestDataIdx = 0;

        for(String tmpName : folderName){
            File tmpFileList = new File(input+File.separator+tmpName);

                radomFileName(tmpFileList);
        }

        for(String tmpName : folderName){



            File outputTraningFolderName = new File( outputTraining + File.separator + tmpName);
            if (tmpName.equals("chest") || tmpName.equals("spine"))
                outputTraningFolderName = new File( outputTraining + File.separator + "other");

            File outputTestFolderName = new File( outputTest + File.separator + tmpName);
            if (tmpName.equals("chest") || tmpName.equals("spine"))
                outputTestFolderName = new File( outputTest + File.separator + "other");

            if(!outputTraningFolderName.exists())
                outputTraningFolderName.mkdirs();
            if(!outputTestFolderName.exists())
                outputTestFolderName.mkdirs();


            File tmp = new File(input+File.separator+tmpName);
            File[] tmpFileList = tmp.listFiles();
            int cnt = 0;
            for(File tmpDcm : tmpFileList) {
                System.out.println((double)cnt/(double)tmpFileList.length);
                if((double)cnt/(double)tmpFileList.length<0.8) {
                    fileCopy(tmpDcm.getAbsolutePath(), outputTraningFolderName.getAbsolutePath() + File.separator + "Training_" + traningDataIdx + ".dcm");
                    traningDataIdx++;
                }else{
                    fileCopy(tmpDcm.getAbsolutePath(), outputTestFolderName.getAbsolutePath() + File.separator + "Training_" + TestDataIdx + ".dcm");
                    TestDataIdx++;
                }

                cnt++;


            }

        }






    }


    public static void fileCopy(String input, String output) {
        //원본 파일경로
        String oriFilePath = input;
        //복사될 파일경로
        String copyFilePath = output;

        //파일객체생성
        File oriFile = new File(oriFilePath);
        //복사파일객체생성
        File copyFile = new File(copyFilePath);

        try {

            FileInputStream fis = new FileInputStream(oriFile); //읽을파일
            FileOutputStream fos = new FileOutputStream(copyFile); //복사할파일

            int fileByte = 0;
            // fis.read()가 -1 이면 파일을 다 읽은것
            while((fileByte = fis.read()) != -1) {
                fos.write(fileByte);
            }
            //자원사용종료
            fis.close();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void radomFileName(File tmp){
        File[] tmpFileList = tmp.listFiles();
        for(File tmpDcm : tmpFileList){
            tmpDcm.renameTo(new File(tmp.getAbsolutePath()+File.separator+UUID.randomUUID().toString()));

        }



    }





        @Test
    public void testReadDicom(){

        File tmpFile = new File("D:\\@Ing\\03_ctims\\녹십자랩셀\\sample_3.dcm");
        DicomReader dicomReader = new DicomReader(tmpFile);
        try {
            dicomReader.getAttirbutesWithOutSQ();
            byte[] tmppixelDAta =dicomReader. getPixelData();

            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(tmppixelDAta));


            ByteArrayInputStream bis = new ByteArrayInputStream(tmppixelDAta);
            File tmpFile2 = new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982.jpg");

            BufferedImage jpgImage = ImageIO.read(bis);

            /*J2KImageReaderSpi spi = new J2KImageReaderSpi();

            J2KImageReader j2KImageReader = new J2KImageReader(spi);
            J2KImageReadParam rp = new J2KImageReadParam();


            j2KImageReader.setInput(iis, true);
            jpgImage = j2KImageReader.read(0, new com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam());*/

            int height = jpgImage.getHeight();
            int width = jpgImage.getWidth();
            System.out.print(" W  : "+ width);
            System.out.print(" H  : "+height );
            System.out.print(" color  : "+  jpgImage.getColorModel() );
            DataBufferUShort dataBuffer = (DataBufferUShort) jpgImage.getRaster().getDataBuffer();

            short[] data =dataBuffer.getData();
            int[] rescale = new int[data.length];
            for(int i=0; i<data.length;i++) {
             /*  int ret1 = (data[i] & 0xff);
                int ret2 = ((data[i] >> 8) & 0xff);
                ret1 = (int)(ret1+256);*/
                System.out.println(data[i]);
                rescale[i] = data[i]-1024;
            }

            byte[] gray = new byte[data.length];
            System.out.println(" test  : "+  gray.length );
            System.out.println(" test  : "+  gray.length );

            int iwindowCenter = 40;
            int iWindowWidth = 300;

            BufferedImage output = new BufferedImage( width,  height, BufferedImage.TYPE_BYTE_GRAY);
            byte[] imagePixelData = ((DataBufferByte)output.getRaster().getDataBuffer()).getData();

            double imin = (int) (iwindowCenter-0.5-(iWindowWidth-1)/2);
            double imax =  (int) (iwindowCenter-0.5+(iWindowWidth-1)/2);


            for(int i=0; i<width*height;i++)
            {
                int idx = i;

                if(rescale[idx]<= imin)
                    imagePixelData[idx] =0;
                else if(rescale[idx] > imax)
                    imagePixelData[idx] = (byte) 255;
                else {
                    byte tmp = (byte) ((rescale[idx]-imin)/(iWindowWidth)*255);
                    //int tmp = (int) ((tmpdcm[idx]-(iwindowCenter-0.5))/(iWindowWidth-1)+0*255);
                    imagePixelData[idx] = tmp;
                }


            }
                       ImageIO.write(output, "jpg", new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982.jpg"));


           /* for (int i = 0; i < data.length; i++) {
                gray[i] = (byte) ((data[i] & 0xff00) >> 8);



            }*/


          int[] sum = new int[width*height];
            int idx  = 0;
          /*  for(int i=0; i<width;i++)
            {
                for(int j=0; j<height;j++)
                {

                    int iTmpRGB = jpgImage.getRGB(i,j);
                    System.out.print("R : " +(byte) (iTmpRGB & 0xff));
                    System.out.print
                            ("G : " + (byte) ((iTmpRGB & 0xff00)>>8));
                    System.out.println("B : " + (byte)( (iTmpRGB & 0xff0000)>>16));

                    }

            }*/


            Arrays.sort(rescale); //.
            // 배열 정렬

// 최소값(Min) 출력
            System.out.println("최소값은 : "+rescale[0]);

// 최대값(Max) 출력
            System.out.println("최대값은 : " +rescale[rescale.length - 1]);

            System.out.println("test : " + (rescale[0]-rescale[rescale.length - 1]));







        } catch (IOException e) {
            e.printStackTrace();
        }

       /* for(File file : new File("D:\\98_data\\04_CTIMS\\4차 업로드 안된 데이터\\[2019-영상0003] 191024 Phantom Data").listFiles()) {

            for (File tmpFile : file.listFiles()) {

                DicomReader dicomReader = new DicomReader(tmpFile);
                try {
                    dicomReader.getAttirbutesWithOutSQ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dicomReader.close();
            }
        }*/



    }


}
