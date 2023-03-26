/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.testshop.tables.dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import org.acme.generated.AbstractDTO;
import org.acme.generated.testshop.tables.interfaces.IFlywaySchemaHistory;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public class FlywaySchemaHistory extends AbstractDTO implements IFlywaySchemaHistory {

    private static final long serialVersionUID = 1L;

    private Integer       installed_rank;
    private String        version;
    private String        description;
    private String        type;
    private String        script;
    private Integer       checksum;
    private String        installed_by;
    private LocalDateTime installed_on;
    private Integer       execution_time;
    private Boolean       success;

    public FlywaySchemaHistory() {}

    public FlywaySchemaHistory(IFlywaySchemaHistory value) {
        this.installed_rank = value.getInstalled_rank();
        this.version = value.getVersion();
        this.description = value.getDescription();
        this.type = value.getType();
        this.script = value.getScript();
        this.checksum = value.getChecksum();
        this.installed_by = value.getInstalled_by();
        this.installed_on = value.getInstalled_on();
        this.execution_time = value.getExecution_time();
        this.success = value.getSuccess();
    }

    public FlywaySchemaHistory(
        Integer       installed_rank,
        String        version,
        String        description,
        String        type,
        String        script,
        Integer       checksum,
        String        installed_by,
        LocalDateTime installed_on,
        Integer       execution_time,
        Boolean       success
    ) {
        this.installed_rank = installed_rank;
        this.version = version;
        this.description = description;
        this.type = type;
        this.script = script;
        this.checksum = checksum;
        this.installed_by = installed_by;
        this.installed_on = installed_on;
        this.execution_time = execution_time;
        this.success = success;
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.installed_rank</code>.
     */
    @NotNull
    @Override
    public Integer getInstalled_rank() {
        return this.installed_rank;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.installed_rank</code>.
     */
    @Override
    public void setInstalled_rank(Integer installed_rank) {
        this.installed_rank = installed_rank;
        this.setAt("installed_rank", installed_rank);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.version</code>.
     */
    @Size(max = 50)
    @Override
    public String getVersion() {
        return this.version;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.version</code>.
     */
    @Override
    public void setVersion(String version) {
        this.version = version;
        this.setAt("version", version);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.description</code>.
     */
    @NotNull
    @Size(max = 200)
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.description</code>.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
        this.setAt("description", description);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.type</code>.
     */
    @NotNull
    @Size(max = 20)
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.type</code>.
     */
    @Override
    public void setType(String type) {
        this.type = type;
        this.setAt("type", type);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.script</code>.
     */
    @NotNull
    @Size(max = 1000)
    @Override
    public String getScript() {
        return this.script;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.script</code>.
     */
    @Override
    public void setScript(String script) {
        this.script = script;
        this.setAt("script", script);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.checksum</code>.
     */
    @Override
    public Integer getChecksum() {
        return this.checksum;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.checksum</code>.
     */
    @Override
    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
        this.setAt("checksum", checksum);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.installed_by</code>.
     */
    @NotNull
    @Size(max = 100)
    @Override
    public String getInstalled_by() {
        return this.installed_by;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.installed_by</code>.
     */
    @Override
    public void setInstalled_by(String installed_by) {
        this.installed_by = installed_by;
        this.setAt("installed_by", installed_by);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.installed_on</code>.
     */
    @Override
    public LocalDateTime getInstalled_on() {
        return this.installed_on;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.installed_on</code>.
     */
    @Override
    public void setInstalled_on(LocalDateTime installed_on) {
        this.installed_on = installed_on;
        this.setAt("installed_on", installed_on);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.execution_time</code>.
     */
    @NotNull
    @Override
    public Integer getExecution_time() {
        return this.execution_time;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.execution_time</code>.
     */
    @Override
    public void setExecution_time(Integer execution_time) {
        this.execution_time = execution_time;
        this.setAt("execution_time", execution_time);
    }

    /**
     * Getter for <code>testshop.flyway_schema_history.success</code>.
     */
    @NotNull
    @Override
    public Boolean getSuccess() {
        return this.success;
    }

    /**
     * Setter for <code>testshop.flyway_schema_history.success</code>.
     */
    @Override
    public void setSuccess(Boolean success) {
        this.success = success;
        this.setAt("success", success);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FlywaySchemaHistory (");

        sb.append(installed_rank);
        sb.append(", ").append(version);
        sb.append(", ").append(description);
        sb.append(", ").append(type);
        sb.append(", ").append(script);
        sb.append(", ").append(checksum);
        sb.append(", ").append(installed_by);
        sb.append(", ").append(installed_on);
        sb.append(", ").append(execution_time);
        sb.append(", ").append(success);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IFlywaySchemaHistory from) {
        setInstalled_rank(from.getInstalled_rank());
        setVersion(from.getVersion());
        setDescription(from.getDescription());
        setType(from.getType());
        setScript(from.getScript());
        setChecksum(from.getChecksum());
        setInstalled_by(from.getInstalled_by());
        setInstalled_on(from.getInstalled_on());
        setExecution_time(from.getExecution_time());
        setSuccess(from.getSuccess());
    }

    @Override
    public <E extends IFlywaySchemaHistory> E into(E into) {
        into.from(this);
        return into;
    }
}
