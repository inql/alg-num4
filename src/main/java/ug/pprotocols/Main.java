package ug.pprotocols;

import ug.pprotocols.algorithm.Approximation;
import ug.pprotocols.tests.AggregatedResults;
import ug.pprotocols.tests.ApproximationGenerator;
import ug.pprotocols.tests.Mode;
import ug.pprotocols.tests.ResultGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Map<Type, Map<Integer, AggregatedResults>> results = generateCsv();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("wyniki.csv"));
            bufferedWriter.write("NOTE:,For agents count higher than 15, montecarlo isnt generated\n");
            for (Type type :
                    results.keySet()) {
                bufferedWriter.write("\n\n");
                bufferedWriter.write(type.toString() + "\n");
                bufferedWriter.write("liczba agentów,błąd bezwgledny(max),błąd bezwzględny(średnia),czas wykonania(generowanie),czas wykonania(obliczanie),ilość wykonań,\n");
                for (Integer agentsNum :
                        new TreeSet<>(results.get(type).keySet())) {
                    bufferedWriter.write("\n"+agentsNum + "," + results.get(type).get(agentsNum).toString());
                }

            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Mode mode : Mode.values()) {

            ApproximationGenerator ag = new ApproximationGenerator(results, mode);
            Map<Type, double[]> approxResults = ag.gen();
            ag.putFunctions(approxResults);
            ag.findDifferences(approxResults);
            ag.writeApproxToCsv(approxResults);

        }

    }

    public static Map<Type, Map<Integer, AggregatedResults>> generateCsv() {

        Map<Integer, Integer> testScope = new HashMap<Integer, Integer>() {{
        }};
        for(int i =15; i<=40; i++){
            testScope.put(i,1);
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
