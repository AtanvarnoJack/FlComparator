package BDD.CheckedClient;

import Analytics.StockAll;
import View.Dialogs.Dialogs;
import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alco on 16/09/2015.
 */
public class GestionBDDCheckedClient {

    /**
     * initBase
     * @throws org.tmatesoft.sqljet.core.SqlJetException
     */
    public void initBase() throws SqlJetException {
        OpenHelperCheckedClient openHelperCheckedClient = new OpenHelperCheckedClient();
        openHelperCheckedClient.createNewBase();
        openHelperCheckedClient.createTables(OpenHelperCheckedClient.getSqlJetDb());
        if (StockAll.clientCheckedList != null){
            openHelperCheckedClient.createNewBase();
            openHelperCheckedClient.createTables(OpenHelperCheckedClient.getSqlJetDb());
            for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                openHelperCheckedClient.addRecordBase(OpenHelperCheckedClient.getSqlJetDb(), i, StockAll.clientCheckedList.get(i), "yes");
            }
        }else {
            StockAll stockAll = new StockAll();
            try {
                StockAll.clientCheckedList = stockAll.loadClientList();
            } catch (FileNotFoundException e) {
                Dialogs dialogs = new Dialogs();
                dialogs.dialogsNoRepositoriesWithFlClientFound();
            }
            for (int i = 0; i < StockAll.clientCheckedList.size(); i++) {
                openHelperCheckedClient.addRecordBase(OpenHelperCheckedClient.getSqlJetDb(), i, StockAll.clientCheckedList.get(i), "yes");
            }
        }
    }

    /***
     * getDataByKey
     * @param key
     * @return List<String>
     * @throws SqlJetException
     */
    public String getDataByKey(String key) throws SqlJetException {
        OpenHelperCheckedClient openHelperCheckedClient = new OpenHelperCheckedClient();
        SqlJetDb db = OpenHelperCheckedClient.getSqlJetDb();
        String recordsFound = openHelperCheckedClient.getRecords(db, key);
        return recordsFound;
    }

    /***
     * getAllData
     * @return HashMap<String, List<String>>
     * @throws SqlJetException
     */
    public List<String> getAllData() throws SqlJetException {
        OpenHelperCheckedClient openHelperCheckedClient = new OpenHelperCheckedClient();
        return openHelperCheckedClient.getAllRecords(OpenHelperCheckedClient.getSqlJetDb());
    }
}
