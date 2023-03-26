package org.acme.util.query;


public class Sorter {
    private final String field;
    private final SorterOperator operator;

    public Sorter(String field, SorterOperator operator) {
        this.field = field;
        this.operator = operator;
    }

    public String getField() {
        return field;
    }

    public SorterOperator getOperator() {
        return operator;
    }


    public static Sorter parseSorterString(String filterString) {
        if (filterString == null || filterString.isEmpty()) {
            return null;
        }
        String[] parts = filterString.split(":");
        if (parts.length >= 1) {
            String field = parts[0];
            SorterOperator operator = SorterOperator.fromString(parts[1]);
            if (operator != null) {
                return new Sorter(field, operator);
            }
        }
        return null;
    }
}