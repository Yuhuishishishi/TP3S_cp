package data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by yuhui on 8/5/2016.
 */
public class Reader {

    private String filePath;
    private JSONParser parser;

    public Reader(String filePaht) {
        this.filePath = filePaht;
        parser = new JSONParser();
    }

    public List<TestRequest> getTests() throws IOException, ParseException {
        JSONObject obj = (JSONObject) this.parser.parse(new FileReader(this.filePath));

        JSONArray testArr = (JSONArray) obj.get("tests");
        List<TestRequest> testList = new ArrayList<TestRequest>();
        for (Object aTestArr : testArr) {
            JSONObject testObj = (JSONObject) aTestArr;

            int tid = ((Long) testObj.get("test_id")).intValue();
            int prep = ((Long) testObj.get("prep")).intValue();
            int tat = ((Long) testObj.get("tat")).intValue();
            int analysis = ((Long) testObj.get("analysis")).intValue();

            int release = ((Long) testObj.get("release")).intValue();
            int deadline = ((Long) testObj.get("deadline")).intValue();

            TestRequest newTest = new TestRequest(tid, release,
                    prep, tat, analysis, deadline);
            testList.add(newTest);
        }

        System.out.println("# tests read in: " + testList.size());
        return testList;
    }

    public List<Vehicle> getVehicles() throws IOException, ParseException {
        JSONObject obj = (JSONObject) this.parser.parse(new FileReader(this.filePath));

        JSONArray vehicleArr = (JSONArray) obj.get("vehicles");
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();

        for (Object aVehicleArr : vehicleArr) {
            JSONObject vehicleObj = (JSONObject) aVehicleArr;

            int vid = ((Long) vehicleObj.get("vehicle_id")).intValue();
            int release = ((Long) vehicleObj.get("release")).intValue();

            Vehicle newVehicle = new Vehicle(vid, release);
            vehicleList.add(newVehicle);
        }

        return vehicleList;
    }

    public Map<Integer, Map<Integer, Boolean>> getRehitRules() throws IOException, ParseException {
        JSONObject obj = (JSONObject) this.parser.parse(new FileReader(this.filePath));
        JSONObject rehitObj = (JSONObject) obj.get("rehit");

        Map<Integer, Map<Integer, Boolean>> rehitMap = new HashMap<Integer, Map<Integer, Boolean>>();
        Set entrySet = rehitObj.entrySet();
        for (Object anEntrySet : entrySet) {
            Map.Entry entry = (Map.Entry) anEntrySet;
            int key1 = Integer.valueOf((String) entry.getKey());
            rehitMap.put(key1, new HashMap<Integer, Boolean>());
            JSONObject nestedObj = (JSONObject) entry.getValue();
            for (Object elem : nestedObj.entrySet()) {
                Map.Entry e2 = (Map.Entry) elem;
                int key2 = Integer.valueOf((String) e2.getKey());
                boolean bool = (Boolean) e2.getValue();
                rehitMap.get(key1).put(key2, bool);
            }
        }

        return rehitMap;
    }
}
