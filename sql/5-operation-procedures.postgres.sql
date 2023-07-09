CREATE OR REPLACE PROCEDURE "AddOrUpdateOperation"(
  Id varchar(10),
  Name varchar(250),
  Description varchar(500)
)
AS $$
BEGIN
    UPDATE "Operation"
    SET "Name" = COALESCE(Name, Id),
        "Description" = COALESCE(Description, '')
    WHERE "Id" = Id;

    IF FOUND THEN
        RETURN;
    END IF;

    INSERT INTO "Operation" ("Id", "Name", "Description")
    VALUES (Id, COALESCE(Name, Id), COALESCE(Description, ''));

    RETURN;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE "RemoveOperation"(
    Id VARCHAR(10)
)
AS $$
BEGIN
    DELETE FROM "Operation" WHERE "Id" = "RemoveOperation".Id;

    IF FOUND THEN
        PERFORM "RemoveRelatedEdges"(Id, 'Operation');
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetOperationList"()
RETURNS TABLE (
    "Id" VARCHAR,
    "Name" VARCHAR,
    "Description" VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT op."Id",
           op."Name",
           op."Description"
    FROM "Operation" op;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetSubOperationsList"(
    OperationId VARCHAR(10),
    IsFlat INT
)
RETURNS TABLE (
    "Id" VARCHAR,
    "Name" VARCHAR,
    "Description" VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
           O."Id",
           O."Name",
           O."Description"
    FROM "Operation" O
    INNER JOIN "Edge" E ON O."Id" = E."StartVertex"
    WHERE E."EndVertex" = OperationId
        AND E."Source" = 'Operation'
        AND (IsFlat = 1 OR E."Hops" = IsFlat);
END;
$$ LANGUAGE plpgsql;

