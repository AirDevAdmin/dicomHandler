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





       for(File file : new File("D:\\98_data\\RCDM 테스트 데이터\\1801_박인자").listFiles()) {

            for (File tmpFile : file.listFiles()) {

                DicomReader dicomReader = new DicomReader(tmpFile);
                try {
                    dicomReader.getAttirbutesWithOutSQ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dicomReader.close();
            }
        }


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
