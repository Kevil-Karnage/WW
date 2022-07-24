package com.program.prediction;

public enum MatchClass {
    S(6, 5),
    A(5, 10),
    B(4, 30),
    C(3, 60),
    D(2, 120),
    LAST(1, -1);

    final int rank;
    final int minPos;

    MatchClass(int rank, int minPos) {
        this.rank = rank;
        this.minPos = minPos;
    }

    public static MatchClass indexOf(int pos) {
        if (pos == -1) {
            return MatchClass.LAST;
        }
        MatchClass[] matchClasses = MatchClass.values();
        for (int i = 0; i < matchClasses.length - 1; i++) {
            // если позиция команды меньше(выше в рейтинге) чем у enuma
            if (pos < matchClasses[i].minPos) {
                // то возвращаем его
                return matchClasses[i];
            }
        }
        // если из первых 5 ничего не подошло, то возвращаем последний
        return LAST;
    }
}