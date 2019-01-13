package ug.pprotocols.tests;

public class AggregatedResults {

    private Results results;
    private long executionCount;

    public AggregatedResults() {
        this.results = new Results(0,0);
        this.executionCount = 0;
    }

    public void updateAggregatedResults(Results values){
        this.results.solveExecutionTime +=values.solveExecutionTime;
        this.results.generateExecutionTime+=values.generateExecutionTime;
        this.executionCount++;
    }

    public void divideByExecutionCount(){
        this.results.solveExecutionTime /=(double)executionCount;
        this.results.generateExecutionTime/=(double)executionCount;
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