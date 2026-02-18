document.addEventListener("DOMContentLoaded", async function () {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  const courses = currentUser.courses;
  const select_type = document.querySelector("#type");
  const select_title = document.querySelector("#title");
  const courseLabel = document.querySelector('label[for="course"]');
  courseLabel.textContent = "Choose course (loading)...";

  let course_data = null;
  let course_selected = null;
  let course_assessments = null;
  let selected_course_data = null;

  const editAssessment = JSON.parse(localStorage.getItem("editAssessment"));
  const isUpdate = !!editAssessment;

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

    if (isUpdate) {
      document.querySelector("h2").textContent = "Update Assessment";
      document.querySelector(".addAssess").textContent = "Update";
      select_course.value = editAssessment.courseCode;
      select_type.value = editAssessment.type;
      select_title.value = editAssessment.title;
      document.querySelector("#date").value = editAssessment.dueDate;
      document.querySelector("#hours").value = editAssessment.effortHours;
      document.querySelector("#weight").value = editAssessment.weight;
    }

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
      const selected_type = select_type.value;

      if (course_selected && selected_type && !isUpdate) {
        selected_course_data = instructor_courses.find(
          (course) => course.code == course_selected
        );
        let assesments_of_type = selected_course_data.assessments.filter(
          (assessment) => assessment.type == selected_type
        );

        let capitalizedType =
          selected_type.charAt(0).toUpperCase() + selected_type.slice(1);
        let followingNumber = assesments_of_type.length + 1;
        let default_name = `${capitalizedType}${
          assesments_of_type.length > 0 ? " " + followingNumber : ""
        }`;
        select_title.value = default_name;
      }
    }

    select_course.addEventListener("change", change_course);
    select_type.addEventListener("change", change_type);

    if (!isUpdate) {
      await change_course({ target: select_course });
    }
  } catch (error) {
    console.error("Error fetching initial course data", error);
  }

  document
    .querySelector(".AddAssesment")
    .addEventListener("submit", submission);

  async function submission(event) {
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

    const currentCourse =
      selected_course_data ||
      (await fetch(`api/${selectedCourse}`).then((res) => res.json()));
    const existingAssessments = currentCourse.assessments;

    if (!isUpdate) {
      const validation_errors = validation(assessment, existingAssessments);
      if (validation_errors) {
        alert(validation_errors);
        return;
      }
    }

    const endpoint = isUpdate
      ? `../api/${editAssessment.courseCode}/assessments/${editAssessment.id}`
      : `api/${selectedCourse}/assessments`;

    const method = isUpdate ? "PUT" : "POST";

    fetch(endpoint, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(assessment),
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to submit assessment");
        if (isUpdate) {
          alert("Assessment updated!");
          localStorage.removeItem("editAssessment");
        } else {
          alert("Assessment added!");
        }
        window.location.href = "GetAssessment.html";
      })
      .catch((err) => {
        console.error(err);
        alert("Something went wrong.");
      });
  }

  function validation(newAssessment, exist) {
    const { type, dueDate } = newAssessment;

    if (type === "final" && exist.some((a) => a.type === "final"))
      return "Only one Final Exam is allowed";

    if (type == "midterm") {
      const count = exist.filter((a) => a.type === "midterm").length;
      if (count >= 2) return "Maximum number of midterms is 2";
    }

    if (exist.some((a) => a.dueDate == dueDate))
      return "There is another assessment already scheduled on this date";

    if (type === "project") {
      const count = exist.filter((a) => a.type === "project").length;
      if (count >= 4) return "Maximum number of project phases is 4";
    }

    if (type === "assignment") {
      const count = exist.filter((a) => a.type === "assignment").length;
      if (count >= 8) return "Maximum number of homeworks is 8";
    }

    return null;
  }
});
