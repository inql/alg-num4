package ug.pprotocols.datatypes;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NullArgumentException;
import ug.pprotocols.operations.DoubleOperation;

public class DoubleComp implements MatrixCompatible<DoubleComp,Double> {

    private double value;

    public DoubleComp(int nominator, int denominator) {this.value = (double)nominator/denominator;}

    public DoubleComp(int value) {
        this(value,1);
    }

    public DoubleComp(double value) {
        this.value = value;
    }

    public DoubleComp(){

    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public DoubleComp setValue(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(double value){this.value = value;}

    @Override
    public String toString() {
        return String.format("%+f",value);
//        return String.valueOf(value);
    }

    public DoubleComp clone(){
        DoubleComp cloned = new DoubleComp();
        cloned.setValue(this.getValue());
        return cloned;
    }

    @Override
    public Double getDoubleValue() {
        return this.value;
    }
    /*

    If the DoubleComp is equal to the argument then 0 is returned.
    If the DoubleComp is less than the argument then -1 is returned.
    If the DoubleComp is greater than the argument then 1 is returned.

     */

    @Override
    public int compareTo(DoubleComp doubleComp) {
        return this.getValue().compareTo(doubleComp.getValue());
    }

    @Override
    public DoubleComp add(DoubleComp doubleComp) throws NullArgumentException {
        return new DoubleOperation().add(this,doubleComp);
    }

    @Override
    public DoubleComp subtract(DoubleComp doubleComp) throws NullArgumentException {
        return new DoubleOperation().subtract(this,doubleComp);
    }

    @Override
    public DoubleComp negate() {
        return new DoubleOperation().multiply(this,new DoubleComp(-1));
    }

    @Override
    public DoubleComp multiply(int i) {
        return new DoubleOperation().multiply(this,new DoubleComp(i));
    }

    @Override
    public DoubleComp multiply(DoubleComp doubleComp) throws NullArgumentException {
        return new DoubleOperation().multiply(this,doubleComp);
    }

    @Override
    public DoubleComp divide(DoubleComp doubleComp) throws NullArgumentException, MathArithmeticException {
        return new DoubleOperation().divide(this,doubleComp);
    }

    @Override
    public DoubleComp reciprocal() throws MathArithmeticException {
        return new DoubleOperation().multiply(this,new DoubleComp(-1));

    }

    @Override
    public Field<DoubleComp> getField() {
        return this;
    }

    @Override
    public DoubleComp getZero() {
        return new DoubleComp(0);
    }

    @Override
    public DoubleComp getOne() {
        return new DoubleComp(1);
    }

    @Override
    public Class<? extends FieldElement<DoubleComp>> getRuntimeClass() {
        return DoubleComp.class;
    }
}
