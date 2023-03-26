
TODO.

Record DAOs are used for flat operations on exactly one table, and can do full CRUD on them
They should not do joins.
They should contain as less as possible. Most is already provided by the @{@link AbstractRecordDAO} abstraction.

View DAOs are used for joined-operations and pagination on a table and relevant join-tables.
They are only used for Read-Operation, and should not do Updates/Deletes/Inserts (record-daos are responsible there)
Most is already provided by the @{@link AbstractViewDAO} abstraction.

We create the dsl-context in a request-scoped way,
to ensure thread-safety, as this is preferred in jooq for the request-context.
we need this factory only because we want to append global data to sql-statements at one place dynamically.

Stateless Service
contains business-logic
It's preferable, to create such business-logic handling in a stateless way.
The service can use daos to interact with the database,
but can also use the dsl-context directly if necessary.
The service will also handle transaction-safety for multiple interactions and will
combine the different database-results into dtos, that contain joined data.