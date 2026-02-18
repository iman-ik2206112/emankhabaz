document.addEventListener("DOMContentLoaded", async function () {
  const logoutButton = document.querySelector("#logout");

  logoutButton.addEventListener("click", (e) => {
    e.preventDefault(); // Prevents the default anchor behavior
    localStorage.clear();
    window.location.href = "../login.html";
  });
});
