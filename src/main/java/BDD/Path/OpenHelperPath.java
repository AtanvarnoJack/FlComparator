package BDD.Path;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import java.io.File;

/**
 * Created by alco on 07/08/2015.
 * OpenHelperPath manage BDD path
 */
public class OpenHelperPath {
    private final static String DB_REPOSITORIES = "Settings/";
    private final static String DB_NAME = "ParamsPathFlRef";
    private final static String TABLE_PARAMS = "params";
    private final static String TABLE_ROW_PATH = "path";
    private final static String INDEX_CHAMPS = "index_champs";

    private static File dbFile;

    /**
     * getSqlJetDb
     * @return SqlJetDb
     * @throws SqlJetException
     */
    public static SqlJetDb getSqlJetDb() throws SqlJetException {
        dbFile = new File(DB_REPOSITORIES+DB_NAME);
        SqlJetDb db = SqlJetDb.open(dbFile, true);
        return db;
    }

    /**
     * createNewBase
     * @return SqlJetDb
     * @throws SqlJetException
     */
    public SqlJetDb createNewBase() throws SqlJetException {
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
        return db;
    }

    /**
     * createTables
     * @param db
     * @throws SqlJetException
     */
    public void createTables (SqlJetDb db) throws SqlJetException {
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            db.createTable("CREATE TABLE "+TABLE_PARAMS+" ("+ TABLE_ROW_PATH +" TEXT NOT NULL PRIMARY KEY)\n");
            db.createIndex("CREATE INDEX " + INDEX_CHAMPS + " ON " + TABLE_PARAMS + "(" + TABLE_ROW_PATH +")\n");
        } finally {
            db.commit();
            db.close();
        }
    }

    /**
     * addRecordBase
     * @param db
     * @param champs
     * @throws SqlJetException
     */
    public void addRecordBase(SqlJetDb db, String champs) throws SqlJetException {
        db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
            ISqlJetTable table = db.getTable(TABLE_PARAMS);
            table.insert(champs);
        } finally {
            db.commit();
            db.close();
        }
    }

    /**
     * getRecords
     * @param db
     * @return
     * @throws SqlJetException
     */
    public String getRecords(SqlJetDb db) throws SqlJetException {
        ISqlJetTable table = db.getTable(TABLE_PARAMS);
        db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
        String found;
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
     * @return String
     * @throws SqlJetException
     */
    private String printRecords(ISqlJetCursor cursor) throws SqlJetException {
        String record = null;
        try {
            if (!cursor.eof()) {
                do {
                    record = cursor.getString(TABLE_ROW_PATH);
                } while(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return record;
    }
}
