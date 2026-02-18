import { PrismaClient } from "@prisma/client";
import path from "path";
import fs from "fs-extra";

async function main() {
  const prisma = new PrismaClient();
  await prisma.assessmentType.deleteMany();
  await prisma.semester.deleteMany();
  await prisma.user.deleteMany();
  await prisma.section.deleteMany();
  await prisma.assessment.deleteMany();
  await prisma.comment.deleteMany();
  
  try {
    const basePath = path.join(process.cwd(), "data");

    const assessmentTypesFilePath = path.join(basePath, "assessment-types.json");
    const assessmentTypes = await fs.readJSON(assessmentTypesFilePath);
    for (const type of assessmentTypes) {
      console.log("Creating assessment type:", type);
      await prisma.assessmentType.create({ data: type });
    }

    const semestersFilePath = path.join(basePath, "semesters.json");
    const semesters = await fs.readJSON(semestersFilePath);
    for (const semester of semesters) {
      console.log("Creating semester:", semester);
      await prisma.semester.create({ data: semester });
    }
    const sectionsFilePath = path.join(basePath, "sections.json");
    const sections = await fs.readJSON(sectionsFilePath);
    for (const section of sections) {
      console.log("Creating section:", section);
      await prisma.section.create({ data: section });
    }

    const usersFilePath = path.join(basePath, "users.json");
    const users = await fs.readJSON(usersFilePath);
    for (const user of users) {
      const { registeredSections, ...userData } = user;
      console.log("Creating user:", user);
      await prisma.user.create({ data: userData });
    }

    

    //updating the users to include registered sections
    
    for (const user of users) {
      console.log(user);
      const registeredSectionsList = user.registeredSections
      if(user.registeredSections){
      await prisma.user.update({
        where:{id:user.id},
        data:{
          registeredSections:{
            connect:registeredSectionsList.map(usercrn=>({crn:usercrn}))
          }
        }
      })
    }
    }

    const assessmentsFilePath = path.join(basePath, "assessments.json");
    const assessments = await fs.readJSON(assessmentsFilePath);
    for (const assessment of assessments) {
      assessment.dueDate=new Date(assessment.dueDate).toISOString()
      console.log("Creating assessment:", assessment);
      await prisma.assessment.create({ data: assessment });
    }

    const commentsFilePath = path.join(basePath, "comments.json");
    const comments = await fs.readJSON(commentsFilePath);
    for (const comment of comments) {
      delete comment.id
      comment.createdDate=new Date(comment.createdDate).toISOString()
      console.log("Creating comment:", comment);
      await prisma.comment.create({ data: comment });
    }
  } catch (e) {
    console.error(e);
    throw e;
  } finally {
    await prisma.$disconnect();
  }
}

await main();