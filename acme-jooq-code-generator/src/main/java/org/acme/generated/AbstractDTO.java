package org.acme.generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.xml.bind.annotation.XmlTransient;
import org.jooq.Record;

import java.util.HashMap;
import java.util.Map;

/**
 * A DTO abstraction for all DTOs of the application
 * <p>All generated DTO classes extend this DTO to provide common functionality.</p>
 * <p>
 * defines modified fields, that keep track of all changes on the fields of the dto.
 * - in unit-tests this map can be used, to serialize only the set fields of the dto to the body of the rest-request.
 * - in database-operations this map can be used, to resolve only the modified fields to the database-operations.
 * </p>
 */
public abstract class AbstractDTO {

    // inspired by: https://blog.jooq.org/orms-should-update-changed-values-not-just-modified-ones/
    private Map<String, Object> modifiedFields = new HashMap<>();

    public AbstractDTO() {
    }

    @JsonIgnore
    @XmlTransient
    protected void setAt(String key, Object value) {
        this.modifiedFields.put(key, value);
    }

    @JsonIgnore
    @XmlTransient
    public void resetModifiedFields() {
        this.modifiedFields.clear();
    }

    @JsonIgnore
    @XmlTransient
    public Map<String, Object> getModifiedFields() {
        return this.modifiedFields;
    }
}
