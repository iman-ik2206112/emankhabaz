import courseRepo from "@/app/repo/courseRepo";


export async function PUT(req, {params}){
    const newAssessment= await req.json()
    const updatedAssesment = await courseRepo.updateAssessment(
      params.courseID,
      parseInt(params.assessmentID),
      newAssessment
    );
    return Response.json(updatedAssesment, { status: 200 });
}

export async function DELETE(req, {params}){
    const assesment= await courseRepo.deleteAssessment(params.courseID,parseInt(params.assessmentID))
    return Response.json({'id':params.assessmentID,'deletedAssessment':assesment},{status:200})

}