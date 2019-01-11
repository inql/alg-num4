package ug.pprotocols;

import ug.pprotocols.algorithm.Approximation;
import ug.pprotocols.tests.AggregatedResults;
import ug.pprotocols.tests.ResultGenerator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
    /*
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
        */

        double[] arguments = { 0.0,0.25,0.5,0.75,1.0 };
        double[] values = { 1.0,1.284,1.6487,2.117,2.7183 };

        Approximation apprGaussSeidel = new Approximation(arguments, values, Type.GAUSS_SEIDEL_MINUS10);
        apprGaussSeidel.Calculate();

        double[] something = apprGaussSeidel.getResults();

        for (int i = 0; i < something.length; i++)
        {
            System.out.println(something[i]);
        }
        System.out.println("Done");
        // Wyniki odrobine się roznia od tych w slajdach z wykladu - dobrze vs niedobrze?
        // na wykladzie jest :
        // a0 = 1.00051
        // a1 = 0.8647
        // a2 = 0.8432
    }

    public static Map<Type, Map<Integer, AggregatedResults>> generateCsv() {

        Map<Integer, Integer> testScope = new HashMap<Integer, Integer>() {{
        }};
        for(int i =3; i<=10; i++){
            testScope.put(i,100-(i*3));
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
