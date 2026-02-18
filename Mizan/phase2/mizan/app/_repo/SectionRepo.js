import prisma from "@/lib/prisma";
import UserRepo from "./UserRepo";
class SectionRepo {


  async #readSections() {
    return await prisma.section.findMany()
  }

  async getSectionById(sectionCRN) {
    return await prisma.section.findUnique({where:{crn:sectionCRN}})
  }

  async getSections(user, semesterId) {
    console.log("SectionRepo.getSections - Semester ID:", semesterId);
    
    if (!user) return [];

    // Then filter by user role
    if (user.isStudent) { 
      //filters this semester. if student, go to student's registeredSections, map to crn, find ones that include said crn
      const userData= await UserRepo.getUser(user.id)
      const registeredSections= userData.registeredSections
      const registeredSectionsList=registeredSections.map(section=>section.crn)
      const studentSections=await prisma.section.findMany({
        where:{
          semester:semesterId,
          crn:{in:registeredSectionsList}
        }})
      return studentSections
    }

    if (user.isInstructor) {
      return await prisma.section.findMany({
        where:{instructorId:user.id, 
        semester:semesterId
      }})
    }

    if (user.isCoordinator) {
      return await prisma.section.findMany({
        where:{
          program:user.program,
          semester:semesterId
        }})
    }
    
    return [];
  }
}

export default new SectionRepo();