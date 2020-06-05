import DicomHandler.ReadDicom.DicomReader;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class dicomReaderTest {

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

    @Test
    public void testReadDicom(){

        File tmpFile = new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982");
        DicomReader dicomReader = new DicomReader(tmpFile);
        try {
            dicomReader.getAttirbutesWithOutSQ();
            byte[] tmppixelDAta =dicomReader. getPixelData();


            ByteArrayInputStream bis = new ByteArrayInputStream(tmppixelDAta);
            File tmpFile2 = new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982.jpg");

            BufferedImage jpgImage = ImageIO.read(bis);

            int height = jpgImage.getHeight();
            int width = jpgImage.getWidth();
            for(int i=0; i<width;i++)
            {
                for(int j=0; j<height;j++)
                {
                    int iTmpRGB = jpgImage.getRGB(i,j);
                    ImageProducer p = jpgImage.getSource();

                    int iByteIdx =  (j*width+i)*3;

                    final int size = Integer.SIZE / 8;
                    ByteBuffer buff = ByteBuffer.allocate(size);


                    buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅


                    System.out.println(iByteIdx);
                    System.out.print("R  : "+ (byte)(iTmpRGB & 0xff));
                    System.out.print("  G  : "+ ((byte)(iTmpRGB & 0xff00) >> 8));
                    System.out.print("  B  : "+((byte)(iTmpRGB & 0xff0000) >> 16));



                }
            }





            writeToFile("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982.jpg",tmppixelDAta);


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
