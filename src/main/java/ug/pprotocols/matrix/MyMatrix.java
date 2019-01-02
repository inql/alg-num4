package ug.pprotocols.matrix;

import ug.pprotocols.Swapper;
import ug.pprotocols.datatypes.MatrixCompatible;

import java.util.stream.IntStream;


public class MyMatrix<T extends MatrixCompatible> {

    private T[][] matrix;

    public int[] rows;
    public int[] columns;

    public MyMatrix(T[][] matrix) {
        this.matrix = matrix;
        rows = IntStream.rangeClosed(0,matrix.length-1).toArray();
        columns = IntStream.rangeClosed(0,matrix[0].length-1).toArray();
    }

    public void swap(int from, int to, Swapper swapper){
        int[] toSwap;
        if(swapper == Swapper.ROW)
            toSwap = rows;
        else
            toSwap = columns;
        int temp = toSwap[from];
        toSwap[from] = toSwap[to];
        toSwap[to] = temp;
    }

    public T getValue(int rowNo, int columnNo){
        try{
            if(columnNo<0 || rowNo<0)
                throw new NullPointerException();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return matrix [rows[rowNo]][columns[columnNo]];
    }

    public void setValue(int rowNo, int columnNo, T value)
    {
        matrix [rows[rowNo]][columns[columnNo]] = value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(int i =0; i<matrix.length; i++){
                result.append("[");
            for(int j=0; j<matrix[i].length; j++){
                result.append(this.getValue(i,j));
                if(j<matrix[i].length-1){
                    result.append(", ");
                }
            }
            result.append("]\n");
        }
        return result.toString();
    }
}
