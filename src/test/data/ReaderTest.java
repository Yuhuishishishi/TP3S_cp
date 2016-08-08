package data;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by yuhui on 8/8/2016.
 */
public class ReaderTest {

    private String filepath = "C:\\Users\\yuhui\\Desktop\\TP3S_cp\\data\\157.tp3s";

    @Test
    public void testGetTests() throws Exception {
        Reader jsonReader = new Reader(filepath);
        List<TestRequest> testArr = jsonReader.getTests();
        int testArrSize = testArr.size();
        assertEquals(testArrSize, 64);
    }

    @Test
    public void testGetVehicles() throws Exception {
        Reader jsonReader = new Reader(filepath);
        List<Vehicle> vehicleArr = jsonReader.getVehicles();
        int vehicleArrSize = vehicleArr.size();
        assertEquals(vehicleArrSize, 60);
    }

    @Test
    public void testGetRehitRules() throws Exception {
        Reader jsonReader = new Reader(filepath);
        Map<Integer, Map<Integer, Boolean>> rehitMap = jsonReader.getRehitRules();
        assertEquals(rehitMap.size(), 64);
        for (Map.Entry<Integer, Map<Integer, Boolean>> elem : rehitMap.entrySet()) {
            Map<Integer, Boolean> nested = elem.getValue();
            assertEquals(nested.size(), 64);
        }
    }
}