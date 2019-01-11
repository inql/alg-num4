package ug.pprotocols;

import ug.pprotocols.algorithm.Approximation;
import ug.pprotocols.tests.AggregatedResults;
import ug.pprotocols.tests.ApproximationGenerator;
import ug.pprotocols.tests.ResultGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Map<Type, Map<Integer, AggregatedResults>> results = generateCsv();

//        try {
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("wyniki.csv"));
//            bufferedWriter.write("NOTE:,For agents count higher than 15, montecarlo isnt generated\n");
//            for (Type type :
//                    results.keySet()) {
//                bufferedWriter.write("\n\n");
//                bufferedWriter.write(type.toString() + "\n");
//                bufferedWriter.write("liczba agentów,błąd bezwgledny(max),błąd bezwzględny(średnia),czas wykonania(generowanie),czas wykonania(obliczanie),ilość wykonań,\n");
//                for (Integer agentsNum :
//                        new TreeSet<>(results.get(type).keySet())) {
//                    bufferedWriter.write("\n"+agentsNum + "," + results.get(type).get(agentsNum).toString());
//                }
//
//            }
//            bufferedWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ApproximationGenerator ag = new ApproximationGenerator(results);
        Map<Type,double[]> approxResults = ag.gen();
        ag.putFunctions(approxResults);
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

    public static Map<Type, Map<Integer, AggregatedResults>> generateCsv() {

        Map<Integer, Integer> testScope = new HashMap<Integer, Integer>() {{
        }};
        for(int i =3; i<=16; i++){
            testScope.put(i,10);
        }

        Type[] types = Type.values();
//        Type[] types = new Type[1];
//        types[0] = Type.LIBRARY_SPARSE;
        Arrays.sort(types);
        List<Type> testCases = Arrays.asList(types);

        ResultGenerator resultGenerator = new ResultGenerator(testScope);
        return resultGenerator.doTests(testCases,100000);
    }


}
