document.addEventListener("DOMContentLoaded", async function () {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  const courses = currentUser.courses;
  const select_type = document.querySelector("#type");
  const courseLabel = document.querySelector('label[for="course"]');
  courseLabel.textContent = "Choose course (loading)...";
  const select_title = document.querySelector("#title");
  let course_data = null;
  let course_selected = null;
  let course_assessments = null;
  let selected_course_data = null;

  try {
    const promises = courses.map(async (course) => {
      const promise = await fetch(`api/${course}`);
      return await promise.json();
    });

    const instructor_courses = await Promise.all(promises);
    const select_course = document.querySelector("#course");

    const options = instructor_courses
      .map((course) => `<option value="${course.code}">${course.name}</option>`)
      .join("");
    select_course.innerHTML = options;
    courseLabel.textContent = "Choose course";

    async function change_course(event) {
      course_selected = event.target.value;
      try {
        course_data = await fetch(`api/${course_selected}`).then((response) =>
          response.json()
        );

        course_assessments = course_data.assessments;
        if (course_assessments) {
          let select_assessment = document.querySelector("#assessment");
          const assessmentOptions = course_assessments
            .map(
              (assessment) =>
                `<option value="${assessment.id}">${assessment.name}</option>`
            )
            .join("");
          select_assessment.innerHTML = assessmentOptions;
        }
      } catch (error) {
        console.error("Error fetching course data:", error);
      }
      change_type();
    }

    async function change_type() {
      course_selected = select_course.value;
      let selected_type = select_type.value;
      if (course_selected && selected_type) {
        selected_course_data = instructor_courses.find((course) => course.code == course_selected);
        let assesments_of_type = selected_course_data.assessments.filter((assessment) => assessment.type == selected_type);
        let capitalizedType =
          selected_type.charAt(0).toUpperCase() + selected_type.slice(1);
        let followingNumber = assesments_of_type.length + 1;
        let default_name = `${capitalizedType}${assesments_of_type.length > 0 ? " " + followingNumber : ""
          }`;
        select_title.value = default_name;
      }
    }
    select_course.addEventListener("change", change_course);

    select_type.addEventListener("change", change_type);
  } catch (error) {
    console.error("Error fetching initial course data", error);
  }

  document
    .querySelector(".AddAssesment")
    .addEventListener("submit", submission);
  function submission(event) {
    event.preventDefault();

    const selectedCourse = document.querySelector("#course").value;
    const selectedType = document.querySelector("#type").value;
    const title = document.querySelector("#title").value;
    const dueDate = document.querySelector("#date").value;
    const effortHours = document.querySelector("#hours").value;
    const weight = document.querySelector("#weight").value;

    const assessment = {
      course: selectedCourse,
      type: selectedType,
      title: title,
      dueDate: dueDate,
      effortHours: effortHours,
      weight: weight,
    };

    const validation_errors = validation(
      assessment,
      selected_course_data.assessments
    );

    if (validation_errors == null) {
      fetch(`api/${selected_course_data.code}/assessments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(assessment),
      }).then((response) => {
        if (!response.ok) {
          throw new Error("Failed to add assessment");
        }
        location.reload();
      });
    } else {
      alert(validation_errors);
    }
  }
  function validation(newAssessment, exist) {
    const { type, dueDate } = newAssessment;

    if (type === "final" && exist.some((a) => a.type === "final"))
      return "one Final Exam is allowed";

    if (type == "midterm") {
      const count = exist.filter((a) => a.type === "midterm").length;
      if (count >= 2) {
        return "Maximum number for midterms is 2";
      }
    }

    if (exist.some((a) => a.dueDate == dueDate))
      return "There is another assessment already scheduled in this date";

    if (type === "project") {
      const count = exist.filter((a) => a.type === "project").length;
      if (count >= 4) {
        return "Maximum number for project phases is 4";
      }
    }

    if (type === "assignment") {
      const count = exist.filter((a) => a.type === "assignment").length;
      if (count >= 8) {
        return "Maximum number for homeworks is 8";
      }
    }
    return null;
  }
});
