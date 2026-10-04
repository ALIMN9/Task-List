// ------------------------------------------------------------
// Front end for the Task REST API.
// The page is served by Spring Boot itself, so we can call the
// API with a relative URL (no CORS setup needed).
// ------------------------------------------------------------

const API_URL = "/api/v1/tasks";

// Grab the HTML elements we need to work with
const dialog = document.getElementById("task-dialog");
const newBtn = document.getElementById("new-btn");
const form = document.getElementById("task-form");
const formTitle = document.getElementById("form-title");
const titleInput = document.getElementById("title");
const descriptionInput = document.getElementById("description");
const dueDateInput = document.getElementById("dueDate");
const priorityInput = document.getElementById("priority");
const submitBtn = document.getElementById("submit-btn");
const cancelBtn = document.getElementById("cancel-btn");
const errorBox = document.getElementById("error");          // error inside the pop-up
const pageError = document.getElementById("page-error");    // error on the main page
const list = document.getElementById("task-list");
const emptyMsg = document.getElementById("empty");
const counter = document.getElementById("counter");

let tasks = [];          // the tasks we last got from the server
let editingId = null;    // id of the task being edited (null = adding a new one)

// ---------- Talking to the API ----------

// Small helper: it sends a request and throws an Error with the
// message from the GlobalExceptionHandler ({ "error": "..." }).
async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });

  if (!response.ok) {
    let message = "Something went wrong (" + response.status + ")";
    try {
      const body = await response.json();
      if (body.error) message = body.error;
    } catch (e) { /* response had no JSON body */ }
    throw new Error(message);
  }

  // DELETE returns 204 No Content, so there is nothing to parse
  return response.status === 204 ? null : response.json();
}

async function loadTasks() {
  try {
    tasks = await request(API_URL);
    render();
  } catch (err) {
    showPageError("Could not load tasks. Is the Spring Boot app running? " + err.message);
  }
}

// ---------- Showing things on the page ----------

function showError(message) {          // inside the pop-up
  errorBox.textContent = message;
  errorBox.hidden = false;
}

function clearError() {
  errorBox.hidden = true;
}

function showPageError(message) {      // on the main page
  pageError.textContent = message;
  pageError.hidden = false;
}

function clearPageError() {
  pageError.hidden = true;
}

function render() {
  list.innerHTML = "";

  tasks.forEach((task) => {
    const li = document.createElement("li");
    li.className = "task " + task.priority + " " + task.status;

    // Checkbox: mark as completed / open
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.status === "COMPLETED";
    checkbox.title = "Mark as done";
    checkbox.addEventListener("change", () => toggleStatus(task));

    // Title, description, meta info
    const body = document.createElement("div");
    body.className = "task-body";

    const title = document.createElement("div");
    title.className = "task-title";
    title.textContent = task.title;
    body.appendChild(title);

    if (task.description) {
      const desc = document.createElement("div");
      desc.className = "task-desc";
      desc.textContent = task.description;
      body.appendChild(desc);
    }

    const meta = document.createElement("div");
    meta.className = "task-meta";
    meta.append(task.priority.toLowerCase() + " priority");
    if (task.dueDate) {
      meta.append(" · due " + task.dueDate);
      const overdue = task.status !== "COMPLETED" && task.dueDate < todayString();
      if (overdue) {
        const tag = document.createElement("span");
        tag.className = "overdue";
        tag.textContent = " (overdue)";
        meta.appendChild(tag);
      }
    }
    body.appendChild(meta);

    // Edit / delete buttons
    const actions = document.createElement("div");
    actions.className = "actions";

    const editBtn = document.createElement("button");
    editBtn.textContent = "Edit";
    editBtn.addEventListener("click", () => startEdit(task));

    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.className = "delete";
    deleteBtn.addEventListener("click", () => removeTask(task));

    actions.append(editBtn, deleteBtn);
    li.append(checkbox, body, actions);
    list.appendChild(li);
  });

  const open = tasks.filter((t) => t.status === "OPEN").length;
  counter.textContent = tasks.length
    ? open + " open · " + (tasks.length - open) + " completed"
    : "";
  emptyMsg.hidden = tasks.length > 0;
}

// Today's date as YYYY-MM-DD (local time), to compare with dueDate
function todayString() {
  const d = new Date();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return d.getFullYear() + "-" + month + "-" + day;
}

// ---------- The pop-up ----------

// "+ New task" button: open an empty pop-up
newBtn.addEventListener("click", () => {
  clearError();
  dialog.showModal();
  titleInput.focus();
});

// Cancel button: close the pop-up
cancelBtn.addEventListener("click", () => dialog.close());

// Clicking the dark area outside the form also closes it
dialog.addEventListener("click", (event) => {
  if (event.target === dialog) dialog.close();
});

// Runs every time the pop-up closes (Cancel, Esc, click outside, or after saving):
// reset the form back to "Add a task" mode.
dialog.addEventListener("close", () => {
  editingId = null;
  form.reset();
  priorityInput.value = "MEDIUM";
  formTitle.textContent = "Add a task";
  submitBtn.textContent = "Add task";
  clearError();
});

// ---------- Actions ----------

// Submit the form: create a new task, or save changes to an existing one
form.addEventListener("submit", async (event) => {
  event.preventDefault();   // stop the browser from reloading the page
  clearError();

  const data = {
    title: titleInput.value.trim(),
    description: descriptionInput.value.trim() || null,
    dueDate: dueDateInput.value || null,
    priority: priorityInput.value,
  };

  try {
    if (editingId) {
      // Your PUT endpoint also requires the status, so keep the current one
      const current = tasks.find((t) => t.id === editingId);
      data.status = current.status;
      await request(API_URL + "/" + editingId, { method: "PUT", body: JSON.stringify(data) });
    } else {
      await request(API_URL, { method: "POST", body: JSON.stringify(data) });
    }
    dialog.close();          // success: hide the pop-up
    await loadTasks();
  } catch (err) {
    showError(err.message);  // failure: keep the pop-up open and show why
  }
});

// Tick / untick the checkbox
async function toggleStatus(task) {
  clearPageError();
  const newStatus = task.status === "COMPLETED" ? "OPEN" : "COMPLETED";
  try {
    await request(API_URL + "/" + task.id, {
      method: "PUT",
      body: JSON.stringify({
        title: task.title,
        description: task.description,
        dueDate: task.dueDate,
        priority: task.priority,
        status: newStatus,
      }),
    });
  } catch (err) {
    showPageError(err.message);
  }
  await loadTasks();   // refresh (also resets the checkbox if the update failed)
}

async function removeTask(task) {
  if (!confirm('Delete "' + task.title + '"?')) return;
  clearPageError();
  try {
    await request(API_URL + "/" + task.id, { method: "DELETE" });
    await loadTasks();
  } catch (err) {
    showPageError(err.message);
  }
}

// Fill the form with the task's values and open the pop-up
function startEdit(task) {
  editingId = task.id;
  titleInput.value = task.title;
  descriptionInput.value = task.description || "";
  dueDateInput.value = task.dueDate || "";
  priorityInput.value = task.priority;
  formTitle.textContent = "Edit task";
  submitBtn.textContent = "Save changes";
  clearError();
  dialog.showModal();
  titleInput.focus();
}

// Load the tasks as soon as the page opens
loadTasks();
