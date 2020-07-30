package DicomHandler.Util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class ExportExcel {


    private static CellStyle HeadStyle;

    public ExportExcel() {

    }

    private static void iniStyle(Workbook xlsxWb) {

        Font defaultFont = xlsxWb.createFont();
        defaultFont.setFontHeightInPoints((short) 11);
        defaultFont.setFontName("맑은 고딕");
      //  defaultFont.setBold(true);


//제목 스타일
        HeadStyle = xlsxWb.createCellStyle();
        HeadStyle.setAlignment(HorizontalAlignment.CENTER);
        HeadStyle.setBorderBottom(BorderStyle.MEDIUM);
        HeadStyle.setBorderLeft(BorderStyle.MEDIUM);
        HeadStyle.setBorderRight(BorderStyle.MEDIUM);
        HeadStyle.setBorderTop(BorderStyle.MEDIUM);
        HeadStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HeadStyle.setFont(defaultFont);

    }


    public static void saveXlsx(String path, List<String> header, List<HashMap<String, String>> data) {

        XSSFWorkbook xlsxWb = new XSSFWorkbook();

        iniStyle(xlsxWb);

        Sheet truomain = xlsxWb.createSheet("TU Domain");

        buildSheet(truomain,header, data);
        try {
            File xlsFile = new File(path);
            FileOutputStream fileOut = new FileOutputStream(xlsFile);
            // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            xlsxWb.write(fileOut);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;


    }



    private static void buildSheet(Sheet sheet,
                                   List<String> header, List<HashMap<String, String>> data) {

        // 컬럼 너비 설정
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(9, 10000);
        // ----------------------------------------------------------
        Row row = null;
        Cell cell = null;
        ///TRDOMClumcet
        setColumnsHead(sheet, header);
        setCellValue(sheet, data, header);

        for (int i = 0; i < header.size(); i++) {
            sheet.autoSizeColumn(i); //cell 크기 자동 맞춤
        }

    }

    private static void setCellValue(Sheet sheet, List<HashMap<String, String>> inputData, List<String> header) {
        Row row = null;
        Cell cell = null;
        int rowidx = 1;
        for (HashMap<String, String> tmpData : inputData) {
            int columnIdx = 0;
            row = sheet.createRow(rowidx);
            for (String tmpHeader : header) {
                cell = row.createCell(columnIdx);
                cell.setCellValue(tmpData.get(tmpHeader));
                columnIdx++;
            }
            rowidx++;
        }
    }

    private static void setColumnsHead(Sheet sheet, List<String> input) {
        Row row = sheet.createRow(0);
        for (int idx = 0; idx < input.size(); idx++) {
            System.out.println("idx : " + idx);
            Cell cell = row.createCell(idx);

            cell.setCellValue(input.get(idx));
            cell.setCellStyle(HeadStyle);


        }
    }

}
