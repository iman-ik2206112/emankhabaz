document.addEventListener("DOMContentLoaded", async () => {
  try {
    const student_json = localStorage.currentUser;
    const student = JSON.parse(student_json);
    const studentCourses = student.courses;

    const response = await fetch("../api/courses/assessments");
    const allAssessments = await response.json();

    const summaryByCourse = {};
    const effortByCourse = {};

    allAssessments.forEach((assessment) => {
      const courseCode = assessment.courseCode;
      const courseName = assessment.courseName;
      const effort = Number(assessment.effortHours) || 0;
      const type = assessment.type?.toLowerCase();

      if (!studentCourses.includes(courseCode)) return;

      if (!summaryByCourse[courseCode]) {
        summaryByCourse[courseCode] = {
          name: courseName,
          homework: 0,
          quiz: 0,
          project: 0,
          exam: 0,
          total: 0,
          effort: 0,
        };
      }

      if (type === "homework" || type === "assignment") {
        summaryByCourse[courseCode].homework++;
      } else if (type === "quiz") {
        summaryByCourse[courseCode].quiz++;
      } else if (type === "project") {
        summaryByCourse[courseCode].project++;
      } else if (["exam", "midterm", "final"].includes(type)) {
        summaryByCourse[courseCode].exam++;
      }

      summaryByCourse[courseCode].total++;
      summaryByCourse[courseCode].effort += effort;

      effortByCourse[courseCode] = summaryByCourse[courseCode].effort;
    });

    const tbody = document.querySelector("table tbody");
    if (!tbody) {
      console.error("Summary table <tbody> not found!");
      return;
    }

    Object.entries(summaryByCourse).forEach(([code, data]) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${code}</td>
        <td>${data.name}</td>
        <td>${data.homework}</td>
        <td>${data.quiz}</td>
        <td>${data.project}</td>
        <td>${data.exam}</td>
        <td>${data.total}</td>
        <td>${data.effort}</td>
      `;
      tbody.appendChild(row);
    });

    const ctx = document.getElementById("effortChart").getContext("2d");
    new Chart(ctx, {
      type: "bar",
      data: {
        labels: Object.keys(effortByCourse),
        datasets: [
          {
            label: "Effort Hours",
            data: Object.values(effortByCourse),
            backgroundColor: "rgba(75, 192, 192, 0.6)",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          title: {
            display: true,
            text: "Effort Hours per Course",
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: "Effort Hours",
            },
          },
          x: {
            title: {
              display: true,
              text: "Course Code",
            },
          },
        },
      },
    });

    //display a sign to the user that the content is loading
    const loadingElement = document.querySelector(".loading");
    if (loadingElement) {
      loadingElement.remove();
    }
  } catch (error) {
    console.error("Error generating the chart:", error);
  }
});
