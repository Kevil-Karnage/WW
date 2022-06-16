package com.program.prediction;

public enum MatchClass {
    S(5, 5),
    A(4, 15),
    B(3, 40),
    C(2, 80),
    D(1, 160),
    E(0, -1);

    final int rank;
    final int minPos;

    MatchClass(int rank, int minPos) {
        this.rank = rank;
        this.minPos = minPos;
    }

    public static MatchClass indexOf(int pos) {
        MatchClass[] matchClasses = MatchClass.values();
        for (int i = 0; i < matchClasses.length - 1; i++) {
            // если позиция команды меньше(выше в рейтинге) чем у enuma
            if (pos < matchClasses[i].minPos) {
                // то возвращаем его
                return matchClasses[i];
            }
        }
        // если из первых 5 ничего не подошло, то возвращаем последний
        return E;
    }
}
