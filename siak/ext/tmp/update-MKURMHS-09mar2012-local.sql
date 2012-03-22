--<ScriptOptions statementTerminator=";"/>

ALTER TABLE "SIAK"."MKURMHS" DROP CONSTRAINT "FK_KURMHS_PRODI";

ALTER TABLE "SIAK"."MKURMHS" DROP CONSTRAINT "FK_KURMHS_MAHASISWA";

ALTER TABLE "SIAK"."MKURMHS" DROP CONSTRAINT "PK_MKURMHS";

DROP INDEX "SIAK"."PK_MKURMHS";

DROP TABLE "SIAK"."MKURMHS";

CREATE TABLE "SIAK"."MKURMHS" (
		"ID" INTEGER NOT NULL,
		"MAHASISWA_ID" INTEGER NOT NULL,
		"KURIKULUM_ID" INTEGER NOT NULL,
		"PRODI_ID" INTEGER NOT NULL,
		"CTHAJAR" VARCHAR(8),
		"CTERM" VARCHAR(4),
		"CCOHORT" VARCHAR(6) NOT NULL,
		"DCREATEDDATE" DATE,
		"CCREATEDBY" VARCHAR(10),
		"DUPDATEDDATE" DATE,
		"CUPDATEDBY" VARCHAR(10)
	);

CREATE UNIQUE INDEX "SIAK"."PK_MKURMHS" ON "SIAK"."MKURMHS" ("ID" ASC);

ALTER TABLE "SIAK"."MKURMHS" ADD CONSTRAINT "PK_MKURMHS" PRIMARY KEY ("ID");

ALTER TABLE "SIAK"."MKURMHS" ADD CONSTRAINT "FK_KURMHS_PRODI" FOREIGN KEY ("PRODI_ID")
	REFERENCES "SIAK"."MPRODI" ("ID");

ALTER TABLE "SIAK"."MKURMHS" ADD CONSTRAINT "FK_KURMHS_MAHASISWA" FOREIGN KEY ("MAHASISWA_ID")
	REFERENCES "SIAK"."MMAHASISWA" ("ID");

