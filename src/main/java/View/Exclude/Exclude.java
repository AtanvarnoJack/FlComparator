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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
@SuppressWarnings("javadoc")
public class Exclude {
    public static final String LIST_VIEW_SAMPLE = "ListViewSample";
    public static final String CLIENT_CHECKED = "Client Checked:";
    public static final String IMG_REPOSITORY_DEG_PNG = "/img/repositoryDeg.png";
    public static final String IMG_ALL_OK_PNG = "/img/allOk.png";
    public static final String IMG_ALL_NOK_PNG = "/img/allNok.png";
    public static final String IMG_SAVE_DEG_PNG = "/img/saveDeg.png";
    public static final String IMG_CLOSE_PNG = "/img/close.png";
    public static final String TEXTURE_CSS = "/texture.css";
    public static final String SEGMENTED_BUTTON_BAR_STYLESHEET = "segmented-button-bar";
    public static final String SPACER = "spacer";
    public static final String LIST_VIEW_STYLECLASS = "listView";
    public static final String STYLE_CLASS_BACKGROUND = "-light-black: rgb(74, 75, 78);" +
            "-dark-black: rgb(39, 40, 40);" +
            "-fx-border-color:\n" +
            "        linear-gradient(to bottom, derive(-fx-base,-30%), derive(-fx-base,-60%)),\n" +
            "        linear-gradient(to bottom, -light-black 2%, -dark-black 98%);\n" +
            "     -fx-border-width: 2px;\n" +
            "     -fx-border-insets: 0px, 1px";

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
        Scene scene = new Scene(vBox);
        Stage stage = new Stage();
        setStyle(vBox, stage);
        List<String> clientList = getClientList();
        setItems(list,clientList, vBox, hBox, scene, stage);
        setList(list, clientList);

        ObservableList<String> data = FXCollections.observableArrayList(StockAll.clientCheckedList);
        list.getItems().clear();
        list.setItems(data);

        list.setCellFactory(list1 -> new ColorRectCell());
        return stage;
    }

    /***
     * getClientList
     * @return
     */
    private List<String> getClientList() {
        List<String> clientList = StockAll.clientCheckedList;
        if (clientList.size() != 0){
            String[] clientSplit = clientList.get(0).split(OpenHelperCheckedClient.getSeparator());
            try {
                clientSplit[1].equals( OpenHelperCheckedClient.getYes());
            }catch (Exception e){
                for (int i = 0; i < clientList.size(); i++) {
                    clientList.set(i,clientList.get(i)+OpenHelperCheckedClient.getSeparator()+ OpenHelperCheckedClient.getYes());
                }
            }
        }
        Collections.sort(clientList);
        StockAll.clientCheckedList = clientList;
        return clientList;
    }

    /***
     * setList method for init listView Items and listView Listener.
     * @param list
     * @param clientList
     */
    private void setList(ListView<String> list, List<String> clientList) {
        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2){
                    ObservableList<String> itemSelected = list.getSelectionModel().getSelectedItems();
                    if (itemSelected != null){
                        String[] clientValue = itemSelected.get(0).split(OpenHelperCheckedClient.getSeparator());
                        if (clientValue[1].equals( OpenHelperCheckedClient.getYes())){
                            clientValue[1] =  OpenHelperCheckedClient.getNo();
                        }else {
                            clientValue[1] =  OpenHelperCheckedClient.getYes();
                        }
                        String concatValueClient = clientValue[0] + OpenHelperCheckedClient.getSeparator() + clientValue[1];
                        for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                            if (StockAll.clientCheckedList.get(i).equals(itemSelected.get(0))){
                                StockAll.clientCheckedList.set(i, concatValueClient);
                            }
                        }
                        Collections.sort(StockAll.clientCheckedList);
                        Collections.sort(clientList);
                        ObservableList<String> data = FXCollections.observableArrayList(StockAll.clientCheckedList);
                        list.getItems().clear();
                        list.setItems(data);
                    }
                }
            }
        });
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
    private void setItems(ListView<String> list, List<String> clientList, VBox vBox, HBox hBox, Scene scene, Stage stage) {

        stage.setScene(scene);
        stage.setTitle(LIST_VIEW_SAMPLE);
        Label label = new Label(CLIENT_CHECKED);
        Button pathButton = new Button();
        Button reInitButton = new Button();
        Button saveButton = new Button();
        Button closeButton = new Button();

        Image pathImg = new Image(getClass().getResourceAsStream(IMG_REPOSITORY_DEG_PNG));
        Image reInitImgOk = new Image(getClass().getResourceAsStream(IMG_ALL_OK_PNG));
        Image reInitImgNok = new Image(getClass().getResourceAsStream(IMG_ALL_NOK_PNG));
        Image saveImg = new Image(getClass().getResourceAsStream(IMG_SAVE_DEG_PNG));
        Image closeImg = new Image(getClass().getResourceAsStream(IMG_CLOSE_PNG));

        pathButton.setGraphic(new ImageView(pathImg));
        String[] client = clientList.get(0).split(OpenHelperCheckedClient.getSeparator());
        if (client[1].equals( OpenHelperCheckedClient.getYes())){
            reInitButton.setGraphic(new ImageView(reInitImgNok));
        }else {
            reInitButton.setGraphic(new ImageView(reInitImgOk));
        }
        saveButton.setGraphic(new ImageView(saveImg));
        closeButton.setGraphic(new ImageView(closeImg));

        String css = TEXTURE_CSS;
        HBox hBoxButton = new HBox(pathButton, reInitButton,saveButton,closeButton);
        ToolBar toolBar = new ToolBar(hBoxButton);
        hBoxButton.getStyleClass().add(SEGMENTED_BUTTON_BAR_STYLESHEET);
        Region region = new Region();
        region.getStyleClass().add(SPACER);
        scene.getStylesheets().add(css);
        list.getStyleClass().add(LIST_VIEW_STYLECLASS);

        pathButton.setOnAction(e -> reLoadClient(list, clientList));
        reInitButton.setOnAction(e -> reInit(list, clientList, reInitButton));
        saveButton.setOnAction(e -> save(stage));
        closeButton.setOnAction(e -> close(stage));

        hBox.setAlignment(Pos.CENTER_RIGHT);
        list.setMaxHeight(300);
        vBox.getChildren().addAll(hBox, label, list);
        hBox.getChildren().addAll(toolBar);
        VBox.setMargin(label, new Insets(1, 5, 1, 5));
        VBox.setMargin(list, new Insets(1, 5, 1, 5));
        VBox.setMargin(hBox, new Insets(1, 5, 5, 5));
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
    }

    /***
     * reLoadClient
     */
    private void reLoadClient(ListView<String> list, List<String> clientList) {
        StockAll.repositoryAllClient = null;
        GestionBDDCheckedClient gestionBDDCheckedClient = new GestionBDDCheckedClient();
        Dialogs dialogs = new Dialogs();
        StockAll stockAll = new StockAll();
        try {
            StockAll.clientCheckedList = stockAll.loadClientList();
            clientList =  getClientList();
            Collections.sort(StockAll.clientCheckedList);
            gestionBDDCheckedClient.initBase();
        } catch (FileNotFoundException e) {
            dialogs.dialogsNoRepositoriesWithFlClientFound();
        } catch (SqlJetException e) {
            dialogs.dialogsBDDError();
        }
        removeItemsList(list);
        FXCollections.observableArrayList().clear();
        ObservableList<String> data = FXCollections.observableArrayList(clientList);
        list.getItems().clear();
        setList(list, clientList);
        list.setItems(data);

        list.setCellFactory(list1 -> new ColorRectCell());
    }

    /***
     * setStyle method for stage
     * @param vBox
     * @param stage
     */
    private void setStyle(VBox vBox, Stage stage) {
        vBox.setStyle(STYLE_CLASS_BACKGROUND);
        stage.initStyle(StageStyle.UNDECORATED);
    }

    /***
     * reInit method for pass all client at YES
     * @param list
     * @param clientList
     * @param reInitButton
     */
    private void reInit(ListView<String> list, List<String> clientList, Button reInitButton) {
        String[] client = clientList.get(0).split(OpenHelperCheckedClient.getSeparator());
        if (client[1].equals( OpenHelperCheckedClient.getYes())){
            Image reInitImg = new Image(getClass().getResourceAsStream(IMG_ALL_OK_PNG));
            reInitButton.setGraphic(new ImageView(reInitImg));
            String[] clientSplit;
            for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                clientSplit = StockAll.clientCheckedList.get(i).split(OpenHelperCheckedClient.getSeparator());
                if (clientSplit[1].equals( OpenHelperCheckedClient.getYes())){
                    clientSplit[1] =  OpenHelperCheckedClient.getNo();
                }
                StockAll.clientCheckedList.set(i,clientSplit[0] + OpenHelperCheckedClient.getSeparator() + clientSplit[1]);
                Collections.sort(StockAll.clientCheckedList);
                ObservableList<String> data = FXCollections.observableArrayList(StockAll.clientCheckedList);
                list.getItems().clear();
                list.setItems(data);
            }
        }else {
            Image reInitImg = new Image(getClass().getResourceAsStream(IMG_ALL_NOK_PNG));
            reInitButton.setGraphic(new ImageView(reInitImg));
            String[] clientSplit;
            for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                clientSplit = StockAll.clientCheckedList.get(i).split(OpenHelperCheckedClient.getSeparator());
                if (clientSplit[1].equals( OpenHelperCheckedClient.getNo())){
                    clientSplit[1] =  OpenHelperCheckedClient.getYes();
                }
                StockAll.clientCheckedList.set(i,clientSplit[0] + OpenHelperCheckedClient.getSeparator() + clientSplit[1]);
                Collections.sort(StockAll.clientCheckedList);
                ObservableList<String> data = FXCollections.observableArrayList(StockAll.clientCheckedList);
                list.getItems().clear();
                list.setItems(data);
            }
        }
    }

    /***
     * init method for init BDD and StockAll Variable
     */
    private void init() {
        try {
            GestionBDDCheckedClient gestionBDDCheckedClient = new GestionBDDCheckedClient();
            if (StockAll.clientCheckedList.size() == 0){
                StockAll stockAll = new StockAll();
                List<String> listClientBdd = gestionBDDCheckedClient.getAllData();

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
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rect = new Rectangle(250, 20);
            if (item != null) {
                boolean clientChecked = false;
                String[] clientSplit = item.split(OpenHelperCheckedClient.getSeparator());
                StackPane stack;
                try {
                    if (clientSplit[1].equals( OpenHelperCheckedClient.getYes())){
                        clientChecked = true;
                    }

                    if(clientChecked){
                        rect.setFill(Color.rgb(3,115,1,0.7));
                    }else {
                        rect.setFill(Color.rgb(20,20,20,0.7));
                    }

                    Label label = new Label(clientSplit[0]);
                    stack = new StackPane();
                    stack.getChildren().addAll(rect, label);
                }catch (Exception e){
                    rect.setFill(Color.rgb(3,115,1,0.7));
                    Label label = new Label(clientSplit[0]);
                    stack = new StackPane();
                    stack.getChildren().addAll(rect, label);
                }
                setGraphic(stack);
            }
        }
    }

    private void removeItemsList(ListView<String> list){
        list.getItems().clear();
        list.getSelectionModel().clearSelection();
        List<String> selectedItemsCopy = new ArrayList<>(list.getSelectionModel().getSelectedItems());
        list.getItems().removeAll(selectedItemsCopy);
    }
}
