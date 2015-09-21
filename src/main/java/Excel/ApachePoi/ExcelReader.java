package Excel.ApachePoi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by alco on 04/08/2015.
 * ExcelReader contain all method to read a Fl File
 */
@SuppressWarnings("javadoc")
public class ExcelReader {
    private final static String TITLE_FORMAT_IGNORE = "Nom de l'Onglet";
    private final static Integer TITLE_POSITION_IN_SHEET = 1;

    /**
     * getWorkbookByFilePath
     * @param path
     * @return Workbook
     * @throws IOException
     */
    public Workbook getWorkbookByFilePath(String path) throws IOException {
        return new XSSFWorkbook(new FileInputStream(path));
    }

    /**
     * getAllSheetName
     * @param wb
     * @return List<String>
     */
    public List<String> getAllSheetName(Workbook wb){
        List<String> listSheetName = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            listSheetName.add(wb.getSheetAt(i).getSheetName());
        }
        return listSheetName;
    }

    /**
     * getAllSheet
     * @param wb
     * @return List<Sheet>
     */
    public List<Sheet> getAllSheet(Workbook wb){
        List<Sheet> listSheetName = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            listSheetName.add(wb.getSheetAt(i));
        }
        return listSheetName;
    }

    /**
     * getAllSheetDisplay
     * @param wb
     * @param format
     * @return List<Sheet>
     */
    public List<Sheet> getAllSheetDisplay(Workbook wb, String format) {
        Sheet sheet = wb.getSheet(format);
        List<String> listTitleFirst = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        List<Sheet> listSheet = new ArrayList<>();
        boolean cellNotEmpty = true;
        int i = 0;
        while (cellNotEmpty) {
            try {
                if (!sheet.getRow(i).getCell(0).toString().equals("")){
                    listTitleFirst.add(sheet.getRow(i).getCell(0).toString());
                }else{
                    cellNotEmpty = false;
                }
                i++;
            }catch (NullPointerException e){
                cellNotEmpty = false;
            }
        }

        for (String title : listTitleFirst) {
            if (!title.equals("") && !title.equals(TITLE_FORMAT_IGNORE)){
                if (!listTitle.contains(title)){
                    listTitle.add(title);
                }
            }
        }

        listSheet.addAll(listTitle.stream().map(wb::getSheet).collect(Collectors.toList()));

        return listSheet;
    }

    /**
     * getAllTitleOfSheet
     * @param sheet
     * @return List<String>
     */
    public List<String> getAllTitleOfSheet(Sheet sheet){
        List<String> listTitle = new ArrayList<>();
        boolean cellNotEmpty = true;
        Row titleRow = sheet.getRow(TITLE_POSITION_IN_SHEET);
        int i = 0;
        while (cellNotEmpty) {
            try {
                if (!titleRow.getCell(i).toString().equals("")){
                    listTitle.add(titleRow.getCell(i).toString());
                }else{
                    cellNotEmpty = false;
                }
                i++;
            }catch (NullPointerException e){
                cellNotEmpty = false;
            }
        }
        return listTitle;
    }

    /**
     * getTitleOfSheetByName
     * @param titleName
     * @param sheet
     * @return Cell
     */
    public Cell getTitleOfSheetByName(String titleName, Sheet sheet){
        Row titleRow = sheet.getRow(TITLE_POSITION_IN_SHEET);
        for (int i = 0; i < titleRow.getPhysicalNumberOfCells(); i++) {
            if(titleRow.getCell(i).toString().equals(titleName)){
                return titleRow.getCell(i);
            }
        }
        return null;
    }

    /**
     * getColumn
     * @param column
     * @param sheet
     * @param numberOfRow
     * @return List<String>
     */
    public List<String> getColumn(int column , Sheet sheet, int numberOfRow){
        List<String> columnList = new ArrayList<>();
        for (int i = 2; i < numberOfRow; i++) {
            columnList.add(sheet.getRow(i).getCell(column).toString().trim().toUpperCase());
        }
        return columnList;
    }

    /**
     * getNumberOfRow
     * @param sheet
     * @return int
     * @throws IllegalArgumentException
     */
    public int getNumberOfRow(Sheet sheet) throws IllegalArgumentException{
        int numberOfRow = 0;
        boolean bool = true;
        try{
            while (bool) {
                try {
                    if(sheet.getRow(numberOfRow).getCell(0).toString().equals("")){
                        bool = false;
                    }else {
                        numberOfRow++;
                    }
                }catch (RuntimeException e){
                    bool = false;
                }
            }
            return numberOfRow;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}
