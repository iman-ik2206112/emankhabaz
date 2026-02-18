import courseRepo from "@/app/repo/courseRepo";

export async function GET(req,{params}){
    const courseData= await courseRepo.getCourse(params.courseID)
    return Response.json(courseData,{status:200})
}