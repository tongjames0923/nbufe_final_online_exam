CREATE TABLE "content" (
	"id"	INTEGER NOT NULL UNIQUE,
	"content_type"	INTEGER NOT NULL,
	"resource"	BLOB NOT NULL,
	"note"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
);