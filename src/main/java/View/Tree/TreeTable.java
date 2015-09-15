package View.Tree;

import Analytics.StockAll;
import BDD.Champs.GestionBDParams;
import BDD.Path.OpenHelperPath;
import Excel.ApachePoi.ExcelReader;
import File.FileChooserFl;
import View.Dialogs.Dialogs;
import View.Home.Home;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by alco on 06/08/2015.
 * TreeTable contain all method of TreeTableDisplayView
 */
public class TreeTable {
    private final static String TITLE_FORMAT_IGNORE = "Formatage_Client";
    private final static String FICHIER_DE_LIAISON = "Fichier de Liaison";
    private final static String SELECTED_TITLE = "Champs sélectionné:";
    private final static String TITLE_TABLE_VIEW_REF = "Réf du Fl:";

    /**
     * startStage
     * @param primaryStage
     * @throws IOException
     */
    protected void startStage(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/TreeTable.fxml"));
        Scene scene = new Scene(parent);

        String css = "/texture.css";
        scene.getStylesheets().add(css);
        Stage stage = new Stage();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.getIcons().add(new Image(Home.getICON_Fl_COMPARATOR()));
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * getFileRef
     * @return File
     */
    protected File getFileRef() {
        Dialogs dialogs = new Dialogs();
        FileChooserFl fileChooserFl = new FileChooserFl();
        OpenHelperPath openHelperPath = new OpenHelperPath();

        if(StockAll.filePathFlRef == null){
            try {
                StockAll.filePathFlRef = new File(openHelperPath.getRecords(OpenHelperPath.getSqlJetDb()));
            } catch (Exception e) {
                try {
                    StockAll.filePathFlRef = fileChooserFl.getFlFile();
                    openHelperPath.addRecordBase(OpenHelperPath.getSqlJetDb(),StockAll.filePathFlRef.getPath());
                } catch (FileNotFoundException FNFE) {
                    dialogs.dialogsNoFileFound();
                } catch (SqlJetException e1) {
                    try {
                        openHelperPath.createNewBase();
                        openHelperPath.createTables(OpenHelperPath.getSqlJetDb());
                        openHelperPath.addRecordBase(OpenHelperPath.getSqlJetDb(),StockAll.filePathFlRef.getPath());
                    } catch (SqlJetException e2) {
                        dialogs.dialogsBDDError();
                    }
                }
            }
        }
        return StockAll.filePathFlRef;
    }

    /**
     * getAllSheetAndTitle
     * @param file
     * @return HashMap<Sheet, List<String>>
     * @throws IOException
     */
    protected HashMap<Sheet, List<String>> getAllSheetAndTitle(File file ) throws IOException {
        HashMap<Sheet, List<String>> newFl = new HashMap<Sheet, List<String>>();
        if (file!= null){
            ExcelReader excelReader = new ExcelReader();

            Workbook wb = excelReader.getWorkbookByFilePath(file.getPath());
            List<Sheet> allSheet = excelReader.getAllSheetDisplay(wb, TITLE_FORMAT_IGNORE);
            for (Sheet sheet : allSheet) {
                List<String> allTitleOfSheet = excelReader.getAllTitleOfSheet(sheet);
                newFl.put(sheet, allTitleOfSheet);
            }
        }
        return newFl;
    }

    /**
     * geTreeItemForEntrySheetAndTitle
     * @param entry
     * @return TreeItem<String>
     */
    protected TreeItem<String> geTreeItemForEntrySheetAndTitle(Entry<Sheet, List<String>> entry) {
        TreeItem<String> childNodeSheet = new TreeItem<String>(entry.getKey().getSheetName());
        List<String> titleList = entry.getValue();
        for (String title : titleList) {
            TreeItem<String> childNodeTitle = new TreeItem<>(entry.getKey().getSheetName()  + ":" + title);
            childNodeSheet.getChildren().add(childNodeTitle);
        }
        return childNodeSheet;
    }

    /**
     * getStringTreeTableColumn
     * @return TreeTableColumn<String, String>
     */
    protected TreeTableColumn<String, String> getStringTreeTableColumn() {
        final TreeTableColumn<String ,String> modelColumn = new TreeTableColumn<String ,String>(TITLE_TABLE_VIEW_REF);
        modelColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue()));
        return modelColumn;
    }

    /**
     * getTreeRoot
     * @param treeTable
     * @param file
     * @return TreeItem<String>
     * @throws IllegalArgumentException
     * @throws IOException
     */
    protected TreeItem<String> getTreeRoot(TreeTable treeTable, File file) throws IllegalArgumentException, IOException {
        HashMap<Sheet, List<String>> sheetTitleList = treeTable.getAllSheetAndTitle(file);
        final TreeItem<String> treeRoot = new TreeItem<String>(FICHIER_DE_LIAISON);
        if (sheetTitleList.size() != 0){
            for (Entry<Sheet, List<String>> entry : sheetTitleList.entrySet()) {
                TreeItem<String> childNodeSheet = treeTable.geTreeItemForEntrySheetAndTitle(entry);
                childNodeSheet.setExpanded(true);
                treeRoot.getChildren().add(childNodeSheet);
            }
            return treeRoot;
        }else {
            throw new IllegalArgumentException("Bad File!");
        }
    }

    /**
     * getDataItemTreeTable
     * @return TreeItem<String>
     */
    protected TreeItem<String> getDataItemTreeTable() {
        StockAll stockAll = new StockAll();
        HashMap<String, List<String>> sheetTitleList = null;
        if (StockAll.listChampCompare != null){
            sheetTitleList = StockAll.listChampCompare;
        }else {
            sheetTitleList = stockAll.loadConstChamps();
            StockAll.listChampCompare = stockAll.loadConstChamps();
        }

        TreeItem<String> treeRoot = new TreeItem<String>(SELECTED_TITLE);
        for (Entry<String, List<String>> entry : sheetTitleList.entrySet()) {
            List<String> champList = entry.getValue();
            for (String champs : champList) {
                TreeItem<String> childNodeTitle = new TreeItem<>(entry.getKey() + ":" + champs);
                childNodeTitle.setExpanded(true);
                treeRoot.getChildren().add(childNodeTitle);
            }
        }
        return treeRoot;
    }

    /**
     * getDataTreeTableColumn
     * @return TreeTableColumn<String, String>
     */
    protected TreeTableColumn<String, String> getDataTreeTableColumn() {
        final TreeTableColumn<String ,String> modelColumn = new TreeTableColumn<String ,String>(SELECTED_TITLE);
        modelColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<String, String> p) ->
                new ReadOnlyStringWrapper(p.getValue().getValue()));
        return modelColumn;
    }

    /**
     * newPath
     * @throws SqlJetException
     */
    protected void newPath() throws SqlJetException {
        StockAll.filePathFlRef = null;
        OpenHelperPath openHelperPath = new OpenHelperPath();
        openHelperPath.createNewBase();
        openHelperPath.createTables(OpenHelperPath.getSqlJetDb());
    }

    /**
     * save
     * @throws SqlJetException
     */
    protected void save() throws SqlJetException {
        GestionBDParams gestionBDParams =new GestionBDParams();
        gestionBDParams.initBase();
    }

    /**
     * reInit
     * @throws SqlJetException
     */
    protected void reInit() throws SqlJetException {
        GestionBDParams gestionBDParams = new GestionBDParams();
        StockAll.listChampCompare = null;
        gestionBDParams.initBase();
    }
}
