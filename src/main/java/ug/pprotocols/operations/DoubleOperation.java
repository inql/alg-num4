package ug.pprotocols.operations;

import ug.pprotocols.datatypes.DoubleComp;

public class DoubleOperation implements DataOperation<DoubleComp> {

    @Override
    public DoubleComp add(DoubleComp element1, DoubleComp element2) {
        DoubleComp result = new DoubleComp();
        result.setValue(element1.getValue()+element2.getValue());
        return result;
    }

    @Override
    public DoubleComp subtract(DoubleComp element1, DoubleComp element2) {
        DoubleComp result = new DoubleComp();
        result.setValue(element1.getValue()-element2.getValue());
        return result;
    }

    @Override
    public DoubleComp multiply(DoubleComp element1, DoubleComp element2) {
        DoubleComp result = new DoubleComp();
        result.setValue(element1.getValue()*element2.getValue());
        return result;
    }

    @Override
    public DoubleComp divide(DoubleComp element1, DoubleComp element2) {
        DoubleComp result = new DoubleComp();
        result.setValue(element1.getValue()/element2.getValue());
        return result;
    }

    @Override
    public DoubleComp abs(DoubleComp element) {
        DoubleComp result = new DoubleComp();
        result.setValue(Math.abs(element.getValue()));
        return result;
    }

    @Override
    public DoubleComp toMinusOnePower(DoubleComp element) {
        DoubleComp result = new DoubleComp();
        result.setValue(1/element.getValue());
        return result;
    }


}
