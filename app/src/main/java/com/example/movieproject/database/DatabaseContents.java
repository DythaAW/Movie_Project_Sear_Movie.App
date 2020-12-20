package com.example.movieproject.database;

public enum DatabaseContents {
    DATABASE("login.db"),
    TABLE_USERS("tbl_users");

    private String name;
    private DatabaseContents(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}