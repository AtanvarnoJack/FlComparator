package Excel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alco on 04/08/2015.
 * Interface ExcelLoader is for have a possibility to change reader (actually Apache POI).
 */
public interface ExcelLoader {
    /**
     * getAllCompareValueFl
     * @param file
     * @return HashMap<String, List<String>>
     * @throws IOException
     */
    HashMap<String, List<String>> getAllCompareValueFl(File file) throws IOException;
}
