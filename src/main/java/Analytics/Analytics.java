package Analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.*;

/**
 * Created by alco on 04/08/2015.
 * Analytics class contain all compare method.
 */
@SuppressWarnings("javadoc")
public class Analytics {

    /***
     * compareTwoHashMapContentFlEquipment
     * compare content of two hashMap without (space or UpperLetter and more or less case).
     * @param newFl
     * @param oldFl
     * @return HashMap<String, List<String>>
     */
    public boolean compareTwoHashMapContentFlEquipment(HashMap<String, List<String>> newFl, HashMap<String, List<String>> oldFl){
        boolean equipmentEquals = true;
        String tag = StockAll.getTag();
        List<String> tagFirstList = newFl.get(tag);
        List<String> tagSecondList = oldFl.get(tag);
        String hostnameFirst;

        for (int i = 0; i < tagFirstList.size(); i++) {
            boolean secondNotEmpty = true;
            List<String> equipmentFirst;
            List<String> equipmentSecond;
            int rangEquipmentSecond = -1;

            hostnameFirst = getHostname(tagFirstList, i);
            equipmentFirst = getEquipmentByInt(newFl, i);
            rangEquipmentSecond = getRangOfEquipmentByHostname(tagSecondList, hostnameFirst, rangEquipmentSecond);
            equipmentSecond = getEquipmentByHostname(oldFl,rangEquipmentSecond);

            for (String value : equipmentSecond) {
                if (!value.equals("")){
                    secondNotEmpty = false;
                }
            }

            if(!equipmentFirst.equals(equipmentSecond) && !secondNotEmpty){
                equipmentEquals = false;
            }
        }

        return equipmentEquals;
    }

    /***
     * getRangOfEquipmentByHostname
     * @param tagSecondList
     * @param hostnameFirst
     * @param rangEquipmentSecond
     * @return int
     */
    private int getRangOfEquipmentByHostname(List<String> tagSecondList, String hostnameFirst, int rangEquipmentSecond) {
        for (int j = 0; j < tagSecondList.size(); j++) {
            if(tagSecondList.get(j).equals(hostnameFirst)){
                rangEquipmentSecond = j;
            }
        }
        return rangEquipmentSecond;
    }

    /***
     * getHostname
     * @param tagFirstList
     * @param pos
     * @return String
     */
    private String getHostname( List<String> tagFirstList, int pos) {
        String hostnameFirst;
        hostnameFirst = tagFirstList.get(pos);
        return hostnameFirst;
    }

    /***
     * getEquipmentByInt
     * @param newFl
     * @param pos
     * @return List<String>
     */
    private  List<String> getEquipmentByInt(HashMap<String, List<String>> newFl, int pos) {
        List<String> equipmentFirst = new ArrayList<>();
        for (Entry<String, List<String>> entry : newFl.entrySet()) {
            List<String> firstList = entry.getValue();
            equipmentFirst.add(firstList.get(pos));
        }
        return equipmentFirst;
    }

    /****
     * getEquipmentByHostname
     * @param oldFl
     * @param rangEquipmentSecond
     * @return List<String>
     */
    private List<String> getEquipmentByHostname(HashMap<String, List<String>> oldFl, int rangEquipmentSecond) {
        List<String> equipmentSecond = new ArrayList<>();
        if (rangEquipmentSecond > -1){
            for(Entry<String, List<String>> entry : oldFl.entrySet()) {
                List<String> secondList = entry.getValue();
                equipmentSecond.add(secondList.get(rangEquipmentSecond));
            }
        }
        return equipmentSecond;
    }
}
