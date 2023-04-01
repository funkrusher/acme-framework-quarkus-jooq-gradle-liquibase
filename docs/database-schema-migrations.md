
Entscheidung für Liquibase Vs. Flyway.

Liquibase kann automatisch Rollbacks durchführen und erlaubt außer SQL auch weitere Formate um Changesets zu definieren.
Bei Flyway ist die Community Edition teilweise begrenzt und außerdem muss manuell ein Undo-Command ausgeführt werden, 
wenn man sein Schema in einen bestimmten Stand bringen möchte wenn man z.b. einen Branch wechselt.

Liquibase ermöglicht außerdem dass Entwickler ihre eigenen Changesets definieren können und diese nur im Changelog dann
reinverlinken. Dadurch kann der übliche Konflikt nicht mehr auftreten dass zwei Entwickler an derselben Nummer Changeset
arbeiten. Stattdessen muss man nur einen neuen Eintrag im Changelog für die neue Datei aufnehmen. Dies ist dem Konflikt
vorzuziehen.

Man kann z.b. die Changesets nach den Feature-Branches benennen und die alphabetische Reihenfolge der Dateien
bestimmt hierbei nicht mehr die Reihenfolge ihrer Ausführung was von Vorteil ist,
da ein Feature mit einer höheren Ticket-ID auch mal schneller fertig sein kann, 
als ein Feature mit einer niedrigeren Ticket-ID

- 8419-feature-xyz.xml
- 8761-feature-abc.xml
- ...



Beispiel für ein Liquibase-Command um automatisch aus einer Datenbank die Migrations zu generieren:
```
liquibase --diffTypes="data" --includeSchema=true --includeTablespace=true --includeCatalog=true --changeLogFile=changelog.xml --url=jdbc:mysql://localhost:3306/testshop --username=root --password="" --changeSetAuthor=myname --driver org.mariadb.jdbc.Driver generateChangeLog
```

