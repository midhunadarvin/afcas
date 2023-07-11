package com.afcas.objects;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
public class Principal implements Serializable {
    private final String id, name, email, displayName, description, dataSource;
    private final PrincipalType principalType;

}

