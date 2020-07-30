import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.Util.StringUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class stringUtillTest {

    @Test
    public void removeOtherCharTest(){
        String Test1 = "1000_test_654";
        String Test2 = "ㄴㅇㅇㅁㅇesd5456412ㄴㅁㅇㅁㄴㅇㅁㄴ3@!#asd";

        System.out.println(StringUtil.removeOtherChar(Test1));
        System.out.println(StringUtil.removeOtherChar(Test2));



    }

}
