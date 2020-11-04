package sample.model.baseModel;

import org.apache.poi.xwpf.usermodel.*;
import sample.classes.Analysis;
import sample.classes.Patient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Documentation {
    private static Documentation documentation = null;
    private XWPFDocument document = null;

    public XWPFDocument getDocument() {
        return document;
    }

    private Documentation() {
        document = new XWPFDocument();
    }

    public void createH1(String cellText) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontSize(18);
        run.setFontFamily("Times New Roman");
        run.setText(cellText);
    }

    public void createH2(String cellText) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        run.setBold(true);
        run.setFontSize(16);
        run.setFontFamily("Times New Roman");
        run.setText(cellText);
    }

    public void createParagraph(String cellText) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        run.setBold(false);
        run.setFontSize(14);
        run.setFontFamily("Times New Roman");
        run.setText(cellText);
    }


    private void fillCell(XWPFParagraph paragraph, String cellText, boolean isTitle) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun run = paragraph.createRun();
        if(isTitle) {
            run.setBold(true);
            run.setFontSize(16);
        } else {
            run.setBold(false);
            run.setFontSize(14);
        }
        run.setFontFamily("Times New Roman");
        run.setText(cellText);
    }

    private void fillRow(XWPFTableRow row, List<String> string, boolean isTitle) {
        int counter = Math.min(row.getTableCells().size(), string.size());
        for (int column = 0; column < counter;column++) {
            row.getCell(column).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            List<IBodyElement> elements = row.getCell(column).getBodyElements();
            int finalColumn = column;
            elements.forEach(element -> {
                fillCell((XWPFParagraph)element, string.get(finalColumn), isTitle);
            });

        }
    }

    public static Documentation getDocumentation() {
        if(documentation == null) {
            documentation = new Documentation();
        }
        return documentation;
    }

    public void closeDocumentation() {
        try {
            document.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        documentation = null;
    }

    public void createTable(int columns, List<ArrayList<String>> list) {
        XWPFTable table = document.createTable(1, columns);
        table.setWidth(10000);
        table.setTableAlignment(TableRowAlign.CENTER);
        XWPFTableRow firstRow = table.getRows().get(0);
        firstRow.setRepeatHeader(true);
        fillRow(firstRow, list.get(0), true);
        for (int counter = 1; counter < list.size();counter++) {
            fillRow(table.createRow(), list.get(counter), false);
        }
    }

    public boolean output(String titleFile) {
        boolean result = true;
        File file = new File("src/sample/data/"+titleFile+".docx");

        String pathFile = "src/sample/data/" + titleFile;
        for (int counter = 1; file.isFile(); counter++) {
            file = new File(pathFile + counter + ".docx");
        }

        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            document.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            result = false;
        }

        return result;
    }
}
