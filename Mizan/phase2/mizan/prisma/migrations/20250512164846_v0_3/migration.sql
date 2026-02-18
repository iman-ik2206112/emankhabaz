/*
  Warnings:

  - A unique constraint covering the columns `[isDefault]` on the table `Semester` will be added. If there are existing duplicate values, this will fail.

*/
-- CreateIndex
CREATE UNIQUE INDEX "Semester_isDefault_key" ON "Semester"("isDefault");
