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