package model;

public enum Status {
    GRAPH_NOT_LODAED {
        @Override
        public String toString() {
            return "Status: Graph not loaded.";
        }
    },
    DIVIDED_GRAPH_LOADED {
        @Override
        public String toString() {
            return "Status: Divided graph loaded successfully!";
        }
    },
    RAW_GRAPH_LOADED {
        @Override
        public String toString() {
            return "Status: Raw graph loaded successfully!";
        }
    },
    X_OUT_OF_RANGE {
        @Override
        public String toString() {
            return "Status: x value is out of range!";
        }
    },
    K_OUT_OF_RANGE {
        @Override
        public String toString() {
            return "Status: k value is out of range!";
        }
    }
}
