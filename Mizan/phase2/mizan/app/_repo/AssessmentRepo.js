import prisma from "@/lib/prisma";
import sectionRepo from "@/app/_repo/SectionRepo";
import { capitalize } from "@/app/actions/utils";

class AssessmentRepo {
  async getAssessmentTypes() {
    return await prisma.assessmentType.findMany();
  }


  async #readAssessments() {
    return await prisma.assessment.findMany()
  }

  async #writeAssessments(assessments) {
    await prisma.assessment.create({data:assessments})
  }

  async getAssessmentById(id) {
    const newId = parseInt(id)
    return await prisma.assessment.findUnique({where:{id:newId}})
  }

  async getAssessmentsBySection(sectionCRN) {
    return await prisma.assessment.findMany({where:{sectionCRN:sectionCRN}})
  }

  async countAssessmentsByType(sectionCRN, type) {
    //return count
    const filteredAssessment= await prisma.assessment.aggregate({
      where:{sectionCRN:sectionCRN,type:type},
      _count:true
    })
    return filteredAssessment._count
  }

  async countAssessmentsByDueDate(sectionCRN, dueDate) {
    const isoDate=new Date(dueDate).toISOString()
    const filteredAssessment= await prisma.assessment.aggregate({
      where:{sectionCRN:sectionCRN,dueDate:isoDate},
      _count:true
    })
    return filteredAssessment._count
  }

  async #getUserAssessments(user, semesterId) {
    //get  user's sections then get assessments from said sections
    const userSections = await sectionRepo.getSections(user, semesterId);
    const sectionCRNs = userSections.map((s) => s.crn);
    const assessments = await prisma.assessment.findMany({
      where:{sectionCRN:{in:sectionCRNs}}
    })
    return assessments
  }

  async getAssessments(user, semesterId, sectionCRN) {
    if (!user && (!sectionCRN || sectionCRN === "all")) return [];

    const assessments =
      sectionCRN && sectionCRN !== "all"
        ? await this.getAssessmentsBySection(sectionCRN)
        : await this.#getUserAssessments(user, semesterId);

    // Sort by section CRN
    assessments.sort((a, b) => a.sectionCRN.localeCompare(b.sectionCRN));

    // Enrich with section data
    for (const assessment of assessments) {
      assessment.section = await sectionRepo.getSectionById(
        assessment.sectionCRN
      );
    }

    return assessments;
  }

  async addAssessment(assessment) {
    assessment.dueDate=new Date(assessment.dueDate).toISOString()
    const {id,...assessmentData}=assessment //excluding the predetermined id
    const newAssessment= await prisma.assessment.create({data:assessmentData})
    return newAssessment;
  }

  async updateAssessment(updatedAssessment) {
    
    const existing=await prisma.assessment.findUnique({where:{id:updatedAssessment.id}})

    if (!existing) throw new Error("Assessment not found");
    updatedAssessment.dueDate=new Date(updatedAssessment.dueDate).toISOString()
    const modifiedAssessment= await prisma.assessment.update({where:{id:updatedAssessment.id},data:updatedAssessment})
    return modifiedAssessment;
  }

  async deleteAssessment(id) {
    await prisma.assessment.delete({where:{id}})
  }

  async generateAssessmentTitle(sectionCRN, type) {
    const count = (await this.countAssessmentsByType(sectionCRN, type)) + 1;
    return type === "project"
      ? `Project Phase ${count}`
      : `${capitalize(type)} ${count}`;
  }

  async getAssessmentSummary(user, semesterId) {
    console.log('semesterId from assessments',semesterId);
    
    const assessments = await this.getAssessments(user, semesterId, "all");
    // Group assessments by sectionCRN and type then compute
    // the count and total effort hours
    const summary = assessments.reduce((acc, assessment) => {
      const key = `${assessment.sectionCRN}-${assessment.type}`;

      if (!acc[key]) {
        acc[key] = {
          sectionCRN: assessment.sectionCRN,
          courseName: `${assessment.section.courseCode} - ${assessment.section.courseName}`,
          type: assessment.type,
          count: 0,
          effortHours: 0,
        };
      }

      acc[key].count += 1;
      acc[key].effortHours += assessment.effortHours;
      return acc;
    }, {});

    console.log("Assessment Summary:", Object.values(summary));
    return Object.values(summary);
  }
}

export default new AssessmentRepo();
