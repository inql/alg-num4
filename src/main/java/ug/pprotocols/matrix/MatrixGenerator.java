package ug.pprotocols.matrix;

import org.apache.commons.math3.linear.SparseFieldMatrix;
import org.apache.commons.math3.linear.SparseFieldVector;
import ug.pprotocols.Type;
import ug.pprotocols.datatypes.DataType;
import ug.pprotocols.datatypes.DoubleComp;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.datatypes.MatrixCompatibleFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MatrixGenerator {

    public int agentsCount;
    public Map<Integer, Case> indexToKey;
    public MatrixCompatibleFactory matrixCompatibleFactory;

    private Case caseToAnalise;

    double basePropability;
    public int numberOfEquations;

    public MatrixGenerator(Case caseToAnalise){
        this.caseToAnalise = caseToAnalise;
        this.matrixCompatibleFactory = new MatrixCompatibleFactory(DataType.DOUBLE);
        this.agentsCount = caseToAnalise.getTotalVoters();
        generateKeys();
        this.numberOfEquations = indexToKey.keySet().size();

        this.basePropability = (double)newton((long)agentsCount,2L);
    }


    public Equation generateEquation(Type equationType){
        if(equationType!=Type.LIBRARY_SPARSE){
            MatrixCompatible[][] matrixA = matrixCompatibleFactory.createMatrix(numberOfEquations,numberOfEquations);
            MatrixCompatible[] vectorB = matrixCompatibleFactory.createArray(numberOfEquations);
            for(int i = 0 ; i<vectorB.length-1; i++){
                vectorB[i] =  matrixCompatibleFactory.createWithNominator(0D);
            }
            vectorB[vectorB.length-1] = matrixCompatibleFactory.createWithNominator(1D);

            for(int i = 0; i< matrixA.length; i++){
                for(int j = 0; j<matrixA.length; j++){
                    matrixA[i][j] = generateValue(i,j);
                }
            }
            return new Equation<>(new MyMatrix<>((DoubleComp[][]) matrixA),vectorB,null);
        }
        else{
            SparseFieldMatrix<DoubleComp> sparseFieldMatrix = new SparseFieldMatrix<>(new DoubleComp(),numberOfEquations,numberOfEquations);
            SparseFieldVector<DoubleComp> sparseFieldVector = new SparseFieldVector<>(new DoubleComp(),numberOfEquations);
            for(int i = 0 ; i<sparseFieldVector.getDimension()-1; i++){
                sparseFieldVector.setEntry(i,new DoubleComp(0D));
            }
            sparseFieldVector.setEntry(sparseFieldVector.getDimension()-1,new DoubleComp(1D));

            for(int i = 0; i< sparseFieldMatrix.getRowDimension(); i++){
                for(int j = 0; j<sparseFieldMatrix.getColumnDimension(); j++){
                    sparseFieldMatrix.addToEntry(i,j,(DoubleComp)generateValue(i,j));
                }
            }
            return new Equation<>(sparseFieldMatrix,sparseFieldVector);
        }

    }

    private MatrixCompatible generateValue(int i, int j){
        Case currentCaseRow = indexToKey.get(i);
        Case currentCaseColumn = indexToKey.get(j);


        if(currentCaseRow.getYesVoters() == agentsCount && currentCaseRow.getNoVoters() ==0 && i == j)
            return matrixCompatibleFactory.createWithNominator(1D);
        else if(currentCaseRow.getYesVoters() == 0 && currentCaseRow.getNoVoters() == agentsCount && i == j)
            return matrixCompatibleFactory.createWithNominator(1D);
        else if(currentCaseRow.getYesVoters() == 0 && currentCaseRow.getNoVoters() == 0 && i == j)
            return matrixCompatibleFactory.createWithNominator(1D);
        else if (currentCaseRow.getYesVoters() == currentCaseColumn.getYesVoters() && currentCaseRow.getNoVoters() == currentCaseColumn.getNoVoters()
                && (currentCaseRow.getYesVoters() > 1 || currentCaseRow.getNoVoters() > 1 || currentCaseRow.getUndecidedVoters() > 1))
            return undecidedAgentsChosenCase(currentCaseRow);
        else if (currentCaseRow.getYesVoters() + 1 == currentCaseColumn.getYesVoters() && currentCaseRow.getNoVoters() == currentCaseColumn.getNoVoters()
                && currentCaseRow.getYesVoters() > 0 && currentCaseRow.getUndecidedVoters() > 0)
            return yesAndUndecidedAgentChosenCase(currentCaseRow);
        else if (currentCaseRow.getYesVoters() == currentCaseColumn.getYesVoters() && currentCaseRow.getNoVoters() + 1 == currentCaseColumn.getNoVoters()
                && currentCaseRow.getNoVoters() > 0 && currentCaseRow.getUndecidedVoters() > 0)
            return noAndUndecidedAgentChosenCase(currentCaseRow);
        else if (currentCaseRow.getYesVoters() - 1 == currentCaseColumn.getYesVoters() && currentCaseRow.getNoVoters() - 1 == currentCaseColumn.getNoVoters()
                && currentCaseRow.getYesVoters() > 0 && currentCaseRow.getNoVoters() > 0)
            return yesAndNoAgentChosenCase(currentCaseRow);
        else if (i == j)
            return matrixCompatibleFactory.createWithNominator(-1D);
        else
            return matrixCompatibleFactory.createWithNominator(0D);
    }



    private MatrixCompatible undecidedAgentsChosenCase(Case currCase){

        double result = -1.0;

        if(currCase.getYesVoters()>1)
            result += (double)newton(currCase.getYesVoters(),2)/ basePropability;

        if(currCase.getNoVoters() > 1)
            result += (double)newton(currCase.getNoVoters(),2)/ basePropability;

        if(currCase.getUndecidedVoters() > 1)
            result += (double)newton(currCase.getUndecidedVoters(),2) / basePropability;

        return matrixCompatibleFactory.createWithNominator(result);
    }

    private MatrixCompatible yesAndUndecidedAgentChosenCase(Case currCase){
        return matrixCompatibleFactory.createWithNominator(undecidedAgentChosenCalculation(currCase,currCase.getYesVoters()));
    }

    private MatrixCompatible noAndUndecidedAgentChosenCase(Case currCase) {
        return matrixCompatibleFactory.createWithNominator(undecidedAgentChosenCalculation(currCase,currCase.getNoVoters()));
    }

    private double undecidedAgentChosenCalculation(Case currCase, int conditionValue){
        double result = 0D;

        if(conditionValue > 0 && currCase.getUndecidedVoters() > 0)
            result= (double)(conditionValue * (currCase.getUndecidedVoters()))/ basePropability;

        return result;
    }

    private MatrixCompatible yesAndNoAgentChosenCase(Case currCase){

        double result = 0D;

        if(currCase.getNoVoters() > 0 && currCase.getYesVoters() > 0)
            result = (double)(currCase.getNoVoters() * currCase.getYesVoters())/ basePropability;

        return matrixCompatibleFactory.createWithNominator(result);

    }

    public Map<Integer,Case> generateKeys(){
        indexToKey = new HashMap<>();

        int k = 0;
        for(int i=0; i<=agentsCount; i++){
            for(int j =0; j<=agentsCount-i; j++){
                indexToKey.put(k++,new Case(i,j,agentsCount));
            }
        }
        return indexToKey;
    }

    private static long newton(long n, long k){
        if (k > n) { return 0; }
        if (n == k) { return 1; } // only one way to chose when n == k
        if (k > n - k) { k = n - k; } // Everything is symmetric around n-k, so it is quicker to iterate over a smaller k than a larger one.
        if (k == 1) { return n; }
        long c = 1;
        for (long i = 1; i <= k; i++)
        {
            c *= n--;
            c /= i;
        }
        return c;
    }




}
