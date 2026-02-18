import courseRepo from "@/app/repo/courseRepo";


export async function POST(req,{params}){
    const comment = await req.json()
    const newComment =await courseRepo.addComment(params.courseID,comment)
    return Response.json(newComment,{status:201})
}