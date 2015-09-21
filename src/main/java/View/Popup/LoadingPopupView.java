package View.Popup;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingPopupView extends Application implements Initializable {
    @FXML
    private AnchorPane AppLoadPopup;

    @FXML
    ProgressBar progressBarLoadingPopup;

    @Override
    public void start(Stage primaryStage) throws IOException {
        LoadingPopup loadingPopup = new LoadingPopup();
        loadingPopup.startLoadingPopup();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadingPopup loadingPopup = new LoadingPopup();
        loadingPopup.initPopup(progressBarLoadingPopup, AppLoadPopup);
    }

    public static String getFileNotFound() {
        return LoadingPopup.getFileNotFound();
    }

    public static String getClientNotFound() {
        return LoadingPopup.getClientNotFound();
    }
}
