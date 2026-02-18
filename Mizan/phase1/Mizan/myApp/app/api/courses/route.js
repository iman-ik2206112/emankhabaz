import courseRepo from "@/app/repo/courseRepo";

export async function GET(req){
    const courseData= await courseRepo.getCourses()
    return Response.json(courseData,{status:200})
}