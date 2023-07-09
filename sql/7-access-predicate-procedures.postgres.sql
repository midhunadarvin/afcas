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

CREATE VIEW "FlatGrantList" AS
    SELECT DISTINCT
        PL."StartVertex" AS "PrincipalId",
        OL."StartVertex" AS "OperationId",
        RL."StartVertex" AS "ResourceId"
    FROM (
        SELECT E."EndVertex", E."StartVertex"
        FROM "Edge" E
        INNER JOIN "AccessPredicate" AP ON E."EndVertex" = AP."PrincipalId"
        WHERE E."Source" = 'Principal'
        UNION
        SELECT "PrincipalId", "PrincipalId"
        FROM "AccessPredicate"
    ) PL
    CROSS JOIN (
        SELECT E."EndVertex", E."StartVertex"
        FROM "Edge" E
        INNER JOIN "AccessPredicate" AP ON E."EndVertex" = AP."OperationId"
        WHERE E."Source" = 'Operation'
        UNION
        SELECT "OperationId", "OperationId"
        FROM "AccessPredicate"
    ) OL
    CROSS JOIN (
        SELECT E."EndVertex", E."StartVertex"
        FROM "Edge" E
        INNER JOIN "AccessPredicate" AP ON E."EndVertex" = AP."ResourceId"
        WHERE E."Source" = 'Resource'
        UNION
        SELECT "ResourceId", "ResourceId"
        FROM "AccessPredicate"
    ) RL
    INNER JOIN "AccessPredicate" ACL ON PL."EndVertex" = ACL."PrincipalId"
                                      AND RL."EndVertex" = ACL."ResourceId"
                                      AND OL."EndVertex" = ACL."OperationId";


CREATE OR REPLACE FUNCTION "GetAuthorizationDigest"(
    PrincipalId VARCHAR(36)
)
RETURNS TABLE (
    "OperationId" VARCHAR,
    "ResourceId" VARCHAR,
    "PredicateType" INT
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
        FGL."OperationId",
        FGL."ResourceId",
        0::INT AS "PredicateType"
    FROM "FlatGrantList" FGL
    WHERE "PrincipalId" = PrincipalId;
END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetAuthorizedOperations"(
    PrincipalId VARCHAR(36),
    ResourceId VARCHAR(100)
)
RETURNS TABLE ("OperationId" VARCHAR)
AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT FGL."OperationId"
    FROM "FlatGrantList" FGL
    WHERE FGL."PrincipalId" = PrincipalId
        AND FGL."ResourceId" = ResourceId;
END $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetAuthorizedResources"(
    PrincipalId VARCHAR(36),
    OperationId VARCHAR(10)
)
RETURNS TABLE ("ResourceId" VARCHAR)
AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT FGL."ResourceId"
    FROM "FlatGrantList" FGL
    WHERE FGL."PrincipalId" = PrincipalId
        AND FGL."OperationId" = OperationId;
END $$
LANGUAGE plpgsql;