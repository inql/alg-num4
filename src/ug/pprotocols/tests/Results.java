package ug.pprotocols.tests;

public class Results {

    double absoluteErrorMax;
    double executionTime;
    double differenceFromMonteCarlo;
    double absoluteErrorAverage;

    public Results(double absoluteErrorMax, double executionTime, double differenceFromMonteCarlo, double absoluteErrorAverage) {
        this.absoluteErrorMax = absoluteErrorMax;
        this.executionTime = executionTime;
        this.differenceFromMonteCarlo = differenceFromMonteCarlo;
        this.absoluteErrorAverage = absoluteErrorAverage;
    }

    @Override
    public String toString() {
        return absoluteErrorMax +","+absoluteErrorAverage+","+differenceFromMonteCarlo+","+executionTime+",";
    }




}
