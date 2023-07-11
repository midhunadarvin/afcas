

CREATE OR REPLACE FUNCTION "Test_TestPrincipal"()
RETURNS VOID
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM "Principal" WHERE "DataSource" = 'TestPrincipal';

    CALL "AddOrUpdatePrincipal"(
        'Prince',
        1,
        'displayName',
        'email',
        'description',
        'TestPrincipal'
    );

    IF NOT EXISTS (SELECT * FROM "Principal" WHERE "DataSource" = 'TestPrincipal' AND "Name" = 'Prince') THEN
        RAISE NOTICE 'PROBLEM 1';
    END IF;

    PERFORM "RemovePrincipal"('Prince');

    IF EXISTS (SELECT * FROM "Principal" WHERE "DataSource" = 'TestPrincipal' AND "Name" = 'Prince') THEN
        RAISE NOTICE 'PROBLEM 2';
    END IF;

   RAISE NOTICE 'TEST RUN COMPLETED...';

END $$;

SELECT "Test_TestPrincipal"();

CREATE OR REPLACE FUNCTION "Test_DeleteAllData"()
RETURNS VOID AS $$
BEGIN
    DELETE FROM "Principal";
    DELETE FROM "Operation";
    DELETE FROM "Edge";
    DELETE FROM "AccessPredicate";
END;
$$ LANGUAGE plpgsql;

SELECT * FROM "Test_DeleteAllData"();