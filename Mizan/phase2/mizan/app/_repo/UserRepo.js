import prisma from "@/lib/prisma"

/* ToDo: Refactor this code to use a database instead of a JSON file.
   When refactoring to use a database, remove private method: 
    #readUsers
   Implement database queries for getUser and login instead.
*/
class UserRepo {
  async getUser(id) {
    return await prisma.user.findUnique({where:{id},include:{registeredSections:true}})
  }

  // Public method to authenticate a user by email and password
  async login(email, password) {
    const user = await prisma.user.findUnique({where:{email}})

    // Check if user exists and password matches
    if (!user || user.password !== password) {
      throw new Error("Incorrect username or password.");
    }
    // Remove password from user object for security
    delete user.password;
    user.name = `${user.firstName} ${user.lastName}`;
    user.isStudent = user.role === "Student";
    user.isInstructor = user.role === "Instructor";
    user.isCoordinator = user.role === "Coordinator";
    return user;
  }
}

export default new UserRepo();
