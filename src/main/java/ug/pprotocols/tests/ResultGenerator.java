package ug.pprotocols.tests;

import ug.pprotocols.Type;
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

        long executionStart,executionStop;
        long generateStart,generateStop;
        double executionTimeInMilliSeconds,generateTimeInMilliSeconds;
        MatrixGenerator matrixGenerator;


        for (Integer agentsNumber :
                testScope.keySet()) {
            System.out.println(agentsNumber);
            matrixGenerator = new MatrixGenerator(new Case(0,0,agentsNumber)); //yes\no voters doesnt matter in that case
            for (Type type :
                        testsResults.keySet()) {
                AggregatedResults aggregatedResults = new AggregatedResults();
                for(int i = 0; i< testScope.get(agentsNumber); i++) {
                    //tutaj liczenie czasu dla generowania
                    generateStart = System.nanoTime();
                    Equation equationToSolve = matrixGenerator.generateEquation(type);
                    generateStop = System.nanoTime();
                    generateTimeInMilliSeconds = ((generateStop-generateStart)/1000000D);
                    System.out.println(matrixGenerator.indexToKey.size());
                    //tutaj liczenie czasu dla obliczania
                    executionStart = System.nanoTime();
                    MatrixCompatible[] results = equationToSolve.evaluate(type);
                    executionStop = System.nanoTime();
                    executionTimeInMilliSeconds = ((executionStop-executionStart)/1000000D);
                    aggregatedResults.updateAggregatedResults(new Results(
                            calculateAbsoluteErrorMax(equationToSolve.getVectorB(),
                                    equationToSolve.getNewVectorB(),equationToSolve),
                                    executionTimeInMilliSeconds,
                                    generateTimeInMilliSeconds,
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
    private Double calculateAbsoluteErrorAverage(MatrixCompatible[] goldenVector, MatrixCompatible[] calculatedVector, Equation equation){
        MatrixCompatible absoluteError = matrixCompatibleFactory.createWithNominator(0D);
        if(equation.getMatrixA()!=null){
            for(int i = 0; i<goldenVector.length; i++){
                absoluteError = dataOperation.add(absoluteError,dataOperation.subtract(goldenVector[i],calculatedVector[equation.getMatrixA().rows[i]]));
            }
        }
        else{
            for(int i = 0; i<goldenVector.length; i++){
                absoluteError = dataOperation.add(absoluteError,dataOperation.subtract(goldenVector[i],calculatedVector[i]));
            }
        }

        return Math.abs(absoluteError.getDoubleValue())/ (double) goldenVector.length;
    }



    @SuppressWarnings("unchecked")
    private Double calculateAbsoluteErrorMax(MatrixCompatible[] goldenVector, MatrixCompatible[] calculatedVector, Equation equation){
        double maxAbsoluteError;
        if(equation.getMatrixA()!=null){
            maxAbsoluteError = Math.abs(dataOperation.subtract(goldenVector[0],calculatedVector[equation.getMatrixA().rows[0]]).getDoubleValue());
            for(int i = 0; i<goldenVector.length; i++){
                double temp = Math.abs(dataOperation.subtract(goldenVector[i],calculatedVector[equation.getMatrixA().rows[i]]).getDoubleValue());
                if (temp > maxAbsoluteError)
                    maxAbsoluteError = temp;
            }
        }
        else{
            maxAbsoluteError = Math.abs(dataOperation.subtract(goldenVector[0],calculatedVector[0]).getDoubleValue());
            for(int i = 0; i<goldenVector.length; i++){
                double temp = Math.abs(dataOperation.subtract(goldenVector[i],calculatedVector[i]).getDoubleValue());
                if (temp > maxAbsoluteError)
                    maxAbsoluteError = temp;
            }
        }
        return maxAbsoluteError;
    }

}
