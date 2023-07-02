CREATE OR REPLACE FUNCTION "Test_TestClosureWithSpaceSavings"()
RETURNS VOID
LANGUAGE plpgsql
AS $$
BEGIN
   DELETE FROM "Edge" WHERE "Source" = 'TestClosure';

   CALL "AddEdgeWithSpaceSavings"('A', 'C', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('A', 'D', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('B', 'D', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('B', 'M', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('C', 'E', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('C', 'H', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('C', 'I', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('D', 'F', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('D', 'G', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('D', 'H', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('I', 'K', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('I', 'J', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('A', 'B', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('B', 'E', 'TestClosure');

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 1';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'E' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 2';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'C' AND "EndVertex" = 'K' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 3';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'D' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 4';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 5';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 6';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 7';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'M' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 8';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'B' AND "EndVertex" = 'H' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 9';
   END IF;

   CALL "RemoveEdgeWithSpaceSavings"('A', 'B', 'TestClosure');

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'E' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 10';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'D' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 11';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'H' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 12';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 13';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'G' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 14';
   END IF;

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'M' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 15';
   END IF;

   CALL "RemoveEdgeWithSpaceSavings"('A', 'D', 'TestClosure');

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'F' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 16';
   END IF;

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'G' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 17';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'H' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 18';
   END IF;

   CALL "RemoveEdgeWithSpaceSavings"('A', 'C', 'TestClosure');

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'E' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 19';
   END IF;

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'I' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 20';
   END IF;

   IF EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'K' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 21';
   END IF;

   CALL "AddEdgeWithSpaceSavings"('A', 'C', 'TestClosure');

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'I' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 22';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'J' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 23';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'K' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 24';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'H' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 25';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'E' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 26';
   END IF;

   CALL "AddEdgeWithSpaceSavings"('P', 'Q', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('P', 'R', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('Q', 'S', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('Q', 'T', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('R', 'T', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('R', 'T', 'TestClosure');
   CALL "AddEdgeWithSpaceSavings"('H', 'P', 'TestClosure');

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'D' AND "EndVertex" = 'T' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 27';
   END IF;

   IF NOT EXISTS (SELECT "Hops" FROM "Edge" WHERE "StartVertex" = 'A' AND "EndVertex" = 'S' AND "Source" = 'TestClosure') THEN
        RAISE NOTICE 'PROBLEM 28';
   END IF;

   RAISE NOTICE 'TEST RUN COMPLETED...';

END $$;