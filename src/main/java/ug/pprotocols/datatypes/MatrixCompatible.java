package ug.pprotocols.datatypes;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

public interface MatrixCompatible<T1,T2> extends Comparable<T1>, FieldElement<T1>, Field<T1> {
    T2 getValue();
    T1 setValue(T2 value);
    T1 clone();
    Double getDoubleValue();
    int compareTo(T1 t1);




}
