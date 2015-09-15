package View.Home;

import Analytics.StockAll;
import BDD.Champs.GestionBDParams;
import View.Dialogs.Dialogs;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 27/07/2015.
 * MainView is a class who contain all background method of the view.
 */
public class Home {
    private final static String APP_TITLE = "Fl Equipment Comparator";
    private final static String ICON_Fl_COMPARATOR = "/img/IconComparator.png";
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

        Parent root = FXMLLoader.load(getClass().getResource("/HomePage.fxml"));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        Scene scene = new Scene(root);

        String css = "/texture.css";
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
        GestionBDParams gestionBDParams = new GestionBDParams();
        if(StockAll.listChampCompare == null){
            HashMap<String, List<String>> recordsFound = null;
            try {
                recordsFound = gestionBDParams.getAllData();
                if (recordsFound.size() == 0){
                    StockAll.listChampCompare = stockAll.loadConstChamps();
                    gestionBDParams.initBase();
                }else {
                    StockAll.listChampCompare = recordsFound;
                }
            } catch (SqlJetException e) {
                try {
                    gestionBDParams.initBase();
                } catch (SqlJetException e1) {
                    dialogs.dialogsBDDError();
                }
            }
        }
    }

    public static String getAppTitle() {
        return APP_TITLE;
    }

    private final static String HELP_TEXT = "Fl Equipement Comparator:\n"+
            "\n"+
            "But: comparer des colonnes du FL.\n"+
            "\n"+
            "Settings:\n"+
            "Permet de choisir les colonnes du FL à comparer.\n"+
            "\t- Save : Sauvegarde la configuration choisie.\n"+
            "\t- ReInit Réinitialise la configuration de base.\n"+
            "\t- Path Réf : Permet de choisir un FL de référence pour la sélection des champs.\n"+
            "\t- Le texte à droite de « Path Réf » indique le fichier choisi pour référence.\n"+
            "\t- L’arbre de gauche permet de choisir les colonnes a ajouté a la liste de comparaison.\n"+
            "\t\t- Double cliquez sur un label de la liste pour l’ajouté à la liste des comparaisons.\n"+
            "\t- L’arbre de droite affiche la liste des champs à comparer. (Feuille : champs)\n"+
            "\t\t- Double cliquez sur un label pour le supprimé de la liste.\n"+
            "\n"+
            "A la première ouverture de Settings le programme va vous demander de choisir un FL de Référence.\n" +
            "Ex : Thot\\\\:\\ROC\\Fichier_Liaison_V4\\Fichier de Liaison Standard\\Fichier de Liaison V4_15.xlsm\n"+
            "\n"+
            "Open Repositories :\n"+
            " Permet de choisir un répertoire pour lancé la comparaison des clients.\n"+
            "Ce répertoire doit contenir la liste des répertoires client.\n"+
            "(Ex : Thot\\\\:\\ROC\\Fichier_Liaison_V4\\Clients_ROC).\n";

    public static String getHelpText() {
        return HELP_TEXT;
    }

    public static String getICON_Fl_COMPARATOR() {
        return ICON_Fl_COMPARATOR;
    }
}
