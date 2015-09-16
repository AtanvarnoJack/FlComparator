package View;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    final Label label = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage oldStage) {
        CheckListView<String> list = new CheckListView<>();
        ObservableList<String> data = FXCollections.observableArrayList("Blue","Yellow","Blue");
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Scene scene = new Scene(vBox, 200, 200);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("ListViewSample");

        Button saveButton = new Button("Save");
        Button closeButton = new Button("Close");

        saveButton.setOnAction(e -> save(stage));
        closeButton.setOnAction(e -> close(stage));

        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(saveButton, closeButton);
        vBox.getChildren().addAll(label, list,hBox);
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setText("Client Checked:");
        list.setItems(data);

        list.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {

                });
        stage.showAndWait();
    }

    private void save(Stage appListView) {

    }

    private void close(Stage appListView) {
        Stage stage = (Stage) appListView.getScene().getWindow();
        stage.close();
    }


}
