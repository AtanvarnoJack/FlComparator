package BDD.CheckedClient;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alco on 16/09/2015.
 */
public class OpenHelperCheckedClient {
    private final static String DB_REPOSITORIES = "Settings/";
    private final static String DB_NAME = "ParamsBaseCheckedClient";
    private final static String TABLE_PARAMS = "params";
    private final static String TABLE_ROW_KEY = "key";
    private static final String TABLE_ROW_CHAMPS = "champs";
    private final static String INDEX_CHAMPS = "index_champs";

    private static File dbFile;

    /***
     * getSqlJetDb
     * @return SqlJetDb
     * @throws org.tmatesoft.sqljet.core.SqlJetException
     */
    public static SqlJetDb getSqlJetDb() throws SqlJetException {
        dbFile = new File(DB_REPOSITORIES+DB_NAME);
        SqlJetDb db = SqlJetDb.open(dbFile, true);
        return db;
    }

    /**
     * createNewBase
     * @throws SqlJetException
     */
    public void createNewBase() throws SqlJetException {
        dbFile = new File(DB_REPOSITORIES+DB_NAME);
        dbFile.delete();

        SqlJetDb db = SqlJetDb.open(dbFile, true);
        db.getOptions().setAutovacuum(true);
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            db.getOptions().setUserVersion(1);
        } finally {
            db.commit();
            db.close();
        }
    }

    /**
     * createTables
     * @param db
     * @throws SqlJetException
     */
    public void createTables (SqlJetDb db) throws SqlJetException {
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            db.createTable("CREATE TABLE "+TABLE_PARAMS+" ("+TABLE_ROW_KEY+" TEXT NOT NULL PRIMARY KEY, "+TABLE_ROW_CHAMPS+" TEXT NOT NULL)\n");
            db.createIndex("CREATE INDEX " + INDEX_CHAMPS + " ON " + TABLE_PARAMS + "(" + TABLE_ROW_KEY + "," + TABLE_ROW_CHAMPS + ")\n");
        } finally {
            db.commit();
            db.close();
        }
    }

    /**
     * addRecordBase
     * @param db
     * @param key
     * @param champs
     * @throws SqlJetException
     */
    public void addRecordBase(SqlJetDb db, int key, String champs) throws SqlJetException {
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            ISqlJetTable table = db.getTable(TABLE_PARAMS);
            table.insert(key, champs);
        } finally {
            db.commit();
            db.close();
        }
    }

    /**
     * getRecords
     * @param db
     * @param key
     * @return String
     * @throws SqlJetException
     */
    public String getRecords(SqlJetDb db, String key) throws SqlJetException {
        ISqlJetTable table = db.getTable(TABLE_PARAMS);
        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        String found;
        try {
            found = printRecords(table.open(), key);
        } finally {
            db.commit();
            db.close();
        }
        return found;
    }

    /**
     * getAllRecords
     * @param db
     * @return HashMap<String, List<String>>
     * @throws SqlJetException
     */
    public List<String> getAllRecords(SqlJetDb db) throws SqlJetException {
        ISqlJetTable table = db.getTable(TABLE_PARAMS);
        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        List<String> found;
        try {
            found = printRecords(table.open());
        } finally {
            db.commit();
            db.close();
        }
        return found;
    }

    /**
     * printRecords
     * @param cursor
     * @param key
     * @return String
     * @throws SqlJetException
     */
    private String printRecords(ISqlJetCursor cursor, String key) throws SqlJetException {
        String record = null;
        try {
            if (!cursor.eof()) {
                do {
                    if (cursor.getString(TABLE_ROW_KEY).equals(key)){
                        record = cursor.getString(TABLE_ROW_CHAMPS);
                    }
                } while(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return record;
    }

    /**
     * printRecords
     * @param cursor
     * @return List<String>
     * @throws SqlJetException
     */
    private static List<String> printRecords(ISqlJetCursor cursor) throws SqlJetException {
        List<String> allRecords = new ArrayList<>();
        try {
            if (!cursor.eof()) {
                do {
                    allRecords.add(cursor.getString(TABLE_ROW_CHAMPS));
                    java.util.Collections.sort(allRecords);
                } while (cursor.next());
            }
        } finally {
            cursor.close();
        }
        return allRecords;
    }
}
