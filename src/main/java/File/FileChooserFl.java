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

    public static final String SELECT_REPOSITORIE = "Select Repositorie";
    public static final String C = "c:";
    public static final String NO_DIRECTORIES_CHOOSE = "No Directories choose!";
    public static final String OPEN_THE_SOURCE_FILE = " : Open the source file";
    public static final String C1 = "C:/";

    /**
     * getRepositoriesClients display a windows FileChooser for select a Excel File
     * @return File
     * @throws FileNotFoundException
     */
    public File getRepositoriesClients() throws FileNotFoundException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(SELECT_REPOSITORIE);
        File defaultDirectory = new File(C);
        chooser.setInitialDirectory(defaultDirectory);
        File file = chooser.showDialog(null);
        if (file == null){
            throw new FileNotFoundException(NO_DIRECTORIES_CHOOSE);
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

        fileChooser.setTitle(Home.getAppTitle() + OPEN_THE_SOURCE_FILE);

        File defaultDirectory = new File(C1);
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showOpenDialog(null);
        if (file == null){
            throw new FileNotFoundException(NO_DIRECTORIES_CHOOSE);
        }
        return file;
    }
}
