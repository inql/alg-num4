package ug.pprotocols.tests;

import org.apache.commons.math3.analysis.function.Power;
import ug.pprotocols.Type;
import ug.pprotocols.algorithm.Approximation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ApproximationGenerator {

    public final  Map<Type, Map<Integer, AggregatedResults>> results;

    private final int startAgentNum = 15;
    private final Mode mode;

    public ApproximationGenerator(Map<Type, Map<Integer, AggregatedResults>> results, Mode mode) {
        this.results = results;
        this.mode = mode;
    }

    public Map<Type,double[]> gen()
    {
        Map<Type,double[]> typeMap = new HashMap<>();

        for (Type type : results.keySet()) {
            typeMap.put(type, generateValue(type, mode));
        }

        return typeMap;
    }

    public void putFunctions(Map<Type,double[]> mp)
    {
        for (Type type: results.keySet()) {

            System.out.println(type);
            for(int i =0; i< mp.get(type).length; i++) {

                System.out.print(mp.get(type)[i] + " x^" + (mp.get(type).length - i - 1) + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public void findDifferences(Map<Type,double[]> approxResults) {
        Power power;
        System.out.println(mode);
        for (Type type : results.keySet()) {
            double[] apprRes = new double[results.get(type).size()];
            double[] progRes = new double[results.get(type).size()];
            for (int i : results.get(type).keySet()) {
                double[] pom = approxResults.get(type);
                double temp = 0.0;
                for (int w = 0; w < pom.length; w++) {
                    power = new Power(pom.length - w - 1);
                    temp += (power.value(ApproximationGenerator.getEquationNumber(i))) * pom[w];
                }
                System.out.println(type + ", " + ApproximationGenerator.getEquationNumber(i));
                System.out.println("wartosc = " + temp);
                System.out.println("Roznica od wartosci z resultow " + (Math.abs(results.get(type).get(i).getExecutionTime() - temp)) / results.get(type).get(i).getExecutionTime());

                apprRes[i-startAgentNum] = temp;
                progRes[i-startAgentNum] = results.get(type).get(i).getExecutionTime();
            }
            System.out.println("Blad aproksymacji = " + approximationError(apprRes,progRes,type));
        }
    }

    private double approximationError(double[] apprRes, double[] progRes, Type type)
    {
        double err = 0;

        for (int i = 0; i < results.get(type).size(); i++)
        {
            err += (progRes[i] - apprRes[i]) * (progRes[i] - apprRes[i]);

        }

        return err;
    }


    public void writeApproxToCsv(Map<Type,double[]> approxResults){
        try {
            Power power;
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("approxwyniki_"+mode.toString()+".csv"));
            for (Type type :
                    results.keySet()) {
                double[] apprRes = new double[results.get(type).size()];
                double[] progRes = new double[results.get(type).size()];
                bufferedWriter.write("\n\n");
                bufferedWriter.write(type.toString() +","+ mode.toString()+"\n");
                bufferedWriter.write("Współczynniki wielomianu:\n");
                for(int i =0; i< approxResults.get(type).length; i++) {

                    bufferedWriter.write(approxResults.get(type)[i] + " x^" + (approxResults.get(type).length - i - 1) + ",");
                }
                bufferedWriter.write("\n");
                bufferedWriter.write("x,f(x),blad wzgledny,blad bezwzgledny\n");
                for (Integer agentsNum :
                        new TreeSet<>(results.get(type).keySet())) {
                    double[] pom = approxResults.get(type);
                    double temp = 0.0;
                    for (int w =0; w < pom.length; w++)
                    {
                        power = new Power(pom.length-w-1);
                        temp += (power.value(ApproximationGenerator.getEquationNumber(agentsNum)))*pom[w];
                    }
                    bufferedWriter.write(ApproximationGenerator.getEquationNumber(agentsNum) + "," + temp+","+(Math.abs(results.get(type).get(agentsNum).getExecutionTime()-temp))/results.get(type).get(agentsNum).getExecutionTime()+","+Math.abs(results.get(type).get(agentsNum).getExecutionTime()-temp)+"\n");
                    apprRes[agentsNum-startAgentNum] = temp;
                    progRes[agentsNum-startAgentNum] = results.get(type).get(agentsNum).getExecutionTime();
                }
                bufferedWriter.write("Blad aproksymacji = " + approximationError(apprRes,progRes,type)+"\n");
                double[] highExec = approxResults.get(type);
                BigDecimal highExecResult = new BigDecimal(0.0);
                BigDecimal equationsCount = new BigDecimal(100000);
                System.out.println("BEFORE");
                for(int w =0; w<highExec.length; w++){
                    highExecResult = highExecResult.add(equationsCount.pow(highExec.length-w-1).multiply(BigDecimal.valueOf(highExec[w])));
                    System.out.println(highExecResult);
                }
                bufferedWriter.write("Czas wykonania dla 100000:,"+highExecResult);
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] generateValue(Type type, Mode mode)
    {
        double[] arguments = new double[results.get(type).size()];
        double[] values = new double[results.get(type).size()];

        for (int i : results.get(type).keySet())
        {
            arguments[i-startAgentNum] = ApproximationGenerator.getEquationNumber(i);
            values[i-startAgentNum] = getTime(results.get(type).get(i));
        }

        Approximation appr = new Approximation(arguments, values, type);
        appr.calculate();

        return appr.getResults();

    }

    public double getTime(AggregatedResults ar)
    {
        return mode == Mode.Execution ? ar.getGenerationTime() : ar.getExecutionTime();
    }


    public static int getEquationNumber(int agentsNumber){
        int result = 0;
        for(int i=0; i<=agentsNumber; i++){
            for(int j =0; j<=agentsNumber-i; j++){
                result++;
            }
        }
        return result;
    }
}
