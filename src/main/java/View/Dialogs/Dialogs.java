package View.Dialogs;

/**
 * Created by alco on 06/08/2015.
 * Dialogs contain all Dialogs popup
 */
@SuppressWarnings("deprecation")
public class Dialogs {
    public void dialogsNoFileFound(){
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("No Fl File!")
                .masthead("No Fl File Found!")
                .message("Please select reference Fl file!")
                .styleClass("/texture.css")
                .showError();
    }
    public void dialogsNoRepositoriesFound(){
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("No File!")
                .masthead("No Repositories Selected!")
                .message("Please select Repositories !")
                .styleClass("/texture.css")
                .showWarning();
    }
    public void dialogsNoRepositoriesWithFlClientFound(){
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("No Fl File!")
                .masthead("No Fl File Found!")
                .message("Please select repositories contains all clients!")
                .styleClass("/texture.css")
                .showError();
    }

    public void dialogsNoRefFileFound() {
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("No Ref File!")
                .masthead("No Ref Fl File Found!")
                .message("Please select Ref Fl File! like: \"thot\\\\:\\ROC\\Fichier_Liaison_V4\\Fichier de Liaison Standard\\Fichier de Liaison V4_15\"!")
                .styleClass("/texture.css")
                .showError();
    }

    public void dialogsBDDError() {
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("BDD Error!")
                .masthead("BDD Not found!")
                .message("Please restart programs")
                .styleClass("/texture.css")
                .showError();
    }

    public void dialogsBadPathRef() {
        org.controlsfx.dialog.Dialogs.create()
                .owner(null)
                .title("Path Warning!")
                .masthead("Path not valid!")
                .message("Please Select a correct path!")
                .styleClass("/texture.css")
                .showWarning();
    }
}
