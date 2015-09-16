package View.Exclude;

import javafx.application.Application;
import javafx.stage.Stage;

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
