import courseRepo from "@/app/repo/courseRepo";

export async function GET(req){
    const comment= await courseRepo.getComments()
    return Response.json(comment,{status:200})
}