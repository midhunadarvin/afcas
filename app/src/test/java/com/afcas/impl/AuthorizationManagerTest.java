package com.afcas.impl;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.Principal;
import com.afcas.objects.Operation;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.IAuthorizationProvider;
import com.afcas.utils.DatabaseHelper;
import com.afcas.objects.PrincipalType;
import com.afcas.utils.ObjectCache;
import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.ResourceAccessPredicateType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


public class AuthorizationManagerTest {

    private IAuthorizationManager authorizationManager;
    private IAuthorizationProvider authorizationProvider;
    static ObjectCache cache = new ObjectCache();

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
        authorizationProvider = new AuthorizationProvider();
        cache = new ObjectCache();
    }

    private static void CreateTestOperations() throws Exception {
        IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
        Operation op1 = Operation
                .builder()
                .id("op1")
                .name("op1")
                .description("operation 1")
                .build();
        cache.addToCache("op1", op1);
        Operation op2 = Operation
                .builder()
                .id("op2")
                .name("op2")
                .description("operation 2")
                .build();
        cache.addToCache("op2", op2);
        Operation op3 = Operation
                .builder()
                .id("op3")
                .name("op3")
                .description("operation 3")
                .build();
        cache.addToCache("op3", op3);

        authorizationManager.addOrUpdate(op1);
        authorizationManager.addOrUpdate(op2);
        authorizationManager.addOrUpdate(op3);
    }

    private static void CreateTestPrincipals() throws Exception {
        IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();

        Principal grp1 = new Principal("grp1", "grp1", "grp1@test.com", "Group 1", "grp1 desc", "TestSource", PrincipalType.Group);
        cache.addToCache("grp1", grp1);
        Principal grp2 = new Principal("grp2", "grp2", "grp2@test.com", "Group 2", "grp2 desc", "TestSource", PrincipalType.Group);
        cache.addToCache("grp2", grp2);
        Principal usr1 = new Principal("usr1", "usr1", "usr1@test.com", "User 1", "usr1 desc", "TestSource", PrincipalType.User);
        cache.addToCache("usr1", usr1);
        Principal usr2 = new Principal("usr2", "usr2", "usr2@test.com", "User 2", "usr2 desc", "TestSource", PrincipalType.User);
        cache.addToCache("usr2", usr2);

        Assert.assertEquals(cache.getSize(), 4);

        authorizationManager.addOrUpdate(grp1, "TestSource");
        authorizationManager.addOrUpdate(grp2, "TestSource");
        authorizationManager.addOrUpdate(usr1, "TestSource");
        authorizationManager.addOrUpdate(usr2, "TestSource");
    }

    @Test
    public void testAddOrUpdate() throws Exception {
        DatabaseHelper.executeQuery("SELECT * FROM \"Test_DeleteAllData\"();");
        Principal principal = Principal.builder()
                .name("John")
                .principalType(PrincipalType.User)
                .displayName("John Doe")
                .email("john.doe@example.com")
                .description("Test principal")
                .dataSource("Test source").build();

        // Act
        authorizationManager.addOrUpdate(principal, "TestSource");

        int size = authorizationManager.getPrincipalList().size();
        // Assert
        Assert.assertEquals(1, size);
    }

    @Test
    public void TestCreateAcl() throws Exception {
        DatabaseHelper.executeQuery("SELECT * FROM \"Test_DeleteAllData\"();");
        IAuthorizationManager manager = AuthorizationManagerFactory.getInstance();

        testCreateDag();

        manager.getPrincipalList();
        manager.getOperationList();

        Principal grp1 = (Principal) cache.getFromCache("grp1");
        //Principal grp2 = ObjectCache.Current.Get< Principal >( "grp2" );
        Principal usr1 = (Principal) cache.getFromCache("usr1");

        Operation op1 = (Operation) cache.getFromCache("op1");
        Operation op2 = (Operation) cache.getFromCache("op2");
        //Operation op3 = ObjectCache.Current.Get< Operation >( "op2" );

        ResourceAccessPredicate rap1 = new ResourceAccessPredicate(
                grp1.getId(),
                op1.getId(),
                "r1",
                ResourceAccessPredicateType.Grant
        );
        manager.addAccessPredicate(rap1);
        ResourceAccessPredicate rap2 = new ResourceAccessPredicate(
                grp1.getId(),
                op1.getId(),
                "r2",
                ResourceAccessPredicateType.Grant
        );
        manager.addAccessPredicate(rap2);
        ResourceAccessPredicate rap3 = new ResourceAccessPredicate(
                grp1.getId(),
                op1.getId(),
                "",
                ResourceAccessPredicateType.Grant
        );
        manager.addAccessPredicate(rap3);

        Assert.assertTrue(authorizationProvider.isAuthorized(grp1.getId(), op1.getId(), "r1"));
        Assert.assertTrue(authorizationProvider.isAuthorized(grp1.getId(), op2.getId(), "r1"));
        Assert.assertTrue(authorizationProvider.isAuthorized(usr1.getId(), op2.getId(), "r1"));
        Assert.assertTrue(authorizationProvider.isAuthorized(usr1.getId(), op2.getId(), ""));
    }

    @Test
    public void testCreateDag() throws Exception {
        DatabaseHelper.executeQuery("SELECT * FROM \"Test_DeleteAllData\"();");
        IAuthorizationManager manager = AuthorizationManagerFactory.getInstance();

        CreateTestPrincipals();
        CreateTestOperations();

        Principal grp1 = (Principal) cache.getFromCache("grp1");
        Principal grp2 = (Principal) cache.getFromCache("grp2");
        Principal usr1 = (Principal) cache.getFromCache("usr1");
        Principal usr2 = (Principal) cache.getFromCache("usr2");

        manager.addGroupMember(grp1, usr1);
        manager.addGroupMember(grp1, usr2);
        manager.addGroupMember(grp2, usr1);
        manager.addGroupMember(grp2, usr2);
        manager.addGroupMember(grp2, grp1);

        Assert.assertTrue(authorizationProvider.isMemberOf(grp1.getId(), usr1.getId()));
        Assert.assertTrue(authorizationProvider.isMemberOf(grp1.getId(), usr2.getId()));
        Assert.assertTrue(authorizationProvider.isMemberOf(grp2.getId(), usr1.getId()));
        Assert.assertTrue(authorizationProvider.isMemberOf(grp2.getId(), usr2.getId()));
        Assert.assertTrue(authorizationProvider.isMemberOf(grp2.getId(), grp1.getId()));

        Assert.assertEquals(2, manager.getMembersList(grp1).size());
        Assert.assertEquals(3, manager.getMembersList(grp2).size());

        Operation op1 = (Operation) cache.getFromCache("op1");
        Operation op2 = (Operation) cache.getFromCache("op2");
        Operation op3 = (Operation) cache.getFromCache("op3");

        manager.addSubOperation(op1, op2);
        manager.addSubOperation(op1, op3);

        Assert.assertTrue(authorizationProvider.isSubOperation(op1.getId(), op2.getId()));
        Assert.assertTrue(authorizationProvider.isSubOperation(op1.getId(), op3.getId()));
    }

}
