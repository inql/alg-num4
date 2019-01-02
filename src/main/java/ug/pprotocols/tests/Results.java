package ug.pprotocols.tests;

public class Results {

    double absoluteErrorMax;
    double solveExecutionTime;
    double absoluteErrorAverage;
    double generateExecutionTime;

    public Results(double absoluteErrorMax, double solveExecutionTime, double absoluteErrorAverage, double generateExecutionTime) {
        this.absoluteErrorMax = absoluteErrorMax;
        this.solveExecutionTime = solveExecutionTime;
        this.absoluteErrorAverage = absoluteErrorAverage;
        this.generateExecutionTime = generateExecutionTime;
    }

    @Override
    public String toString() {
        return absoluteErrorMax +","+absoluteErrorAverage+","+generateExecutionTime+","+ solveExecutionTime +",";
    }




}
