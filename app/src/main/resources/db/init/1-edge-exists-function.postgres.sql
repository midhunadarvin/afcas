-- Create function EdgeExists
CREATE OR REPLACE FUNCTION "EdgeExists"(
    StartVertexId varchar(100),
    EndVertexId varchar(100),
    Source varchar(150)
) RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT "Hops"
        FROM "Edge"
        WHERE "StartVertex" = StartVertexId
            AND "EndVertex" = EndVertexId
            AND "Source" = Source
    );
END;
$$ LANGUAGE plpgsql;