package ug.pprotocols;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.linear.SparseFieldMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import ug.pprotocols.datatypes.DoubleComp;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.matrix.Case;
import ug.pprotocols.matrix.MatrixGenerator;
import ug.pprotocols.tests.AggregatedResults;
import ug.pprotocols.tests.ResultGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

//        Map<Type, Map<Integer, AggregatedResults>> results = generateCsv();
//
//        try {
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("wyniki.csv"));
//            bufferedWriter.write("NOTE:,For agents count higher than 15, montecarlo isnt generated\n");
//            for (Type type :
//                    results.keySet()) {
//                bufferedWriter.write("\n\n");
//                bufferedWriter.write(type.toString() + "\n");
//                bufferedWriter.write("liczba agentów,błąd bezwgledny(max),błąd bezwzględny(średnia),roznica do MonteCarlo,czas wykonania,ilość wykonań,\n");
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
        MatrixGenerator matrixGenerator = new MatrixGenerator(new Case(0,0,3));
        System.out.println(matrixGenerator.generateEquation());
    }

    public static Map<Type, Map<Integer, AggregatedResults>> generateCsv() {

        Map<Integer, Integer> testScope = new HashMap<Integer, Integer>() {{
        }};
        for(int i =3; i<=30; i++){
            testScope.put(i,100-(i*3));
        }

        Type[] types = Type.values();
        Arrays.sort(types);
        List<Type> testCases = Arrays.asList(types);

        ResultGenerator resultGenerator = new ResultGenerator(testScope);
        return resultGenerator.doTests(testCases,100000);
    }
}
