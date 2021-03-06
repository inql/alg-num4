package ug.pprotocols.matrix;

import org.apache.commons.math3.linear.*;
import org.ejml.data.DMatrixSparseCSC;
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
    public SparseRealMatrix sparseMatrix;
    public SparseRealMatrix sparseVector;

    public SparseFieldMatrix<DoubleComp> sparseFieldMatrix;
    public SparseFieldMatrix<DoubleComp> sparseFieldVector;
    private MatrixCompatible[] vectorB;
    private MatrixCompatible[] vectorXGauss;
    private MatrixCompatible[] vectorXGaussSparse;
    private MatrixCompatible[] vectorXGS;
    private FieldMatrix<DoubleComp> vectorXLibSparse;
    private MatrixCompatible[] newVectorB;
    private GaussImpl gauss;
    private LUDecomposition solver;
    private FieldLUDecomposition<DoubleComp> solverField;

    public Equation(MyMatrix<T> matrixA, MatrixCompatible[] vectorB, MatrixCompatible vectorX) {
        this.matrixA = matrixA;
        this.vectorB = vectorB;
    }


    public Equation(SparseRealMatrix sparseFieldMatrix, SparseRealMatrix sparseFieldVector) {
        this.sparseMatrix = sparseFieldMatrix;
        this.sparseVector = sparseFieldVector;
        solver = new LUDecomposition(sparseMatrix);
    }

    public Equation(SparseFieldMatrix<DoubleComp> sparseFieldMatrix, SparseFieldMatrix<DoubleComp> sparseFieldVector) {
        this.sparseFieldMatrix = sparseFieldMatrix;
        this.sparseFieldVector = sparseFieldVector;
        solverField = new FieldLUDecomposition<>(sparseFieldMatrix);
    }

    @Override
    public String toString() {
        return "Equation{\n" +
                "matrixA=\n" + matrixA +
                ", \nvectorB=\n" + Arrays.deepToString(vectorB) + "\n" +
                "sparse matrixA=\n"+sparseFieldMatrix + "\n" +
                "sparse vectorB=\n"+ (sparseFieldVector);
    }

    public MatrixCompatible[] evaluate(Type type){
        switch (type){
            case GAUSS:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGauss = gauss.gauss(vectorB,false);
                setNewVectorB(matrixA,this.vectorXGauss);
                return this.vectorXGauss;
            case GAUSS_SPARSE:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGaussSparse = gauss.gauss(vectorB,true);
                setNewVectorB(matrixA,this.vectorXGaussSparse);
                return this.vectorXGaussSparse;
            case GAUSS_SEIDEL_MINUS10:
                gauss = new GaussImpl(matrixA,new MatrixCompatibleFactory(DataType.DOUBLE),new DoubleOperation(), ChoiceType.PARTIAL);
                this.vectorXGS = gauss.gaussSeidel(vectorB,type.getPrecision());
                setNewVectorB(matrixA,this.vectorXGS);
                return this.vectorXGS;
            case LIBRARY_SPARSE:
                solver.getSolver().solve(sparseVector);
                return null;
            case LIBRARY_SPARSE_FIELD:
                this.vectorB = sparseFieldVector.getColumnVector(0).toArray();
                this.vectorXLibSparse = solverField.getSolver().solve(sparseFieldVector);
                this.newVectorB = sparseFieldMatrix.multiply(vectorXLibSparse).getColumn(0);
                return vectorXLibSparse.getColumn(0);
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

    public MatrixCompatible[] getVectorXGS() {
        return vectorXGS;
    }

    public MatrixCompatible[] getVectorXGaussSparse() {
        return vectorXGaussSparse;
    }
}
