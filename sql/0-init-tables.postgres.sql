-- Create table Principal
CREATE TABLE IF NOT EXISTS "Principal" (
    "ObjectId" uuid NOT NULL,
    "PrincipalType" smallint NOT NULL,
    "Name" varchar(256) NOT NULL,
    "Email" varchar(500) NOT NULL,
    "DisplayName" varchar(500) NOT NULL,
    "Description" varchar(500) NOT NULL,
    "DataSource" varchar(100) NOT NULL,
    CONSTRAINT "PK_Principal" PRIMARY KEY ("ObjectId")
) WITH (FILLFACTOR = 90);

-- Create unique index
CREATE UNIQUE INDEX IX_Principal_1 ON "Principal" ("Name");

-- Create table Operation
CREATE TABLE IF NOT EXISTS "Operation" (
    "Id" varchar(100) NOT NULL,
    "Name" varchar(250) NOT NULL,
    "Description" varchar(500) NOT NULL,
    CONSTRAINT PK_Operation_1 PRIMARY KEY ("Id")
);

-- Create table Resource
CREATE TABLE IF NOT EXISTS "Resource" (
    "Id" varchar(100) NOT NULL,
    "Name" varchar(100),
    PRIMARY KEY ("Id")
);

-- Create table AccessPredicate
CREATE TABLE IF NOT exists "AccessPredicate" (
    "PrincipalId"  varchar(256) NOT NULL,
    "OperationId" varchar(100) NOT NULL,
    "ResourceId" varchar(256) NOT NULL,
    "PredicateType" smallint NOT NULL,
    CONSTRAINT PK_AccessPredicate PRIMARY KEY ("PrincipalId", "OperationId", "ResourceId", "PredicateType")
) WITH (FILLFACTOR = 90);

-- Create table Edge
CREATE TABLE IF NOT EXISTS "Edge" (
    "StartVertex" varchar(256) NOT NULL,
    "EndVertex" varchar(256) NOT NULL,
    "Hops" integer NOT NULL,
    "Source" varchar(150) NOT NULL,
    "DelMark" boolean NOT NULL
);

-- Create indexes on Edge table
CREATE INDEX IF NOT EXISTS "IX_Edge_1" ON "Edge" ("StartVertex", "EndVertex");
CREATE INDEX IF NOT EXISTS "IX_Edge_2" ON "Edge" ("EndVertex", "StartVertex");
CREATE INDEX IF NOT EXISTS "IX_Edge_3" ON "Edge" ("Hops");
CREATE INDEX IF NOT EXISTS "IX_Edge_4" ON "Edge" ("Source");
CREATE INDEX IF NOT EXISTS "IX_Edge_5" ON "Edge" ("DelMark");