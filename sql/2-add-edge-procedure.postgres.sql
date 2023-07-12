-- Create or replace procedure AddEdgeWithSpaceSavings
CREATE OR REPLACE PROCEDURE "AddEdgeWithSpaceSavings"(
    IN StartVertexId varchar(256),
    IN EndVertexId varchar(256),
    IN source varchar(150)
)
LANGUAGE plpgsql
AS $$
BEGIN
   IF EXISTS (
      SELECT "Hops"
      FROM "Edge"
      WHERE "StartVertex" = StartVertexId
      AND "EndVertex" = EndVertexId
      AND "Source" = source
      AND "Hops" = 0
   ) THEN
      RETURN; -- DO NOTHING!!!
   END IF;

   IF StartVertexId = EndVertexId OR EXISTS (
      SELECT "Hops"
      FROM "Edge"
      WHERE "StartVertex" = EndVertexId
      AND "EndVertex" = StartVertexId
      AND "Source" = source
   ) THEN
      RAISE EXCEPTION 'Attempt to create a circular relation detected!';
      RETURN;
   END IF;

   CREATE TEMPORARY table IF NOT EXISTS "Candidates" (
      "StartVertex" varchar(256),
      "EndVertex" varchar(256)
   );

   truncate "Candidates";


   INSERT INTO "Candidates" (
      -- step 1: A's incoming edges to B
      SELECT "StartVertex", EndVertexId
      FROM "Edge"
      WHERE "EndVertex" = StartVertexId
      AND "Source" = source
   )
   UNION
   (
      -- step 2: A to B's outgoing edges
      SELECT StartVertexId, "EndVertex"
      FROM "Edge"
      WHERE "StartVertex" = EndVertexId
      AND "Source" = source
   )
   UNION
   (
      -- step 3: A’s incoming edges to end vertex of B's outgoing edges
      SELECT A."StartVertex", B."EndVertex"
      FROM "Edge" A
      CROSS JOIN "Edge" B
      WHERE A."EndVertex" = StartVertexId
      AND B."StartVertex" = EndVertexId
      AND A."Source" = source
      AND B."Source" = source
   );

   -- step 4: Insert A-B edge
   INSERT INTO "Edge" (
      "StartVertex",
      "EndVertex",
      "Hops",
      "Source",
      "DelMark"
   ) VALUES (
      StartVertexId,
      EndVertexId,
      0,
      source,
      false
   );

   -- step 5: Insert all pairs from candidates table taking care not to re-insert any existing edges
   INSERT INTO "Edge" (
      "StartVertex",
      "EndVertex",
      "Hops",
      "Source",
      "DelMark"
   )
   SELECT "StartVertex",
          "EndVertex",
          1,
          source,
          false
   FROM "Candidates" C
   WHERE NOT EXISTS (
      -- filter the candidate edges that already exists. The transitive closure already exists. The check is done during
      -- deletion to prevent the already existing edges from being deleted.
      SELECT *
      FROM "Edge" E
      WHERE E."StartVertex" = C."StartVertex"
      AND E."EndVertex" = C."EndVertex"
      AND E."Hops" = 1
   );

  drop table "Candidates";

END $$;