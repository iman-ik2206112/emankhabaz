document.addEventListener("DOMContentLoaded", async function () {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  const courses = currentUser.courses;
  const courseLabel = document.querySelector('label[for="course"]');
  const heading = document.querySelector("h2");
  const respondData = JSON.parse(sessionStorage.getItem("respondTo") || null);
  let selected_course_data = null;

  try {
    const courseData = await Promise.all(
      courses.map((course) => fetch(`api/${course}`).then((res) => res.json()))
    );

    const selectCourse = document.querySelector("#course");
    const options = courseData
      .map((course) => `<option value="${course.code}">${course.name}</option>`)
      .join("");

    selectCourse.innerHTML = options;
    courseLabel.textContent = "Choose course";

    if (respondData) {
      heading.textContent = "Respond to Comment";
      document.querySelector("#title").value = `Re: ${respondData.title}`;
      selectCourse.value = respondData.courseCode;
      selectCourse.disabled = true;
    }

    selectCourse.addEventListener("change", (e) => {
      const selected = e.target.value;
      selected_course_data = courseData.find((c) => c.code === selected);
    });
  } catch (error) {
    console.error("Error loading course data:", error);
  }

  document
    .querySelector(".comment-form")
    .addEventListener("submit", async function (event) {
      event.preventDefault();

      heading.textContent = "Posting Comment...";
      const selectedCourse = document.querySelector("#course").value;
      const title = document.querySelector("#title").value;
      const body = document.querySelector("#body").value;

      const comment = {
        title,
        body,
        date: new Date().toISOString().split("T")[0],
        author: currentUser.name,
        isResponse: title.startsWith("Re:"),
      };

      try {
        const response = await fetch(`api/${selectedCourse}/comment`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(comment),
        });

        if (!response.ok) throw new Error("Failed to add comment");

        sessionStorage.removeItem("respondTo");
        heading.textContent = "Add Comment";
        alert("Comment posted!");
        window.location.href = "./GetComment.html";
      } catch (err) {
        heading.textContent = "Add Comment";
        alert("Error adding comment: " + err.message);
      }
    });
});
