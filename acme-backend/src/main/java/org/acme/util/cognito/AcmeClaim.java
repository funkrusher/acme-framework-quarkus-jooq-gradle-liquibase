package org.acme.util.cognito;

import java.util.List;

public class AcmeClaim {

    private Integer clientId;

    private Integer userId;

    public AcmeClaim() {
    }

    public AcmeClaim(final Integer clientId, final Integer userId) {
        this.clientId = clientId;
        this.userId = userId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Integer getUserId() {
        return userId;
    }


    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
