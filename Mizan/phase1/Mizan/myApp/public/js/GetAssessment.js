document.addEventListener("DOMContentLoaded", async function () {
  const student_json = localStorage.currentUser;
  const user = JSON.parse(student_json);
  const courses_codes = user.courses;
  const isInstructor = user.role === "instructor";

  let course_data = null;

  // Add 'Add Assessment' button for instructors
  if (isInstructor) {
    const main = document.querySelector(".getassessment-main");
    const addButton = document.createElement("button");
    addButton.id = "add-assessment";
    addButton.textContent = "Add Assessment";

    addButton.addEventListener("click", () => {
      window.location.href = "./Addassessment.html";
    });

    main.insertBefore(addButton, main.firstChild);
  }

  // Fetch course data for each course
  const promises = courses_codes.map(async (course) => {
    const response = await fetch(`api/${course}`);
    return await response.json();
  });

  const taken_courses = await Promise.all(promises);
  const select_course_box = document.querySelector("#course");
  const display_loading_data = document.querySelector("#loading-data");

  // Add the "All Courses" option to the dropdown
  const options = taken_courses
    .map((course) => `<option value="${course.code}">${course.name}</option>`)
    .join("");

  select_course_box.insertAdjacentHTML(
    "beforeend",
    `<option value="all">All Courses</option>` + options
  );
  document.querySelector("#course-label").textContent = "Choose a course:";

  const tableHeadRow = document.querySelector("thead tr");
  if (isInstructor) {
    const editHeader = document.createElement("th");
    editHeader.textContent = "Actions";
    tableHeadRow.appendChild(editHeader);
  }

  // Show all assessments across all courses
  async function displayAssessments(courseCode = "all") {
    const allAssessmentsTable = document.querySelector("#assessmentbody");
    allAssessmentsTable.innerHTML = ""; // Clear previous rows

    let assessmentsToDisplay = [];

    if (courseCode === "all") {
      // Combine assessments from all courses
      taken_courses.forEach((course) => {
        const course_assessments = course.assessments || [];
        course_assessments.forEach((assessment) => {
          assessmentsToDisplay.push({
            ...assessment,
            courseCode: course.code,
            courseName: course.name,
          });
        });
      });
    } else {
      // Filter assessments from the selected course
      const selectedCourse = taken_courses.find(
        (course) => course.code === courseCode
      );
      assessmentsToDisplay = (selectedCourse?.assessments || []).map(
        (assessment) => ({
          ...assessment,
          courseCode: selectedCourse.code,
          courseName: selectedCourse.name,
        })
      );
    }

    // Populate the table with the relevant assessments
    assessmentsToDisplay.forEach((assessment) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${assessment.courseName}</td>
        <td>${assessment.title}</td>
        <td>${assessment.type}</td>
        <td>${assessment.dueDate}</td>
        <td>${assessment.effortHours}</td>
        <td>${assessment.weight}</td>
      `;

      if (isInstructor) {
        const editCell = document.createElement("td");
        editCell.innerHTML = `
          <button class="edit-button" data-id="${assessment.id}" data-course="${assessment.courseCode}">Update</button>
          <button class="delete-button" data-id="${assessment.id}" data-course="${assessment.courseCode}">Delete</button>
        `;
        row.appendChild(editCell);
      }

      allAssessmentsTable.appendChild(row);
    });
  }

  // Load assessments based on selected course
  async function change_course(event) {
    const course_selected = event.target.value;
    display_loading_data.innerHTML = "Loading...";

    try {
      await displayAssessments(course_selected);
      display_loading_data.innerHTML = "";
    } catch (error) {
      console.error("Error fetching course data:", error);
      display_loading_data.innerHTML = "Failed to load assessments.";
    }
  }

  // Add event listener for course selection
  select_course_box.addEventListener("change", change_course);

  // Listen for clicks on the edit and delete buttons
  document.addEventListener("click", function (e) {
    if (e.target.classList.contains("edit-button")) {
      const id = e.target.dataset.id;
      const courseCode = e.target.dataset.course;

      const selected = taken_courses
        .find((course) => course.code === courseCode)
        .assessments.find((a) => a.id == id);

      if (selected) {
        localStorage.setItem(
          "editAssessment",
          JSON.stringify({
            ...selected,
            courseCode,
          })
        );
        window.location.href = "./Addassessment.html";
      }
    }

    if (e.target.classList.contains("delete-button")) {
      const id = e.target.dataset.id;
      const course = e.target.dataset.course;

      if (confirm("Are you sure you want to delete this assessment?")) {
        fetch(`../api/${course}/assessments/${id}`, {
          method: "DELETE",
        }).then((res) => {
          if (res.ok) location.reload();
          else alert("Failed to delete assessment.");
        });
      }
    }
  });

  // Initial load to display all assessments
  displayAssessments();
});
