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
