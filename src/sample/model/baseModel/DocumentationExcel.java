package sample.model.baseModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DocumentationExcel {
    private Workbook book;
    private Sheet sheet;

    public Sheet getSheet() {return sheet;}

    public boolean addSheet(String titleNewSheet){
        boolean result = false;
        if(book != null) {
            sheet = book.createSheet(titleNewSheet);
            result = true;
        }
        return result;
    }

    public DocumentationExcel(String titleNewSheet) {
        book = new HSSFWorkbook();
        sheet = book.createSheet(titleNewSheet);
    }

    public void renderTable(List<ArrayList<String>> twoDimensionalArray) {
        AtomicInteger counter = new AtomicInteger();
        AtomicBoolean isTitle = new AtomicBoolean(true);
        twoDimensionalArray.forEach(array -> {

            if (sheet.getLastRowNum() < 0) {
                counter.set(0);
            }

            renderRow(sheet.createRow(counter.getAndIncrement()), array, isTitle.get());
            isTitle.set(false);
        });
        int columns = twoDimensionalArray.get(0).size();
        for (int count = 0;count < columns;count++) {
            sheet.autoSizeColumn(count);
        }
    }

    public void closeDocumentation() {
        try {
            book.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean output(String titleFile) {
        boolean result = false;
        File file = new File("src/sample/data/"+titleFile+".xls");

        String pathFile = "src/sample/data/" + titleFile;
        for (int counter = 1; file.isFile(); counter++) {
            file = new File(pathFile + counter + ".xls");
        }

        try {
            file.createNewFile();
            book.write(new FileOutputStream(file.getAbsolutePath()));
            result = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    private CellStyle createCellStyle(boolean isTitle) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        if(isTitle) {
            cellStyle.setBorderBottom(BorderStyle.THICK);
            cellStyle.setBorderRight(BorderStyle.THICK);
            cellStyle.setBorderLeft(BorderStyle.THICK);
            cellStyle.setBorderTop(BorderStyle.THICK);
        } else {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
        }

        org.apache.poi.ss.usermodel.Font font = book.createFont();
        font.setFontHeightInPoints((isTitle)?(short)18:14);
        font.setBold(isTitle);
        font.setFontName("Times New Roman");

        cellStyle.setFont(font);
        return cellStyle;
    }

    private void renderCell(Cell cell, String value, boolean isTitle) {
        cell.setCellStyle(createCellStyle(isTitle));
        cell.setCellValue(value);
    }

    private void renderRow(Row row, ArrayList<String> array, boolean isTitle) {
        AtomicInteger counter = new AtomicInteger();
        array.forEach(value -> {
            if(row.getLastCellNum() < 0) {
                counter.set(0);
            }
            renderCell(row.createCell(counter.getAndIncrement()),value, isTitle);
        });
    }
}
