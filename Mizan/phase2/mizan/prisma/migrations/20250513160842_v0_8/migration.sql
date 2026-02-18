-- RedefineTables
PRAGMA defer_foreign_keys=ON;
PRAGMA foreign_keys=OFF;
CREATE TABLE "new_Comment" (
    "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "sectionCRN" TEXT NOT NULL,
    "authorId" INTEGER,
    "title" TEXT NOT NULL,
    "content" TEXT NOT NULL,
    "createdDate" DATETIME NOT NULL,
    "replyToCommentId" INTEGER NOT NULL,
    CONSTRAINT "Comment_authorId_fkey" FOREIGN KEY ("authorId") REFERENCES "User" ("id") ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT "Comment_sectionCRN_fkey" FOREIGN KEY ("sectionCRN") REFERENCES "Section" ("crn") ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO "new_Comment" ("authorId", "content", "createdDate", "id", "replyToCommentId", "sectionCRN", "title") SELECT "authorId", "content", "createdDate", "id", "replyToCommentId", "sectionCRN", "title" FROM "Comment";
DROP TABLE "Comment";
ALTER TABLE "new_Comment" RENAME TO "Comment";
PRAGMA foreign_keys=ON;
PRAGMA defer_foreign_keys=OFF;
