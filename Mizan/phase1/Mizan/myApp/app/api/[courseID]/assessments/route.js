import courseRepo from "@/app/repo/courseRepo";

export async function GET(req, {params}){
    const assesment= await courseRepo.getAssessments(params.id)
    const return_json= {...assesment,id:params.id}
    return Response.json(return_json,{status:200})

}

export async function POST(req,{params}){
    const assesment = await req.json()
    const newAssessment =await courseRepo.addAssessment(params.courseID,assesment)
    return Response.json(newAssessment,{status:201})
}