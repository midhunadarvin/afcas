package com.afcas.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResourceAccessPredicate {
    private final String principalId;
    private final String operationId;
    private final String resourceId;
    private final ResourceAccessPredicateType accessPredicateType;
}
