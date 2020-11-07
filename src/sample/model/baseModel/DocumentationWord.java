package sample.model.baseModel;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentationWord {
    private XWPFDocument document;

    public DocumentationWord() {
        document = new XWPFDocument();
    }

    public void createH1(String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = createRun(paragraph,18, true);
        run.setText(text);
    }

    public void createH2(String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = createRun(paragraph,16, true);
        run.setText(text);
    }

    public void createH3(String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = createRun(paragraph,14, true);
        run.setText(text);
    }

    public void createParagraph(String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = createRun(paragraph,14, false);
        run.setText(text);
    }

    public void closeDocumentation() {
        try {
            document.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createTable(List<ArrayList<String>> list) {
        XWPFTable table = document.createTable(1, list.get(0).size());
        table.setWidth(10000);
        table.setTableAlignment(TableRowAlign.CENTER);

        XWPFTableRow firstRow = table.getRows().get(0);
        firstRow.setRepeatHeader(true);
        renderRow(firstRow, list.get(0), true);

        for (int counter = 1; counter < list.size(); counter++) {
            renderRow(table.createRow(), list.get(counter), false);
        }
    }

    public boolean output(String titleFile) {
        boolean result = false;
        File file = new File("src/sample/data/" + titleFile + ".docx");

        String pathFile = "src/sample/data/" + titleFile;
        for (int counter = 1; file.isFile(); counter++) {
            file = new File(pathFile + counter + ".docx");
        }

        try {
            file.createNewFile();
            document.write(new FileOutputStream(file.getAbsolutePath()));
            result = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    private void renderCell(XWPFParagraph paragraph, String cellText, boolean isTitle) {
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setVerticalAlignment(TextAlignment.CENTER);
        XWPFRun run = createRun(paragraph, (isTitle)?16:14,isTitle);
        run.setText(cellText);
    }

    private void renderRow(XWPFTableRow row, List<String> string, boolean isTitle) {
        int counter = Math.min(row.getTableCells().size(), string.size());
        for (int column = 0; column < counter; column++) {
            row.getCell(column).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
            List<IBodyElement> elements = row.getCell(column).getBodyElements();
            int finalColumn = column;
            elements.forEach(element -> {
                renderCell((XWPFParagraph) element, string.get(finalColumn), isTitle);
            });
        }
    }

    private XWPFRun createRun(XWPFParagraph paragraph, int fontSize, boolean isBold) {
        XWPFRun result = paragraph.createRun();
        result.setBold(isBold);
        result.setFontSize(fontSize);
        result.setFontFamily("Times New Roman");
        return result;
    }
}
