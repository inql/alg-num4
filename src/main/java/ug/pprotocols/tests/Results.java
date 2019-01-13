package ug.pprotocols.tests;

public class Results {

    double solveExecutionTime;
    double generateExecutionTime;

    public Results(double solveExecutionTime, double generateExecutionTime) {
        this.solveExecutionTime = solveExecutionTime;
        this.generateExecutionTime = generateExecutionTime;
    }

    @Override
    public String toString() {
        return generateExecutionTime+","+ solveExecutionTime +",";
    }




}
