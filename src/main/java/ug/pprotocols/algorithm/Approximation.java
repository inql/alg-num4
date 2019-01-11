package ug.pprotocols.algorithm;

import ug.pprotocols.Type;
import ug.pprotocols.datatypes.DoubleComp;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.matrix.Equation;
import ug.pprotocols.matrix.MyMatrix;
import ug.pprotocols.operations.DoubleOperation;

public class Approximation {

    private int m;

    private double results[];
    private MyMatrix<DoubleComp> matrix;
    private DoubleComp[] vector;

    public Approximation(double[] arguments, double[] values, Type type )
    {
        getM(type);
        if (arguments.length != values.length) {
            System.out.println("Wrong arguments");
        }

        matrix = getMatrix(arguments);
        vector = getVector(arguments,values);
    }

    public void calculate()
    {
        Equation eq = new Equation(matrix,vector, null);
        MatrixCompatible[] result = eq.evaluate(Type.GAUSS_SEIDEL_MINUS10); // Na sztywno jest gauss-sparse. Do zmiany?

        results = new double[m+1];
        int k = m;
        for (int i = 0; i < m+1; i++)
        {
            results[i] = result[k].getDoubleValue();
            k--;
        }
    }

    public double[] getResults()
    {
        return results;
    }

    private MyMatrix<DoubleComp> getMatrix(double[] arguments)
    {
        double[] s = new double[m*2+1];

        for (int k = 0; k < m*2+1; k++)
        {
            s[k] = 0;
            for(int i = 0; i < arguments.length; i++)
            {
                s[k] += Math.pow(arguments[i],k);
            }
        }

        DoubleComp[][] forMatrix = new DoubleComp[m+1][m+1];
        for (int i = 0; i < m+1; i++)
            for (int j = 0; j < m+1; j++)
            {
                forMatrix[i][j] = new DoubleComp(s[i+j]);
            }

         return new MyMatrix<>(forMatrix);

    }

    private DoubleComp[] getVector(double[] arguments, double[] values)
    {
        double[] res = new double[m+1];


        for (int k = 0; k < m+1; k++)
        {
            res[k] = 0;
            for(int i = 0; i < arguments.length; i++)
            {
                res[k] += values[i] * Math.pow(arguments[i],k);
            }
        }
        DoubleComp[] ret = new DoubleComp[m+1];

        for (int i = 0; i < m+1; i++)
            ret[i] = new DoubleComp(res[i]);

        return ret;
    }


    void getM(Type type)
    {
        switch (type)
        {
            case GAUSS:
                m = 3;
                break;
            case GAUSS_SEIDEL_MINUS10:
            case GAUSS_SPARSE:
                m = 2;
                break;
            case LIBRARY_SPARSE:
                m = 1;
                break;
                default:
                    System.out.println("Wrong type");
                    break;
        }
    }
}
