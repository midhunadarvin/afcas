CREATE OR REPLACE
PROCEDURE "RemoveEdgeWithSpaceSavings" (
	StartVertexId varchar(256),
	EndVertexId varchar(256),
	SOURCE varchar(150)
)
LANGUAGE plpgsql
AS $$
BEGIN
   DELETE
	FROM
		"Edge"
	WHERE
		"Hops" = 0
		AND "StartVertex" = StartVertexId
		AND "EndVertex" = EndVertexId
		AND "Source" = SOURCE;

	IF NOT FOUND THEN
	       RETURN;
	-- NOTHING TO DELETE
	END IF;

	--UPDATE Edge SET DelMark = true

   UPDATE "Edge" OE
      SET "DelMark" = true
      FROM (
      	SELECT E."StartVertex", E."EndVertex", E."Hops", E."Source" FROM "Edge" E
			INNER JOIN (
					-- 1: all edges that end in A
					SELECT
						"StartVertex",
						EndVertexId AS "EndVertex"
					FROM
						"Edge"
					WHERE
						"EndVertex" = StartVertexId
				UNION
					-- 2: all edges that start from B
					SELECT
						StartVertexId,
						"EndVertex" AS "EndVertex"
					FROM
						"Edge"
					WHERE
						"StartVertex" = EndVertexId
				UNION
					SELECT
						A."StartVertex",
						B."EndVertex"
					FROM
						"Edge" A
					CROSS JOIN "Edge" B
					WHERE
						A."EndVertex" = StartVertexId
						AND B."StartVertex" = EndVertexId
				) AS C
		   	ON C."StartVertex" = E."StartVertex"
			AND C."EndVertex" = E."EndVertex"
		WHERE
			E."Hops" > 0
      ) AS subquery
	WHERE
	OE."StartVertex" = subquery."StartVertex" AND OE."EndVertex" = subquery."EndVertex" AND OE."Hops" = subquery."Hops" AND OE."Source" = subquery."Source";

	WITH "SafeRows" AS (
		SELECT
			"StartVertex",
			"EndVertex"
		FROM
			"Edge"
		WHERE
			"DelMark" = FALSE
	) UPDATE "Edge" OE
		SET "DelMark" = FALSE
			FROM (
				SELECT E."StartVertex", E."EndVertex", E."Hops", E."Source" FROM "Edge" E
					INNER JOIN
						"SafeRows" S1
					 ON
					 	S1."StartVertex" = E."StartVertex"
					 INNER JOIN
					 	"SafeRows" S2
					 ON
						S1."EndVertex" = S2."StartVertex"
						AND S2."EndVertex" = E."EndVertex"
					WHERE
						E."DelMark" = TRUE
			) AS subquery
		WHERE
			OE."StartVertex" = subquery."StartVertex" AND OE."EndVertex" = subquery."EndVertex" AND OE."Hops" = subquery."Hops" AND OE."Source" = subquery."Source";

	WITH "SafeRows" AS (
		SELECT
			"StartVertex",
			"EndVertex"
		FROM
			"Edge"
		WHERE
			"DelMark" = FALSE
	)
	UPDATE
		"Edge" OE
		SET
			"DelMark" = FALSE
		FROM (
			SELECT E."StartVertex", E."EndVertex", E."Hops", E."Source" FROM "Edge" E
					INNER JOIN "SafeRows" S1
		               ON S1."StartVertex" = E."StartVertex"
		            INNER JOIN "SafeRows" S2
		               ON S1."EndVertex" = S2."StartVertex"
		            INNER JOIN "SafeRows" S3
		               ON S2."EndVertex" = S3."StartVertex"
		              AND S3."EndVertex" = E."EndVertex"
					WHERE
						E."DelMark" = TRUE
		) AS subquery
	WHERE
		OE."StartVertex" = subquery."StartVertex" AND OE."EndVertex" = subquery."EndVertex" AND OE."Hops" = subquery."Hops" AND OE."Source" = subquery."Source";

	DELETE
		FROM
			"Edge"
		WHERE
			"DelMark" = TRUE;
END $$



--- RemoveRelatedEdges
--- Id     - Vertex Id
--- Source - source of vertex
CREATE OR REPLACE FUNCTION "RemoveRelatedEdges"(
    Id varchar(256),
    Source varchar(150)
)
RETURNS void AS $$
DECLARE
    StartVertex varchar(256);
    EndVertex varchar(256);
    EdgesToBeRemoved CURSOR FOR
        SELECT "StartVertex", "EndVertex", "Source"
        FROM "Edge"
        WHERE "Hops" = 0
          AND "Source" = Source
          AND ("StartVertex" = Id OR "EndVertex" = Id);
BEGIN
    OPEN EdgesToBeRemoved;
    LOOP
        FETCH EdgesToBeRemoved INTO StartVertex, EndVertex, Source;
        EXIT WHEN NOT FOUND;

        PERFORM "RemoveEdgeWithSpaceSavings"(StartVertex, EndVertex, Source);
    END LOOP;
    CLOSE EdgesToBeRemoved;
END;
$$ LANGUAGE plpgsql;