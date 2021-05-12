package com.example.user.criminalintent.database;

/**
 * Created by user on 2019/9/11.
 */

public class CrimeDbSchema {
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "sovled";
            public static final String SUSPECT="suspect";
        }

    }
}
