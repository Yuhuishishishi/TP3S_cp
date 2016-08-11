package Algorithm;


import data.DataInstance;
import data.TestRequest;
import data.Vehicle;
import ilog.cp.*;
import ilog.concert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhui on 8/8/2016.
 */
public class ConstraintProgramming {

    private IloIntervalVar[][] intervalVars;
    private IloIntervalVar[] intervalVarsAux;
    private IloIntervalVar[] prepVars;
    private IloIntervalVar[] tatVars;
    private IloIntervalVar[] analysisVars;

    public void solve() {

        IloCP model;

        try {
            model = buildModel();

            if (model.solve()) {
                printSol(model);
            } else {
                System.out.println("Model infeasible");
            }
        } catch (IloException e) {
            e.printStackTrace();
        }

    }

    private void printSol(IloCP model) throws IloException {
        List<TestRequest> testArr = DataInstance.getInstance().getTestArr();
        List<Vehicle> vehicleArr = DataInstance.getInstance().getVehicleArr();

        final int numTest = testArr.size();
        final int numVehicle = vehicleArr.size();

        // which vehicle a test is assigned to
        for (int i = 0; i < numTest; i++) {
            for (int v = 0; v < numVehicle; v++) {
                if (model.isPresent(intervalVars[i][v])) {
                    System.out.println("Test " + testArr.get(i).getTid() + "" +
                            " assigned to " + vehicleArr.get(v).getVid() + "" +
                            "\t start: " + model.getStart(intervalVars[i][v]) + "" +
                            " end: " + model.getEnd(intervalVars[i][v]) + "" +
                            " dur: " + testArr.get(i).getDur());

                }


            }
            System.out.println("Test " + testArr.get(i).getTid() + "" +
                    model.getDomain(intervalVarsAux[i]));
            System.out.println("Test prep" + testArr.get(i).getTid() + "" +
                    model.getDomain(prepVars[i]));
            System.out.println("Test tat" + testArr.get(i).getTid() + "" +
                    model.getDomain(tatVars[i]));
            System.out.println("Test analysis" + testArr.get(i).getTid() + "" +
                    model.getDomain(analysisVars[i]));
        }

        System.out.println("==========================================");
        for (int v = 0; v < numVehicle; v++) {
            List<Integer> assignedTests = new ArrayList<Integer>();
            for (int i = 0; i < numTest; i++) {

                if (model.isPresent(intervalVars[i][v])) {
                    assignedTests.add(testArr.get(i).getTid());
                }
            }
            StringBuilder bd = new StringBuilder("Vehicle " + vehicleArr.get(v).getVid() + ": ");
            for (int id : assignedTests) {
                bd.append(id).append(",");
            }
            System.out.println(bd.toString());
        }
    }

    private IloCP buildModel() throws IloException {
        // build the constraint programming formulation
        IloCP model = new IloCP();

        List<TestRequest> testArr = DataInstance.getInstance().getTestArr();
        List<Vehicle> vehicleArr = DataInstance.getInstance().getVehicleArr();

        final int numTest = testArr.size();
        final int numVehicle = vehicleArr.size();

        final int deadlineSlack = 20;
        final int cbbCap = 5;
        final int roushCap = 30;

        // decision variables
        intervalVars = new IloIntervalVar[numTest][numVehicle];
        intervalVarsAux = new IloIntervalVar[numTest];
        prepVars = new IloIntervalVar[numTest];
        tatVars = new IloIntervalVar[numTest];
        analysisVars = new IloIntervalVar[numTest];


        // initialization of interval variables
        for (int i = 0; i < numTest; i++) {
            TestRequest test = testArr.get(i);
            intervalVarsAux[i] = model.intervalVar();
            prepVars[i] = model.intervalVar(test.getPrep(), "test " +  test.getTid() + " prep");
            tatVars[i] = model.intervalVar(test.getTat(), "test " + test.getTid() + " tat");
            analysisVars[i] = model.intervalVar(test.getAnalysis(), "test " + test.getTid() + " analysis");

            for (int v = 0; v < numVehicle; v++) {
                Vehicle vehicle = vehicleArr.get(v);
                intervalVars[i][v] = model.intervalVar(test.getDur(), "test " + test.getTid() + " on vehicle " + vehicle.getVid() );
                intervalVars[i][v].setOptional();
            }
        }

        // constraints

        // relations between intervals
        for (int i = 0; i < numTest; i++) {
            // prep before tat, tat before analysis
            model.add(model.endAtStart(prepVars[i], tatVars[i]));
            model.add(model.endAtStart(tatVars[i], analysisVars[i]));

            // span constraints
//            model.add(model.span(intervalVarsAux[i],
//                    new IloIntervalVar[]{prepVars[i], tatVars[i], analysisVars[i]}));
            model.add(model.startAtStart(prepVars[i], intervalVarsAux[i]));

        }

        // time window constraints
        for (int i = 0; i < numTest; i++) {
            TestRequest test = testArr.get(i);

            tatVars[i].setStartMin(test.getRelease());
//            intervalVars[i][v].setEndMax(test.getDeadline() + deadlineSlack);
//            intervalVarsAux[i].setStartMin(test.getRelease());
            intervalVarsAux[i].setEndMax(test.getDeadline() + deadlineSlack);
        }

        // vehicle release time
        for (int v = 0; v < numVehicle; v++) {
            Vehicle vehicle = vehicleArr.get(v);
            for (int i = 0; i < numTest; i++) {
                intervalVars[i][v].setStartMin(vehicle.getRelease());
            }
        }

        // test on one vehicle
        for (int i = 0; i < numTest; i++) {
            model.add(model.alternative(intervalVarsAux[i], intervalVars[i]));
        }

        // no overlap on the same vehicle
        for (int v = 0; v < numVehicle; v++) {
            IloIntervalVar[] tmpArr = new IloIntervalVar[numTest];
            for (int i = 0; i < numTest; i++) {
                tmpArr[i] = intervalVars[i][v];
            }
            model.add(model.noOverlap(tmpArr));
        }


        // compatibility of tests
        for (int i = 0; i < numTest; i++) {
            TestRequest test1 = testArr.get(i);
            for (int j = 0; j < i; j++) {
                TestRequest test2 = testArr.get(j);

                // if not compatible, add a constraints
                boolean isTest1AllowBeforeTest2 = DataInstance.getInstance().getRelation(test1.getTid(),
                        test2.getTid());
                boolean isTest2AllowBeforeTest1 = DataInstance.getInstance().getRelation(test2.getTid(),
                        test1.getTid());

                for (int v = 0; v < numVehicle; v++) {
                    if (!isTest1AllowBeforeTest2) {
                        model.add(model.endBeforeStart(intervalVars[j][v],
                                intervalVars[i][v]));
                    }

                    if (!isTest2AllowBeforeTest1) {
                        model.add(model.endBeforeStart(intervalVars[i][v],
                                intervalVars[j][v]));
                    }
                }
            }
        }

        IloCumulFunctionExpr cbbUsage = model.cumulFunctionExpr();
        IloCumulFunctionExpr roushUsage = model.cumulFunctionExpr();

        for (int i = 0; i < numTest; i++) {

                cbbUsage = model.sum(cbbUsage,
                        model.pulse(tatVars[i], 1));
                roushUsage = model.sum(roushUsage,
                        model.pulse(prepVars[i], 1));

        }

        // global resource constraints
        model.add(model.le(cbbUsage, cbbCap));
        model.add(model.le(roushUsage, roushCap));


        return model;
    }
}
