package ug.pprotocols.operations;

import ug.pprotocols.datatypes.MatrixCompatible;

public interface DataOperation<T extends MatrixCompatible> {

    T add(T element1, T element2);
    T subtract(T element1, T element2);
    T multiply(T element1, T element2);
    T divide(T element1, T element2);
    T abs(T element);
    T toMinusOnePower(T element);


}
