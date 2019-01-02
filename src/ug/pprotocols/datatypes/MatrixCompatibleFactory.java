package ug.pprotocols.datatypes;

import java.math.BigInteger;

public class MatrixCompatibleFactory {

    private DataType dataType;

    public MatrixCompatibleFactory(DataType dataType){
        this.dataType = dataType;
    }

    public MatrixCompatible createWithNominator(Double value){
        if(dataType == DataType.DOUBLE){
            return new DoubleComp().setValue(value);

        }
        else{
            return null;
        }
    }

    public MatrixCompatible createWithDenominator(BigInteger nominator, BigInteger denominator){
        if(dataType == DataType.DOUBLE){
            return new DoubleComp(nominator.intValue(),denominator.intValue());
        }
        else{
            return null;
        }
    }

    public MatrixCompatible[] createArray(int length){
        if(dataType == DataType.DOUBLE){
            return new DoubleComp[length];
        }
        else{
            return null;
        }
    }

    public MatrixCompatible[][] createMatrix(int x, int y){
        if(dataType == DataType.DOUBLE){
            return new DoubleComp[x][y];
        }
        else{
            return null;
        }
    }

}
