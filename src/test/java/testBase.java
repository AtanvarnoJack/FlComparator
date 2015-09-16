import Analytics.StockAll;
import BDD.Champs.GestionBDDParams;
import org.testng.annotations.Test;
import org.tmatesoft.sqljet.core.SqlJetException;

import java.util.HashMap;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by alco on 07/08/2015.
 */
public class testBase {

    @Test
    public void testBase(){
        GestionBDDParams gestionBDDParams = new GestionBDDParams();
        StockAll stockAll = new StockAll();
        StockAll.listChampCompare = stockAll.loadConstChamps();
        try {
            gestionBDDParams.initBase();
            HashMap<String, List<String>> found = gestionBDDParams.getAllData();
            List<String> found2 = gestionBDDParams.getDataByKey("PRINCIPAL");
            System.out.println();
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
    }
}
