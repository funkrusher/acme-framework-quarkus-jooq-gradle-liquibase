package org.acme.util.query;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class Filter {
    private final String filterName;
    private final LinkedHashMap<String, String> values;

    public Filter(String filterName, LinkedHashMap<String, String> values) {
        this.filterName = filterName;
        this.values = values;
    }

    public String getFilterName() {
        return filterName;
    }

    public LinkedHashMap<String, String> getValues() {
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
                    if (parts.length >= 1) {
                        String filterName = parts[0];
                        List<String> keyValues = parts.length > 1 ? Arrays.asList(parts[1].split("\\|")) : new ArrayList<>();

                        LinkedHashMap values = new LinkedHashMap<String, String>();
                        for (String kv : keyValues) {
                            String[] splits = kv.split("=");
                            values.put(splits[0], splits[1]);
                        }
                        filters.add(new Filter(filterName, values));
                    }
                });
        return filters;
    }
}