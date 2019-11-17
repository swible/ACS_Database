package ru.swible.pojo;

import java.util.LinkedList;
import java.util.List;

public class SqlSelectResult extends SqlResult {
    private List<String> colNames = new LinkedList<>();
    private List<List<String>> colValues = new LinkedList<>();

    public List<String> getColNames() {
        return colNames;
    }

    public void setColNames(List<String> colNames) {
        this.colNames = colNames;
    }

    public List<List<String>> getColValues() {
        return colValues;
    }

    public void setColValues(List<List<String>> colValues) {
        this.colValues = colValues;
    }
}
