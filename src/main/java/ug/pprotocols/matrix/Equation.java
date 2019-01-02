package ug.pprotocols.matrix;

import org.apache.commons.math3.linear.SparseFieldMatrix;
import org.apache.commons.math3.linear.SparseFieldVector;
import ug.pprotocols.ChoiceType;
import ug.pprotocols.Type;
import ug.pprotocols.algorithm.GaussImpl;
import ug.pprotocols.datatypes.DataType;
import ug.pprotocols.datatypes.DoubleComp;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.datatypes.MatrixCompatibleFactory;
import ug.pprotocols.operations.DoubleOperation;

import java.util.Arrays;

public class Equation<T extends MatrixCompatible> {

    private MyMatrix<T> matrixA;
    public SparseFieldMatrix<DoubleComp> sparseFieldMatrix;
    public SparseFieldVector<DoubleComp> sparseFieldVector;
    private MatrixCompatible[] vectorB;
    private MatrixCompatible[] vectorXGauss;
    private MatrixCompatible[] vectorXJac;
    private MatrixCompatible[] vectorXGaussSparse;
    private MatrixCompatible[] vectorXGS;
    private MatrixCompatible[] newVectorB;
    private GaussImpl gauss;

    Equation(MyMatrix<T> matrixA, MatrixCompatible[] vectorB, MatrixCompatible vectorX, SparseFieldMatrix<DoubleComp> sparseFieldMatrix, SparseFieldVector<DoubleComp> sparseFieldVector) {
        this.matrixA = matrixA;
        this.vectorB = vectorB;
        this.sparseFieldMatrix = sparseFieldMatrix;
        this.sparseFieldVector = sparseFieldVector;
    }

    @Override
    public String toString() {
        return "Equation{\n" +
                "matrixA=\n" + matrixA +
                ", \nvectorB=\n" + Arrays.deepToString(vectorB) + "\n" +
                "sparse matrixA=\n"+sparseFieldMatrix + "\n" +
                "sparse vectorB=\n"+ Arrays.toString(sparseFieldVector.toArray());
    }

    public MatrixCompatible[] evaluate(Type type){
        switch (type){
            case GAUSS:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGauss = gauss.gauss(vectorB,false);
                setNewVectorB(matrixA,this.vectorXGauss);
                return this.vectorXGauss;
            case JACOBIAN_MINUS6:
            case JACOBIAN_MINUS10:
            case JACOBIAN_MINUS14:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXJac = gauss.jacobian(vectorB,type.getPrecision());
                setNewVectorB(matrixA,this.vectorXJac);
                return this.vectorXJac;
            case GAUSS_SPARSE:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGaussSparse = gauss.gauss(vectorB,true);
                setNewVectorB(matrixA,this.vectorXGaussSparse);
                return this.vectorXGaussSparse;
            case GAUSS_SEIDEL_MINUS6:
            case GAUSS_SEIDEL_MINUS10:
            case GAUSS_SEIDEL_MINUS14:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGS = gauss.gaussSeidel(vectorB,type.getPrecision());
                setNewVectorB(matrixA,this.vectorXGS);
                return this.vectorXGS;
        }
        return null;
    }

    public void setNewVectorB (MyMatrix myMatrix, MatrixCompatible[] vectorX){
        newVectorB =  gauss.multiplyMatrixWithVector(myMatrix,vectorX);

    }

    public MatrixCompatible[] getNewVectorB() {
        return newVectorB;
    }

    public MyMatrix<T> getMatrixA() {
        return matrixA;
    }

    public MatrixCompatible[] getVectorB() {
        return vectorB;
    }

    public MatrixCompatible[] getVectorXGauss() {
        return vectorXGauss;
    }

    public MatrixCompatible[] getVectorXJac() {
        return vectorXJac;
    }

    public MatrixCompatible[] getVectorXGS() {
        return vectorXGS;
    }

    public MatrixCompatible[] getVectorXGaussSparse() {
        return vectorXGaussSparse;
    }
}
