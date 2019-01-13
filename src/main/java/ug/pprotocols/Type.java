package ug.pprotocols;

public enum Type {

    GAUSS(0D){
        @Override
        public String toString(){
            return "GAUSS";
        }
    },
    GAUSS_SPARSE(0D){
        @Override
        public String toString(){
            return "GAUSS_SPARSE";
        }
    },
    GAUSS_SEIDEL_MINUS10(1E-10){
        @Override
        public String toString(){
            return "GAUSS_SEIDEL_MINUS10";
        }
    },
    LIBRARY_SPARSE(0D){
        @Override
        public String toString() {return "LIBRARY_SPARSE";}
    },
    LIBRARY_SPARSE_FIELD(0D){
        @Override
        public String toString() {return "LIBRARY_SPARSE_FIELD";}
    };

    private final double precision;

    Type(double precision){
        this.precision = precision;
    }

    public double getPrecision() {
        return precision;
    }
}
