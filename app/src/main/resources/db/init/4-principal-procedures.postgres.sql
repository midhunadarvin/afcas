CREATE OR REPLACE PROCEDURE "AddOrUpdatePrincipal" (
	_Name varchar(256),
	_PrincipalType int,
	_DisplayName varchar(500),
	_Email varchar(500),
	_Description varchar(500),
	_Source varchar(150)
)
 AS $$
BEGIN
      UPDATE
		"Principal"
		SET
			"DisplayName" = COALESCE(
				_DisplayName,
				_Name
			),
			"PrincipalType" = _PrincipalType,
			"Email" = COALESCE(
				_Email,
				''
			),
			"Description" = COALESCE(
				_Description,
				''
			),
			"DataSource" = COALESCE(
				_Source,
				''
			)
		WHERE
			"Name" = _Name;

		IF FOUND THEN
		        RETURN;
		END IF;

	INSERT
		INTO
		"Principal" (
			"ObjectId",
			"Name",
			"DisplayName",
			"PrincipalType",
			"Email",
			"Description",
			"DataSource"
		)
	VALUES (
		gen_random_uuid(),
		_Name,
		COALESCE(
			_DisplayName,
			_Name
		),
		_PrincipalType,
		COALESCE(
			_Email,
			''
		),
		COALESCE(
			_Description,
			''
		),
		COALESCE(
			_Source,
			''
		)
	);
END;

$$
LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE "RemovePrincipal"(
    Name VARCHAR(256)
)
AS $$
BEGIN
    DELETE FROM "Principal" WHERE "Name" = Name;
    IF FOUND THEN
        PERFORM "RemoveRelatedEdges"(Name, 'Principal');
    END IF;
END;
$$ LANGUAGE plpgsql;


-- DROP PROCEDURE IF EXISTS GetPrincipalList;
CREATE OR REPLACE FUNCTION "GetPrincipalList"(
    PrincipalType INT DEFAULT NULL
)
RETURNS TABLE (
    "Name" VARCHAR,
    "DisplayName" VARCHAR,
    "PrincipalType" INT,
    "Email" VARCHAR,
    "Description" VARCHAR
)
AS $$
BEGIN
    RETURN QUERY
    SELECT P."Name",
           P."DisplayName",
           P."PrincipalType"::INT,
           P."Email",
           P."Description"
    FROM "Principal" P
    WHERE P."PrincipalType" = "GetPrincipalList".PrincipalType OR "GetPrincipalList".PrincipalType IS NULL;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION "GetMembersList"(
    IN GroupName varchar(256),
    IN IsFlat int
)
RETURNS TABLE (
    "Name" varchar(256),
    "DisplayName" varchar(256),
    "PrincipalType" varchar(256),
    "Email" varchar(256),
    "Description" varchar(256)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT
           P."Name",
           P."DisplayName",
           P."PrincipalType",
           P."Email",
           P."Description"
      FROM "Principal" P
      INNER JOIN "Edge" E
          ON P."Name" = E."StartVertex"
      WHERE E."EndVertex" = GroupName
        AND E."Source" = 'Principal'
        AND (IsFlat = 1 OR E."Hops" = IsFlat);
END;
$$ LANGUAGE plpgsql;

