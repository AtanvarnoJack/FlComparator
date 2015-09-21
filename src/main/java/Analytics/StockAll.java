package Analytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import BDD.CheckedClient.OpenHelperCheckedClient;
import File.FileChooserFl;
import View.Dialogs.Dialogs;

/**
 * Created by alco on 05/08/2015.
 * StockAll is a static class for data with her method
 */
public class StockAll {
    private final static String TAG = "Hostname";
    private final static String PRINCIPAL = "PRINCIPAL";
    public static String allRequestError = null;
    public static File repositoryAllClient = null;
    public static File filePathFlRef = null;
    public static HashMap<String, List<String>> listChampCompare = null;
    public static List<String> clientCheckedList = new ArrayList<>();

    /***
     * loadConstChamps
     * @return HashMap<String, List<String>>
     */
    public HashMap<String, List<String>> loadConstChamps() {
        HashMap<String, List<String>> listConstChamp = new HashMap<String, List<String>>();
        if (this.listChampCompare == null) {
            List<String> constChampsList = getListConst();
            listConstChamp.put(PRINCIPAL, constChampsList);
            return listConstChamp;
        } else {
            return this.listChampCompare;
        }
    }

    /***
     * getListConst()
     * @return List<String>
     */
    private List<String> getListConst() {
        List<String> constChampsList = new ArrayList<String>();
        constChampsList.add(TAG);
        constChampsList.add("Organization_Outils Level 1_1");
        constChampsList.add("Organization_Outils Level 1_2");
        constChampsList.add("Organization_Outils Level 1_3");
        constChampsList.add("Organization_Outils Level 1_4");
        constChampsList.add("Organization_Outils Level 2_1");
        constChampsList.add("Organization_Outils Level 2_2");
        constChampsList.add("Organization_Outils Level 2_3");
        constChampsList.add("Organization_Outils Level 2_4");
        constChampsList.add("Product Categorization Tier 1");
        constChampsList.add("Product Categorization Tier 2");
        return constChampsList;
    }

    public static String getTag() {
        return TAG;
    }

    /***
     * addListChampCompare
     * @param key
     * @param value
     */
    public static void addListChampCompare(String key, String value) {
        HashMap<String, List<String>> listChampCompareNew = new HashMap<String, List<String>>();
        boolean existInList = false;
        for (Entry<String, List<String>> entry : listChampCompare.entrySet()) {
            if(entry.getKey().toString().equals(key)){
                existInList = true;
                boolean alreadyInList = false;
                List<String> listFound = entry.getValue();
                for (String found : listFound) {
                    if (found.equals(value)){
                        alreadyInList = true;
                    }
                }
                if (!alreadyInList) {
                    listFound.add(value);
                }
                listChampCompareNew.put(entry.getKey(), listFound);
            }else {
                listChampCompareNew.put(entry.getKey(), entry.getValue());
            }
        }
        if (!existInList){
            List<String> newList = new ArrayList<>();
            newList.add(value);
            listChampCompare.put(key,newList);
        }else {
            listChampCompare = listChampCompareNew;
        }
    }

    /***
     * deleteOneOfListChampCompare
     * @param key
     * @param value
     */
    public static void deleteOneOfListChampCompare(String key, String value) {
        HashMap<String, List<String>> listChampCompareNew = new HashMap<String, List<String>>();
        for (Entry<String, List<String>> entry : listChampCompare.entrySet()) {
            if(entry.getKey().toString().equals(key)){
                List<String> listNew = new ArrayList<>();
                List<String> listFound = entry.getValue();
                for (String champ : listFound) {
                    if (!champ.equals(value)){
                        listNew.add(champ);
                    }
                }
                listChampCompareNew.put(entry.getKey(), listNew);
            }else {
                listChampCompareNew.put(entry.getKey(),entry.getValue());
            }
        }
        listChampCompare.clear();
        listChampCompare = listChampCompareNew;
    }

    public List<String> loadClientList() throws FileNotFoundException {
        List<String> clientList = new ArrayList<>();
        FileChooserFl fileChooserFl = new FileChooserFl();
        if (repositoryAllClient == null){
            Dialogs dialogs = new Dialogs();
            dialogs.dialogsSelectARepository();
            repositoryAllClient = fileChooserFl.getRepositoriesClients();
        }
        for (File file : repositoryAllClient.listFiles()) {
            if (file.isDirectory()) {
                clientList.add(file.getName());
            }
        }
        return clientList;
    }

    public List<String> getYesClientCheckedList(){
        List<String> yesClient = new ArrayList<>();
        for (String client : clientCheckedList) {
            String[] clientSplit = client.split(OpenHelperCheckedClient.getSeparator());
            if (clientSplit[1].equals( OpenHelperCheckedClient.getYes())){
                yesClient.add(clientSplit[0]);
            }
        }
        return yesClient;
    }

    public List<String> getNoClientCheckedList(){
        List<String> noClient = new ArrayList<>();
        for (String client : clientCheckedList) {
            String[] clientSplit = client.split(OpenHelperCheckedClient.getSeparator());
            if (clientSplit[1].equals( OpenHelperCheckedClient.getNo())){
                noClient.add(clientSplit[0]);
            }
        }
        return noClient;
    }
}
