package View.Popup;

import Analytics.*;
import Excel.ApachePoi.ExcelLoaderApache;
import Excel.ExcelLoader;

import View.Home.Home;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import File.FileLoaderFl;
import File.FileChooserFl;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 06/08/2015.
 * contain all method of LoadingPopupView
 */
public class LoadingPopup {
    private final static String FILE_NOT_FOUND = "FileNotFoundException!";
    private final static String CLIENT_NOT_FOUND = "ClientNotFoundException!";
    public static final String TEXTURE_CSS = "/texture.css";
    public static final String POPUP_LOADING_FXML = "/PopupLoading.fxml";

    Task processTask;
    Task processWait;

    protected void startLoadingPopup() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(POPUP_LOADING_FXML));
        Scene scene = new Scene(parent);

        String css = TEXTURE_CSS;
        scene.getStylesheets().add(css);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.showAndWait();
    }

    protected void initPopup(ProgressBar progressBarLoadingPopup, AnchorPane appLoadPopup) {
        progressBarLoadingPopup.setProgress(-1);
        FileChooserFl fileChooserFl = new FileChooserFl();
        File pathClients;
        try {
            pathClients = fileChooserFl.getRepositoriesClients();

            processTask = this.showProcess(pathClients);
            progressBarLoadingPopup.setProgress(0);
            progressBarLoadingPopup.progressProperty().unbind();
            progressBarLoadingPopup.progressProperty().bind(processTask.progressProperty());

            processTask.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    StockAll.allRequestError = newValue;
                    progressBarLoadingPopup.progressProperty().unbind();
                    progressBarLoadingPopup.setProgress(1);
                    processTask.cancel();
                    Stage stage = (Stage) appLoadPopup.getScene().getWindow();
                    stage.close();
                }
            });
            new Thread(processTask).start();
        } catch (FileNotFoundException e) {
            processWait = this.waitError();
            processWait.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    StockAll.allRequestError = newValue;
                    processWait.cancel();
                    Stage stage = (Stage) appLoadPopup.getScene().getWindow();
                    stage.close();
                }
            });
            new Thread(processWait).start();
        }
    }
    /**
     * Thread Task for loading method in background
     * @param fileChooser
     * @return Task
     */
    protected Task showProcess(final File fileChooser) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int NumberClientFound = 0;
                if (fileChooser != null){
                    FileLoaderFl fileLoaderFl = new FileLoaderFl();
                    ExcelLoader excelLoader = new ExcelLoaderApache();
                    Analytics analytics = new Analytics();

                    String allErrorClient = null;
                    StockAll stockAll = new StockAll();
                    List<File> clientAllFileList = fileLoaderFl.getClientFileList(fileChooser, stockAll.getNoClientCheckedList());
//                    List<File> clientAllFileList = fileLoaderFl.getAllClientFileList(fileChooser);
                    List<String> listClient = fileLoaderFl.getAllClientName(clientAllFileList);
                    for (int i = 0; i < clientAllFileList.size(); i++) {
                        File prodFlClient = fileLoaderFl.getProdFlClient(clientAllFileList.get(i), listClient.get(i));
                        File lastArchFlClient = fileLoaderFl.getArchFlClient(clientAllFileList.get(i));
                        if (prodFlClient != null && lastArchFlClient != null){
                            NumberClientFound++;
                            HashMap<String, List<String>> prodValueFlListCompare = null;
                            HashMap<String, List<String>> archValueFlListCompare = null;
                            try {
                                prodValueFlListCompare = excelLoader.getAllCompareValueFl(prodFlClient);
                                archValueFlListCompare = excelLoader.getAllCompareValueFl(lastArchFlClient);
                            }catch (IllegalArgumentException e){}
                            if (prodValueFlListCompare != null && archValueFlListCompare != null){
                                if(!analytics.compareTwoHashMapContentFlEquipment(prodValueFlListCompare, archValueFlListCompare)){
                                    if(allErrorClient == null){
                                        allErrorClient = "All Client in Error:\n" + listClient.get(i);
                                    }else {
                                        allErrorClient = allErrorClient + "\n" + listClient.get(i);
                                    }
                                }
                            }
                        }
                        updateProgress(i+1,clientAllFileList.size());
                    }
                    if(NumberClientFound > 0){
                        updateMessage(allErrorClient);
                    }else {
                        updateMessage(CLIENT_NOT_FOUND);
                    }
                }
                return true;
            }
        };
    }

    /**
     * Thread Task For wait before close popup loading
     * @return Task
     */
    protected Task waitError() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                Thread.sleep(50);
                updateMessage(FILE_NOT_FOUND);
                return true;
            }
        };
    }

    public static String getFileNotFound() {
        return FILE_NOT_FOUND;
    }

    public static String getClientNotFound() {
        return CLIENT_NOT_FOUND;
    }
}
