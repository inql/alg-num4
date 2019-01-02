package ug.pprotocols.algorithm;

import ug.pprotocols.datatypes.DataType;
import ug.pprotocols.datatypes.MatrixCompatible;
import ug.pprotocols.datatypes.MatrixCompatibleFactory;
import ug.pprotocols.matrix.Case;

import java.util.Map;
import java.util.Random;

public class Mcarlo {

    final Random random;
    final int numOfIterations;


    public Mcarlo(int numOfIterations)
    {
        random = new Random();
        this.numOfIterations = numOfIterations;
    }

    public State[] createArrayOfAgents(int total, int Y, int N)
    {
        State[] allAgents = new State[total];
        for (int i = 0; i < Y; i++)
        {
            allAgents[i] = State.YES;
        }
        for (int i = Y; i < Y+N; i++)
        {
            allAgents[i] = State.NO;
        }
        for (int i = N+Y; i < total; i++)
        {
            allAgents[i] = State.UNDECIDED;
        }


        return allAgents;

    }
    public MatrixCompatible[] getAllProbabilities(Map<Integer,Case> allCases){

        MatrixCompatible[] allProbabilities = new MatrixCompatibleFactory(DataType.DOUBLE).createArray(allCases.size());

        for(int i = 0; i<allCases.size(); i++){
            allProbabilities[i] = countProbability(allCases.get(i));
        }

        return allProbabilities;
    }

    public MatrixCompatible countProbability(Case caseOfProgram) {

        MatrixCompatibleFactory matrixCompatibleFactory = new MatrixCompatibleFactory(DataType.DOUBLE);
        State[] motherArray = createArrayOfAgents(caseOfProgram.getTotalVoters(),caseOfProgram.getYesVoters(), caseOfProgram.getNoVoters());
        int countYes = 0;

        if (motherArray.length < 2)
        {
            if (motherArray[0] == State.YES)
                return matrixCompatibleFactory.createWithNominator(1D);
            else return matrixCompatibleFactory.createWithNominator(0D);
        }

        for (int i = 0; i < numOfIterations; i++)
        {
            State[] allAgents = motherArray.clone();

            while (!areSameStatesArray(allAgents))
            {
                int first = random.nextInt(motherArray.length);
                int second;
                do{
                    second = random.nextInt(motherArray.length);
                } while (first == second);

                State temp = changePair(allAgents[first],allAgents[second]);
                allAgents[second] = changePair(allAgents[second],allAgents[first]);
                allAgents[first] = temp;

            }
            if (allAgents[0] == State.YES)
                countYes++;

        }



        return matrixCompatibleFactory.createWithNominator((double)countYes/numOfIterations);

    }


    public State changePair(State o1, State o2)
    {
        if ((o1 == State.YES && o2 == State.UNDECIDED) || (o2 == State.YES && o1 == State.UNDECIDED))
            return State.YES;
        else if ((o1 == State.YES && o2 == State.NO) || (o2 == State.YES && o1 == State.NO))
            return State.UNDECIDED;
        else if ((o1 == State.UNDECIDED && o2 == State.NO) || (o2 == State.UNDECIDED && o1 == State.NO))
            return State.NO;

        return o1;
    }



    public boolean areSameStatesArray(State[] a){
        for(int i=1; i<a.length; i++){
            if(a[0] != a[i]){
                return false;
            }
        }

        return true;
    }




}
