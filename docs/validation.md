# Validation

Wir wollen die eingehenden Daten für die Fälle Insert und Update auf Korrektheit für diese Fälle prüfen,
bevor wir sie an die Datenbank schicken.

Dadurch ist sichergestellt, dass:
- Fehler in den Eingabedaten frühzeitig auffallen und eine genaue Fehlerursache bestimmt werden kann
- keine falschen Daten in die Datenbank gespeichert werden.

Wir setzen dies über die `validation-api` Bibliothek um, die den Standard hierfür definiert
und wir wählen dafür den `hibernate-validator`, der bereits in Quarkus mitgeliefert wird.

Hierfür ist es nötig dass der jOOQ-Codegenerator den automatisch erzeugten Code bereits mit passenden 
Validation-Annotations anreichert, so dass die DTO und Record Objekte, welche die Eingabedaten zur Datenbank transportieren,
bereits für die Validierung vorbereitet sind.

Folgende Annotationen der `validation-api` sind hier eine gute Basis:
- NotNull
- Size

Der Quarkus Hibernate-Validator hat jedoch trotz seines Komforts eine komische Besonderheit.
In Gradle Multi-Modul Projects kann er die entsprechenden ValidationConstraints nicht auflösen bzw. wirft einen Fehler in folgendem Stil:
```
javax.validation.UnexpectedTypeException: HV000030: No validator could be found for constraint 'javax.validation.constraints.Size' validating type 'java.lang.String'. Check configuration for 'myMethodPost.arg0.dataId'
```
Siehe auch:
- https://stackoverflow.com/questions/70092714/unexpectedtypeexception-hv000030-no-validator-could-be-found-for-constraint-j
- https://quarkus.io/guides/cdi-reference

Hier muss man nun ein Jandex-Plugin im Modul einbinden, erst danach arbeitet der Validator wie erwartet.
Das ganze Thema wird in folgendem Artikel sehr gut beschrieben:
- https://www.baeldung.com/quarkus-bean-discovery-index
