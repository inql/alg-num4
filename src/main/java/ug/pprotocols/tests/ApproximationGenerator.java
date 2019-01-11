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


    public double[] generateValue(Type type)
    {
        double[] arguments = new double[results.get(type).size()];
        double[] values = new double[results.get(type).size()];

        int k = 0;
        for (int i : results.get(type).keySet())
        {
            arguments[k] = getEquationNumber(i);
            values[k] = results.get(type).get(i).getExecutionTime();
            k++;
        }

        Approximation appr = new Approximation(arguments, values, type);
        appr.Calculate();

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



}
