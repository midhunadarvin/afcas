package com.afcas.objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceAccessPredicate {
    private final String principalId;
    private final String operationId;
    private final String resourceId;
    private final ResourceAccessPredicateType accessPredicateType;
}
