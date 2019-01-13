package ug.pprotocols;

public enum Type {

    LIBRARY_SPARSE_M_3(0D){
        @Override
        public String toString(){
            return "LIBRARY_SPARSE_M_3";
        }
    },
    LIBRARY_SPARSE_M_2(0D){
        @Override
        public String toString(){
            return "LIBRARY_SPARSE_M_2";
        }
    },
    LIBRARY_SPARSE_M_1(0D){
        @Override
        public String toString() {return "LIBRARY_SPARSE_M_1";}
    },
    GAUSS_SPARSE(0D){
        @Override
        public String toString() {return "Gauss_Sparse";}
    };

    private final double precision;

    Type(double precision){
        this.precision = precision;
    }

    public double getPrecision() {
        return precision;
    }
}
