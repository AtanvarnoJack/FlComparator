package View.Exclude;

import Analytics.StockAll;
import BDD.CheckedClient.GestionBDDCheckedClient;
import BDD.CheckedClient.OpenHelperCheckedClient;
import View.Dialogs.Dialogs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alco on 16/09/2015.
 * Exclude analytics
 */
public class Exclude {

    /***
     * start method call onStart of ExcludeView
     */
    protected void start(){
        ListView<String> list = new ListView<>();
        init();
        Stage stage = getStage(list);
        stage.showAndWait();
    }

    /***
     * getStage method return stage exclude complete
     * @param list
     * @return
     */
    private Stage getStage(ListView<String> list) {

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        Scene scene = new Scene(vBox, 300, 300);
        Stage stage = new Stage();
        setStyle(vBox, stage);
        List<String> clientList = setItems(list, vBox, hBox, scene, stage);
        setList(list, clientList);
        return stage;
    }

    /***
     * setList method for init listView Items and listView Listener.
     * @param list
     * @param clientList
     */
    private void setList(final ListView<String> list, final List<String> clientList) {
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
                    Collections.sort(StockAll.clientCheckedList);
                    Collections.sort(clientList);
                    ObservableList<String> data = FXCollections.observableArrayList(clientList);
                    list.setItems(data);
                }
            }
        });

        list.setItems(data);

        list.setCellFactory(list1 -> new ColorRectCell(clientList));
    }

    /***
     * setItems init all items on stage
     * @param list
     * @param vBox
     * @param hBox
     * @param scene
     * @param stage
     * @return
     */
    private List<String> setItems(ListView<String> list, VBox vBox, HBox hBox, Scene scene, Stage stage) {
        stage.setScene(scene);
        stage.setTitle("ListViewSample");
        Label label = new Label("Client Checked:");
        Button reInitButton = new Button("All Ok");
        Button saveButton = new Button("Save");
        Button closeButton = new Button("Close");
        ToolBar toolBar = new ToolBar(reInitButton,saveButton,closeButton);
        //todo toolbar position
        reInitButton.setOnAction(e -> reInit(list));
        saveButton.setOnAction(e -> save(stage));
        closeButton.setOnAction(e -> close(stage));

        hBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.getChildren().addAll(label, list,hBox);
        hBox.getChildren().addAll(toolBar);
        VBox.setMargin(label, new Insets(1, 5, 1, 5));
        VBox.setMargin(list, new Insets(1, 5, 1, 5));
        VBox.setMargin(hBox, new Insets(1, 5, 5, 5));
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        List<String> clientList = StockAll.clientCheckedList;
        Collections.sort(StockAll.clientCheckedList);
        Collections.sort(clientList);
        return clientList;
    }

    /***
     * setStyle method for stage
     * @param vBox
     * @param stage
     */
    private void setStyle(VBox vBox, Stage stage) {
        vBox.setStyle("-light-black: rgb(74, 75, 78);" +
                "-dark-black: rgb(39, 40, 40);" +
                "-fx-border-color:\n" +
                "        linear-gradient(to bottom, derive(-fx-base,-30%), derive(-fx-base,-60%)),\n" +
                "        linear-gradient(to bottom, -light-black 2%, -dark-black 98%);\n" +
                "     -fx-border-width: 2px;\n" +
                "     -fx-border-insets: 0px, 1px");
        stage.initStyle(StageStyle.UNDECORATED);
    }

    /***
     * reInit method for pass all client at YES
     * @param list
     */
    private void reInit(ListView<String> list) {
        String[] clientSplit;
        for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
            clientSplit = StockAll.clientCheckedList.get(i).split(OpenHelperCheckedClient.getSeparator());
            if (clientSplit[1].equals("no")){
                clientSplit[1] = "yes";
            }
            StockAll.clientCheckedList.set(i,clientSplit[0] + OpenHelperCheckedClient.getSeparator() + clientSplit[1]);
            Collections.sort(StockAll.clientCheckedList);
            ObservableList<String> data = FXCollections.observableArrayList(StockAll.clientCheckedList);
            list.setItems(data);
        }
    }

    /***
     * init method for init BDD and StockAll Variable
     */
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

    /***
     * save method for save BDD
     * @param stage
     */
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

    /***
     * close method for close stage
     * @param appListView
     */
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
