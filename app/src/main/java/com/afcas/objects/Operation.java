package com.afcas.objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Operation {
    private final String id, name, description;
}
