package com.afcas.objects;

public class ResourceAccessPredicate {
    private final String principalId;
    private final String operationId;
    private final ResourceHandle resourceId;
    private final ResourceAccessPredicateType accessPredicateType;

    public ResourceAccessPredicate(String principalId, String operationId, ResourceHandle resourceId, ResourceAccessPredicateType accessPredicateType) {
        this.principalId = principalId;
        this.operationId = operationId;
        this.resourceId = resourceId;
        this.accessPredicateType = accessPredicateType;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public String getOperationId() {
        return operationId;
    }

    public ResourceHandle getResourceId() {
        return resourceId;
    }

    public ResourceAccessPredicateType getAccessPredicateType() {
        return accessPredicateType;
    }
}
