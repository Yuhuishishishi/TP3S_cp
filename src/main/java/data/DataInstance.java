package data;

import java.util.List;
import java.util.Map;

/**
 * Created by yuhui on 8/5/2016.
 */
public class DataInstance {

    private static DataInstance instance = null;
    private List<TestRequest> testArr;
    private List<Vehicle> vehicleArr;
    private Map<Integer, Map<Integer, Boolean>> rehitRules;

    private DataInstance () {

    }

    public static void init(List<TestRequest> tests, List<Vehicle> vehicles,
                            Map<Integer, Map<Integer, Boolean>> rehitRules) {

        instance = new DataInstance();
        instance.testArr = tests;
        instance.vehicleArr = vehicles;
        instance.rehitRules = rehitRules;
    }

    public static DataInstance getInstance() {
        if (DataInstance.instance == null) {
            DataInstance.instance = new DataInstance();
        }

        return DataInstance.instance;
    }

    public boolean getRelation(int tid1, int tid2) throws IllegalArgumentException{
        if (!rehitRules.containsKey(tid1)) {
            throw new IllegalArgumentException(tid1 + " is not a valid tid.");
        }

        Map<Integer, Boolean> nested = getInstance().rehitRules.get(tid1);
        if (!nested.containsKey(tid2)) {
            throw new IllegalArgumentException(tid1 + ", " + tid2 + " is not a valid tid pair.");
        }

        return getInstance().rehitRules.get(tid1).get(tid2);

    }


    public List<TestRequest> getTestArr() {
        return testArr;
    }

    public List<Vehicle> getVehicleArr() {
        return vehicleArr;
    }

    public Map<Integer, Map<Integer, Boolean>> getRehitRules() {
        return rehitRules;
    }
}
