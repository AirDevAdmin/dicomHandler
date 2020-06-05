package DicomHandler;

import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.SortDicom.DicomSortHandler;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReader;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;

import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

public class main {



    public static void main(String[] args) {




        File tmpFile = new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982");
        DicomReader dicomReader = new DicomReader(tmpFile);
        HashMap<Integer, Integer> tmpList = new HashMap<>();
        try {
            dicomReader.getAttirbutesWithOutSQ();
            byte[] tmppixelDAta =dicomReader. getPixelData();

            ImageInputStream stream = ImageIO.createImageInputStream( tmppixelDAta); // File or input stream
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);











            ByteArrayInputStream bis = new ByteArrayInputStream(tmppixelDAta);
           // File tmpFile2 = new File("D:\\98_data\\03_AiCRO_Dev\\Compressed DICOM\\00000982.jpg");

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

                    if(iByteIdx==0)
                        tmpList.put(iTmpRGB,0);


                    final int size = Integer.SIZE / 8;

                    if(!tmpList.containsKey(iTmpRGB))
                        tmpList.put(iTmpRGB,0);




                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Integer> keysey = tmpList.keySet();
        int idx = 0;
        int sum = 0;
        for(int i : keysey){

            byte[] tmparr = intToByteArray(i);

           int  b0 = (int)(tmparr[1]+256);
            int  b1 = (int)(tmparr[0]);
           int tmp = ((b1 << 8) + b0);



         /*   System.out.print("R  : "+ (byte)(i & 0xff));
            System.out.print("  G  : "+ ((byte)(i & 0xff00) >> 8));
            System.out.print("  B  : "+((byte)(i & 0xff0000) >> 16));*/
            System.out.println(tmp);
            sum += i & 0xff;
            idx++;

        }


        System.out.println((double)sum/idx);

        System.out.println((double)sum/(idx-1));


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
    public static byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte)(value >> 24);
        byteArray[1] = (byte)(value >> 16);
        byteArray[2] = (byte)(value >> 8);
        byteArray[3] = (byte)(value);
        return byteArray;
    }


}
