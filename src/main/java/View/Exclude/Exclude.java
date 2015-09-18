package View.Exclude;

import Analytics.StockAll;
import BDD.CheckedClient.GestionBDDCheckedClient;
import BDD.CheckedClient.OpenHelperCheckedClient;
import View.Dialogs.Dialogs;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alco on 16/09/2015.
 * Exclude analytics
 */
public class Exclude {

    protected void start(){
        ListView<String> list = new ListView<>();
        init();
        Stage stage = getStage(list);
        stage.showAndWait();
    }

    private Stage getStage(ListView<String> list) {

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Scene scene = new Scene(vBox, 300, 300);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("ListViewSample");

        Label label = new Label("Client Checked:");
        Button saveButton = new Button("Save");
        Label space = new Label("  ");
        Button closeButton = new Button("Close");

        saveButton.setOnAction(e -> save(stage));
        closeButton.setOnAction(e -> close(stage));

        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(saveButton, space, closeButton);
        vBox.getChildren().addAll(label, list,hBox);
        VBox.setMargin(label, new Insets(1, 5, 1, 5));
        VBox.setMargin(list, new Insets(1, 5, 1, 5));
        VBox.setMargin(hBox, new Insets(1, 5, 5, 5));
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        List<String> clientList = StockAll.clientCheckedList;

        ObservableList<String> data = FXCollections.observableArrayList(clientList);
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2){
                    ObservableList<String> itemSelected = list.getSelectionModel().getSelectedItems();
                    String[] clientValue = itemSelected.get(0).split(OpenHelperCheckedClient.getSeparator());
                    if (clientValue[1].equals("yes")){
                        clientValue[1] = "no";
                    }else {
                        clientValue[1] = "yes";
                    }
                    String concatValueClient = clientValue[0] + OpenHelperCheckedClient.getSeparator() + clientValue[1];
                    for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                        if (StockAll.clientCheckedList.get(i).equals(itemSelected.get(0))){
                            StockAll.clientCheckedList.remove(i);
                            StockAll.clientCheckedList.add(concatValueClient);
                        }
                    }
                }
            }
        });
        list.setItems(data);

        list.setCellFactory(list1 -> new ColorRectCell(clientList));

        return stage;
    }

    private void init() {
        try {
            GestionBDDCheckedClient gestionBDDCheckedClient = new GestionBDDCheckedClient();
            if (StockAll.clientCheckedList.size() == 0){
                List<String> listClientBdd = new ArrayList<>();
                try {
                    listClientBdd = gestionBDDCheckedClient.getAllData();
                }catch (SqlJetException e){}

                StockAll stockAll = new StockAll();
                if (listClientBdd.size() == 0){
                    StockAll.clientCheckedList = stockAll.loadClientList();
                    gestionBDDCheckedClient.initBase();
                }else {
                    StockAll.clientCheckedList = listClientBdd;
                }
            }
        } catch (SqlJetException e) {
            Dialogs dialogs = new Dialogs();
            dialogs.dialogsBDDError();
        } catch (FileNotFoundException e) {
            Dialogs dialogs = new Dialogs();
            dialogs.dialogsNoRepositoriesWithFlClientFound();
        }
    }

    private void save(Stage stage){
        GestionBDDCheckedClient gestionBDDCheckedClient = new GestionBDDCheckedClient();
        try {
            gestionBDDCheckedClient.initBase();
        } catch (SqlJetException e) {
            Dialogs dialogs = new Dialogs();
            dialogs.dialogsBDDError();
        }
        close(stage);
    }

    private void close(Stage appListView) {
        Stage stage = (Stage) appListView.getScene().getWindow();
        stage.close();
    }

    /***
     * ColorRectCell cellFactory for cell color
     */
    static class ColorRectCell extends ListCell<String> {
        private  List<String> clientCheckedList = new ArrayList<>();

        ColorRectCell(List<String> clientCheckedList){
            this.clientCheckedList = clientCheckedList;
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(250, 20);
            if (item != null) {
                boolean clientChecked = false;
                String[] clientSplit = item.split(OpenHelperCheckedClient.getSeparator());
                if (clientSplit[1].equals("yes")){
                    clientChecked = true;
                }

                if(clientChecked){
                    rect.setFill(Color.rgb(0,255,0,0.2));
                }else {
                    rect.setFill(Color.rgb(255,0,0,0.2));
                }

                Label label = new Label(clientSplit[0]);
                StackPane stack = new StackPane();
                stack.getChildren().addAll(rect, label);

                setGraphic(stack);
            }
        }
    }
}
