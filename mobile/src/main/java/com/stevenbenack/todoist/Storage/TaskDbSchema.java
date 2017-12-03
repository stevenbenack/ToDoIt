package com.stevenbenack.todoist.Storage;


import org.joda.time.DateTime;

public class CrimeDbSchema {
    public static final class TaskTable {
        public static final String NAME = "toDoTask";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String CREATED = "created";
            public static final String DUE = "due";
            public static final String PRIORITY = "priority";
            public static final String DONE = "done";
        }
    }
}
