package com.example.movieproject.database;

import java.util.List;

public interface Database {

    public List<Object> select(String queryString);
    public int insert(String tableName, Object content);
    boolean update(String tableName, Object content);
    boolean delete(String tableName, int id);
    boolean execute(String queryString);
}
