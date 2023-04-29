package org.acme.daos;

import org.acme.jooq.JooqContext;
import org.acme.util.request.RequestContext;
import org.jooq.*;
import org.jooq.Record;

import java.util.Collection;

import static org.jooq.impl.DSL.row;

/**
 * A common base-class for DAOs
 * <p>
 * This type is implemented by DAO classes to provide a common context-scoped API
 * for common actions
 * </p>
 * @param <R> The generic record type.
 * @param <T> The generic primary key type. This is a regular
 *            <code>&lt;T&gt;</code> type for single-column keys, or a
 *            {@link Record} subtype for composite keys.
 */
public abstract class AbstractBaseDAO<R extends UpdatableRecord<R>, T> {

    private JooqContext jooqContext;
    private final Table<R> table;

    // -------------------------------------------------------------------------
    // Constructors and initialisation
    // -------------------------------------------------------------------------

    protected AbstractBaseDAO(JooqContext jooqContext, Table<R> table) {
        this.jooqContext = jooqContext;
        this.table = table;
    }

    // ------------------------------------------------------------------------
    // Template methods for subclasses
    // ------------------------------------------------------------------------

    /**
     * Extract the ID value of the given record
     *
     * @param record record of the table
     * @return value of the id field(s) of the given record.
     */
    protected abstract T getId(R record);

    // ------------------------------------------------------------------------
    // Helper methods for subclasses, for extracting/resolving common meta.
    // ------------------------------------------------------------------------

    protected <L> Field<L> autoIncrementField() {
        // Get the fields of the table that are auto-increment
        Field<L>[] autoIncrementFields = table().fieldStream()
                .filter(f -> f.getDataType().identity())
                .toArray(Field[]::new);
        if (autoIncrementFields.length > 0) {
            return autoIncrementFields[0];
        } else {
            return null;
        }
    }

    protected Field<?>[] pk() {
        UniqueKey<?> key = table.getPrimaryKey();
        return key == null ? null : key.getFieldsArray();
    }

    protected T compositeKeyRecord(Object... values) {
        UniqueKey<R> key = table().getPrimaryKey();
        if (key == null)
            return null;

        TableField<R, Object>[] fields = (TableField<R, Object>[]) key.getFieldsArray();
        Record result = ctx().newRecord(fields);

        for (int i = 0; i < values.length; i++)
            result.set(fields[i], fields[i].getDataType().convert(values[i]));

        return (T) result;
    }

    protected Condition equal(Field<?>[] pk, T id) {
        if (pk.length == 1)
            return ((Field<Object>) pk[0]).equal(pk[0].getDataType().convert(id));

            // [#2573] Composite key T types are of type Record[N]
        else
            return row(pk).equal((Record) id);
    }

    protected Condition equal(Field<?>[] pk, Collection<T> ids) {
        if (pk.length == 1)
            if (ids.size() == 1)
                return equal(pk, ids.iterator().next());
            else
                return ((Field<Object>) pk[0]).in(pk[0].getDataType().convert(ids));

            // [#2573] Composite key T types are of type Record[N]
        else
            return row(pk).in(ids.toArray(new Record[]{}));
    }

    // -------------------------------------------------------------------------
    // DAO API
    // -------------------------------------------------------------------------

    /**
     * Expose the dslContext this <code>DAO</code> is operating.
     *
     * @return the <code>DAO</code>'s underlying <code>dslContext</code>
     */
    public DSLContext ctx() {
        return this.jooqContext.getCtx();
    }

    /**
     * Expose the requestContext this <code>DAO</code> is operating.
     *
     * @return the <code>DAO</code>'s underlying <code>requestContext</code>
     */
    public RequestContext requestContext() {
        return this.jooqContext.getRequestContext();
    }

    /**
     * Expose the table this <code>DAO</code> is operating.
     *
     * @return the <code>DAO</code>'s underlying <code>table</code>
     */
    public Table<R> table() {
        return this.table;
    }

}
