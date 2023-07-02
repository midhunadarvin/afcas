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