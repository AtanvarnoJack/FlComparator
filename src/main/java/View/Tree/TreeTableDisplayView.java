package View.Tree;

import Analytics.StockAll;
import View.Dialogs.Dialogs;
import View.ExcludeView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by alco on 05/08/2015.
 */
public class TreeTableDisplayView extends Application implements Initializable {

    @FXML
    AnchorPane AppTree;
    @FXML
    TreeTableView treeTableAff;
    @FXML
    TreeTableView treeTableData;
    @FXML
    Label labelTreeTable;
    @FXML
    Button treeSaveButton;
    @FXML
    Button treeReInitButton;
    @FXML
    Button treeButtonPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TreeTable treeTable = new TreeTable();
        treeTable.startStage(primaryStage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        TreeTable treeTable = new TreeTable();
        Dialogs dialogs = new Dialogs();
        File file = treeTable.getFileRef();
        if (file == null){
            dialogs.dialogsNoRefFileFound();
        }else {
            try{
                refreshFlTableView(treeTable, file);
            }catch (IllegalArgumentException IAE){
                dialogs.dialogsBadPathRef();
            } catch (IOException e) {
                dialogs.dialogsBadPathRef();
            }
            refreshDataTableView(treeTable);
            labelTreeTable.setText(file.getPath());
        }
        Image saveImg = new Image(getClass().getResourceAsStream("/img/saveDeg.png"));
        Image reIntiImg = new Image(getClass().getResourceAsStream("/img/undoArrowDeg.png"));
        Image pathImg = new Image(getClass().getResourceAsStream("/img/folderDeg.png"));
        treeSaveButton.setGraphic(new ImageView(saveImg));
        treeReInitButton.setGraphic(new ImageView(reIntiImg));
        treeButtonPath.setGraphic(new ImageView(pathImg));
    }

    private void refreshFlTableView(TreeTable treeTable, File file) throws IllegalArgumentException, IOException {
        try {
            TreeItem<String> treeRoot = treeTable.getTreeRoot(treeTable, file);
            TreeTableColumn<String, String> modelColumn = treeTable.getStringTreeTableColumn();
            mousseDoubleClickColumnFirst();
            modelColumn.setMinWidth(400);
            treeTableAff.getColumns().setAll(modelColumn);
            treeTableAff.setTreeColumn(modelColumn);
            treeTableAff.setRoot(treeRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshDataTableView(TreeTable treeTable) {
        TreeItem<String> treeDataItem = treeTable.getDataItemTreeTable();
        treeDataItem.setExpanded(true);
        TreeTableColumn<String, String> dataSelectedColumn = treeTable.getDataTreeTableColumn();
        mousseDoubleClickColumnData();
        dataSelectedColumn.setMinWidth(400);
        treeTableData.getColumns().setAll(dataSelectedColumn);
        treeTableData.setTreeColumn(dataSelectedColumn);
        treeTableData.setRoot(treeDataItem);
    }

    private void mousseDoubleClickColumnFirst() {
        TreeTable treeTable = new TreeTable();
        treeTableAff.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    String found = treeTableAff.getSelectionModel().getSelectedItem().toString();
                    String[] arrayFound = found.split(":");
                    if (arrayFound.length == 3){
                        StockAll.addListChampCompare(arrayFound[1].trim(), arrayFound[2].replace("]", " ").trim());
                        refreshDataTableView(treeTable);
                    }
                }
            }
        });
    }

    private void mousseDoubleClickColumnData() {
        TreeTable treeTable = new TreeTable();
        treeTableData.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    String found = treeTableData.getSelectionModel().getSelectedItem().toString();
                    String[] arrayFound = found.split(":");
                    if (arrayFound.length == 3){
                        StockAll.deleteOneOfListChampCompare(arrayFound[1].trim(), arrayFound[2].replace("]", " ").trim());
                        refreshDataTableView(treeTable);
                    }
                }
            }
        });
    }

    public void handleButtonTreePath(ActionEvent actionEvent) {
        Dialogs dialogs = new Dialogs();
        TreeTable treeTable = new TreeTable();
        try {
            treeTable.newPath();
        } catch (SqlJetException e) {
            dialogs.dialogsBDDError();
        }
        File file = treeTable.getFileRef();
        if (file == null){
            dialogs.dialogsNoRefFileFound();
        }else {
            labelTreeTable.setText(file.getPath());
            try {
                refreshFlTableView(treeTable, file);
            } catch (IOException e) {
                dialogs.dialogsBadPathRef();
            }
            refreshDataTableView(treeTable);
        }
    }

    public void buttonHandleActionSaveTableTree(ActionEvent actionEvent) {
        Dialogs dialogs = new Dialogs();
        TreeTable treeTable = new TreeTable();
        try {
            treeTable.save();
        } catch (SqlJetException e) {
            dialogs.dialogsBDDError();
        }
        Stage stage = (Stage) AppTree.getScene().getWindow();
        stage.close();
    }

    public void buttonHandleActionReInit(ActionEvent actionEvent) {
        Dialogs dialogs = new Dialogs();
        TreeTable treeTable = new TreeTable();
        try {
            treeTable.reInit();
            refreshDataTableView(treeTable);
        } catch (SqlJetException e) {
            dialogs.dialogsBDDError();
        }
    }

    public void buttonHandleActionExclude(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ExcludeView excludeView = new ExcludeView();
        try {
            excludeView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
