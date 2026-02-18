import prisma from "@/lib/prisma"

export async function getSemesters() {
  return prisma.semester.findMany()
}

export async function getDefaultSemesterId() {
  const defaultSemester = await prisma.semester.findFirst({where:{isDefault:true}})
  return defaultSemester?.id || null;
}
