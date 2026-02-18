const form = document.querySelector("#login-form");
const email = document.querySelector("#email");
const password = document.querySelector("#password");

form.addEventListener("submit", checkInfo);

async function checkInfo(e) {
  e.preventDefault();
  const userValue = email.value;
  const passValue = password.value;

  const promise = await fetch("/api/users", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email: userValue, password: passValue }),
  });
  const userFound = await promise.json();

  if (userFound) {
    userFound.status = true;
    localStorage.currentUser = JSON.stringify(userFound); // Save logged-in user info

    switch (userFound.role) {
      case "student":
        window.location.href = "./summary.html";
        break;
      case "instructor":
        window.location.href = "./GetAssessment.html";
        break;
      case "coordinator":
        window.location.href = "./summary.html";
        break;
      default:
        alert("Invalid role detected");
    }
  } else {
    alert("email or password is incorrect");
  }

  form.reset();
}
