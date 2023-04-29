package org.acme.util.query;

public enum SorterOperator {

    ASC("asc"),
    DESC("desc");

    private final String symbol;

    SorterOperator(String symbol)    {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static SorterOperator fromString(String symbol) {
        for (SorterOperator operator : SorterOperator.values()) {
            if (operator.getSymbol().equals(symbol)) {
                return operator;
            }
        }
        return null;
    }
}