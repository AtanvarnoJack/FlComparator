package Excel.ApachePoi;

import Analytics.StockAll;
import Excel.ExcelLoader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 04/08/2015.
 * ExcelLoaderApache mange reader class for read Fl file
 */
@SuppressWarnings("javadoc")
public class ExcelLoaderApache implements ExcelLoader {

    /**
     * getAllCompareValueFl
     * @param file
     * @return HashMap<String, List<String>>
     * @throws IOException
     * @throws IllegalArgumentException
     */
    @Override
    public HashMap<String, List<String>> getAllCompareValueFl(File file) throws IOException, IllegalArgumentException {
        ExcelReader excelReader = new ExcelReader();
        StockAll stockAll = new StockAll();

        Workbook wb = excelReader.getWorkbookByFilePath(file.getPath());
        HashMap<String, List<String>> constChampsList = stockAll.loadConstChamps();
        HashMap<String, List<String>> outMap = new HashMap<>();

        for ( String sheetName : constChampsList.keySet() ) {
            Sheet sheet = wb.getSheet(sheetName);
            int numberOfRow = excelReader.getNumberOfRow(sheet);
            List<String> constChampList = constChampsList.get(sheetName);
            for (String champs : constChampList) {
                Cell cellTitle = excelReader.getTitleOfSheetByName(champs, sheet);
                outMap.put(champs,excelReader.getColumn(cellTitle.getColumnIndex(), sheet, numberOfRow));
            }
        }
        return outMap;
    }
}
