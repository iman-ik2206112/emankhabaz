/*
  Warnings:

  - You are about to drop the column `userId` on the `Section` table. All the data in the column will be lost.

*/
-- CreateTable
CREATE TABLE "_StudentSections" (
    "A" TEXT NOT NULL,
    "B" INTEGER NOT NULL,
    CONSTRAINT "_StudentSections_A_fkey" FOREIGN KEY ("A") REFERENCES "Section" ("crn") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "_StudentSections_B_fkey" FOREIGN KEY ("B") REFERENCES "User" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- RedefineTables
PRAGMA defer_foreign_keys=ON;
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Section" (
    "crn" TEXT NOT NULL PRIMARY KEY,
    "courseCode" TEXT NOT NULL,
    "courseName" TEXT NOT NULL,
    "creditHours" INTEGER NOT NULL,
    "instructorId" INTEGER NOT NULL,
    "program" TEXT NOT NULL,
    "semester" TEXT NOT NULL,
    CONSTRAINT "Section_semester_fkey" FOREIGN KEY ("semester") REFERENCES "Semester" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "new_Section" ("courseCode", "courseName", "creditHours", "crn", "instructorId", "program", "semester") SELECT "courseCode", "courseName", "creditHours", "crn", "instructorId", "program", "semester" FROM "Section";
DROP TABLE "Section";
ALTER TABLE "new_Section" RENAME TO "Section";
CREATE UNIQUE INDEX "Section_crn_key" ON "Section"("crn");
CREATE UNIQUE INDEX "Section_courseCode_key" ON "Section"("courseCode");
PRAGMA foreign_keys=ON;
PRAGMA defer_foreign_keys=OFF;

-- CreateIndex
CREATE UNIQUE INDEX "_StudentSections_AB_unique" ON "_StudentSections"("A", "B");

-- CreateIndex
CREATE INDEX "_StudentSections_B_index" ON "_StudentSections"("B");
