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
    public void testReconDicom() {
        ///File[] dmcList = new File("D:\\98_data\\03_AiCRO_Dev\\PET-CT\\pet\\TEST0100000032.dcm").listFiles();
        File dmcList = new File("D:\\98_data\\03_AiCRO_Dev\\PET-CT\\pet\\TEST0100000032.dcm");


       /* tmpArray[0] = UUID.randomUUID().toString();
        tmpArray[1] = UUID.randomUUID().toString();
        tmpArray[2] = UUID.randomUUID().toString();


        for (File tmp : dmcList)*/
            rewirteData(dmcList);
   /*  File tmp =
                new File("D:\\98_data\\singo\\R1101_1_SCN_200108\\105");
        rewirteData(tmp);*/


    }

    public void rewirteData(File tmpFile) {
        //  File tmpFile = new File("D:\\98_data\\03_AiCRO_Dev\\200723_익명화안됨\\R1101_1_SCN_200108\\105");
        DicomReader dicomReader = new DicomReader(tmpFile);


        try {
            HashMap<Integer, String> atribut = dicomReader.getAttirbutesWithOutSQ();
            double[] tmppixelDAta = DataConverter.DCMPixelData2Singnal(dicomReader.getPixelData(), atribut);
            BufferedImage bufferedImage = DataConverter.FastSignal2bffImg(tmppixelDAta, Integer.parseUnsignedInt(atribut.get(Tag.Rows)),
                    Integer.parseUnsignedInt(atribut.get(Tag.Rows)),
                    Double.parseDouble(atribut.get(Tag.Rows)),Double.parseDouble(atribut.get(Tag.Rows)),Integer.parseUnsignedInt(atribut.get(Tag.Rows)));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int unsigned32(int n) {
        return n & 0xFFFFFFFF;
    }


}
