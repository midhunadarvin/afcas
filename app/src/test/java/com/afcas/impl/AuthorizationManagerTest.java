package com.afcas.impl;

import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;
import com.afcas.utils.DatabaseHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AuthorizationManagerTest {

    private AuthorizationManager authorizationManager;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        DatabaseHelper.init(
                "localhost",
                "5432",
                "postgres",
                "123456"
        );
        authorizationManager = new AuthorizationManager();
    }

    @Test
    public void testAddOrUpdate() throws Exception {
        // Arrange
        Principal principal = Principal.builder()
                .name("John")
                .principalType(PrincipalType.User)
                .displayName("John Doe")
                .email("john.doe@example.com")
                .description("Test principal")
                .dataSource("Test source").build();

        // Act
        Object result = authorizationManager.addOrUpdate(principal, "source");

        // Assert
        Assert.assertEquals(0, result); // Assuming the stored procedure returns an ID of "123"
    }

    // Add more test methods for the other methods in AuthorizationManager

}
