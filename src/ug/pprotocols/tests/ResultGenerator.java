package ug.pprotocols.tests;

import ug.pprotocols.Type;
import ug.pprotocols.algorithm.Mcarlo;
import ug.pprotocols.datatypes.DataType;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.datatypes.MatrixCompatibleFactory;
import ug.pprotocols.matrix.Case;
import ug.pprotocols.matrix.Equation;
import ug.pprotocols.matrix.MatrixGenerator;
import ug.pprotocols.operations.DataOperation;
import ug.pprotocols.operations.DoubleOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultGenerator {

    private Map<Integer,Integer> testScope;
    private final MatrixCompatibleFactory matrixCompatibleFactory = new MatrixCompatibleFactory(DataType.DOUBLE);
    private final DataOperation dataOperation = new DoubleOperation();

    public ResultGenerator(Map<Integer, Integer> testScope) {
        this.testScope = testScope;
    }

    public Map<Type,Map<Integer,AggregatedResults>> doTests(List<Type> testCase, int iterationsCount){
        Map<Type,Map<Integer,AggregatedResults>> testsResults = new HashMap<>();


        for (Type type :
                testCase) {
            testsResults.put(type,new HashMap<>());
        }

        long start,stop;
        double timeInMilliSeconds;
        MatrixGenerator matrixGenerator;


        for (Integer agentsNumber :
                testScope.keySet()) {
            System.out.println(agentsNumber);
            matrixGenerator = new MatrixGenerator(new Case(0,0,agentsNumber)); //yes\no voters doesnt matter in that case
            MatrixCompatible[] monteCarloValues;
            monteCarloValues = null;
            for (Type type :
                        testsResults.keySet()) {
                AggregatedResults aggregatedResults = new AggregatedResults();
                for(int i = 0; i< testScope.get(agentsNumber); i++) {
                        Equation equationToSolve = matrixGenerator.generateEquation();
                    System.out.println(matrixGenerator.indexToKey.size());
                        start = System.nanoTime();
                        MatrixCompatible[] results = equationToSolve.evaluate(type);
                        stop = System.nanoTime();
                        timeInMilliSeconds = ((stop-start)/1000000D);
                        if(monteCarloValues != null)
                            aggregatedResults.updateAggregatedResults(new Results(
                                    calculateAbsoluteErrorMax(equationToSolve.getVectorB(),
                                            equationToSolve.getNewVectorB(),equationToSolve),
                                    timeInMilliSeconds,
                                    calculateAbsoluteErrorAverage(monteCarloValues, results,equationToSolve),
                                    calculateAbsoluteErrorAverage(equationToSolve.getVectorB(),
                                            equationToSolve.getNewVectorB(),equationToSolve)));
                        else
                            aggregatedResults.updateAggregatedResults(new Results(
                                    calculateAbsoluteErrorMax(equationToSolve.getVectorB(),
                                            equationToSolve.getNewVectorB(),equationToSolve),
                                    timeInMilliSeconds,
                                    0,
                                    calculateAbsoluteErrorAverage(equationToSolve.getVectorB(),
                                            equationToSolve.getNewVectorB(),equationToSolve)));
                    }
            aggregatedResults.divideByExecutionCount();
            testsResults.get(type).put(agentsNumber,aggregatedResults);
                }
        }


        return testsResults;
    }

    @SuppressWarnings("unchecked")
    private Double calculateAbsoluteErrorAverage(MatrixCompatible[] mCarloResultVector, MatrixCompatible[] calculatedVector, Equation equation){
        MatrixCompatible absoluteError = matrixCompatibleFactory.createWithNominator(0D);
        for(int i = 0; i<mCarloResultVector.length; i++){
            absoluteError = dataOperation.add(absoluteError,dataOperation.subtract(mCarloResultVector[i],calculatedVector[equation.getMatrixA().rows[i]]));
        }
        return Math.abs(absoluteError.getDoubleValue())/ (double) mCarloResultVector.length;
    }



    @SuppressWarnings("unchecked")
    private Double calculateAbsoluteErrorMax(MatrixCompatible[] goldenVector, MatrixCompatible[] calculatedVector, Equation equation){
        double maxAbsoluteError = Math.abs(dataOperation.subtract(goldenVector[0],calculatedVector[equation.getMatrixA().rows[0]]).getDoubleValue());
        for(int i = 0; i<goldenVector.length; i++){
            double temp = Math.abs(dataOperation.subtract(goldenVector[i],calculatedVector[equation.getMatrixA().rows[i]]).getDoubleValue());
            if (temp > maxAbsoluteError)
                maxAbsoluteError = temp;
        }
        return maxAbsoluteError;
    }

}
