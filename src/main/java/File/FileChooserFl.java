package File;

import View.Home.Home;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by alco on 04/08/2015.
 * FileChooserFl contain all method implement a FileChooser
 */
public class FileChooserFl {
    /**
     * getRepositoriesClients display a windows FileChooser for select a Excel File
     * @return File
     * @throws FileNotFoundException
     */
    public File getRepositoriesClients() throws FileNotFoundException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Repositorie");
        File defaultDirectory = new File("c:");
        chooser.setInitialDirectory(defaultDirectory);
        File file = chooser.showDialog(null);
        if (file == null){
            throw new FileNotFoundException("No Directories choose!");
        }
        return file;
    }

    /**
     * getFlFile
     * @return File
     * @throws FileNotFoundException
     */
    public File getFlFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel files (*.xlsm)", "*.xlsm"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*")
        );

        fileChooser.setTitle(Home.getAppTitle() + " : Open the source file");

        File defaultDirectory = new File("C:/");
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showOpenDialog(null);
        if (file == null){
            throw new FileNotFoundException("No Directories choose!");
        }
        return file;
    }
}
