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
    JACOBIAN_MINUS6(1E-6){
        @Override
        public String toString(){
            return "JACOBIAN_MINUS6";
        }
    },
    GAUSS_SEIDEL_MINUS6(1E-6){
        @Override
        public String toString(){
            return "GAUSS_SEIDEL_MINUS6";
        }
    },
    JACOBIAN_MINUS10(1E-10){
        @Override
        public String toString(){
            return "JACOBIAN_MINUS10";
        }
    },
    GAUSS_SEIDEL_MINUS10(1E-10){
        @Override
        public String toString(){
            return "GAUSS_SEIDEL_MINUS10";
        }
    },
    JACOBIAN_MINUS14(1E-14){
        @Override
        public String toString(){
            return "JACOBIAN_MINUS14";
        }
    },
    GAUSS_SEIDEL_MINUS14(1E-14){
        @Override
        public String toString(){
            return "GAUSS_SEIDEL_MINUS14";
        }
    };

    private final double precision;

    Type(double precision){
        this.precision = precision;
    }

    public double getPrecision() {
        return precision;
    }
}
