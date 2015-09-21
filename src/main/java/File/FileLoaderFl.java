package File;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 04/08/2015.
 * FileLoaderFl contain all method for load a file
 */
@SuppressWarnings("javadoc")
public class FileLoaderFl {
    private final static String START_FL_NAME = "Fichier Liaison V4";
    private final static String EXTENSION = ".xlsm";
    public static final String YEARS = "YEARS";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";
    public static final String SECONDS = "SECONDS";

    public int getNumberOfClient(File pathClients){
        return pathClients.listFiles().length;
    }

    /**
     * getAllClientFileList
     * @param pathClients
     * @return List<File>
     * @throws FileNotFoundException
     */
    public List<File> getAllClientFileList(File pathClients) throws FileNotFoundException {
        List<File> listClientFile = new ArrayList<>();
        File[] listClientFileArray = pathClients.listFiles();
        if (listClientFileArray != null){
            for (File file : listClientFileArray) {
                if (file.isDirectory()){
                    listClientFile.add(file);
                }
            }
        }
        return listClientFile;
    }

    /***
     * getClientFileList
     * @param pathClients
     * @param excludeClientNameList
     * @return List<File>
     * @throws FileNotFoundException
     */
    public List<File> getClientFileList(File pathClients, List<String> excludeClientNameList) throws FileNotFoundException {
        List<File> listClientFile = new ArrayList<>();
        File[] listClientFileArray = pathClients.listFiles();
        for (File file : listClientFileArray) {
            if (file.isDirectory()){
                boolean excluded = false;
                for (String excludeName : excludeClientNameList) {
                    if (file.getName().equals(excludeName)){
                        excluded = true;
                    }
                }
                if (!excluded){
                    listClientFile.add(file);
                }
            }
        }
        return listClientFile;
    }
    /**
     * getAllClientName
     * @param clientFileList
     * @return List<String>
     * @throws IllegalArgumentException
     */
    public List<String> getAllClientName(List<File> clientFileList) throws IllegalArgumentException {
        List<String> listClient = new ArrayList<>();
        for (File client : clientFileList) {
            if(client.isDirectory()){
                listClient.add(client.getName());
            }
        }
        return listClient;
    }

    /**
     * getProdFlClient
     * @param client
     * @param clientName
     * @return File
     */
    public File getProdFlClient(File client, String clientName) {
        File file = null;
        File[] flFound = client.listFiles();
        if (flFound != null){
            for (File fl : flFound) {
                if (fl.isFile()){
                    String[] splitFound = fl.getName().split("_");
                    if(splitFound.length == 2) { //Fichier Liaison V4_ADEO.xlsm
                        if (splitFound[0].equals(START_FL_NAME) && splitFound[1].equals(clientName + EXTENSION)) {
                            file = fl;
                        }
                    }
                }
            }
        }
        return file;
    }

    /**
     * getArchFlClient
     * @param client
     * @return File
     */
    public File getArchFlClient(File client) {
        HashMap<String,Integer> paramsDateOld = new HashMap<>();
        HashMap<String,Integer> paramsDateNew = new HashMap<>();

        //Fichier Liaison V4_ADEO_2014_8_18-18h11m23s.xlsm
        File file = null;
        File[] flFound = client.listFiles();
        if (flFound != null){
            for (File fl : flFound) {
                if (file == null){
                    try {
                        String[] splitFound = fl.getName().split("_");
                        if(splitFound.length == 5) {
                            paramsDateOld.put(YEARS,Integer.parseInt(splitFound[2]));
                            paramsDateOld.put(MONTH,Integer.parseInt(splitFound[3]));
                            String[] splitDay = splitFound[4].split("-");
                            if (splitDay.length == 2){
                                paramsDateOld.put(DAY,Integer.parseInt(splitDay[0]));
                                String[] splitHours = splitDay[1].split("h");
                                paramsDateOld.put(HOUR,Integer.parseInt(splitHours[0]));
                                String[] splitMinute = splitHours[1].split("m");
                                paramsDateOld.put(MINUTE,Integer.parseInt(splitMinute[0]));
                                String[] splitSeconds = splitMinute[1].split("s");
                                paramsDateOld.put(SECONDS,Integer.parseInt(splitSeconds[0]));
                                file = fl;
                            }
                        }
                    }catch (Exception ignored){}

                }else {
                    try {
                        String[] splitFound = fl.getName().split("_");
                        paramsDateNew.put(YEARS,Integer.parseInt(splitFound[2]));
                        paramsDateNew.put(MONTH,Integer.parseInt(splitFound[3]));
                        String[] splitDay = splitFound[4].split("-");
                        paramsDateNew.put(DAY,Integer.parseInt(splitDay[0]));
                        String[] splitHours = splitDay[1].split("h");
                        paramsDateNew.put(HOUR,Integer.parseInt(splitHours[0]));
                        String[] splitMinute = splitHours[1].split("m");
                        paramsDateNew.put(MINUTE,Integer.parseInt(splitMinute[0]));
                        String[] splitSeconds = splitMinute[1].split("s");
                        paramsDateNew.put(SECONDS,Integer.parseInt(splitSeconds[0]));

                        if(paramsDateOld.get(YEARS) > paramsDateNew.get(YEARS)){
                            file = fl;
                        }else if(paramsDateOld.get(MONTH) > paramsDateNew.get(MONTH)){
                            file = fl;
                        }else if(paramsDateOld.get(DAY) > paramsDateNew.get(DAY)){
                            file = fl;
                        }else if(paramsDateOld.get(HOUR) > paramsDateNew.get(HOUR)){
                            file = fl;
                        }else if(paramsDateOld.get(MINUTE) > paramsDateNew.get(MINUTE)){
                            file = fl;
                        }else if(paramsDateOld.get(SECONDS) > paramsDateNew.get(SECONDS)){
                            file = fl;
                        }else{
                            file = fl;
                        }
                    }catch (Exception ignored){}
                }
            }
        }
        return file;
    }
}
