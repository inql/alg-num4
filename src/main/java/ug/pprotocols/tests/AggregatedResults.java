package ug.pprotocols.tests;

public class AggregatedResults {

    private Results results;
    private long executionCount;

    public AggregatedResults() {
        this.results = new Results(0,0,0,0);
        this.executionCount = 0;
    }

    public void updateAggregatedResults(Results values){
        this.results.absoluteErrorMax +=values.absoluteErrorMax;
        this.results.solveExecutionTime +=values.solveExecutionTime;
        this.results.generateExecutionTime+=values.generateExecutionTime;
        this.results.absoluteErrorAverage+=values.absoluteErrorAverage;
        this.executionCount++;
    }

    public void divideByExecutionCount(){
        this.results.absoluteErrorMax /=(double)executionCount;
        this.results.solveExecutionTime /=(double)executionCount;
        this.results.generateExecutionTime/=(double)executionCount;
        this.results.absoluteErrorAverage/=(double)executionCount;
    }

    public double getExecutionTime()
    {
        return results.solveExecutionTime;
    }

    public double getGenerationTime()
    {
        return results.generateExecutionTime;
    }

    @Override
    public String toString() {
        return results.toString()+executionCount+",";
    }
}