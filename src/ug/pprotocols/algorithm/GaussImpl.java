package ug.pprotocols.algorithm;

import ug.pprotocols.ChoiceType;
import ug.pprotocols.datatypes.DoubleComp;
import ug.pprotocols.matrix.Equation;
import ug.pprotocols.matrix.MyMatrix;
import ug.pprotocols.Swapper;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.datatypes.MatrixCompatibleFactory;
import ug.pprotocols.operations.DataOperation;

import java.math.BigInteger;

public class GaussImpl {

    public MyMatrix myMatrix;
    private final MatrixCompatibleFactory matrixCompatibleFactory;
    private final DataOperation dataOperation;
    private final ChoiceType choiceType;

    public GaussImpl(MyMatrix myMatrix, MatrixCompatibleFactory matrixCompatibleFactory, DataOperation dataOperation, ChoiceType choiceType) {
        this.myMatrix = myMatrix;
        this.matrixCompatibleFactory = matrixCompatibleFactory;
        this.dataOperation = dataOperation;
        this.choiceType = choiceType;
    }

    @SuppressWarnings("unchecked")
    public MatrixCompatible[] gauss(MatrixCompatible[] vectorB, boolean isSparse){
        MatrixCompatible zero = matrixCompatibleFactory.createWithNominator(0D);
        int n = vectorB.length;
        for(int p = 0; p<n; p++){
            switchRowOrColumn(p,p,vectorB);
            for(int i = p+1; i<n; i++){
                MatrixCompatible alpha = dataOperation.divide(myMatrix.getValue(i,p),myMatrix.getValue(p,p));
                if(!isSparse || myMatrix.getValue(i,p).compareTo(zero)!=0){
                    vectorB[i] = dataOperation.subtract(vectorB[i],dataOperation.multiply(alpha,vectorB[p]));
                    for (int j=p; j<n; j++){
                        myMatrix.setValue(i,j,dataOperation.subtract(myMatrix.getValue(i,j),dataOperation.multiply(alpha,myMatrix.getValue(p,j))));
                    }
                }

            }


        }

        MatrixCompatible[] vectorX = matrixCompatibleFactory.createArray(n);
        for (int i =n-1; i>=0; i--){
            MatrixCompatible sum = matrixCompatibleFactory.createWithDenominator(BigInteger.ZERO,BigInteger.ONE);
            for(int j = i+1; j<n; j++){
                sum = dataOperation.add(sum,dataOperation.multiply(myMatrix.getValue(i,j),vectorX[myMatrix.columns[j]]));
            }
            vectorX[myMatrix.columns[i]] = dataOperation.divide(dataOperation.subtract(vectorB[i],sum),myMatrix.getValue(i,i));
        }
        for(int i =0; i<vectorX.length; i++){
            vectorX[myMatrix.columns[i]].setValue(vectorX[myMatrix.columns[i]].getValue());
        }
        return vectorX;

    }

    @SuppressWarnings("unchecked")
    public MatrixCompatible[] switchRowOrColumn(int startX, int startY, MatrixCompatible[] vectorB){


        if(choiceType == ChoiceType.NONE)
            return vectorB;

        else if (choiceType == ChoiceType.PARTIAL)
        {
            int rowToSwitch = startX;
            for (int i=startX; i < myMatrix.rows.length; i++)
            {
                if ( dataOperation.abs(myMatrix.getValue(i,startY)).compareTo(dataOperation.abs(myMatrix.getValue(rowToSwitch,startY))) > 0)
                    rowToSwitch = i;

            }

            myMatrix.swap(startX,rowToSwitch, Swapper.ROW);
            MatrixCompatible temp = vectorB[startX];
            vectorB[startX] = vectorB[rowToSwitch];
            vectorB[rowToSwitch] = temp;

            return vectorB;

        } else if (choiceType == ChoiceType.FULL)
        {
            int rowToSwitch = startY;
            int columnToSwitch = startX;
            for (int i=startY; i < myMatrix.rows.length; i++)
            {
                for (int o=startX; o < myMatrix.columns.length; o++) {

                    if (dataOperation.abs(myMatrix.getValue(i, o)).compareTo(dataOperation.abs(myMatrix.getValue(rowToSwitch, columnToSwitch))) > 0) {
                        rowToSwitch = i;
                        columnToSwitch = o;
                    }
                }

            }
            myMatrix.swap(startX,rowToSwitch, Swapper.ROW);
            myMatrix.swap(startY,columnToSwitch, Swapper.COLUMN);
            MatrixCompatible temp = vectorB[startX];
            vectorB[startX] = vectorB[rowToSwitch];
            vectorB[rowToSwitch] = temp;

            return vectorB;

        }
        return vectorB;

    }

    public MatrixCompatible[] jacobian(MatrixCompatible[] vectorB, double precision){

        int n = vectorB.length;
        MatrixCompatible[] vectorX = matrixCompatibleFactory.createArray(n);
        MatrixCompatible[] prevVectorX = matrixCompatibleFactory.createArray(n);

        for (int i = 0; i < n; i++)
        {
            vectorX[i] = new DoubleComp(0);
            prevVectorX[i] = new DoubleComp(0);
        }

        boolean isPrecisionReached = false;
        while (!isPrecisionReached)
        {
            for (int i =0 ; i< n; i++)
            {
                vectorX[i] = vectorB[i];
                for (int j = 0; j < n; j++)
                {
                    if (i != j)
                        vectorX[i] = dataOperation.subtract(vectorX[i], dataOperation.multiply(myMatrix.getValue(i,j),  prevVectorX[j]));
                }
                vectorX[i] = dataOperation.divide(vectorX[i], myMatrix.getValue(i,i));
            }
            if (calculateAbsoluteError(vectorX,prevVectorX) < precision)
                isPrecisionReached = true;

            for (int x =0 ; x < n; x++) {
                prevVectorX[x] = vectorX[x];
            }
        }

        return vectorX;
    }

    public MatrixCompatible[] gaussSeidel(MatrixCompatible[] vectorB, double precision){

        int n = vectorB.length;
        MatrixCompatible[] vectorX = matrixCompatibleFactory.createArray(n);
        MatrixCompatible[] prevVectorX = matrixCompatibleFactory.createArray(n);

        for (int i = 0; i < n; i++)
        {
            vectorX[i] = new DoubleComp(0);
            prevVectorX[i] = new DoubleComp(0);
        }

        boolean isPrecisionReached = false;
        while (!isPrecisionReached)
        {
            for (int i =0 ; i< n; i++)
            {
                vectorX[i] = vectorB[i];
                for (int j = 0; j < i; j++)
                {
                    vectorX[i] = dataOperation.subtract(vectorX[i], dataOperation.multiply(myMatrix.getValue(i, j), prevVectorX[j]));
                }
                for (int j = i+1; j < n; j++)
                {
                    vectorX[i] = dataOperation.subtract(vectorX[i], dataOperation.multiply(myMatrix.getValue(i, j), prevVectorX[j]));
                }
                vectorX[i] = dataOperation.divide(vectorX[i], myMatrix.getValue(i,i));
            }
            if (calculateAbsoluteError(vectorX,prevVectorX) < precision)
                isPrecisionReached = true;

            for (int x =0 ; x < n; x++) {
                prevVectorX[x] = vectorX[x];
            }
        }

        return vectorX;
    }

    private Double calculateAbsoluteError(MatrixCompatible[] vectorX, MatrixCompatible[] prevVectorX){
//        MatrixCompatible absoluteError = matrixCompatibleFactory.createWithNominator(0D);
//        for(int i = 0;i < vectorX.length; i++){
//            absoluteError = dataOperation.add(absoluteError,dataOperation.subtract(vectorX[i],prevVectorX[myMatrix.rows[i]]));
//        }
//        return Math.abs(absoluteError.getDoubleValue())/ (double) vectorX.length;
        double maxAbsoluteError = Math.abs(dataOperation.subtract(vectorX[0],prevVectorX[myMatrix.rows[0]]).getDoubleValue());
        for(int i = 0; i<vectorX.length; i++){
            double temp = Math.abs(dataOperation.subtract(vectorX[i],prevVectorX[myMatrix.rows[i]]).getDoubleValue());
            if (temp > maxAbsoluteError)
                maxAbsoluteError = temp;
        }
        return maxAbsoluteError;
    }


    public MatrixCompatible[][] multiplyMatrices(MatrixCompatible[][] a, MatrixCompatible[][] b) {

        int rowLengthA = a.length;
        int rowLengthB = b.length;
        int colLengthA = a[0].length;
        int colLengthB = b[0].length;

        if (rowLengthA != colLengthB)
            return null;

        MatrixCompatible[][] result = matrixCompatibleFactory.createMatrix(rowLengthA ,colLengthB);
        for (int x = 0; x < colLengthA; x++) {
            for (int y = 0; y < rowLengthB; y++) {
                result[x][y] = new DoubleComp(0);
            }
        }


        for (int x = 0; x < colLengthA; x++) {
            for (int y = 0; y < rowLengthB; y++) {
                for (int k = 0; k < rowLengthA; k++) {
                    result[x][y] =  dataOperation.add(result[x][y], dataOperation.multiply(a[x][k], b[k][y]));
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public MatrixCompatible[] multiplyMatrixWithVector(MyMatrix a, MatrixCompatible[] vector)
    {
        if (a.rows.length != vector.length)
            return null;
        MatrixCompatible[] result = matrixCompatibleFactory.createArray(vector.length);

        for (int i=0; i <a.rows.length; i++)
            result[i] = matrixCompatibleFactory.createWithNominator(0D);

        for (int x = 0; x < a.rows.length; x++)
        {
            for (int y = 0; y < a.columns.length; y++)
            {
                result[x] = dataOperation.add(result[x], dataOperation.multiply(a.getValue(x,y), vector[y]));

            }

        }
        return result;
    }


}