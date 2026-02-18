import fs from "fs-extra";
import path from "path";

class CourseRepo {
  constructor() {
    this.filePath = path.join(process.cwd(), "app/data/courses.json");
  }

  async getCourses() {
    return fs.readJSON(this.filePath);
  }
  async getCourse(courseCode) {
    const courses = await this.getCourses();
    const foundCourse = courses.find((a) => a.code == courseCode);
    return foundCourse;
  }

  async writeCourses(courses) {
    return fs.writeJSON(this.filePath, courses, { spaces: 2 });
  }

  //GET /api/course/assesment
  async getAssessments() {
    const courses = await this.getCourses();
    return courses.flatMap((course) =>
      course.assessments.map((a) => ({
        ...a,
        courseCode: course.code,
        courseName: course.name,
      }))
    );
  }

  //GET /api/corses/comment
  async getComments() {
    const courses = await this.getCourses();
    return courses.flatMap((course) =>
      course.comments.map((c) => ({
        ...c,
        courseCode: course.code,
        courseName: course.name,
      }))
    );
  }

  //POST /api/[course]/assesment
  async addAssessment(courseCode, assessment) {
    const courses = await this.getCourses();
    const course = courses.find((c) => c.code === courseCode);
    if (!course) throw new Error("Course not found");

    const maxId = Math.max(0, ...course.assessments.map((a) => a.id));
    assessment.id = maxId + 1;
    course.assessments.push(assessment);

    await this.writeCourses(courses);
    return assessment;
  }

  //POST /api/[corses]/comment
  async addComment(courseCode, comment) {
    const courses = await this.getCourses();
    const course = courses.find((c) => c.code === courseCode);
    if (!course) throw new Error("Course not found");

    const maxId = Math.max(0, ...course.comments.map((c) => c.id));
    comment.id = maxId + 1;

    course.comments.push(comment);
    await this.writeCourses(courses);

    return comment;
  }

  //PUT /api/[course]/[assesment]
  async updateAssessment(courseCode, assessmentId, updatedFields) {
    const courses = await this.getCourses();
    const course = courses.find((c) => c.code === courseCode);
    if (!course) throw new Error("Course not found");

    const assessment_index = course.assessments.findIndex(
      (a) => a.id === assessmentId
    );
    course.assessments[assessment_index] = {
      ...course.assessments[assessment_index],
      ...updatedFields,
    };

    await this.writeCourses(courses);
    return course.assessments[assessment_index];
  }

  //DELETE /api/[course]/[assesment]
  async deleteAssessment(courseCode, assessmentId) {
    const courses = await this.getCourses();
    const course = courses.find((c) => c.code === courseCode);
    if (!course) throw new Error("Course not found");

    const index = course.assessments.findIndex((a) => a.id === assessmentId);
    if (index === -1) throw new Error("Assessment not found");

    const [removed] = course.assessments.splice(index, 1);
    await this.writeCourses(courses);
    return removed;
  }
}

export default new CourseRepo();
