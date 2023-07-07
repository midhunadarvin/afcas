CREATE OR REPLACE PROCEDURE "AddAccessPredicate"(
    PrincipalId VARCHAR(256),
    OperationId VARCHAR(10),
    ResourceId VARCHAR(256),
    PredicateType INT
)
AS $$
BEGIN
    INSERT INTO "AccessPredicate"
               ("PrincipalId"
               ,"OperationId"
               ,"ResourceId"
               ,"PredicateType")
         VALUES
               ("AddAccessPredicate".PrincipalId
               ,"AddAccessPredicate".OperationId
               ,"AddAccessPredicate".ResourceId
               ,"AddAccessPredicate".PredicateType);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE "RemoveAccessPredicate"(
        PrincipalId VARCHAR(256),
        OperationId VARCHAR(10),
        ResourceId VARCHAR(256),
        PredicateType INT
)
AS $$
BEGIN
    DELETE FROM "AccessPredicate" WHERE "PrincipalId" = "RemoveAccessPredicate".PrincipalId
        AND "OperationId" = "RemoveAccessPredicate".OperationId
        AND "ResourceId" = "RemoveAccessPredicate".ResourceId
        AND "PredicateType" = "RemoveAccessPredicate".PredicateType;
END;
$$ LANGUAGE plpgsql;
