package ug.pprotocols.matrix;

import java.util.Objects;

public class Case{

    private final int yesVoters;
    private final int noVoters;
    private final int totalVoters;
    private final int undecidedVoters;

    public Case(int yesVoters, int noVoters, int totalVoters) {
        this.yesVoters = yesVoters;
        this.noVoters = noVoters;
        this.totalVoters = totalVoters;
        this.undecidedVoters = totalVoters-yesVoters-noVoters;
        if(undecidedVoters<0)
            throw new IllegalArgumentException("Wrong number of voters!");
    }

    public int getNoVoters() {
        return noVoters;
    }

    public int getTotalVoters() {
        return totalVoters;
    }

    public int getYesVoters() {
        return yesVoters;
    }

    public int getUndecidedVoters() {
        return undecidedVoters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case aCase = (Case) o;
        return yesVoters == aCase.yesVoters &&
                noVoters == aCase.noVoters &&
                totalVoters == aCase.totalVoters &&
                undecidedVoters == aCase.undecidedVoters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(yesVoters, noVoters, totalVoters, undecidedVoters);
    }

    @Override
    public String toString() {
        return "Case{" +
                "yesVoters=" + yesVoters +
                ", noVoters=" + noVoters +
                ", totalVoters=" + totalVoters +
                ", undecidedVoters=" + undecidedVoters +
                '}';
    }
}
