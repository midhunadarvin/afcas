package com.afcas.objects;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resource {
    private final String id, name;
}
