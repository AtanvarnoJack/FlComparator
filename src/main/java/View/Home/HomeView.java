package View.Home;

import Analytics.StockAll;
import View.Dialogs.Dialogs;
import View.Popup.LoadingPopupView;
import View.Tree.TreeTableDisplayView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by alco on 06/07/2015.3
 * View main for view HomePage/fxml
 */
public class HomeView extends Application implements Initializable {
    Home home;

    @FXML
    Label labelOutput;
    @FXML
    Button OpenRepositoryButton;
    @FXML
    Button SettingButton;
    @FXML
    Button HelpButton;

    @FXML
    ScrollPane scrollPaneLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        home = new Home();
        home.startStage(primaryStage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Dialogs dialogs = new Dialogs();
        home = new Home();
        home.initStage();
        labelOutput.setText(asciiDraw);

        Image repositoryImg = new Image(getClass().getResourceAsStream("/img/repositoryDeg.png"));
        Image settingImg = new Image(getClass().getResourceAsStream("/img/settingDeg.png"));
        Image helpImg = new Image(getClass().getResourceAsStream("/img/helpDeg.png"));

        OpenRepositoryButton.setGraphic(new ImageView(repositoryImg));
        SettingButton.setGraphic(new ImageView(settingImg));
        HelpButton.setGraphic(new ImageView(helpImg));
    }

    public void handleButtonActionOpenRepository(ActionEvent actionEvent) {
        Dialogs dialogs = new Dialogs();
        LoadingPopupView loadingPopupView = new LoadingPopupView();
        StockAll.allRequestError = null;

        try {
            loadingPopupView.start(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StockAll.allRequestError == null){
            labelOutput.setText("All Client are Ok. ;)");
        }else if(StockAll.allRequestError.equals(LoadingPopupView.getFileNotFound())){
            dialogs.dialogsNoRepositoriesFound();
            StockAll.allRequestError = null;
        }else {
            if(StockAll.allRequestError.equals(LoadingPopupView.getClientNotFound())){
                dialogs.dialogsNoRepositoriesWithFlClientFound();
                labelOutput.setText("No Fl File Found!");
            }else {
                labelOutput.setText(StockAll.allRequestError);
            }
        }
    }


    public void handleButtonHelp(ActionEvent actionEvent) {
        labelOutput.setText(Home.getHelpText());
    }

    public void handleButtonArchitecture(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        TreeTableDisplayView treeTableDisplayView = new TreeTableDisplayView();
        try {
            treeTableDisplayView.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String asciiDraw =
            "*************************************************************************************\n" +
            "*                                                                                   *\n" +
            "*              _____   _            _____   _____       ___  ___   _____            *\n" +
            "*             |  ___| | |          /  ___| /  _  \\     /   |/   | |  _  \\           *\n" +
            "*             | |__   | |          | |     | | | |    / /|   /| | | |_| |           *\n" +
            "*             |  __|  | |          | |     | | | |   / / |__/ | | |  ___/           *\n" +
            "*             | |     | |___       | |___  | |_| |  / /       | | | |               *\n" +
            "*             |_|     |_____|      \\_____| \\_____/ /_/        |_| |_|               *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*                                      o8888888o                                    *\n" +
            "*                                    o88888888888o                                  *\n" +
            "*                                   8888 88888 8888                                 *\n" +
            "*                                  o888   888   888o                                *\n" +
            "*                                  8888   888   8888                                *\n" +
            "*                                  8888   888   8888                                *\n" +
            "*                                  8888o o888o o8888                                *\n" +
            "*                                  988 8o88888o8 88P                                *\n" +
            "*                                   8oo 9888889 oo8                                 *\n" +
            "*                                    988o     o88P                                  *\n" +
            "*                                      98888888P                                    *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*                                                                                   *\n" +
            "*************************************************************************************";
}
