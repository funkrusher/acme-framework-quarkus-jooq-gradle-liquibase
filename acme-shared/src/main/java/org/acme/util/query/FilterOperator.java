package org.acme.util.query;

public enum FilterOperator {
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    CONTAINS("contains");

    private final String symbol;

    FilterOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static FilterOperator fromString(String symbol) {
        for (FilterOperator operator : FilterOperator.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        return null;
    }
}