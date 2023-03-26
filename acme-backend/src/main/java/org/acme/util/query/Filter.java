package org.acme.util.query;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filter {
    private final String field;
    private final FilterOperator operator;
    private final List<String> values;

    public Filter(String field, FilterOperator operator, List<String> values) {
        this.field = field;
        this.operator = operator;
        this.values = values;
    }

    public String getField() {
        return field;
    }

    public FilterOperator getOperator() {
        return operator;
    }

    public List<String> getValues() {
        return values;
    }

    public static List<Filter> parseFilterString(String filterString) {
        final List<Filter> filters = new ArrayList<>();
        if (filterString == null || filterString.isEmpty()) {
            return filters;
        }
        String[] filterParams = filterString.split(",");
        Arrays.stream(filterParams)
                .map(param -> param.split(":"))
                .forEach(parts -> {
                    if (parts.length >= 2) {
                        String field = parts[0];
                        FilterOperator operator = FilterOperator.fromString(parts[1]);
                        if (operator != null) {
                            List<String> values = parts.length > 2 ? Arrays.asList(parts[2].split("\\|")) : new ArrayList<>();
                            filters.add(new Filter(field, operator, values));
                        }
                    }
                });
        return filters;
    }
}