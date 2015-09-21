package BDD.Champs;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 07/08/2015.
 * OpenHelperParams manage BDD Params
 */
@SuppressWarnings("javadoc")
public class OpenHelperParams {
    private final static String DB_REPOSITORIES = "Settings/";
    private final static String DB_NAME = "ParamsBaseFlComparator";
    private final static String TABLE_PARAMS = "params";
    private final static String TABLE_ROW_KEY = "key";
    private static final String TABLE_ROW_CHAMPS = "champs";
    private final static String INDEX_CHAMPS = "index_champs";

    private static File dbFile;

    /***
     * getSqlJetDb
     * @return SqlJetDb
     * @throws SqlJetException
     */
    public static SqlJetDb getSqlJetDb() throws SqlJetException {
        dbFile = new File(DB_REPOSITORIES+DB_NAME);
        return SqlJetDb.open(dbFile, true);
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
    public void addRecordBase(SqlJetDb db, String key, String champs) throws SqlJetException {
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
    public HashMap<String, List<String>> getAllRecords(SqlJetDb db) throws SqlJetException {
        ISqlJetTable table = db.getTable(TABLE_PARAMS);
        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        HashMap<String, List<String>> found;
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
     * @return HashMap<String, List<String>>
     * @throws SqlJetException
     */
    private static HashMap<String, List<String>> printRecords(ISqlJetCursor cursor) throws SqlJetException {
        HashMap<String, List<String>> allRecords = new HashMap<>();
        try {
            if (!cursor.eof()) {
                do {
                    List<String> champsList = new ArrayList<>();
                    String[] arrayFound = cursor.getString(TABLE_ROW_CHAMPS).split(":");
                    Collections.addAll(champsList, arrayFound);
                    allRecords.put(cursor.getString(TABLE_ROW_KEY),champsList);
                } while(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return allRecords;
    }
}
