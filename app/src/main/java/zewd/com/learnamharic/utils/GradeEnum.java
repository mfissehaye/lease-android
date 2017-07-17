package zewd.com.learnamharic.utils;

public enum GradeEnum {
    A_PLUS(95) {
        @Override
        public String toString() {
            return "A+";
        }
    },
    A(90) {
        @Override
        public String toString() {
            return "A";
        }
    },
    A_MINUS(85) {
        @Override
        public String toString() {
            return "A-";
        }
    },
    B_PLUS(80) {
        @Override
        public String toString() {
            return "B+";
        }
    },
    B(75) {
        @Override
        public String toString() {
            return "B";
        }
    },
    B_MINUS(70) {
        @Override
        public String toString() {
            return "B-";
        }
    },
    C_PLUS(65) {
        @Override
        public String toString() {
            return "C+";
        }
    },
    C(60) {
        @Override
        public String toString() {
            return "C";
        }
    },
    C_MINUS(55) {
        @Override
        public String toString() {
            return "C-";
        }
    },
    D_PLUS(50) {
        @Override
        public String toString() {
            return "D+";
        }
    },
    D(45) {
        @Override
        public String toString() {
            return "D";
        }
    },
    D_MINUS(35) {
        @Override
        public String toString() {
            return "D-";
        }
    },
    F(0) {
        @Override
        public String toString() {
            return "F";
        }
    };

    private final float minimumScore;

    GradeEnum(float minimumScore) {
        this.minimumScore = minimumScore;
    }

    public static GradeEnum getGrade(float score) {

        for(int i = 0; i < values().length; i++) {

            GradeEnum grade = values()[i];

            if(score > grade.minimumScore) {

                return grade;
            }
        }
        return F;
    }
}