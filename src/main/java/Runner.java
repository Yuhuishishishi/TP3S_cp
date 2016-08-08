import Algorithm.ConstraintProgramming;
import data.DataInstance;
import data.Reader;
import data.TestRequest;
import data.Vehicle;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yuhui on 8/8/2016.
 */
public class Runner {

    public static void main(String[] args) {
        String filepath = "C:\\Users\\yuhui\\Desktop\\TP3S_cp\\data\\157.tp3s";

        Reader jsonReader = new Reader(filepath);
        List<TestRequest> testArr;
        List<Vehicle> vehicleArr;
        Map<Integer, Map<Integer, Boolean>> rehitMap;
        // read in and init data instance
        try {
            testArr = jsonReader.getTests();
            vehicleArr = jsonReader.getVehicles();
            rehitMap = jsonReader.getRehitRules();

            DataInstance.init(testArr, vehicleArr, rehitMap);

            ConstraintProgramming cp = new ConstraintProgramming();
            cp.solve();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }
}
