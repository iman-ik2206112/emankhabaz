document.addEventListener("DOMContentLoaded", async function () {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  const isInstructor = currentUser?.role === "instructor";

  if (!isInstructor) {
    document
      .querySelector("button.add-comment")
      .addEventListener("click", function () {
        window.location.href = "./AddComment.html";
      });
  } else {
    const addCommentButton = document.querySelector("button.add-comment");
    if (addCommentButton) addCommentButton.remove();
  }

  const courses_codes = currentUser.courses;
  const select_box = document.querySelector("#course");
  const table = document.querySelector("#commentbody");
  const selectCourseLabel = document.querySelector('label[for="course"]');
  selectCourseLabel.textContent = "Loading Courses...";

  const courseResponses = await Promise.all(
    courses_codes.map(async (code) => {
      const res = await fetch(`api/${code}`);
      return await res.json();
    })
  );

  const options = courseResponses
    .map((course) => `<option value="${course.code}">${course.name}</option>`)
    .join("");
  select_box.innerHTML = `<option value="all" selected>All Courses</option>${options}`;
  selectCourseLabel.textContent = "Select Course";

  const tableHeadRow = document.querySelector("thead tr");
  if (isInstructor) {
    const respondHeader = document.createElement("th");
    respondHeader.textContent = "Respond";
    tableHeadRow.appendChild(respondHeader);
  }

  function renderComments(course) {
    if (!course.comments) return;

    const parentComments = course.comments.filter((c) => !c.isResponse);
    const responses = course.comments.filter((c) => c.isResponse);

    parentComments.forEach((comment) => {
      const row = document.createElement("tr");
      row.innerHTML = `
          <td>${course.name}</td>
          <td>${comment.title}</td>
          <td>${comment.body}</td>
          <td>${comment.author}</td>
          <td>${comment.date}</td>
        `;

      if (isInstructor) {
        const cell = document.createElement("td");
        cell.innerHTML = `
            <button class="respond-btn" data-course="${course.code}" data-id="${comment.id}" data-title="${comment.title}">Respond</button>
          `;
        row.appendChild(cell);
      }

      table.appendChild(row);

      responses
        .filter((r) => r.title === `Re: ${comment.title}`)
        .forEach((resp) => {
          const respRow = document.createElement("tr");
          respRow.innerHTML = `
              <td>${course.name}</td>
              <td style="padding-left: 2rem;">↳ ${resp.title}</td>
              <td>${resp.body}</td>
              <td>${resp.author}</td>
              <td>${resp.date}</td>
            `;
          table.appendChild(respRow);
        });
    });
  }

  function updateComments() {
    const selected = select_box.value;
    table.innerHTML = "";
    if (selected === "all") {
      courseResponses.forEach(renderComments);
    } else {
      const course = courseResponses.find((c) => c.code === selected);
      if (course) renderComments(course);
    }
  }

  updateComments();
  select_box.addEventListener("change", updateComments);

  //respond button
  document.addEventListener("click", function (e) {
    if (e.target.classList.contains("respond-btn")) {
      const courseCode = e.target.dataset.course;
      const commentId = e.target.dataset.id;
      const title = e.target.dataset.title;

      sessionStorage.setItem(
        "respondTo",
        JSON.stringify({ courseCode, commentId, title })
      );
      window.location.href = "./AddComment.html";
    }
  });
});
