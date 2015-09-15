package BDD.Champs;

import Analytics.StockAll;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by alco on 07/08/2015.
 * GestionBDParams contain method for manage OpenHelper BDD Params
 */
public class GestionBDParams {

    /**
     * initBase
     * @throws SqlJetException
     */
    public void initBase() throws SqlJetException {
        OpenHelperParams openHelperParams = new OpenHelperParams();
        openHelperParams.createNewBase();
        openHelperParams.createTables(OpenHelperParams.getSqlJetDb());
        if (StockAll.listChampCompare != null){
            openHelperParams.createNewBase();
            openHelperParams.createTables(OpenHelperParams.getSqlJetDb());
            for (Entry<String, List<String>> entry : StockAll.listChampCompare.entrySet()){
                openHelperParams.addRecordBase(OpenHelperParams.getSqlJetDb(),entry.getKey(),concatList(entry.getValue()));
            }
        }else {
            StockAll stockAll = new StockAll();
            StockAll.listChampCompare = stockAll.loadConstChamps();
            for (Entry<String, List<String>> entry :  StockAll.listChampCompare.entrySet()){
                openHelperParams.addRecordBase(OpenHelperParams.getSqlJetDb(),entry.getKey(),concatList(entry.getValue()));
            }
        }
    }

    /***
     * getDataByKey
     * @param key
     * @return List<String>
     * @throws SqlJetException
     */
    public List<String> getDataByKey(String key) throws SqlJetException {
        OpenHelperParams openHelperParams = new OpenHelperParams();
        SqlJetDb db = OpenHelperParams.getSqlJetDb();
        String recordsFound = openHelperParams.getRecords(db, key);
        return undoConcatChamps(recordsFound);
    }

    /***
     * getAllData
     * @return HashMap<String, List<String>>
     * @throws SqlJetException
     */
    public HashMap<String, List<String>> getAllData() throws SqlJetException {
        OpenHelperParams openHelperParams = new OpenHelperParams();
        return openHelperParams.getAllRecords(OpenHelperParams.getSqlJetDb());
    }

    /***
     * concatList
     * @param champList
     * @return String
     */
    private String concatList(List<String> champList){
        String concat = null;
        for (String champ : champList) {
            if (concat == null){
                concat = champ;
            }else {
                concat = concat + ":" + champ;
            }
        }
        return concat;
    }

    /***
     * concatChamps
     * @param champs
     * @param key
     * @return String
     */
    private String concatChamps(HashMap<String, List<String>> champs, String key){
        String concat = null;
        for (Entry<String, List<String>> entry : champs.entrySet()) {
            if (entry.getKey().equals(key)){
                List<String> listChamps = entry.getValue();
                for (String champ : listChamps) {
                    if (concat == null){
                        concat = champ;
                    }else {
                        concat = concat + ":" + champ;
                    }
                }
            }
        }
        return concat;
    }

    /***
     * undoConcatChamps
     * @param concatChamps
     * @return List<String>
     */
    private List<String> undoConcatChamps(String concatChamps){
        List<String> champsList = new ArrayList<>();
        String[] arrayFound = concatChamps.split(":");
        for (String champ : arrayFound) {
            champsList.add(champ);
        }
        return champsList;
    }
}
