package View.Exclude;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.CheckListView;

/**
 * Created by alco on 15/09/2015.
 * Exclude View
 */
public class ExcludeView extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage oldStage) {
        Exclude exclude = new Exclude();
        exclude.start();
    }




}
