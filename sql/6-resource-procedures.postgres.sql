CREATE OR REPLACE PROCEDURE "AddOrUpdateResource"(
  Id varchar(100),
  Name varchar(250)
)
AS $$
BEGIN
    UPDATE "Resource"
    SET "Name" = COALESCE(Name, Id)
    WHERE "Id" = Id;

    IF FOUND THEN
        RETURN;
    END IF;

    INSERT INTO "Resource" ("Id", "Name")
    VALUES (Id, COALESCE(Name, Id));

    RETURN;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE "RemoveResource"(
    Id VARCHAR(100)
)
AS $$
BEGIN
    DELETE FROM "Resource" WHERE "Id" = "RemoveResource".Id;

    IF FOUND THEN
        PERFORM "RemoveRelatedEdges"(Id, 'Resource');
    END IF;
END;
$$ LANGUAGE plpgsql;

