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
