document.addEventListener("DOMContentLoaded", () => {
  //collecting due dates to fill in the calendar and left box
  const assessments_due = [];
  async function getDues() {
    const currentUser = JSON.parse(localStorage.getItem("currentUser"));
    const userCourses = currentUser.courses;
    const promise = await fetch("/api/courses/assessments");
    const allAssessments = await promise.json();
    const userAssessments = allAssessments.filter((assessment) =>
      userCourses.includes(assessment.courseCode)
    );

    assessments_due.push(...userAssessments);

    renderCalendar(currentMonth, currentYear);
    document.querySelector(".assessments-due").innerHTML = "Select a Date";
    const month = String(currentMonth + 1).padStart(2, "0");
    const day = String(currentDate.getDate()).padStart(2, "0");
    const date_selected = `${currentYear}-${month}-${day}`;
    renderRightBox(date_selected);
  }
  getDues();

  function highlightDates(i, year, month, day) {
    assessments_due.forEach((assessment) => {
      const dateString = assessment.dueDate;
      const date = new Date(dateString);
      if (
        i === date.getDate() &&
        year === date.getFullYear() &&
        month === date.getMonth()
      ) {
        day.classList.add("day-with-assessment");
      }
    });
  }

  //Rendering Calender
  const calendarDates = document.querySelector(".calendar-dates");
  const monthYear = document.querySelector("#month-year");
  const prevMonthBtn = document.querySelector("#prev-month");
  const nextMonthBtn = document.querySelector("#next-month");

  let currentDate = new Date();
  let currentMonth = currentDate.getMonth();
  let currentYear = currentDate.getFullYear();

  const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  function renderCalendar(month, year) {
    calendarDates.innerHTML = "";
    monthYear.textContent = `${months[month]} ${year}`;

    const firstDay = new Date(year, month, 1).getDay();

    const daysInMonth = new Date(year, month + 1, 0).getDate();

    for (let i = 0; i < firstDay; i++) {
      const blank = document.createElement("div");
      calendarDates.appendChild(blank);
    }

    const today = new Date();

    for (let i = 1; i <= daysInMonth; i++) {
      const day = document.createElement("div");
      day.textContent = i;

      // Highlight today's date
      if (
        i === today.getDate() &&
        year === today.getFullYear() &&
        month === today.getMonth()
      ) {
        day.classList.add("current-date");
      }
      //highlighting any date with assignments due
      highlightDates(i, year, month, day);

      calendarDates.appendChild(day);
    }
  }

  prevMonthBtn.addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
      currentMonth = 11;
      currentYear--;
    }
    renderCalendar(currentMonth, currentYear);
  });

  nextMonthBtn.addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
      currentMonth = 0;
      currentYear++;
    }
    renderCalendar(currentMonth, currentYear);
  });
  function renderRightBox(date_selected) {
    const date_selected_assessments = assessments_due.filter(
      (assessment) => assessment.dueDate == date_selected
    );
    const assessments_box = document.querySelector(".assessments-due");
    assessments_box.innerHTML = `${date_selected}`;
    date_selected_assessments.forEach((assessment) => {
      const assessment_div = document.createElement("div");
      assessment_div.classList.add("assessment");
      assessment_div.innerHTML = `
      <p class="name">${assessment.courseName}-${assessment.title}</p>
      <p>Type: ${assessment.type}</p>
      <p>Effort hours: ${assessment.effortHours}</p>
      <p>Weight: ${assessment.weight}</p>`;
      assessments_box.appendChild(assessment_div);
    });
  }
  calendarDates.addEventListener("click", (e) => {
    if (
      !e.target ||
      !e.target.textContent.trim() ||
      e.target.textContent.length > 2 ||
      e.target == "0"
    ) {
      return; // Ignore clicks on empty or invalid elements
    }
    const divs = document.querySelectorAll(".calendar-dates div"); // Only selects divs inside .calendar-dates
    divs.forEach((div) => {
      div.classList.remove("date-selected");
    });

    e.target.classList.add("date-selected");
    const month = String(currentMonth + 1).padStart(2, "0");
    const day = String(e.target.textContent).padStart(2, "0");
    const date_selected = `${currentYear}-${month}-${day}`;
    renderRightBox(date_selected);
  });
});
