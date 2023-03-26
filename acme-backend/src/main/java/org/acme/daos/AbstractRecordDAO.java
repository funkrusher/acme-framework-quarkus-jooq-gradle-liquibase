package org.acme.daos;

import org.acme.generated.AbstractDTO;
import org.acme.jooq.JooqContext;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.exception.DataAccessException;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.name;

/**
 * A common base-class for CRUD Record-DAOs
 * <p>
 * This type is implemented by DAO classes to provide a common API for CRUD actions on a table
 * and returns Records that are typed to this table.
 * </p>
 * @param <R> The generic record type.
 * @param <Y> The DTO type that this record is related to (helpful for automatic mapping)
 * @param <T> The generic primary key type. This is a regular
 *            <code>&lt;T&gt;</code> type for single-column keys, or a
 *            {@link Record} subtype for composite keys.
 */
public abstract class AbstractRecordDAO<R extends UpdatableRecord<R>, Y, T> extends AbstractBaseDAO<R, T> {

    // -------------------------------------------------------------------------
    // Constructors and initialisation
    // -------------------------------------------------------------------------

    protected AbstractRecordDAO(JooqContext jooqContext, Table<R> table) {
        super(jooqContext, table);
    }

    // ------------------------------------------------------------------------
    // Internal/Private Helper methods
    // ------------------------------------------------------------------------

    private List<R> transformToRecords(List<? extends Y> objects) {
        List<R> records = new ArrayList<>();
        boolean isDTO = false;
        if (objects.size() > 0) {
            if (objects.get(0) instanceof AbstractDTO) {
                isDTO = true;
            }
        }
        for (Y obj : objects) {
            if (isDTO) {
                // create new record for this pojo/table that has no change-mark for all its fields
                // then set the change-marks
                AbstractDTO pojo = (AbstractDTO) obj;
                R record = ctx().newRecord(table(), obj);
                record.changed(false);

                for (Field<?> field : record.fields()) {
                    boolean isModified = pojo.getModifiedFields().keySet().contains(field.getName());
                    if (isModified) {
                        record.changed(field.getName(), true);
                    }
                }
                records.add(record);
            } else {
                records.add(ctx().newRecord(table(), obj));
            }
        }
        return records;
    }

    private List<R> prepareInserts(List<R> records) {
        List<R> results = new ArrayList<>();
        for (R record : records) {
            results.add(prepareInsert(record));
        }
        return results;
    }

    private R prepareInsert(R record) {
        // Get the table definition from the record
        Table<?> table = record.getTable();

        // Get the fields of the table that are auto-increment
        Field<?>[] autoIncrementFields = table().fieldStream()
                .filter(f -> f.getDataType().identity())
                .toArray(Field[]::new);

        // Set the auto-increment fields to null using reflection
        for (Field<?> field : autoIncrementFields) {
            try {
                Field<T> recordField = (Field<T>) field;
                record.set(recordField, null);

                // note: setting changed=true, will also mark all fields as NULL that are NOT NULL in database,
                // and jooq would in that case not use the DEFAULT of the database, but will say that they
                // should not be null
                // record.changed(true);

            } catch (Exception e) {
                // Handle any exceptions that occur while setting the field to null
                // For example, if the field is not nullable, you may get a NullPointerException
                // You can log the exception or handle it in any other way that is appropriate for your application
                e.printStackTrace();
            }
        }
        return record;
    }

    private List<R> prepareUpdates(List<R> records) {
        List<R> results = new ArrayList<>();
        for (R record : records) {
            results.add(prepareUpdate(record));
        }
        return results;
    }

    private R prepareUpdate(R record) {
        // Set the auto-increment fields to null using reflection
        for (Field<?> pkField : pk()) {
            try {
                // primary key values are never allowed to be changed for an update!
                // the database will give an error, if we try to change them in an update clause
                // they are only allowed to be used in where-clause of the update.
                Field<T> recordField = (Field<T>) pkField;
                record.changed(recordField, false);

            } catch (Exception e) {
                // Handle any exceptions that occur while setting the field to null
                // For example, if the field is not nullable, you may get a NullPointerException
                // You can log the exception or handle it in any other way that is appropriate for your application
                e.printStackTrace();
            }
        }
        return record;
    }

    // -------------------------------------------------------------------------
    // DAO API
    // -------------------------------------------------------------------------

    /**
     * Performs an <code>INSERT</code> statement for a given DTO.
     *
     * @param dto The DTO to be inserted
     * @return inserted DTO with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> A insertAndReturnDTO(A dto) {
        return insertAndReturnDTOs(singletonList(dto)).get(0);
    }

    /**
     * Performs a <code>INSERT</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be inserted
     * @return inserted DTOs with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> List<A> insertAndReturnDTOs(A... dtos) {
        return insertAndReturnDTOs(asList(dtos));
    }

    /**
     * Performs a <code>INSERT</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be inserted
     * @return inserted DTOs with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> List<A> insertAndReturnDTOs(List<A> dtos) {
        List<R> records = transformToRecords(dtos);
        Result<R> result = insertAndReturn(records);
        int i = 0;
        for (R item : result) {
            Field<?> setField = records.get(i).field(autoIncrementField());
            records.get(i).setValue(setField, item.getValue(autoIncrementField()));
            i++;
        }
        int j = 0;
        for (R record : records) {
            A obj = dtos.get(j);
            record.into(obj);
            j++;
        }
        return dtos;
    }

    /**
     * Performs an <code>INSERT</code> statement for a given Record.
     *
     * @param record The record to be inserted
     * @return inserted record with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public R insertAndReturn(R record) {
        return insertAndReturn(singletonList(record)).get(0);
    }

    /**
     * Performs a <code>INSERT</code> statement for a given set of records.
     *
     * @param records The records to be inserted
     * @return inserted records with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public List<R> insertAndReturn(R... records) {
        return insertAndReturn(asList(records));
    }

    /**
     * Performs a <code>INSERT</code> statement for a given set of records.
     *
     * @param records The records to be inserted
     * @return inserted records with database-generated ID
     * @throws DataAccessException if something went wrong executing the query
     */
    public Result<R> insertAndReturn(List<R> records) {
        List<R> inserts = prepareInserts(records);

        int k = 1;
        InsertSetMoreStep step = ctx().insertInto(inserts.get(0).getTable())
                .set(inserts.get(0));
        if (inserts.size() > 1) {
            step.set(inserts.get(k));
            k++;
        }
        Result<R> result = step
                .returning(autoIncrementField())
                .fetch();
        return result;
    }

    /**
     * Performs a <code>INSERT</code> statement for a given DTO.
     *
     * @param dto The DTO to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> int insertDTO(A dto) throws DataAccessException {
        return insertDTOs(singletonList(dto))[0];
    }

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> int[] insertDTOs(A... dtos) throws DataAccessException {
        return insertDTOs(dtos);
    }

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> int[] insertDTOs(List<A> dtos) throws DataAccessException {
        List<R> records = transformToRecords(dtos);
        return insert(records);
    }

    /**
     * Performs a <code>INSERT</code> statement for a given record.
     *
     * @param record The record to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public int insert(R record) throws DataAccessException {
        return insert(singletonList(record))[0];
    }

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of records.
     *
     * @param records The records to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public int[] insert(R... records) throws DataAccessException {
        return insert(records);
    }

    /**
     * Performs a batch <code>INSERT</code> statement for a given set of records.
     *
     * @param records The records to be inserted
     * @throws DataAccessException if something went wrong executing the query
     */
    public int[] insert(List<R> records) throws DataAccessException {
        List<R> inserts = prepareInserts(records);
        if (inserts.size() == 1) {
            return new int[]{inserts.get(0).insert()};
        } else if (inserts.size() > 0) {
            // Execute a batch INSERT
            return ctx().batchInsert(inserts).execute();
        }
        return new int[]{0};
    }

    /**
     * Performs an <code>UPDATE</code> statement for a given DTO.
     *
     * @param dto The DTO to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void updateDTO(Y dto) throws DataAccessException {
        updateDTOs(singletonList(dto));
    }

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void updateDTOs(Y... dtos) throws DataAccessException {
        updateDTOs(asList(dtos));
    }

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void updateDTOs(List<? extends Y> dtos) throws DataAccessException {
        List<R> records = transformToRecords(dtos);
        update(records);
    }

    /**
     * Performs an <code>UPDATE</code> statement for a given record.
     *
     * @param record The record to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void update(R record) throws DataAccessException {
        update(singletonList(record));
    }

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of records.
     *
     * @param records The records to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void update(R... records) throws DataAccessException {
        update(asList(records));
    }

    /**
     * Performs a batch <code>UPDATE</code> statement for a given set of records.
     *
     * @param records The records to be updated
     * @throws DataAccessException if something went wrong executing the query
     */
    public void update(List<R> records) throws DataAccessException {
        List<R> updates = prepareUpdates(records);
        if (updates.size() > 1) {
            // Execute a batch UPDATE
            List<UpdateConditionStep<R>> conditions = new ArrayList<>();
            for (R record : updates) {
                conditions.add(ctx().update(table())
                        .set(record)
                        .where(equal(pk(), getId(record))));
            }
            ctx().batch(conditions).execute();
        } else if (updates.size() == 1) {
            // Execute a regular UPDATE
            ctx().update(table()).set(updates.get(0)).where(equal(pk(), getId(updates.get(0)))).execute();
        }
    }

    /**
     * Performs a <code>DELETE</code> statement for a DTO
     *
     * @param dto The DTO to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> void deleteDTO(A dto) throws DataAccessException {
        deleteDTOs(singletonList(dto));
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> void deleteDTOs(A... dtos) throws DataAccessException {
        deleteDTOs(asList(dtos));
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of DTOs.
     *
     * @param dtos The DTOs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public <A extends Y> void deleteDTOs(List<A> dtos) throws DataAccessException {
        List<R> records = transformToRecords(dtos);
        delete(records);
    }

    /**
     * Performs a <code>DELETE</code> statement for a record
     *
     * @param record The record to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void delete(R record) throws DataAccessException {
        delete(singletonList(record));
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of records.
     *
     * @param records The records to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void delete(R... records) throws DataAccessException {
        delete(asList(records));
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of records.
     *
     * @param records The records to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void delete(List<R> records) throws DataAccessException {
        if (records.size() > 1) {
            // Execute a batch DELETE
            ctx().batchDelete(records).execute();
        } else if (records.size() == 1) {
            // Execute a regular DELETE
            records.get(0).delete();
        }
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of IDs.
     *
     * @param id The ID to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void deleteById(T id) throws DataAccessException {
        deleteById(singletonList(id));
    }


    /**
     * Performs a <code>DELETE</code> statement for a given set of IDs.
     *
     * @param ids The IDs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void deleteById(T... ids) throws DataAccessException {
        deleteById(asList(ids));
    }

    /**
     * Performs a <code>DELETE</code> statement for a given set of IDs.
     *
     * @param ids The IDs to be deleted
     * @throws DataAccessException if something went wrong executing the query
     */
    public void deleteById(Collection<T> ids) throws DataAccessException {
        Field<?>[] pk = pk();
        if (pk != null)
            ctx().delete(table()).where(equal(pk, ids)).execute();
    }

    /**
     * Checks if a given record exists.
     *
     * @param object The record whose existence is checked
     * @return Whether the record already exists
     * @throws DataAccessException if something went wrong executing the query
     */
    public boolean exists(R object) throws DataAccessException {
        return existsById(getId(object));
    }

    /**
     * Checks if a given ID exists.
     *
     * @param id The ID whose existence is checked
     * @return Whether the ID already exists
     * @throws DataAccessException if something went wrong executing the query
     */
    public boolean existsById(T id) throws DataAccessException {
        Field<?>[] pk = pk();

        if (pk != null)
            return ctx()
                    .selectCount()
                    .from(table())
                    .where(equal(pk, id))
                    .fetchOne(0, Integer.class) > 0;
        else
            return false;
    }

    /**
     * Count all records of the underlying table.
     *
     * @return The number of records of the underlying table
     * @throws DataAccessException if something went wrong executing the query
     */
    public long count() throws DataAccessException {
        return ctx()
                .selectCount()
                .from(table())
                .fetchOne(0, Long.class);
    }

    /**
     * Find all records of the underlying table.
     *
     * @return All records of the underlying table
     * @throws DataAccessException if something went wrong executing the query
     */
    public List<R> findAll() throws DataAccessException {
        return ctx()
                .selectFrom(table())
                .fetch();
    }

    /**
     * Find a record of the underlying table by ID.
     *
     * @param id The ID of a record in the underlying table
     * @return A record of the underlying table given its ID, or
     * <code>null</code> if no record was found.
     * @throws DataAccessException if something went wrong executing the query
     */
    public R findById(T id) throws DataAccessException {
        Field<?>[] pk = pk();

        if (pk != null)
            return ctx().selectFrom(table())
                    .where(equal(pk, id))
                    .fetchOne();
        return null;
    }

    /**
     * Find a record of the underlying table by ID.
     *
     * @param id The ID of a record in the underlying table
     * @return A record of the underlying table given its ID.
     * @throws DataAccessException if something went wrong executing the query
     */
    public Optional<R> findOptionalById(T id) throws DataAccessException {
        return Optional.ofNullable(findById(id));
    }

    /**
     * Find records by a given field and a range of values.
     *
     * @param field          The field to compare values against
     * @param lowerInclusive The range's lower bound (inclusive), or unbounded
     *                       if <code>null</code>.
     * @param upperInclusive The range's upper bound (inclusive), or unbounded
     *                       if <code>null</code>.
     * @return A list of records fulfilling
     * <code>field BETWEEN lowerInclusive AND upperInclusive</code>
     * @throws DataAccessException if something went wrong executing the query
     */
    public <Z> List<R> fetchRange(Field<Z> field, Z lowerInclusive, Z upperInclusive) throws DataAccessException {
        return ctx()
                .selectFrom(table())
                .where(
                        lowerInclusive == null
                                ? upperInclusive == null
                                ? noCondition()
                                : field.le(upperInclusive)
                                : upperInclusive == null
                                ? field.ge(lowerInclusive)
                                : field.between(lowerInclusive, upperInclusive))
                .fetch();
    }

    /**
     * Find records by a given field and a set of values.
     *
     * @param field  The field to compare values against
     * @param values The accepted values
     * @return A list of records fulfilling <code>field IN (values)</code>
     * @throws DataAccessException if something went wrong executing the query
     */
    public <Z> List<R> fetch(Field<Z> field, Z... values) throws DataAccessException {
        return fetch(field, Arrays.asList(values));
    }

    /**
     * Find records by a given field and a set of values.
     *
     * @param field  The field to compare values against
     * @param values The accepted values
     * @return A list of records fulfilling <code>field IN (values)</code>
     * @throws DataAccessException if something went wrong executing the query
     */
    public <Z> List<R> fetch(Field<Z> field, Collection<? extends Z> values) throws DataAccessException {
        return ctx()
                .selectFrom(table())
                .where(field.in(values))
                .fetch();
    }

    /**
     * Find a unique record by a given field and a value.
     *
     * @param field The field to compare value against
     * @param value The accepted value
     * @return A record fulfilling <code>field = value</code>, or
     * <code>null</code>
     * @throws DataAccessException This exception is thrown
     *                             <ul>
     *                             <li>if something went wrong executing the query</li>
     *                             <li>if the query returned more than one value</li>
     *                             </ul>
     */
    public <Z> R fetchOne(Field<Z> field, Z value) throws DataAccessException {
        return ctx()
                .selectFrom(table())
                .where(field.equal(value))
                .fetchOne();
    }

    /**
     * Find a unique record by a given field and a value.
     *
     * @param field The field to compare value against
     * @param value The accepted value
     * @return A record fulfilling <code>field = value</code>
     * @throws DataAccessException This exception is thrown
     *                             <ul>
     *                             <li>if something went wrong executing the query</li>
     *                             <li>if the query returned more than one value</li>
     *                             </ul>
     */
    public <Z> Optional<R> fetchOptional(Field<Z> field, Z value) throws DataAccessException {
        return Optional.ofNullable(fetchOne(field, value));
    }

}
