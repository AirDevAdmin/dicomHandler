import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.SortDicom.DicomSortHandler;
import DicomHandler.SortDicom.DicomSortSeriesHandler;
import DicomHandler.TagCategory.DicomReconTag;
import DicomHandler.Util.AES256Util;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class dicomSortTest {


    @Test
    public void testSort(){


        DicomSortHandler dicomSortSeriesHandler = new DicomSortHandler();

        dicomSortSeriesHandler.setRootPath(new File("D:\\@Ing\\03_ctims\\녹십자랩셀\\test"));
        dicomSortSeriesHandler.readRootPath();
        try {
            dicomSortSeriesHandler.startRecleanDicom();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test");



    }

    @Test
    public void testEncode() throws UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String patientidORI = "12345678";
        AES256Util aes256Util = new AES256Util();

        String incode = aes256Util.aesEncode(patientidORI);
        String decode = aes256Util.aesDecode(incode);;


        System.out.println("ori : "+patientidORI);
        System.out.println("encode : "+incode);
        System.out.println("decode : "+decode);





    }

    @Test
    public void printTag()  {

        DicomReconTag.printAllTag();;






    }


}
