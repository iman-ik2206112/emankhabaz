-- CreateTable
CREATE TABLE "AssessmentType" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "label" TEXT NOT NULL
);

-- CreateTable
CREATE TABLE "Assessments" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "sectionCRN" TEXT NOT NULL,
    "type" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "effortHours" INTEGER NOT NULL,
    "weight" INTEGER NOT NULL,
    "dueDate" DATETIME NOT NULL,
    CONSTRAINT "Assessments_sectionCRN_fkey" FOREIGN KEY ("sectionCRN") REFERENCES "Sections" ("crn") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Assessments_type_fkey" FOREIGN KEY ("type") REFERENCES "AssessmentType" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Comments" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "sectionCRN" TEXT NOT NULL,
    "authorId" INTEGER NOT NULL,
    "title" TEXT NOT NULL,
    "content" TEXT NOT NULL,
    "createdDate" DATETIME NOT NULL,
    "replyToCommentId" INTEGER NOT NULL,
    CONSTRAINT "Comments_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES "Users" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Comments_sectionCRN_fkey" FOREIGN KEY ("sectionCRN") REFERENCES "Sections" ("crn") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Sections" (
    "crn" TEXT NOT NULL PRIMARY KEY,
    "courseCode" TEXT NOT NULL,
    "courseName" TEXT NOT NULL,
    "creditHours" INTEGER NOT NULL,
    "instructorId" INTEGER NOT NULL,
    "program" TEXT NOT NULL,
    "semester" TEXT NOT NULL,
    "userId" INTEGER NOT NULL,
    CONSTRAINT "Sections_userId_fkey" FOREIGN KEY ("userId") REFERENCES "Users" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Sections_semester_fkey" FOREIGN KEY ("semester") REFERENCES "Semesters" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

-- CreateTable
CREATE TABLE "Semesters" (
    "id" TEXT NOT NULL PRIMARY KEY,
    "label" TEXT NOT NULL,
    "isDefault" BOOLEAN NOT NULL
);

-- CreateTable
CREATE TABLE "Users" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "firstName" TEXT NOT NULL,
    "lastName" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "password" TEXT NOT NULL,
    "role" TEXT NOT NULL,
    "program" TEXT NOT NULL
);

-- CreateIndex
CREATE UNIQUE INDEX "Sections_crn_key" ON "Sections"("crn");

-- CreateIndex
CREATE UNIQUE INDEX "Sections_courseCode_key" ON "Sections"("courseCode");
