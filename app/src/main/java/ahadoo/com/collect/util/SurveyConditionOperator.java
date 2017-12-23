package ahadoo.com.collect.util;

enum SurveyConditionOperator {
    OR {
        @Override
        public String toString() {
            return "||";
        }
    },
    AND {
        @Override
        public String toString() {
            return "&&";
        }
    },
    EQ {
        @Override
        public String toString() {
            return "==";
        }
    },
    NEQ {
        @Override
        public String toString() {
            return "!=";
        }
    },
    LE {
        @Override
        public String toString() {
            return "<=";
        }
    },
    LT {
        @Override
        public String toString() {
            return "<";
        }
    },
    GE {
        @Override
        public String toString() {
            return ">=";
        }
    },
    GT {
        @Override
        public String toString() {
            return ">";
        }
    }
}
