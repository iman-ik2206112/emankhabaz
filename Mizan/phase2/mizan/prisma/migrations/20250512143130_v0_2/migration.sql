/*
  Warnings:

  - You are about to drop the `Assessments` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `Comments` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `Sections` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `Semesters` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `Users` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropTable
PRAGMA foreign_keys=off;
DROP TABLE "Assessments";
PRAGMA foreign_keys=on;

-- DropTable
PRAGMA foreign_keys=off;
DROP TABLE "Comments";
PRAGMA foreign_keys=on;

-- DropTable
PRAGMA foreign_keys=off;
DROP TABLE "Sections";
PRAGMA foreign_keys=on;

-- DropTable
PRAGMA foreign_keys=off;
DROP TABLE "Semesters";
PRAGMA foreign_keys=on;

-- DropTable
PRAGMA foreign_keys=off;
DROP TABLE "Users";
PRAGMA foreign_keys=on;

-- CreateTable
CREATE TABLE "Semester" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "label" TEXT NOT NULL,
    "isDefault" BOOLEAN NOT NULL
);

-- CreateTable
CREATE TABLE "User" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "firstName" TEXT NOT NULL,
    "lastName" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "role" TEXT NOT NULL,
    "program" TEXT NOT NULL
);

-- CreateTable
CREATE TABLE "Assessment" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "sectionCRN" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "effortHours" INTEGER NOT NULL,
    "weight" INTEGER NOT NULL,
    "dueDate" DATETIME NOT NULL,
    CONSTRAINT "Assessment_sectionCRN_fkey" FOREIGN KEY ("sectionCRN") REFERENCES "Section" ("crn") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Assessment_type_fkey" FOREIGN KEY ("type") REFERENCES "AssessmentType" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Comment" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "sectionCRN" TEXT NOT NULL,
    "authorId" INTEGER NOT NULL,
    "title" TEXT NOT NULL,
    "content" TEXT NOT NULL,
    "createdDate" DATETIME NOT NULL,
    "replyToCommentId" INTEGER NOT NULL,
    CONSTRAINT "Comment_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES "User" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Comment_sectionCRN_fkey" FOREIGN KEY ("sectionCRN") REFERENCES "Section" ("crn") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Section" (
    "crn" TEXT NOT NULL PRIMARY KEY,
    "courseCode" TEXT NOT NULL,
    "courseName" TEXT NOT NULL,
    "creditHours" INTEGER NOT NULL,
    "instructorId" INTEGER NOT NULL,
    "program" TEXT NOT NULL,
    "semester" TEXT NOT NULL,
    "userId" INTEGER NOT NULL,
    CONSTRAINT "Section_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Section_semester_fkey" FOREIGN KEY ("semester") REFERENCES "Semester" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateIndex
CREATE UNIQUE INDEX "Section_crn_key" ON "Section"("crn");

-- CreateIndex
CREATE UNIQUE INDEX "Section_courseCode_key" ON "Section"("courseCode");
