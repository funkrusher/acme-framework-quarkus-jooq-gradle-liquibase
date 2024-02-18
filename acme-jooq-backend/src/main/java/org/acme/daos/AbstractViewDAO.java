package org.acme.daos;

import org.acme.generated.AbstractDTO;
import org.acme.jooq.JooqContext;
import org.acme.util.query.*;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;

/**
 * A common base-class for read-only ViewDAOs
 * <p>
 * This type is implemented by DAO classes to provide a common API for common actions on a joined/paginated view
 * and returns DTOs that are mappable to this view.
 * </p>
 * @param <R> The generic record type.
 * @param <P> The generic DTO type.
 * @param <T> The generic primary key type. This is a regular
 *            <code>&lt;T&gt;</code> type for single-column keys, or a
 *            {@link Record} subtype for composite keys.
 */
public abstract class AbstractViewDAO<R extends UpdatableRecord<R>, P extends AbstractDTO, T> extends AbstractBaseDAO<R, T> {

    // -------------------------------------------------------------------------
    // Constructors and initialisation
    // -------------------------------------------------------------------------

    protected AbstractViewDAO(JooqContext jooqContext, Table<R> table) {
        super(jooqContext, table);
    }

    private SelectJoinStep<Record> getViewQuery() {
        // prepare our view-query with the fields/joins of the subclass.
        return ctx()
                .select(getViewFields())
                .from(getViewJoins());
    }

    // ------------------------------------------------------------------------
    // Template methods for subclasses
    // ------------------------------------------------------------------------

    /**
     * Define all fields in the different tables that are part of this view, and should be resolved
     * into the DTOs that this view is returning.
     *
     * @return fields
     */
    abstract protected List<Field<?>> getViewFields();

    /**
     * Define all joins on the different tables that are part of this view, and should be resolved
     * into the DTOs that this view is returning.
     *
     * @return joins
     */
    abstract protected TableOnConditionStep<Record> getViewJoins();

    /**
     * Define the mapping of the resulting records to the DTOs that this view is returning.
     *
     * @param records resulting records containing joined table data.
     * @return DTOs
     */
    abstract protected List<P> recordsToView(List<Record> records);

    // -------------------------------------------------------------------------
    // DAO API
    // -------------------------------------------------------------------------

    /**
     * Find a fully resolved DTO for this view by ID.
     *
     * @param id The ID of a record in the underlying table
     * @return A view-resolved DTO for the record of the underlying table given its ID, or
     * <code>null</code> if no record was found.
     * @throws DataAccessException if something went wrong executing the query
     */
    public P findById(T id) throws DataAccessException {
        Field<?>[] pk = pk();
        if (pk != null) {
            Result<Record> result = getViewQuery()
                    .where(equal(pk, id))
                    .fetch();
            if (result != null && result.size() > 0) {
                return recordsToView(result).get(0);
            }
        }
        return null;
    }

    /**
     * Find a fully resolved DTO for this view by ID.
     *
     * @param id The ID of a record in the underlying table
     * @return A view-resolved DTO for the record of the underlying table given its ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public Optional<P> findOptionalById(T id) throws DataAccessException {
        return Optional.ofNullable(findById(id));
    }

    /**
     * Performs a paginated query for this view
     *
     * @param queryParameters The paginated query
     * @return list of view-resolved DTOs
     * @throws DataAccessException if something went wrong executing the query
     */
    public List<P> query(final QueryParameters queryParameters) throws DataAccessException {
        Collection<SortField<?>> sortFields = new ArrayList<>();
        if (queryParameters.getSorter() != null) {
            Sorter sorter = queryParameters.getSorter();
            Field<?> sorterField = table().field(field(name(sorter.getField())));
            if (sorter.getOperator() == SorterOperator.ASC) {
                sortFields.add(sorterField.sort(SortOrder.ASC));
            } else if (sorter.getOperator() == SorterOperator.ASC) {
                sortFields.add(sorterField.sort(SortOrder.DESC));
            }
        }

        // TODO: this filter must use the getViewFields, instead of the table().field approach
        // so it could also filter on the joined tables fields, which is currently not possible

        // also see: https://blog.jooq.org/a-functional-programming-approach-to-dynamic-sql-with-jooq/
        Collection<Condition> filterFields = new ArrayList<>();
        if (queryParameters.getFilters().size() > 0) {
            for (Filter filter : queryParameters.getFilters()) {
                Field<?> filterField = table().field(field(name(filter.getField())));
                Class<?> type = filterField.getType();

                if (String.class.isAssignableFrom(type)) {
                    String value = filter.getValues().get(0);
                    Field<String> field = table().field(field(name(filter.getField()), String.class));
                    filterFields.add(field.eq(value));
                } else if (Integer.class.isAssignableFrom(type)) {
                    Integer value = Integer.parseInt(filter.getValues().get(0));
                    Field<Integer> field = table().field(field(name(filter.getField()), Integer.class));
                    filterFields.add(field.eq(value));
                } else if (Long.class.isAssignableFrom(type)) {
                    Long value = Long.parseLong(filter.getValues().get(0));
                    Field<Long> field = table().field(field(name(filter.getField()), Long.class));
                    filterFields.add(field.eq(value));
                } else if (BigDecimal.class.isAssignableFrom(type)) {
                    BigDecimal value = new BigDecimal(filter.getValues().get(0));
                    Field<BigDecimal> field = table().field(field(name(filter.getField()), BigDecimal.class));
                    if (filter.getOperator() == FilterOperator.EQUALS) {
                        filterFields.add(field.eq(value));
                    } else if (filter.getOperator() == FilterOperator.GREATER_THAN_OR_EQUALS) {
                        filterFields.add(field.ge(value));
                    }
                }
            }
        }

        // Remote-Paginate Query that copes with the typical OFFSET/LIMIT trick for joined tables.
        //
        // 1. join all tables for filtering but only select the grouped-ids of the main-table where the filters
        // have matched, so we can make sure that the OFFSET/LIMIT is working correctly.
        //
        // 2. select all full joined entries for the found grouped-ids of the main-table (those may be more entries
        // than the LIMIT given, but this is ok then as they are combined in the upper layer.
        //
        var query = ctx()
                .select(pk())
                .from(getViewJoins())
                .where(DSL.and(filterFields))
                .groupBy(pk())
                .orderBy(sortFields)
                .offset(queryParameters.getPage())
                .limit(queryParameters.getPageSize());

        var records = query.fetchInto(table());
        List<T> ids = new ArrayList<T>();
        for (R record : records) {
            T id = getId(record);
            ids.add(id);
        }
        Field<?>[] pk = pk();
        var result = getViewQuery().where(equal(pk, ids)).fetch();

        return recordsToView(result);
    }
}
