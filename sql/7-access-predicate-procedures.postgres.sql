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