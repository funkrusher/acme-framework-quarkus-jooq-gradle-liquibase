package org.acme.util.panache;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import org.acme.entity.ProductEntity;
import org.acme.util.query.Filter;
import org.acme.util.query.QueryParameters;
import org.acme.util.query.Sorter;
import org.acme.util.query.SorterOperator;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanacheQueryFactory<T extends PanacheEntityBase> {

    private Class<T> panacheEntityClass;

    public PanacheQueryFactory(Class<T> panacheEntityClass) {
        this.panacheEntityClass = panacheEntityClass;
    }

    public PanacheQuery<T> createFromQueryParameters(QueryParameters queryParameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Apply sorters to the query
        Sort sort = Sort.empty();
        if (queryParameters.getSorter() != null) {
            Sorter sorter = queryParameters.getSorter();
            if (sorter.getOperator().equals(SorterOperator.ASC)) {
                sort = Sort.by(sorter.getField(), Sort.Direction.Ascending);
            } else if (sorter.getOperator().equals(SorterOperator.DESC)) {
                sort = Sort.by(sorter.getField(), Sort.Direction.Descending);
            }
        }

        // Create a PanacheQuery object
        Method findAllMethod = panacheEntityClass.getDeclaredMethod("findAll");

        PanacheQuery<T> panacheQuery = (PanacheQuery<T>) findAllMethod.invoke(sort);

        // Set the page and pageSize
        panacheQuery = panacheQuery.page(queryParameters.getPage(), queryParameters.getPageSize());

        // Create the filters
        if (queryParameters.getFilters().size() > 0) {
            for (Filter filter : queryParameters.getFilters()) {
                Map<String, Object> filterValues = new HashMap<>();
                for (Map.Entry<String, String> entry : filter.getValues().entrySet()) {
                    Object value = convertFieldValueToExpectedType(
                            filter.getFilterName(), entry.getKey(), entry.getValue());
                    filterValues.put(entry.getKey(), value);
                }
                panacheQuery = panacheQuery.filter(filter.getFilterName(), filterValues);
            }
        }
        return panacheQuery;
    }


    protected Object convertFieldValueToExpectedType(String filterName, String fieldName, String fieldValue) {
        FilterDef[] filterDefAnnotations = panacheEntityClass.getAnnotationsByType(FilterDef.class);
        for (FilterDef filterDef : filterDefAnnotations) {
            if (filterDef.name().equals(filterName)) {
                for (ParamDef paramDef : filterDef.parameters()) {
                    if (paramDef.name().equals(fieldName)) {
                        Class<?> expectedType = paramDef.type();
                        return parse(fieldValue, expectedType);
                    }
                }
            }
        }
        return fieldValue;
    }

    protected Object parse(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value);
        } else {
            throw new IllegalArgumentException("Unsupported parameter type: " + type.getName());
        }
    }

}
