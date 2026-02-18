document.addEventListener("DOMContentLoaded", function () {
  // #region User's Name
  /*****************************************
   * Display User name in start of header *
   *****************************************/

  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  const userName = currentUser.name;
  const role = currentUser.role;

  const navUl = document.querySelector("nav ul");
  const userNameLi = document.createElement("li");
  const userNameLink = document.createElement("a");
  userNameLink.href = "#";
  userNameLink.className = "user-name";
  userNameLink.textContent = `${userName} (${role})`;

  userNameLi.appendChild(userNameLink);
  navUl.insertBefore(userNameLi, navUl.firstChild); // Insert as first child

  userNameLink.style.pointerEvents = "none";
  userNameLink.style.textDecoration = "none";
  userNameLink.style.color = "inherit";
  // #endregion

  // #region Update Nav
  /*****************************************
 Hide some nav elements based on user's role
 *****************************************/

  switch (role) {
    case "instructor":
      instructorRole();
      break;
    case "coordinator":
      coordinatorRole();
      break;
    default:
      console.error("Undefined role:", role);
  }

  function instructorRole() {
    const calendar = document.querySelector("nav ul :nth-child(3)");
    if (calendar) {
      calendar.remove();
    }
    const summary = document.querySelector("nav ul :nth-child(4)");
    if (summary) {
      summary.remove();
    }
  }

  function coordinatorRole() {
    const calendar = document.querySelector("nav ul :nth-child(3)");
    if (calendar) {
      calendar.remove();
    }
  }
});
// #endregion
