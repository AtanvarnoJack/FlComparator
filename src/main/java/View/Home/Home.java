package View.Home;

import Analytics.StockAll;
import BDD.Champs.GestionBDDParams;
import BDD.CheckedClient.GestionBDDCheckedClient;
import View.Dialogs.Dialogs;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 27/07/2015.
 * MainView is a class who contain all background method of the view.
 */
@SuppressWarnings("javadoc")
public class Home {
    private final static String APP_TITLE = "Fl Equipment Comparator";
    private final static String ICON_Fl_COMPARATOR = "/img/IconComparator.png";
    public static final String HOME_PAGE_FXML = "/HomePage.fxml";
    public static final String TEXTURE_CSS = "/texture.css";

    /**
     * startStage start the stage of application with default params
     * @param stage
     * @throws java.io.IOException
     */
    protected void startStage(Stage stage) throws IOException {
        stage.setTitle(APP_TITLE);
        stage.setMinHeight(500);
        stage.setMinWidth(600);
        stage.getIcons().add(new Image(ICON_Fl_COMPARATOR));

        Parent root = FXMLLoader.load(getClass().getResource(HOME_PAGE_FXML));
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        Scene scene = new Scene(root);

        String css = TEXTURE_CSS;
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * initStage init the stage of application with default params after start
     */
    public void initStage(){
        Dialogs dialogs = new Dialogs();
        StockAll stockAll = new StockAll();
        loadChampCompare(dialogs, stockAll);
        loadClientChecked(dialogs, stockAll);
    }

    private void loadClientChecked(Dialogs dialogs, StockAll stockAll) {
        GestionBDDCheckedClient gestionBDDCheckedClient = new GestionBDDCheckedClient();
        if (StockAll.clientCheckedList.size() == 0){
            List<String> clientListChecked;
            try {
                clientListChecked = gestionBDDCheckedClient.getAllData();
                if (clientListChecked.size() == 0){
                    StockAll.clientCheckedList = stockAll.loadClientList();
                    gestionBDDCheckedClient.initBase();
                }else {
                    StockAll.clientCheckedList = clientListChecked;
                }
            } catch (SqlJetException e) {
                try {
                    StockAll.clientCheckedList = stockAll.loadClientList();
                    gestionBDDCheckedClient.initBase();
                } catch (SqlJetException e1) {
//                    dialogs.dialogsBDDError();
                } catch (FileNotFoundException e1) {
                    dialogs.dialogsNoRepositoriesWithFlClientFound();
                }
            } catch (FileNotFoundException e) {
                dialogs.dialogsNoRepositoriesWithFlClientFound();
            }
        }
    }

    private void loadChampCompare(Dialogs dialogs, StockAll stockAll) {
        GestionBDDParams gestionBDDParams = new GestionBDDParams();
        if(StockAll.listChampCompare == null){
            HashMap<String, List<String>> recordsFound;
            try {
                recordsFound = gestionBDDParams.getAllData();
                if (recordsFound.size() == 0){
                    StockAll.listChampCompare = stockAll.loadConstChamps();
                    gestionBDDParams.initBase();
                }else {
                    StockAll.listChampCompare = recordsFound;
                }
            } catch (SqlJetException e) {
                try {
                    gestionBDDParams.initBase();
                } catch (SqlJetException e1) {
                    dialogs.dialogsBDDError();
                }
            }
        }
    }

    public static String getAppTitle() {
        return APP_TITLE;
    }

    private final static String HELP_TEXT = "Fl Equipement Comparator:\n" +
            "\n" +
            "Run: Fl_Equipement_Comparator.jar\n" +
            "Need: java jdk 1.8 or jre 1.8\n" +
            "\n" +
            "But: comparer des colonnes du FL.\n" +
            "\n" +
            "Home:\n" +
            "A la premiére ouverture de \"Home\" le programme va vous demander de choisir le dossier de tous les clients en production.\n" +
            "\n" +
            "\n" +
            "Settings View:\n" +
            "Permet de choisir les colonnes du FL à comparer.\n" +
            "\t- Save : Sauvegarde la configuration choisie.\n" +
            "\t- ReInit Réinitialise la configuration de base.\n" +
            "\t- Exclude : Permet de choisir les clients à tester.\n" +
            "\t- Path Réf : Permet de choisir un FL de référence pour la sélection des champs.\n" +
            "\t- Le texte à droite de à Path \"Rèf\" indique le fichier choisi pour référence.\n" +
            "\t- L'arbre de gauche permet de choisir les colonnes a ajoute a la liste de comparaison.\n" +
            "\t\t- Double cliquez sur un label de la liste pour l'ajout à la liste des comparaisons.\n" +
            "\t- L'arbre de droite affiche la liste des champs à comparer. (Feuille: champs)\n" +
            "\t\t- Double cliquez sur un label pour le supprimé de la liste.\n" +
            "\n" +
            "A la premiére ouverture de \"Settings\" le programme va vous demander de choisir un FL de Référence.\n" +
            "\n" +
            "Open Repositories:\n" +
            "Permet de choisir un répertoire pour lancer la comparaison des clients.\n" +
            "Ce répertoire doit contenir la liste des répertoires client.\n" +
            "\n" +
            "Exclude View:\n" +
            "Permet de choisir les clients à tester.\n" +
            "\t- All Ok : Permet de selectionné tous les clients.\n" +
            "\t- Save : Permet de sauvegarder les choix en base.\n" +
            "\t- Close : fermer la fenêtre.\n";

    public static String getHelpText() {
        return HELP_TEXT;
    }

    public static String getICON_Fl_COMPARATOR() {
        return ICON_Fl_COMPARATOR;
    }
}
