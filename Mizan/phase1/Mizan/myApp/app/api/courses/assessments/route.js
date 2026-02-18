import courseRepo from "@/app/repo/courseRepo";

export async function GET(req){
    const assessment= await courseRepo.getAssessments()
    return Response.json(assessment,{status:200})
}