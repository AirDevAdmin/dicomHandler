import DicomHandler.ReadDicom.DicomReader;
import DicomHandler.SortDicom.DicomSortHandler;
import DicomHandler.SortDicom.DicomSortSeriesHandler;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class dicomSortTest {


    @Test
    public void testSort(){


        DicomSortHandler dicomSortSeriesHandler = new DicomSortHandler();

        dicomSortSeriesHandler.setRootPath(new File("D:\\98_data\\04_CTIMS\\4차 업로드 안된 데이터\\3차"));
        dicomSortSeriesHandler.readRootPath();
        try {
            dicomSortSeriesHandler.startRecleanDicom();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("test");



    }


}
