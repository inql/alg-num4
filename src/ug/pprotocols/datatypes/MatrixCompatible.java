package ug.pprotocols.datatypes;

public interface MatrixCompatible<T1,T2> extends Comparable<T1> {

    T2 getValue();
    T1 setValue(T2 value);
    T1 clone();
    Double getDoubleValue();
    int compareTo(T1 t1);


}
