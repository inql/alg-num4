package ug.pprotocols.tests;

import ug.pprotocols.Type;
import ug.pprotocols.algorithm.Approximation;

import java.util.HashMap;
import java.util.Map;

public class ApproximationGenerator {

    public final  Map<Type, Map<Integer, AggregatedResults>> results;

    public ApproximationGenerator(Map<Type, Map<Integer, AggregatedResults>> results) {
        this.results = results;
    }

    public Map<Type,double[]> gen()
    {
        Map<Type,double[]> typeMap = new HashMap<>();

        for (Type type: results.keySet())
        {
            typeMap.put(type,generateValue(type));

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

    public void findDifferences(Map<Type,double[]> approxResults)
    {
        for (Type type : results.keySet())
        {
            for (int i : results.get(type).keySet())
            {
                double[] pom = approxResults.get(type);
                double temp = 0.0;
                for (int w =0; w < pom.length; w++)
                {
                    temp += ApproximationGenerator.pow(ApproximationGenerator.getEquationNumber(i), pom.length - w - 1) * pom[w];
                }
                System.out.println(type + ", " + ApproximationGenerator.getEquationNumber(i));
                System.out.println("wartosc = " + temp);
                System.out.println("Roznica od wartosci z resultow " +  Math.abs(results.get(type).get(i).getExecutionTime()-temp));

            }
        }
    }


    public double[] generateValue(Type type)
    {
        double[] arguments = new double[results.get(type).size()];
        double[] values = new double[results.get(type).size()];

        for (int i : results.get(type).keySet())
        {
            arguments[i-3] = i;
            values[i-3] = results.get(type).get(i).getExecutionTime();
        }

        Approximation appr = new Approximation(arguments, values, type);
        appr.calculate();

        return appr.getResults();

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

    public static double pow(double x, int power){

        if(power<0)
            return pow(1.0/x,power-1);

        double result=1.0;

        for(int i=0;i<power;i++)
            result=x;

        return result;
    }



}
