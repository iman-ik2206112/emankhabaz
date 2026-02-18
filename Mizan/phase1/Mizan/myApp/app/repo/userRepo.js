import fs from "fs-extra";
import path from "path";

class UserRepo {
  constructor() {
    this.filePath = path.join(process.cwd(), "app/data/users.json");
  }

  async getUsers() {
    const users = await fs.readJson(this.filePath);
    return users;
  }

  async login(email_input, password_input) {
    const userData = await this.getUsers();
    const userFound = userData.find(
      (user) => user.email === email_input && user.password === password_input
    );
    return userFound;
  }
}

export default new UserRepo();
