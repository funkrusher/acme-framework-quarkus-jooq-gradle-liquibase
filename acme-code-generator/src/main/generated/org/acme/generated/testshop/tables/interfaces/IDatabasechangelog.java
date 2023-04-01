/*
 * This file is generated by jOOQ.
 */
package org.acme.generated.testshop.tables.interfaces;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Valid
public interface IDatabasechangelog extends Serializable {

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.ID</code>.
     */
    public void setID(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.ID</code>.
     */
    @NotNull
    @Size(max = 255)
    public String getID();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.AUTHOR</code>.
     */
    public void setAUTHOR(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.AUTHOR</code>.
     */
    @NotNull
    @Size(max = 255)
    public String getAUTHOR();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.FILENAME</code>.
     */
    public void setFILENAME(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.FILENAME</code>.
     */
    @NotNull
    @Size(max = 255)
    public String getFILENAME();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.DATEEXECUTED</code>.
     */
    public void setDATEEXECUTED(LocalDateTime value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.DATEEXECUTED</code>.
     */
    @NotNull
    public LocalDateTime getDATEEXECUTED();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.ORDEREXECUTED</code>.
     */
    public void setORDEREXECUTED(Integer value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.ORDEREXECUTED</code>.
     */
    @NotNull
    public Integer getORDEREXECUTED();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.EXECTYPE</code>.
     */
    public void setEXECTYPE(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.EXECTYPE</code>.
     */
    @NotNull
    @Size(max = 10)
    public String getEXECTYPE();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.MD5SUM</code>.
     */
    public void setMD5SUM(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.MD5SUM</code>.
     */
    @Size(max = 35)
    public String getMD5SUM();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.DESCRIPTION</code>.
     */
    public void setDESCRIPTION(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.DESCRIPTION</code>.
     */
    @Size(max = 255)
    public String getDESCRIPTION();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.COMMENTS</code>.
     */
    public void setCOMMENTS(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.COMMENTS</code>.
     */
    @Size(max = 255)
    public String getCOMMENTS();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.TAG</code>.
     */
    public void setTAG(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.TAG</code>.
     */
    @Size(max = 255)
    public String getTAG();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.LIQUIBASE</code>.
     */
    public void setLIQUIBASE(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.LIQUIBASE</code>.
     */
    @Size(max = 20)
    public String getLIQUIBASE();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.CONTEXTS</code>.
     */
    public void setCONTEXTS(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.CONTEXTS</code>.
     */
    @Size(max = 255)
    public String getCONTEXTS();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.LABELS</code>.
     */
    public void setLABELS(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.LABELS</code>.
     */
    @Size(max = 255)
    public String getLABELS();

    /**
     * Setter for <code>testshop.DATABASECHANGELOG.DEPLOYMENT_ID</code>.
     */
    public void setDEPLOYMENT_ID(String value);

    /**
     * Getter for <code>testshop.DATABASECHANGELOG.DEPLOYMENT_ID</code>.
     */
    @Size(max = 10)
    public String getDEPLOYMENT_ID();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IDatabasechangelog
     */
    public void from(IDatabasechangelog from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IDatabasechangelog
     */
    public <E extends IDatabasechangelog> E into(E into);
}
