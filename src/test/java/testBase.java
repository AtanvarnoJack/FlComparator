import Analytics.StockAll;
import BDD.Champs.GestionBDParams;
import File.FileLoaderFl;
import org.testng.annotations.Test;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by alco on 07/08/2015.
 */
public class testBase {

    @Test
    public void testBase(){
        GestionBDParams gestionBDParams = new GestionBDParams();
        StockAll stockAll = new StockAll();
        StockAll.listChampCompare = stockAll.loadConstChamps();
        try {
            gestionBDParams.initBase();
            HashMap<String, List<String>> found = gestionBDParams.getAllData();
            List<String> found2 = gestionBDParams.getDataByKey("PRINCIPAL");
            System.out.println();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
    }
}
