package View.Exclude;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by alco on 16/09/2015.
 */
public class Exclude {
    final

    protected void start() {
        ListView<String> list = new ListView<>();
        ObservableList<String> data = FXCollections.observableArrayList("Blue", "Yellow", "Blue");
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Scene scene = new Scene(vBox, 200, 200);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("ListViewSample");

        Label label = new Label("Client Checked:");
        Button saveButton = new Button("Save");
        Button closeButton = new Button("Close");

        saveButton.setOnAction(e -> save(stage));
        closeButton.setOnAction(e -> close(stage));

        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(saveButton, closeButton);
        vBox.getChildren().addAll(label, list,hBox);
        vBox.setMargin(list,new Insets(1,5,1,5));
        vBox.setMargin(label,new Insets(5,5,1,5));
        vBox.setMargin(hBox,new Insets(1,5,5,5));
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        list.setItems(data);

        list.getSelectionModel().selectedItemProperty().addListener(
                (ov, old_val, new_val) -> {
                    System.out.println("ov = " + ov);
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
